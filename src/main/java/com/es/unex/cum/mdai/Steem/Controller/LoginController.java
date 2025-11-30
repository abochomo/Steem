package com.es.unex.cum.mdai.Steem.Controller;

import com.es.unex.cum.mdai.Steem.Modelo.Usuario;
import com.es.unex.cum.mdai.Steem.Services.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam String email, @RequestParam String password, HttpSession session, Model model) {
        try{
            Usuario usuarioLogueado = usuarioService.loginUsuario(email, password);
            session.setAttribute("usuario", usuarioLogueado);
            usuarioService.setUser(usuarioLogueado);
            return "redirect:/";
        }
        catch (RuntimeException e){
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

}
