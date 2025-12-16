package com.es.unex.cum.mdai.Steem.Controller;

import com.es.unex.cum.mdai.Steem.Modelo.Cliente;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.ui.Model;
import com.es.unex.cum.mdai.Steem.Modelo.Usuario;
import com.es.unex.cum.mdai.Steem.Services.UsuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/perfil")
    public String verPerfil(Model model, Principal principal) {
        if (principal == null) return "redirect:/login";
        Usuario usuario = usuarioService.findUserByEmail(principal.getName());
        model.addAttribute("usuario", usuario);
        return "perfil";
    }

    // --- EDITAR DATOS ---
    @GetMapping("/editar")
    public String mostrarFormularioEditar(Model model, Principal principal) {
        if (principal == null) return "redirect:/login";
        Usuario usuario = usuarioService.findUserByEmail(principal.getName());
        model.addAttribute("usuario", usuario);
        return "editar_perfil";
    }

    @PostMapping("/editar")
    public String procesarEdicion(@RequestParam("nombreUsuario") String nuevoNombre,
                                  Principal principal,
                                  RedirectAttributes redirectAttributes) {
        Usuario usuario = usuarioService.findUserByEmail(principal.getName());

        usuario.setNombreUsuario(nuevoNombre);
        usuarioService.guardarUsuario(usuario); // Funciona para Clientes y Desarrolladores

        redirectAttributes.addFlashAttribute("mensaje", "Perfil actualizado correctamente");
        return "redirect:/usuario/perfil";
    }

    // --- CAMBIAR CONTRASEÑA ---
    @GetMapping("/cambiar-password")
    public String mostrarFormularioPassword() {
        return "cambiar_password";
    }

    @PostMapping("/cambiar-password")
    public String procesarCambioPassword(@RequestParam("passwordActual") String passwordActual,
                                         @RequestParam("nuevaPassword") String nuevaPassword,
                                         Principal principal,
                                         RedirectAttributes redirectAttributes) {

        Usuario usuario = usuarioService.findUserByEmail(principal.getName());

        // Verificamos contraseña actual
        if (!passwordEncoder.matches(passwordActual, usuario.getPassword())) {
            redirectAttributes.addFlashAttribute("error", "La contraseña actual es incorrecta");
            return "redirect:/usuario/cambiar-password";
        }

        // Guardamos nueva contraseña
        usuario.setPassword(passwordEncoder.encode(nuevaPassword));
        usuarioService.guardarUsuario(usuario);

        redirectAttributes.addFlashAttribute("mensaje", "Contraseña cambiada con éxito");
        return "redirect:/usuario/perfil";
    }

    @PostMapping("/eliminar")
    public String eliminarCuenta(HttpServletRequest request, HttpServletResponse response) {
        // 1. Obtener la autenticación actual para saber a quién borrar
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            // 2. Buscar y eliminar al usuario
            Usuario usuario = usuarioService.findUserByEmail(auth.getName());
            if (usuario != null) {
                usuarioService.eliminarUsuario(usuario.getIdUsuario());
            }

            // 3. CERRAR SESIÓN MANUALMENTE
            // Esto realiza el logout de Spring Security sin necesidad de redirigir a /logout
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        // 4. Redirigir al login con un mensaje (opcionalmente puedes manejar el mensaje en la URL)
        return "redirect:/login?eliminado";
    }
}