package com.es.unex.cum.mdai.Steem.Controller;

import com.es.unex.cum.mdai.Steem.Modelo.Cliente;
import com.es.unex.cum.mdai.Steem.Modelo.Desarrollador;
import com.es.unex.cum.mdai.Steem.Modelo.Usuario;
import com.es.unex.cum.mdai.Steem.Services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registro")
public class RegistroController {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/")
    public String registro() {
        return "registro";
    }

    @GetMapping("/registro/cliente")
    public String registrarCliente(Cliente user) {
        usuarioService.registrarUsuario(user);
        return "redirect:/login";
    }
    @GetMapping("/registro/desarrollador")
    public String registrarDesarrollador(Desarrollador user) {
        usuarioService.registrarUsuario(user);
        return "redirect:/login";
    }
}
