package com.es.unex.cum.mdai.Steem.Controller;

import com.es.unex.cum.mdai.Steem.Modelo.Juego;
import com.es.unex.cum.mdai.Steem.Modelo.Resenha;
import com.es.unex.cum.mdai.Steem.Modelo.Usuario;
import com.es.unex.cum.mdai.Steem.Repositorio.ResenhaRepositorio; // Añadir import
import com.es.unex.cum.mdai.Steem.Services.JuegoService;
import com.es.unex.cum.mdai.Steem.Services.ResenhaService;
import com.es.unex.cum.mdai.Steem.Services.UsuarioService; // Añadir import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/resenha")
public class ResenhaController {

    @Autowired
    private ResenhaService resenhaService;

    @Autowired
    private JuegoService juegoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ResenhaRepositorio resenhaRepositorio;

    // 1. MOSTRAR FORMULARIO (Modificado para cargar datos si existen)
    @GetMapping("/nueva/{idJuego}")
    public String mostrarFormularioResenha(@PathVariable("idJuego") Long idJuego, Model model) {

        // Obtenemos usuario actual
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Usuario usuario = usuarioService.findUserByEmail(email);

        // Verificamos el juego
        Juego juego = juegoService.getJuegoById(idJuego);
        if (juego == null) {
            return "redirect:/biblioteca?error=juego_no_encontrado";
        }

        // --- LÓGICA DE CARGA ---
        Resenha resenha;

        // Buscamos si ya existe una reseña de este usuario para este juego
        Optional<Resenha> resenhaExistente = resenhaRepositorio
                .findByCliente_IdAndJuego_IdJuego(usuario.getIdUsuario(), idJuego);

        if (resenhaExistente.isPresent()) {
            // Si existe, la cargamos (esto rellenará el texto y los votos automáticamente)
            resenha = resenhaExistente.get();
        } else {
            // Si no, creamos una nueva vacía
            resenha = new Resenha();
        }

        model.addAttribute("juego", juego);
        model.addAttribute("resenha", resenha);

        return "crear_resenha";
    }

    // 2. GUARDAR RESEÑA (Igual que antes)
    @PostMapping("/guardar")
    public String guardarResenha(@ModelAttribute("resenha") Resenha resenha,
                                 @RequestParam("idJuego") Long idJuego) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        try {
            resenhaService.publicarResenha(resenha, idJuego, email);
        } catch (Exception e) {
            return "redirect:/biblioteca?error=" + e.getMessage();
        }

        return "redirect:/juego/" + idJuego;
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarResenha(@PathVariable("id") Long idResenha) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        // Necesitamos el ID del juego para redirigir al usuario allí después de borrar
        // Lo recuperamos antes de borrar la reseña
        Long idJuego = null;
        try {
            // Nota: Podríamos hacer esto más limpio en el servicio, pero para no cambiar la firma
            // recuperamos la reseña un momento solo para saber el juego
            Optional<Resenha> r = resenhaRepositorio.findById(idResenha);
            if (r.isPresent()) {
                idJuego = r.get().getJuego().getIdJuego();
            }

            resenhaService.borrarResenha(idResenha, email);

        } catch (Exception e) {
            return "redirect:/biblioteca?error=" + e.getMessage();
        }

        // Si todo va bien, volvemos a la página del juego
        return "redirect:/juego/" + idJuego;
    }
}