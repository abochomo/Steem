package com.es.unex.cum.mdai.Steem.Controller;

import com.es.unex.cum.mdai.Steem.Modelo.Juego;
import com.es.unex.cum.mdai.Steem.Services.BibliotecaService;
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
@RequestMapping("/reembolsar")
public class ReembolsoController {

    @Autowired
    BibliotecaService bibliotecaService;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    JuegoService juegoService;

    // Usamos POST porque estamos modificando el estado del servidor (borrando datos)
    @PostMapping("/confirmar/{idJuego}")
    public String reembolsarJuego(@PathVariable int idJuego, Model model, RedirectAttributes redirectAttributes) {

        // 1. Obtener el usuario autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        long idUsuario = usuarioService.findUserByEmail(email).getIdUsuario();

        // 2. Verificar que el juego existe
        Juego juego = juegoService.getJuegoById(idJuego);
        if (juego == null){
            redirectAttributes.addFlashAttribute("error", "El juego no existe, no se puede reembolsar.");
            return "redirect:/biblioteca";
        }

        // 3. Verificar si el usuario REALMENTE tiene el juego (Lógica Inversa)
        boolean yaEnBiblioteca = bibliotecaService.tieneJuego(idUsuario, idJuego);

        if (!yaEnBiblioteca) {
            // Si NO lo tiene, no podemos reembolsarlo. Es un error de seguridad o lógico.
            redirectAttributes.addFlashAttribute("error", "No puedes reembolsar un juego que no tienes.");
            return "redirect:/biblioteca";
        }

        // 4. Ejecutar el reembolso
        // Asumo que crearás un método 'devolverJuego' o 'eliminarJuego' en tu servicio
        bibliotecaService.reembolsarJuego(idUsuario, idJuego);

        // 5. Feedback y Redirección
        redirectAttributes.addFlashAttribute("info", "Has reembolsado " + juego.getTitulo() + " correctamente.");
        return "redirect:/biblioteca";
    }

    @GetMapping("/{idJuego}")
    public String mostrarVistaReembolso(@PathVariable int idJuego, Model model, RedirectAttributes redirectAttributes) {
        // Obtenemos usuario
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        long idUsuario = usuarioService.findUserByEmail(email).getIdUsuario();

        // Validamos que el juego exista y que el usuario lo tenga
        Juego juego = juegoService.getJuegoById(idJuego);
        boolean yaEnBiblioteca = bibliotecaService.tieneJuego(idUsuario, idJuego);

        if (juego == null || !yaEnBiblioteca) {
            redirectAttributes.addFlashAttribute("error", "No se puede acceder al reembolso de este juego.");
            return "redirect:/biblioteca";
        }

        // Pasamos el juego al modelo para mostrar su foto y título en el HTML
        model.addAttribute("juego", juego);

        return "reembolso"; // Retorna la vista reembolso.html
    }
}