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

import java.util.Date;
import java.util.Optional;

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
        // Pasamos un objeto Juego vacío para que el formulario lo rellene
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
            // Seguridad: Solo el dueño puede editar
            if (!juego.getDesarrollador().getEmail().equals(emailUsuario)) {
                return "redirect:/biblioteca?error=no_autorizado";
            }
            model.addAttribute("juego", juego);
            return "editar_juego";
        } else {
            return "redirect:/biblioteca?error=juego_no_encontrado";
        }
    }

    // --- 3. PROCESAR EL GUARDADO (SIRVE PARA AMBOS) ---
    @PostMapping("/guardar")
    public String guardarCambios(@Valid @ModelAttribute("juego") Juego juegoForm,
                                 BindingResult result,
                                 Model model) {

        if (result.hasErrors()) {
            // Thymeleaf necesita saber si es edición o creación para poner el título correcto
            // Como el ID viene en el objeto 'juegoForm', la vista ya lo sabe
            return "editar_juego";
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String emailUsuario = auth.getName();

        // CASO A: JUEGO NUEVO (ID es 0 o null)
        if (juegoForm.getIdJuego() == 0) {
            Usuario usuario = usuarioService.findUserByEmail(emailUsuario);

            if (usuario instanceof Desarrollador) {
                juegoForm.setDesarrollador((Desarrollador) usuario);
                juegoForm.setFechaPublicacion(new Date());
                // Al ser nuevo, el ID es 0, así que addJuego lo guardará como nuevo registro
                juegoService.addJuego(juegoForm);
            }
        }
        // CASO B: JUEGO EXISTENTE (ID > 0)
        else {
            Optional<Juego> juegoOriginalOpt = juegoService.getAllJuegos().stream()
                    .filter(j -> j.getIdJuego() == juegoForm.getIdJuego())
                    .findFirst();

            if (juegoOriginalOpt.isPresent()) {
                Juego juegoOriginal = juegoOriginalOpt.get();

                // Seguridad extra: Verificar dueño antes de modificar
                if (juegoOriginal.getDesarrollador().getEmail().equals(emailUsuario)) {
                    juegoOriginal.setTitulo(juegoForm.getTitulo());
                    juegoOriginal.setDescripcion(juegoForm.getDescripcion());
                    juegoOriginal.setPrecio(juegoForm.getPrecio());
                    juegoOriginal.setImagenUrl(juegoForm.getImagenUrl());

                    juegoService.updateJuego(juegoOriginal);
                }
            }
        }

        return "redirect:/biblioteca";
    }
}