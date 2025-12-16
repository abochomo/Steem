package com.es.unex.cum.mdai.Steem.Controller;

import com.es.unex.cum.mdai.Steem.Modelo.Biblioteca;
import com.es.unex.cum.mdai.Steem.Modelo.Carrito;
import com.es.unex.cum.mdai.Steem.Modelo.Cliente;
import com.es.unex.cum.mdai.Steem.Modelo.Juego;
import com.es.unex.cum.mdai.Steem.Modelo.Usuario;
import com.es.unex.cum.mdai.Steem.Services.BibliotecaService;
import com.es.unex.cum.mdai.Steem.Services.ClienteService;
import com.es.unex.cum.mdai.Steem.Services.JuegoService;
import com.es.unex.cum.mdai.Steem.Services.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired
    private JuegoService juegoService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private BibliotecaService bibliotecaService; // Necesario para registrar la compra

    // 1. Añadir juego al carrito
    @GetMapping("/add/{id}")
    public String addCarrito(@PathVariable("id") Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        Carrito carrito = (Carrito) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new Carrito();
            carrito.setJuegosEnCarrito(new ArrayList<>());
            carrito.setPrecioTotal(0.0);
        }

        // Nota: Asegúrate de que tu servicio use findById o getJuegoById según lo tengas definido
        Juego juego = juegoService.getJuegoById(Math.toIntExact(id));

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
        // Redirigir a la página desde donde se llamó sería ideal, pero por ahora al home:
        return "redirect:/";
    }

    // 2. Ver el carrito
    @GetMapping
    public String verCarrito(HttpSession session, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Usuario usuario = clienteService.findClienteByEmail(username);

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

        // IMPORTANTE: Pasamos la LISTA de juegos, no el objeto carrito entero,
        // para que tu HTML (th:each="juego: ${carrito}") funcione sin cambios.
        model.addAttribute("carrito", carrito.getJuegosEnCarrito());
        model.addAttribute("cliente", (Cliente) usuario);

        return "carrito";
    }

    // 3. PROCESAR COMPRA (Movido aquí desde CompraController)
    @PostMapping("/comprar")
    public String comprarCarrito(@RequestParam(value = "idJuegos", required = false) List<Integer> idsJuegos,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {

        if (idsJuegos == null || idsJuegos.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "No has seleccionado ningún juego.");
            return "redirect:/carrito";
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Cliente cliente = (Cliente) clienteService.findClienteByEmail(auth.getName());

        // Recuperamos el carrito de la sesión para poder actualizarlo después
        Carrito carritoSession = (Carrito) session.getAttribute("carrito");

        double totalCompra = 0;
        List<Juego> juegosAComprar = new ArrayList<>();

        // 1. Calcular total y validar
        for (Integer id : idsJuegos) {
            Juego juego = juegoService.getJuegoById(id);
            // Verificamos que no lo tenga ya comprado
            if (juego != null && !bibliotecaService.tieneJuego(cliente.getIdUsuario(), id)) {
                juegosAComprar.add(juego);
                totalCompra += juego.getPrecio();
            }
        }

        // 2. Verificar Saldo
        if (cliente.getSaldo() < totalCompra) {
            redirectAttributes.addFlashAttribute("error", "Saldo insuficiente. Total: " + totalCompra + " €");
            return "redirect:/cliente/cargarSaldo";
        }

        // 3. Realizar la transacción
        clienteService.guardarCliente(cliente);

        for (Juego juego : juegosAComprar) {
            // Usamos la lógica de bibliotecaService o lo hacemos manual
            bibliotecaService.comprarJuego(cliente.getIdUsuario(), (int) juego.getId());

            // 4. IMPORTANTE: Eliminar los juegos comprados del carrito de la sesión
            if (carritoSession != null) {
                // Buscamos el juego en el objeto carrito y lo borramos
                // Usamos removeIf para borrar de la lista de forma segura
                carritoSession.getJuegosEnCarrito().removeIf(j -> j.getId() == juego.getId());
            }
        }

        // Actualizamos el carrito en la sesión (ahora tiene menos juegos)
        session.setAttribute("carrito", carritoSession);

        redirectAttributes.addFlashAttribute("info", "Compra realizada con éxito.");
        return "redirect:/biblioteca";
    }
}