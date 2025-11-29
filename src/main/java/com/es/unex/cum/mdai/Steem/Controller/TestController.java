package com.es.unex.cum.mdai.Steem.Controller;

//import com.es.unex.cum.mdai.Steem.Modelo.Biblioteca; // <--- Necesario
//import com.es.unex.cum.mdai.Steem.Modelo.Cliente;
//import com.es.unex.cum.mdai.Steem.Modelo.Desarrollador;
//import com.es.unex.cum.mdai.Steem.Modelo.Juego;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Controller
//public class TestController {
//
//    @GetMapping("/")
//    public String paginaPrincipal(@RequestParam(required = false) String keyword, Model model) {
//        // --- MOCKS (Simplificado para el ejemplo) ---
//        List<Juego> todosLosJuegos = generarJuegosMock();
//
//        List<Juego> juegosParaMostrar;
//        if (keyword != null && !keyword.isEmpty()) {
//            juegosParaMostrar = todosLosJuegos.stream()
//                    .filter(j -> j.getTitulo().toLowerCase().contains(keyword.toLowerCase()))
//                    .collect(Collectors.toList());
//        } else {
//            juegosParaMostrar = todosLosJuegos;
//        }
//
//        model.addAttribute("listaJuegos", juegosParaMostrar);
//        model.addAttribute("keyword", keyword);
//        return "index";
//    }
//
//    @GetMapping("/login")
//    public String verLogin() { return "login"; }
//
//    @GetMapping("/registro")
//    public String verRegistro() { return "registro"; }
//
//    @GetMapping("/registroDesarrollador")
//    public String verRegistroDesarrollador() { return "registroDesarrollador"; }
//
//    @GetMapping("/perfil")
//    public String verPerfil(Model model) {
//        Cliente usuarioMock = new Cliente();
//        usuarioMock.setNombreUsuario("GamerPro2025");
//        usuarioMock.setEmail("jugador@steem.com");
//        usuarioMock.setFechaRegistro(new Date());
//        model.addAttribute("usuario", usuarioMock);
//        return "perfil";
//    }
//
//    // --- NUEVO: BIBLIOTECA ---
//    @GetMapping("/biblioteca")
//    public String verBiblioteca(Model model) {
//        // 1. Simulamos que el usuario tiene 2 juegos comprados
//        List<Biblioteca> miBiblioteca = new ArrayList<>();
//        List<Juego> juegosDisponibles = generarJuegosMock();
//
//        // Compra ficticia 1: Super Mario Fake
//        Biblioteca b1 = new Biblioteca();
//        b1.setId(101L); // ID de la relación
//        b1.setJuego(juegosDisponibles.get(0));
//        // b1.setUsuario(usuarioActual); // En mock no hace falta setear el usuario
//        miBiblioteca.add(b1);
//
//        // Compra ficticia 2: Elden Ring
//        Biblioteca b2 = new Biblioteca();
//        b2.setId(102L);
//        b2.setJuego(juegosDisponibles.get(2));
//        miBiblioteca.add(b2);
//
//        // 2. Pasamos la lista a la vista
//        model.addAttribute("biblioteca", miBiblioteca);
//
//        return "biblioteca";
//    }
//
//    private List<Juego> generarJuegosMock() {
//        List<Juego> juegos = new ArrayList<>();
//
//        Desarrollador devNintendo = new Desarrollador();
//        devNintendo.setNombreUsuario("Nintendo"); // Asumiendo que usas herencia de Usuario o similar
//        // Si Desarrollador no tiene 'setNombre', ajusta esto a tu modelo real
//
//        Juego j1 = new Juego(); j1.setIdJuego(1L); j1.setTitulo("Super Mario Fake"); j1.setDescripcion("Plataformas."); j1.setPrecio(59.99); j1.setDesarrollador(devNintendo);
//        juegos.add(j1);
//
//        Juego j2 = new Juego(); j2.setIdJuego(2L); j2.setTitulo("Call of Duty: Mock Ops"); j2.setDescripcion("Disparos."); j2.setPrecio(69.99); j2.setDesarrollador(devNintendo);
//        juegos.add(j2);
//
//        Juego j3 = new Juego(); j3.setIdJuego(3L); j3.setTitulo("Elden Ring (Demo)"); j3.setDescripcion("RPG."); j3.setPrecio(49.99); j3.setDesarrollador(devNintendo);
//        juegos.add(j3);
//
//        return juegos;
//    }
//}