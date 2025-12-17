package com.es.unex.cum.mdai.Steem.Controller;

import com.es.unex.cum.mdai.Steem.Modelo.Desarrollador;
import com.es.unex.cum.mdai.Steem.Modelo.Juego;
import com.es.unex.cum.mdai.Steem.Modelo.Resenha;
import com.es.unex.cum.mdai.Steem.Modelo.Usuario;
import com.es.unex.cum.mdai.Steem.Repositorio.ResenhaRepositorio;
import com.es.unex.cum.mdai.Steem.Services.JuegoService;
import com.es.unex.cum.mdai.Steem.Services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

	@Autowired
	private JuegoService juegoService;

    @Autowired
    private ResenhaRepositorio resenhaRepositorio;

    @Autowired
    private UsuarioService usuarioService;

    /*
    @Autowired
    private DesarrolladorService desarrolladorService;
    */
    @GetMapping("/")
    public String home(@RequestParam(required = false) String keyword, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            // 1. MANTENEMOS EL EMAIL (lo que pediste)
            String email = auth.getName();
            model.addAttribute("username", email);

            // 2. BUSCAMOS EL NOMBRE PARA MOSTRAR (Estudio o Usuario)
            Usuario user = usuarioService.findUserByEmail(email);
            if (user != null) {
                if (user instanceof Desarrollador) {
                    // Si es desarrollador, preferimos el Nombre del Estudio
                    String nombreEstudio = ((Desarrollador) user).getNombreEstudio();
                    // Si por error fuera null, usamos el nombre de usuario
                    model.addAttribute("nombreMostrar", nombreEstudio != null ? nombreEstudio : user.getNombreUsuario());
                } else {
                    // Si es cliente, su nombre de usuario normal
                    model.addAttribute("nombreMostrar", user.getNombreUsuario());
                }
            }
        }

        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            String username = auth.getName();
            model.addAttribute("username", username);
        }
        //Descomentar para generar juegos mock
        //generarJuegosMock();

        List<Juego> listaJuegos; // Esta es la lista que enviaremos al HTML

        if (keyword != null && !keyword.isEmpty()) {
            // CASO A: El usuario escribió algo en el buscador
            // Vamos a la BD y pedimos SOLO los que coincidan
            listaJuegos = juegoService.buscarJuegos(keyword);
        } else {
            // CASO B: El usuario acaba de entrar o borró el buscador
            // Vamos a la BD y pedimos TODOS
            listaJuegos = juegoService.getAllJuegos();
        }

        // Al final, sea cual sea el caso, mandamos 'listaJuegos' a la vista
        model.addAttribute("listaJuegos", listaJuegos);
        model.addAttribute("keyword", keyword); // Para que el input no se borre

        return "index";
    }
    @GetMapping("/juego/{id}")
    public String verDetalleJuego(@PathVariable("id") Long id, Model model) {

        // Buscamos el juego.
        // Nota: Asumo que tienes un método findById en tu servicio.
        // Si no, puedes filtrar la lista getAllJuegos() como hicimos en la IA.
        Optional<Juego> juegoOpt = juegoService.getAllJuegos().stream()
                .filter(j -> j.getIdJuego() == id)
                .findFirst();

        if (juegoOpt.isPresent()) {
            model.addAttribute("juego", juegoOpt.get());
            List<Resenha> listaResenas = resenhaRepositorio.findByJuego_IdJuego(id);
            // Las pasamos a la vista HTML
            model.addAttribute("listaResenas", listaResenas);
            return "detalles_juego"; // Nombre del nuevo HTML
        } else {
            return "redirect:/"; // Si no existe, vuelve al inicio
        }
    }
}


