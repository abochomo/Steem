package com.es.unex.cum.mdai.Steem.Controller;

import com.es.unex.cum.mdai.Steem.Modelo.Desarrollador;
import com.es.unex.cum.mdai.Steem.Modelo.Juego;
import com.es.unex.cum.mdai.Steem.Services.JuegoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

	@Autowired
	private JuegoService juegoService;

    @GetMapping("/")
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            String username = auth.getName();
            model.addAttribute("username", username);
        }
        List<Juego> todosLosJuegos = generarJuegosMock();

        List<Juego> juegos = generarJuegosMock();
        model.addAttribute("listaJuegos", juegos);
        return "index";
    }

    private List<Juego> generarJuegosMock() {
        List<Juego> juegos = new ArrayList<>();

        Desarrollador devNintendo = new Desarrollador();
        devNintendo.setNombreUsuario("Nintendo"); // Asumiendo que usas herencia de Usuario o similar
        // Si Desarrollador no tiene 'setNombre', ajusta esto a tu modelo real

        Juego j1 = new Juego(); j1.setIdJuego(1L); j1.setTitulo("Super Mario Fake"); j1.setDescripcion("Plataformas."); j1.setPrecio(59.99); j1.setDesarrollador(devNintendo);
        juegos.add(j1);

        Juego j2 = new Juego(); j2.setIdJuego(2L); j2.setTitulo("Call of Duty: Mock Ops"); j2.setDescripcion("Disparos."); j2.setPrecio(69.99); j2.setDesarrollador(devNintendo);
        juegos.add(j2);

        Juego j3 = new Juego(); j3.setIdJuego(3L); j3.setTitulo("Elden Ring (Demo)"); j3.setDescripcion("RPG."); j3.setPrecio(49.99); j3.setDesarrollador(devNintendo);
        juegos.add(j3);

        return juegos;
    }
}
