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

        // 1. Obtener juegos
        List<Juego> todosLosJuegos = juegoService.getAllJuegos();
        if (todosLosJuegos.isEmpty()) {
            return ResponseEntity.badRequest().body("El catálogo está vacío.");
        }

        // 2. Llamar a la IA
        System.out.println("Enviando petición a la IA con preferencias: " + preferencias);
        String respuestaIA = openAIService.obtenerRecomendacion(preferencias, todosLosJuegos);

        // 3. Verificación básica
        if (respuestaIA == null || respuestaIA.trim().isEmpty()) {
            return ResponseEntity.status(503).body("La IA no devolvió respuesta.");
        }

        // DEBUG
        System.out.println("========================================");
        System.out.println("RESPUESTA CRUDA DE LA IA:\n" + respuestaIA);
        System.out.println("========================================");

        try {
            // 4. LIMPIEZA
            String respuestaLimpia = respuestaIA.replaceAll("<think>[\\s\\S]*?</think>", "").trim();
            System.out.println("Respuesta limpia: " + respuestaLimpia);

            // 5. BUSCAR EL NÚMERO (CORREGIDO)
            // "-?" permite detectar el signo negativo si existe.
            Pattern pattern = Pattern.compile("-?\\d+");
            Matcher matcher = pattern.matcher(respuestaLimpia);

            Long idEncontrado = null;
            while (matcher.find()) {
                idEncontrado = Long.parseLong(matcher.group());
            }

            // Si la IA dijo texto pero no números, asumimos que no encontró nada (-1)
            if (idEncontrado == null) {
                System.err.println("No encontré números, asumiendo -1 (No encontrado).");
                idEncontrado = -1L;
            }

            // 6. MANEJO DEL CASO "NO ENCONTRADO" (-1)
            if (idEncontrado == -1) {
                Juego juegoNoEncontrado = new Juego();
                juegoNoEncontrado.setIdJuego(-1);
                juegoNoEncontrado.setTitulo("Sin resultados");
                juegoNoEncontrado.setDescripcion("Lo siento, no he encontrado ningún juego que coincida con tus gustos.");
                juegoNoEncontrado.setPrecio(0.0);

                return ResponseEntity.ok(juegoNoEncontrado);
            }

            // 7. BUSCAR EN BD (Si es un ID normal)
            Long finalIdJuego = idEncontrado;
            Optional<Juego> juegoOpt = todosLosJuegos.stream()
                    .filter(j -> j.getIdJuego() == finalIdJuego)
                    .findFirst();

            if (juegoOpt.isPresent()) {
                return ResponseEntity.ok(juegoOpt.get());
            } else {
                return ResponseEntity.status(404).body("La IA recomendó el ID " + finalIdJuego + " pero no existe.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error interno: " + e.getMessage());
        }
    }
}