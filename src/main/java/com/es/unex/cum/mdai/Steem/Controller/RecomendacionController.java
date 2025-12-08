package com.es.unex.cum.mdai.Steem.Controller;

import com.es.unex.cum.mdai.Steem.Modelo.Juego;
import com.es.unex.cum.mdai.Steem.Services.JuegoService;
import com.es.unex.cum.mdai.Steem.Services.OpenAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/ai")
public class RecomendacionController {

    @Autowired
    private OpenAIService openAIService;

    @Autowired
    private JuegoService juegoService;

    @PostMapping("/recomendar")
    public ResponseEntity<?> recomendarJuego(@RequestParam String preferencias) {

        // 1. Obtener lista de juegos
        List<Juego> todosLosJuegos = juegoService.getAllJuegos();
        if (todosLosJuegos.isEmpty()) {
            return ResponseEntity.badRequest().body("No hay juegos en el catálogo para analizar.");
        }

        // 2. Consultar a la IA
        String respuestaIA = openAIService.obtenerRecomendacion(preferencias, todosLosJuegos);

        if (respuestaIA == null) {
            System.err.println("Error: La respuesta de OpenAIService fue NULL.");
            return ResponseEntity.status(503).body("La IA no respondió (posible sobrecarga o error de conexión).");
        }

        System.out.println("Respuesta cruda de la IA: " + respuestaIA); // LOG PARA DEPURAR

        try {
            // 3. LIMPIEZA INTELIGENTE (NUEVO)
            // Buscamos cualquier secuencia de dígitos en la respuesta usando Regex
            // Esto ignorará etiquetas <think>, texto extra, puntos, etc.
            Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(respuestaIA);

            Long idJuego = null;

            // Si encuentra números, cogemos el último que aparezca (suele ser la conclusión tras el <think>)
            // O el primero si prefieres, pero en DeepSeek la respuesta final suele estar al final.
            while (matcher.find()) {
                idJuego = Long.parseLong(matcher.group());
            }

            if (idJuego == null) {
                return ResponseEntity.status(500).body("La IA no devolvió ningún ID válido. Dijo: " + respuestaIA);
            }

            // Variable final efectiva para usar en lambda
            Long finalIdJuego = idJuego;

            // 4. Buscar el juego en BD
            Optional<Juego> juegoOpt = todosLosJuegos.stream()
                    .filter(j -> j.getIdJuego() == finalIdJuego)
                    .findFirst();

            if (juegoOpt.isPresent()) {
                return ResponseEntity.ok(juegoOpt.get());
            } else {
                return ResponseEntity.status(404).body("La IA recomendó el ID " + finalIdJuego + " pero no existe en la BD.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error procesando la respuesta: " + e.getMessage());
        }
    }
}