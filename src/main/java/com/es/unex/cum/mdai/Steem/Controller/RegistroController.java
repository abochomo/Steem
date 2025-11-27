package com.es.unex.cum.mdai.Steem.Controller;

import com.es.unex.cum.mdai.Steem.Modelo.Cliente;
import com.es.unex.cum.mdai.Steem.Modelo.Desarrollador;
import com.es.unex.cum.mdai.Steem.Modelo.Usuario;
import com.es.unex.cum.mdai.Steem.Services.ClienteService;
import com.es.unex.cum.mdai.Steem.Services.DesarrolladorService;
import com.es.unex.cum.mdai.Steem.Services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/registro")
public class RegistroController {
    @Autowired
    private ClienteService clienteService;

    @Autowired
    private DesarrolladorService desarrolladorService;

    @GetMapping("/")
    public String registro() {
        return "registro";
    }

    @GetMapping("/cliente")
    public String mostrarFormularioCliente(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "registro";
    }

    @PostMapping("/cliente")
    public String registrarCliente(Cliente user) {
        clienteService.registroCliente(user);
        return "redirect:/login";
    }
    @PostMapping("/registro/desarrollador")
    public String registrarDesarrollador(Desarrollador user) {
        desarrolladorService.registroDesarrollador(user);
        return "redirect:/login";
    }
}
