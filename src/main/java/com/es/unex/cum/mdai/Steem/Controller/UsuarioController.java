package com.es.unex.cum.mdai.Steem.Controller;

import org.springframework.ui.Model;
import com.es.unex.cum.mdai.Steem.Modelo.Usuario;
import com.es.unex.cum.mdai.Steem.Services.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/perfil")
    public String verPerfil(HttpSession session, Model model) {
        Usuario sessionUser = (Usuario) session.getAttribute("usuario");
        if (sessionUser == null) {
            return "redirect:/login";
        }
        Usuario usuarioAux = usuarioService.findUser(sessionUser.getIdUsuario());
        model.addAttribute("usuario", usuarioAux);
        return "perfil";
    }
}

