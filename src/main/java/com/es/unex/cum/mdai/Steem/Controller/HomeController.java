package com.es.unex.cum.mdai.Steem.Controller;

import com.es.unex.cum.mdai.Steem.Modelo.Juego;
import com.es.unex.cum.mdai.Steem.Modelo.Resenha;
import com.es.unex.cum.mdai.Steem.Repositorio.ResenhaRepositorio;
import com.es.unex.cum.mdai.Steem.Services.JuegoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

	@Autowired
	private JuegoService juegoService;

    @Autowired
    private ResenhaRepositorio resenhaRepositorio;

    /*
    @Autowired
    private DesarrolladorService desarrolladorService;
    */
    @GetMapping("/")
    public String home(@RequestParam(required = false) String keyword, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            String username = auth.getName();
            model.addAttribute("username", username);
        }
        //Descomentar para generar juegos mock
        //generarJuegosMock();

        List<Juego> listaJuegos; // Esta es la lista que enviaremos al HTML

        if (keyword != null && !keyword.isEmpty()) {
            // CASO A: El usuario escribió algo en el buscador
            // Vamos a la BD y pedimos SOLO los que coincidan
            listaJuegos = juegoService.buscarJuegos(keyword);
        } else {
            // CASO B: El usuario acaba de entrar o borró el buscador
            // Vamos a la BD y pedimos TODOS
            listaJuegos = juegoService.getAllJuegos();
        }

        // Al final, sea cual sea el caso, mandamos 'listaJuegos' a la vista
        model.addAttribute("listaJuegos", listaJuegos);
        model.addAttribute("keyword", keyword); // Para que el input no se borre

        return "index";
    }
    @GetMapping("/comprar/{id}")
    public String verDetalleJuego(@PathVariable("id") Long id, Model model) {

        // Buscamos el juego.
        // Nota: Asumo que tienes un método findById en tu servicio.
        // Si no, puedes filtrar la lista getAllJuegos() como hicimos en la IA.
        Optional<Juego> juegoOpt = juegoService.getAllJuegos().stream()
                .filter(j -> j.getIdJuego() == id)
                .findFirst();

        if (juegoOpt.isPresent()) {
            model.addAttribute("juego", juegoOpt.get());
            List<Resenha> listaResenas = resenhaRepositorio.findByJuegoId(id);
            // Las pasamos a la vista HTML
            model.addAttribute("listaResenas", listaResenas);
            return "detalles_juego"; // Nombre del nuevo HTML
        } else {
            return "redirect:/"; // Si no existe, vuelve al inicio
        }
    }
}

    /*
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


