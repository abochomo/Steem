package com.es.unex.cum.mdai.Steem.Controller;

import com.es.unex.cum.mdai.Steem.Modelo.*;
import com.es.unex.cum.mdai.Steem.Repositorio.DesarrolladorRepositorio;
import com.es.unex.cum.mdai.Steem.Repositorio.JuegoRepositorio;
import com.es.unex.cum.mdai.Steem.Services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private DesarrolladorService desarrolladorService;

    @Autowired
    private PasswordEncoder passwordEncoder; // Necesario para encriptar al editar

    @Autowired
    private JuegoService juegoService;

    @Autowired
    private JuegoRepositorio juegoRepositorio; // Para borrar directamente si el service no tiene delete

    @Autowired
    private DesarrolladorRepositorio desarrolladorRepositorio; // Para el desplegable de autores

    @Autowired
    private BibliotecaService bibliotecaService;

    @Autowired
    private ResenhaService resenhaService;

    // 1. DASHBOARD
    @GetMapping("/panel")
    public String adminPanel(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("usuarioAdmin", auth.getName());
        return "admin/dashboard";
    }

    // 2. LISTAR USUARIOS (Usando UsuarioService)
    @GetMapping("/usuarios")
    public String gestionUsuarios(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("usuarioAdmin", auth.getName());

        // Usamos el nuevo método del servicio
        List<Usuario> listaUsuarios = usuarioService.listarTodos();
        model.addAttribute("usuarios", listaUsuarios);

        return "admin/usuarios";
    }

    // 3. FORMULARIO NUEVO
    @GetMapping("/usuarios/nuevo")
    public String crearUsuarioForm(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("usuarioAdmin", auth.getName());

        // Como Usuario es abstracto, instanciamos un Cliente por defecto para que Thymeleaf no falle
        model.addAttribute("usuario", new Cliente());
        model.addAttribute("titulo", "Nuevo Usuario");
        return "admin/usuario_form";
    }

    // 4. FORMULARIO EDITAR
    @GetMapping("/usuarios/editar/{id}")
    public String editarUsuarioForm(@PathVariable("id") Long id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("usuarioAdmin", auth.getName());

        Usuario usuario = usuarioService.findUser(id);

        if (usuario != null) {
            model.addAttribute("usuario", usuario);
            model.addAttribute("titulo", "Editar Usuario");

            // === LÓGICA DE VARIABLES SEPARADAS ===
            // Extraemos los datos específicos y los mandamos "sueltos" al modelo
            // para evitar el error de Thymeleaf al intentar leer atributos que no existen.

            if (usuario instanceof Cliente) {
                Cliente c = (Cliente) usuario;
                model.addAttribute("cliSaldo", c.getSaldo());
                // Formato fecha para el input type="date"
                model.addAttribute("cliFechaNacimiento", c.getFechaNacimiento());
            }
            else if (usuario instanceof Desarrollador) {
                Desarrollador d = (Desarrollador) usuario;
                model.addAttribute("devNombreEstudio", d.getNombreEstudio());
                model.addAttribute("devActivo", d.isActivo());
            }

            return "admin/usuario_form";
        } else {
            return "redirect:/admin/usuarios";
        }
    }

    // 5. GUARDAR (CREAR O EDITAR)
    // Recibimos los parámetros manualmente (@RequestParam) para manejar la herencia
    @PostMapping("/usuarios/guardar")
    public String guardarUsuario(
            @RequestParam(required = false) Long id,
            @RequestParam String tipo,
            @RequestParam String nombreUsuario,
            @RequestParam String email,
            @RequestParam(required = false) String password,

            // Campos específicos de Cliente
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaNacimiento,
            @RequestParam(required = false) Float saldo,

            // Campos específicos de Desarrollador
            @RequestParam(required = false) String nombreEstudio,
            @RequestParam(required = false, defaultValue = "false") boolean activo
    ) {

        if (id == null || id == 0) {
            // === CREACIÓN DE NUEVO USUARIO ===
            // Delegamos en los servicios específicos (ellos encriptan la contraseña y ponen fecha registro)

            if ("CLIENTE".equals(tipo)) {
                Cliente c = new Cliente();
                c.setNombreUsuario(nombreUsuario);
                c.setEmail(email);
                c.setPassword(password); // El servicio lo encriptará
                c.setFechaNacimiento(fechaNacimiento);
                c.setSaldo(saldo != null ? saldo : 0f);

                clienteService.registroCliente(c);

            } else if ("DESARROLLADOR".equals(tipo)) {
                Desarrollador d = new Desarrollador();
                d.setNombreUsuario(nombreUsuario);
                d.setEmail(email);
                d.setPassword(password); // El servicio lo encriptará
                d.setNombreEstudio(nombreEstudio);
                d.setActivo(activo);

                desarrolladorService.registroDesarrollador(d);
            }

        } else {
            // === EDICIÓN DE USUARIO EXISTENTE ===
            Usuario u = usuarioService.findUser(id);
            if (u != null) {
                u.setNombreUsuario(nombreUsuario);
                u.setEmail(email);

                // Solo cambiamos la contraseña si el campo no está vacío
                if (password != null && !password.isEmpty()) {
                    u.setPassword(passwordEncoder.encode(password));
                }

                // Actualizar campos específicos según el tipo real del objeto
                if (u instanceof Cliente) {
                    Cliente c = (Cliente) u;
                    if (fechaNacimiento != null) c.setFechaNacimiento(fechaNacimiento);
                    if (saldo != null) c.setSaldo(saldo);
                    // Guardamos usando el servicio genérico o específico
                    clienteService.guardarCliente(c);
                }
                else if (u instanceof Desarrollador) {
                    Desarrollador d = (Desarrollador) u;
                    d.setNombreEstudio(nombreEstudio);
                    d.setActivo(activo);
                    // DesarrolladorService no tiene guardar genérico en tu código,
                    // así que usamos usuarioService que sí tiene guardarUsuario
                    usuarioService.guardarUsuario(d);
                }
            }
        }

        return "redirect:/admin/usuarios";
    }

    // 6. BORRAR
    @GetMapping("/usuarios/borrar/{id}")
    public String borrarUsuario(@PathVariable("id") Long id) {
        usuarioService.eliminarUsuario(id);
        return "redirect:/admin/usuarios";
    }

    // === LISTADO DE JUEGOS ===
    @GetMapping("/juegos")
    public String listarJuegos(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("usuarioAdmin", auth.getName());

        model.addAttribute("juegos", juegoService.getAllJuegos());
        return "admin/juegos";
    }

    // === FORMULARIO NUEVO JUEGO ===
    @GetMapping("/juegos/nuevo")
    public String nuevoJuego(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("usuarioAdmin", auth.getName());

        Juego juego = new Juego();
        // Inicializamos fecha para que no de error
        juego.setFechaPublicacion(new java.util.Date());

        model.addAttribute("juego", juego);
        // Pasamos la lista de desarrolladores para el <select>
        model.addAttribute("desarrolladores", desarrolladorRepositorio.findAll());
        model.addAttribute("titulo", "Nuevo Juego");

        return "admin/juego_form";
    }

    // === FORMULARIO EDITAR JUEGO ===
    @GetMapping("/juegos/editar/{id}")
    public String editarJuego(@PathVariable("id") Long id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("usuarioAdmin", auth.getName());

        Juego juego = juegoService.getJuegoById(id);

        model.addAttribute("juego", juego);
        model.addAttribute("desarrolladores", desarrolladorRepositorio.findAll());
        model.addAttribute("titulo", "Editar Juego: " + juego.getTitulo());

        return "admin/juego_form";
    }

    // === GUARDAR JUEGO (Crear o Editar) ===
    @PostMapping("/juegos/guardar")
    public String guardarJuego(@ModelAttribute Juego juego,
                               @RequestParam(required = false) Long idDesarrollador) {

        // Asignamos el desarrollador si se ha seleccionado uno
        if (idDesarrollador != null) {
            Desarrollador dev = desarrolladorRepositorio.findById(idDesarrollador).orElse(null);
            juego.setDesarrollador(dev);
        }

        // Si es edición (tiene ID), usamos update, si no, add
        if (juego.getIdJuego() > 0) {
            juegoService.updateJuego(juego);
        } else {
            juegoService.addJuego(juego);
        }

        return "redirect:/admin/juegos";
    }

    @GetMapping("/juegos/eliminar/{id}")
    public String eliminarJuego(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {

        // Intentamos el borrado seguro
        boolean eliminado = juegoService.eliminarJuegoSeguro(id);

        if (eliminado) {
            redirectAttributes.addFlashAttribute("mensajeExito", "Juego eliminado correctamente.");
        } else {
            // Mensaje explicativo si falla
            redirectAttributes.addFlashAttribute("mensajeError", "No se puede eliminar: El juego está presente en la biblioteca de uno o más usuarios.");
        }

        return "redirect:/admin/juegos";
    }

    // === LISTADO DE BIBLIOTECAS (GLOBAL) ===
    @GetMapping("/bibliotecas")
    public String listarBibliotecas(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("usuarioAdmin", auth.getName());

        List<Biblioteca> bibliotecas = bibliotecaService.getAllBibliotecas();
        model.addAttribute("bibliotecas", bibliotecas);

        return "admin/bibliotecas";
    }

    // === ELIMINAR Y REEMBOLSAR ===
    @GetMapping("/bibliotecas/eliminar/{id}")
    public String eliminarBiblioteca(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            bibliotecaService.eliminarEntradaYReembolsar(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Juego eliminado de la biblioteca y saldo reembolsado al usuario.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al procesar el reembolso: " + e.getMessage());
        }
        return "redirect:/admin/bibliotecas";
    }

    // === LISTADO DE RESEÑAS ===
    @GetMapping("/resenas")
    public String listarResenas(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("usuarioAdmin", auth.getName());

        List<Resenha> resenas = resenhaService.listarTodas();
        model.addAttribute("resenas", resenas);

        return "admin/resenas";
    }

    // === ELIMINAR RESEÑA (Moderación) ===
    @GetMapping("/resenas/eliminar/{id}")
    public String eliminarResena(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            resenhaService.borrarResenhaAdmin(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Reseña eliminada correctamente por moderación.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al eliminar la reseña.");
        }
        return "redirect:/admin/resenas";
    }

}