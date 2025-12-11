package com.es.unex.cum.mdai.Steem.Controller;


import com.es.unex.cum.mdai.Steem.Modelo.Juego;
import com.es.unex.cum.mdai.Steem.Modelo.Usuario;
import com.es.unex.cum.mdai.Steem.Services.BibliotecaService;
import com.es.unex.cum.mdai.Steem.Services.JuegoService;
import com.es.unex.cum.mdai.Steem.Services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/comprar")
public class CompraController {

    @Autowired
    BibliotecaService bibliotecaService;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    JuegoService juegoService;

    @PostMapping("/{idJuego}")
    public String comprarJuego(@PathVariable int idJuego, Model model, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        long idUsuario = usuarioService.findUserByEmail(email).getIdUsuario();
        Usuario user = usuarioService.findUser(idUsuario);
        Juego juego = juegoService.getJuegoById(idJuego);
        if (juego == null){
            redirectAttributes.addFlashAttribute("error", "El juego no existe.");
            return "redirect:/biblioteca";
        }
        boolean yaEnBiblioteca = bibliotecaService.tieneJuego(idUsuario, idJuego);
        if (yaEnBiblioteca) {
            redirectAttributes.addFlashAttribute("info", "El juego ya está en tu biblioteca.");
            return "redirect:/biblioteca";
        }
        redirectAttributes.addFlashAttribute("info", "Has comprado " + juego.getTitulo() + " correctamente.");
        bibliotecaService.comprarJuego(idUsuario, idJuego);
        return "redirect:/biblioteca"; // Redirigir a la biblioteca después de la compra
    }
    @GetMapping("/{idJuego}")
    public String mostrarCompraJuego(@PathVariable int idJuego, Model model, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        long idUsuario = usuarioService.findUserByEmail(email).getIdUsuario();
        Usuario user = usuarioService.findUser(idUsuario);
        Juego juego = juegoService.getJuegoById(idJuego);
        if (juego == null){
            redirectAttributes.addFlashAttribute("error", "El juego no existe.");
            return "redirect:/biblioteca";
        }
        boolean yaEnBiblioteca = bibliotecaService.tieneJuego(idUsuario, idJuego);
        if (yaEnBiblioteca) {
            redirectAttributes.addFlashAttribute("info", "El juego ya está en tu biblioteca.");
            return "redirect:/biblioteca";
        }
        redirectAttributes.addFlashAttribute("info", "Has comprado " + juego.getTitulo() + " correctamente.");
        bibliotecaService.comprarJuego(idUsuario, idJuego);
        return "redirect:/biblioteca";
    }
}
