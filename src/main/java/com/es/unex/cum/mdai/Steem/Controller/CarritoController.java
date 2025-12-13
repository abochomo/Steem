package com.es.unex.cum.mdai.Steem.Controller;

import com.es.unex.cum.mdai.Steem.Modelo.Carrito;
import com.es.unex.cum.mdai.Steem.Modelo.Cliente;
import com.es.unex.cum.mdai.Steem.Modelo.Juego;
import com.es.unex.cum.mdai.Steem.Modelo.Usuario;
import com.es.unex.cum.mdai.Steem.Services.JuegoService;
import com.es.unex.cum.mdai.Steem.Services.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired
    private JuegoService juegoService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/add/{id}")
    public String addCarrito(@PathVariable("id") Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        Carrito carrito = (Carrito) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new Carrito();
            carrito.setJuegosEnCarrito(new ArrayList<>());
            carrito.setPrecioTotal(0.0);
        }

        Juego juego = juegoService.getJuegoById(id);

        if (juego != null) {
            boolean yaExiste = carrito.getJuegosEnCarrito().stream().anyMatch(j -> j.getId() == id);

            if (!yaExiste) {
                carrito.agregarJuego(juego);
                session.setAttribute("carrito", carrito);
                redirectAttributes.addFlashAttribute("mensaje", "Juego añadido al carrito");
            } else {
                redirectAttributes.addFlashAttribute("error", "Este juego ya está en tu carrito");
            }
        }
        return "redirect:/";
    }

    @GetMapping
    public String verCarrito(HttpSession session, Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioService.findUserByEmail(username);

        if (!(usuario instanceof Cliente)) {
            return "redirect:/";
        }

        Carrito carrito = (Carrito) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new Carrito();
            carrito.setJuegosEnCarrito(new ArrayList<>());
            carrito.setPrecioTotal(0.0);
            session.setAttribute("carrito", carrito);
        }

        model.addAttribute("carrito", carrito);
        model.addAttribute("cliente", (Cliente) usuario);

        return "carrito";
    }
}