package com.es.unex.cum.mdai.Steem.Controller;

import com.es.unex.cum.mdai.Steem.Modelo.Biblioteca;
import com.es.unex.cum.mdai.Steem.Modelo.Juego;
import com.es.unex.cum.mdai.Steem.Modelo.Usuario;
import com.es.unex.cum.mdai.Steem.Modelo.Usuario.TipoUsuario;
import com.es.unex.cum.mdai.Steem.Repositorio.JuegoRepositorio;
import com.es.unex.cum.mdai.Steem.Services.BibliotecaService;
import com.es.unex.cum.mdai.Steem.Services.JuegoService;
import com.es.unex.cum.mdai.Steem.Services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/biblioteca")
public class BibliotecaController {

    @Autowired
    private BibliotecaService bibliotecaService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JuegoService juegoService;

    @GetMapping
    public String verBiblioteca(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return "redirect:/login";
        }
        else {
            String username = auth.getName();
            Usuario user = usuarioService.findUserByEmail(username);
            List<Biblioteca> biblioteca= bibliotecaService.getBiblioteca(user.getIdUsuario());
            List<Juego> juegosEnBiblioteca = biblioteca.stream()
                    .map(b -> juegoService.getJuegoById(b.getJuego().getId()))
                    .toList();
            model.addAttribute("biblioteca", juegosEnBiblioteca);
        }
        return "biblioteca";
    }


}
