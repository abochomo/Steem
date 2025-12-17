package com.es.unex.cum.mdai.Steem.Controller;

import com.es.unex.cum.mdai.Steem.Modelo.Desarrollador;
import com.es.unex.cum.mdai.Steem.Modelo.Juego;
import com.es.unex.cum.mdai.Steem.Modelo.Usuario;
import com.es.unex.cum.mdai.Steem.Services.JuegoService;
import com.es.unex.cum.mdai.Steem.Services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/dashboard")
public class JuegoGestionController {

    @Autowired
    private JuegoService juegoService;

    @Autowired
    private UsuarioService usuarioService;

    // --- 1. MOSTRAR FORMULARIO VACÍO (NUEVO JUEGO) ---
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("juego", new Juego());
        return "editar_juego";
    }

    // --- 2. MOSTRAR FORMULARIO CON DATOS (EDITAR) ---
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") Long id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String emailUsuario = auth.getName();

        Optional<Juego> juegoOpt = juegoService.getAllJuegos().stream()
                .filter(j -> j.getIdJuego() == id)
                .findFirst();

        if (juegoOpt.isPresent()) {
            Juego juego = juegoOpt.get();
            if (!juego.getDesarrollador().getEmail().equals(emailUsuario)) {
                return "redirect:/biblioteca?error=no_autorizado";
            }
            model.addAttribute("juego", juego);
            return "editar_juego";
        } else {
            return "redirect:/biblioteca?error=juego_no_encontrado";
        }
    }

    // --- 3. PROCESAR EL GUARDADO ---
    @PostMapping("/guardar")
    public String guardarCambios(@Valid @ModelAttribute("juego") Juego juegoForm,
                                 BindingResult result,
                                 Model model) {

        if (result.hasErrors()) {
            return "editar_juego";
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String emailUsuario = auth.getName();

        // === NUEVA LÓGICA: PROCESAR IMAGEN ===
        try {
            String url = juegoForm.getImagenUrl();
            // Si la URL no está vacía y empieza por http/https (es de internet)
            if (url != null && !url.isEmpty() && (url.startsWith("http://") || url.startsWith("https://"))) {
                // Llamamos a la función que descarga y devuelve la nueva ruta local
                String rutaLocal = descargarYGuardarImagen(url);
                juegoForm.setImagenUrl(rutaLocal);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Si falla la descarga, no bloqueamos el guardado, pero podrías mostrar un error
            System.err.println("Error descargando la imagen: " + e.getMessage());
        }
        // =====================================

        // CASO A: JUEGO NUEVO
        if (juegoForm.getIdJuego() == 0) {
            Usuario usuario = usuarioService.findUserByEmail(emailUsuario);
            if (usuario instanceof Desarrollador) {
                juegoForm.setDesarrollador((Desarrollador) usuario);
                juegoForm.setFechaPublicacion(new Date());
                juegoService.addJuego(juegoForm);
            }
        }
        // CASO B: JUEGO EXISTENTE
        else {
            Optional<Juego> juegoOriginalOpt = juegoService.getAllJuegos().stream()
                    .filter(j -> j.getIdJuego() == juegoForm.getIdJuego())
                    .findFirst();

            if (juegoOriginalOpt.isPresent()) {
                Juego juegoOriginal = juegoOriginalOpt.get();
                if (juegoOriginal.getDesarrollador().getEmail().equals(emailUsuario)) {
                    juegoOriginal.setTitulo(juegoForm.getTitulo());
                    juegoOriginal.setDescripcion(juegoForm.getDescripcion());
                    juegoOriginal.setPrecio(juegoForm.getPrecio());

                    // Actualizamos la imagen con la nueva (sea URL o local ya procesada)
                    juegoOriginal.setImagenUrl(juegoForm.getImagenUrl());

                    juegoService.updateJuego(juegoOriginal);
                }
            }
        }

        return "redirect:/biblioteca";
    }

    /**
     * Método auxiliar para descargar imágenes
     */
    private String descargarYGuardarImagen(String urlImagen) throws Exception {
        // 1. Definir dónde guardar (Carpeta del proyecto / src / main / resources / static / images / downloads)
        // Usamos user.dir para obtener la raíz del proyecto
        String projectRoot = System.getProperty("user.dir");
        String directorioDestino = projectRoot + "/src/main/resources/static/images/downloads/";

        // Crear directorio si no existe
        Path uploadPath = Paths.get(directorioDestino);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 2. Generar un nombre único para el archivo (para evitar sobreescribir)
        // Usamos UUID y asumimos extensión jpg por defecto (podrías mejorar esto detectando la extensión real)
        String nombreArchivo = "cover_" + UUID.randomUUID().toString() + ".jpg";

        // 3. Descargar
        try (InputStream in = new URL(urlImagen).openStream()) {
            Path filePath = uploadPath.resolve(nombreArchivo);
            Files.copy(in, filePath, StandardCopyOption.REPLACE_EXISTING);

            // IMPORTANTE: También copiamos a la carpeta target/classes para que se vea INMEDIATAMENTE sin reiniciar
            // Esto es un truco para desarrollo
            String targetPathStr = projectRoot + "/target/classes/static/images/downloads/";
            Path targetPath = Paths.get(targetPathStr);
            if (Files.exists(Paths.get(projectRoot + "/target/classes/static/"))) {
                if (!Files.exists(targetPath)) Files.createDirectories(targetPath);
                Files.copy(filePath, targetPath.resolve(nombreArchivo), StandardCopyOption.REPLACE_EXISTING);
            }
        }

        // 4. Retornar la ruta relativa para guardar en la BD
        return "/images/downloads/" + nombreArchivo;
    }
}