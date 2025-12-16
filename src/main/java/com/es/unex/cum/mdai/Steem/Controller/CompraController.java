package com.es.unex.cum.mdai.Steem.Controller;


import com.es.unex.cum.mdai.Steem.Modelo.*;
import com.es.unex.cum.mdai.Steem.Services.BibliotecaService;
import com.es.unex.cum.mdai.Steem.Services.ClienteService;
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
    ClienteService clienteService;

    @Autowired
    JuegoService juegoService;

    @PostMapping("/{idJuego}")
    public String comprarJuego(@PathVariable int idJuego, Model model, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Cliente cliente = clienteService.findClienteByEmail(email);
        long idUsuario = cliente.getIdUsuario();
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
    public String mostrarResumenCompra(@PathVariable int idJuego, Model model, RedirectAttributes redirectAttributes) {
        // 1. Obtener usuario autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario user = clienteService.findClienteByEmail(auth.getName());

        // Seguridad: Solo clientes pueden comprar
        if (!(user instanceof Cliente)) {
            redirectAttributes.addFlashAttribute("error", "Debes ser cliente para comprar juegos.");
            return "redirect:/";
        }
        Cliente cliente = (Cliente) user;

        // 2. Obtener Juego
        Juego juego = juegoService.getJuegoById(idJuego);
        if (juego == null) {
            return "redirect:/";
        }

        // 3. Verificar si ya lo tiene (para no venderlo 2 veces)
        if (bibliotecaService.tieneJuego(cliente.getIdUsuario(), idJuego)) {
            redirectAttributes.addFlashAttribute("info", "Ya tienes este juego en tu biblioteca.");
            return "redirect:/biblioteca";
        }

        // 4. Calcular Saldos
        double saldoActual = cliente.getSaldo();
        double precioJuego = juego.getPrecio();
        double saldoRestante = saldoActual - precioJuego;
        boolean tieneSaldoSuficiente = saldoRestante >= 0;

        // 5. Pasar datos a la vista
        model.addAttribute("juego", juego);
        model.addAttribute("cliente", cliente);
        model.addAttribute("saldoRestante", saldoRestante);
        model.addAttribute("tieneSaldoSuficiente", tieneSaldoSuficiente);

        return "resumen_compra"; // Nombre del nuevo HTML
    }
}
