package com.es.unex.cum.mdai.Steem.Controller;

import com.es.unex.cum.mdai.Steem.Modelo.Desarrollador;
import com.es.unex.cum.mdai.Steem.Modelo.Juego;
import com.es.unex.cum.mdai.Steem.Services.DesarrolladorService;
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

    /*
    DESCOMENTAR PARA PODER GENERAR JUEGOS MOCK
    @Autowired
    private DesarrolladorService desarrolladorService;
    */
    @GetMapping("/")
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            String username = auth.getName();
            model.addAttribute("username", username);
        }
        //Descomentar para generar juegos mock
        //generarJuegosMock();

        List<Juego> todosLosJuegos = juegoService.getAllJuegos();
        model.addAttribute("listaJuegos", todosLosJuegos);

        return "index";
    }

    /*
    DESCOMENTAR PARA GENERAR JUEGOS MOCK
    private void generarJuegosMock() {
        List<Juego> juegos = new ArrayList<>();

        Desarrollador devNintendo = new Desarrollador();
        devNintendo.setNombreEstudio("Nintendo");
        devNintendo.setNombreUsuario("Nintendo");
        devNintendo.setEmail("devNintendo@nintendo.a");
        devNintendo.setPassword("Nintendo");
        desarrolladorService.registroDesarrollador(devNintendo);

        Juego j1 = new Juego(); j1.setTitulo("Super Mario Fake"); j1.setDescripcion("Plataformas."); j1.setPrecio(59.99); j1.setDesarrollador(devNintendo);
        juegos.add(j1);
        juegoService.addJuego(j1);
        Juego j2 = new Juego();j2.setTitulo("Call of Duty: Mock Ops"); j2.setDescripcion("Disparos."); j2.setPrecio(69.99); j2.setDesarrollador(devNintendo);
        juegos.add(j2);
        juegoService.addJuego(j2);
        Juego j3 = new Juego();j3.setTitulo("Elden Ring (Demo)"); j3.setDescripcion("RPG."); j3.setPrecio(49.99); j3.setDesarrollador(devNintendo);
        juegos.add(j3);
        juegoService.addJuego(j3);
    }
     */
}
