package com.es.unex.cum.mdai.Steem.Controller;

import com.es.unex.cum.mdai.Steem.Modelo.Cliente;
import com.es.unex.cum.mdai.Steem.Services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
@Controller
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/cargarSaldo")
    public String cargarSaldo() {
        return "recarga";
    }

    @PostMapping("/cargarSaldo")
    public String procesarRecarga(@RequestParam("cantidad") float cantidad, Principal principal, RedirectAttributes redirectAttributes) {

        // Validación básica de seguridad en el servidor
        if (cantidad <= 0) {
            redirectAttributes.addFlashAttribute("error", "La cantidad debe ser mayor a 0.");
            return "redirect:/cliente/cargarSaldo";
        }

        Cliente cliente = clienteService.findClienteByEmail(principal.getName());
        clienteService.cargarSaldo(cantidad); // Asumiendo que has actualizado tu servicio para usar ID

        redirectAttributes.addFlashAttribute("info", "Has recargado " + cantidad + " € correctamente.");
        return "redirect:/usuario/perfil";
    }
}
