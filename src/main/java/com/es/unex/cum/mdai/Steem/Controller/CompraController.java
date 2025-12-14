package com.es.unex.cum.mdai.Steem.Controller;


import com.es.unex.cum.mdai.Steem.Modelo.*;
import com.es.unex.cum.mdai.Steem.Services.BibliotecaService;
import com.es.unex.cum.mdai.Steem.Services.JuegoService;
import com.es.unex.cum.mdai.Steem.Services.UsuarioService;
import jakarta.servlet.http.HttpSession;
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

import java.util.Date;

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
    public String mostrarResumenCompra(@PathVariable int idJuego, Model model, RedirectAttributes redirectAttributes) {
        // 1. Obtener usuario autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario user = usuarioService.findUserByEmail(auth.getName());

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

    @PostMapping("/carrito")
    public String comprarDesdeCarrito(HttpSession session, RedirectAttributes redirectAttributes) {
        // 1. Obtención de usuario (Igual que en tus otros métodos)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Usuario user = usuarioService.findUserByEmail(email);
        long idUsuario = user.getIdUsuario();

        // Verificación de seguridad extra (Solo Clientes tienen saldo/carrito)
        if (!(user instanceof Cliente)) {
            return "redirect:/";
        }
        Cliente cliente = (Cliente) user;

        // 2. Obtener Carrito
        Carrito carrito = (Carrito) session.getAttribute("carrito");
        if (carrito == null || carrito.getJuegosEnCarrito().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El carrito está vacío.");
            return "redirect:/carrito";
        }

        // 3. Comprobación de Saldo (La parte nueva solicitada)
        if (cliente.getSaldo() < carrito.getPrecioTotal()) {
            redirectAttributes.addFlashAttribute("error", "Saldo insuficiente. Total: " + carrito.getPrecioTotal() + "€");
            return "redirect:/carrito";
        }

        // 4. Procesamiento (Bucle utilizando tu lógica existente)
        int juegosProcesados = 0;

        for (Juego juego : carrito.getJuegosEnCarrito()) {
            int idJuego = (int) juego.getId();

            // Reutilizamos EXACTAMENTE tu lógica de validación
            boolean yaEnBiblioteca = bibliotecaService.tieneJuego(idUsuario, idJuego);

            if (!yaEnBiblioteca) {
                // Delegamos la compra al servicio, igual que en comprarJuego
                bibliotecaService.comprarJuego(idUsuario, idJuego);
                juegosProcesados++;
            }
        }

        // 5. Limpieza y Feedback
        carrito.vaciarCarrito();
        session.setAttribute("carrito", carrito);

        if (juegosProcesados > 0) {
            redirectAttributes.addFlashAttribute("info", "Has comprado " + juegosProcesados + " juegos correctamente.");
        } else {
            redirectAttributes.addFlashAttribute("info", "No se realizaron cargos, ya tenías todos los juegos.");
        }

        return "redirect:/biblioteca";
    }
}
