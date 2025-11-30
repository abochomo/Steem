package com.es.unex.cum.mdai.Steem.Controller;

import org.springframework.ui.Model;
import com.es.unex.cum.mdai.Steem.Modelo.Usuario;
import com.es.unex.cum.mdai.Steem.Services.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/perfil")
    public String verPerfil(Model model, Principal principal) {

        if (principal == null) {

            return "redirect:/login";
        }
        String email = principal.getName();
        Usuario usuario = usuarioService.findUserByEmail(email);
        model.addAttribute("usuario", usuario);
        return "perfil";
    }
}

