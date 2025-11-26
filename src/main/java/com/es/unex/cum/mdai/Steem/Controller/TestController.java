package com.es.unex.cum.mdai.Steem.Controller; // Ajusta a tu paquete real

import com.es.unex.cum.mdai.Steem.Modelo.Desarrollador;
import com.es.unex.cum.mdai.Steem.Modelo.Juego;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TestController {

    @GetMapping("/") // Esto atiende a http://localhost:8080/
    public String paginaPrincipal(Model model) {

        // --- ZONA DE DATOS FALSOS (MOCK) ---
        // Creamos una lista manual para no necesitar la Base de Datos aún
        List<Juego> juegosFalsos = new ArrayList<>();

        // Juego 1
        Juego j1 = new Juego();
        j1.setIdJuego(1L);
        j1.setTitulo("Super Mario Fake");
        j1.setDescripcion("Un juego de plataformas clásico simulado.");
        j1.setPrecio(59.99);
        j1.setDesarrollador(new Desarrollador());
        juegosFalsos.add(j1);

        // Juego 2
        Juego j2 = new Juego();
        j2.setIdJuego(2L);
        j2.setTitulo("Call of Duty: Mock Ops");
        j2.setDescripcion("Disparos y acción sin base de datos.");
        j2.setPrecio(69.99);
        j2.setDesarrollador(new Desarrollador());
        juegosFalsos.add(j2);

        // Juego 3
        Juego j3 = new Juego();
        j3.setIdJuego(3L);
        j3.setTitulo("Elden Ring (Demo)");
        j3.setDescripcion("Explora un mundo vasto.");
        j3.setPrecio(49.99);
        j3.setDesarrollador(new Desarrollador());
        juegosFalsos.add(j3);

        // --- PASAR DATOS A LA VISTA ---
        // "listaJuegos" debe coincidir con lo que pusiste en el th:each del HTML
        model.addAttribute("listaJuegos", juegosFalsos);

        return "index";
    }

    @GetMapping("/login")
    public String verLogin() {
        return "login"; // Busca login.html
    }
}