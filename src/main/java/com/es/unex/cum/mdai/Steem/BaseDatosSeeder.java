package com.es.unex.cum.mdai.Steem;

import com.es.unex.cum.mdai.Steem.Modelo.*;
import com.es.unex.cum.mdai.Steem.Repositorio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class BaseDatosSeeder implements org.springframework.boot.CommandLineRunner {
    @Autowired
    ClienteRepositorio clienteRepositorio;
    @Autowired
    DesarrolladorRepositorio desarrolladorRepositorio;
    @Autowired
    JuegoRepositorio juegoRepositorio;
    @Autowired
    BibliotecaRepositorio bibliotecaRepositorio;
    @Autowired
    ResenhaRepositorio resenhaRepositorio;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private void cargarDatosIniciales() {

        if (juegoRepositorio.count() > 0) {
            System.out.println("--> Base de datos ya inicializada. Saltando Seeder.");
            return;
        }
        Cliente cliente = new Cliente();
        cliente.setNombreUsuario("JuanPerez");
        cliente.setEmail("juan@gmail.com");
        cliente.setPassword("juan");
        cliente.setPassword(passwordEncoder.encode(cliente.getPassword()));
        cliente.setFechaRegistro(new Date());
        cliente.setTipoUsuario(Usuario.TipoUsuario.CLIENTE);
        cliente.setSaldo(150.0F);
        Cliente cliente2 = new Cliente();
        cliente2.setNombreUsuario("MariaLopez");
        cliente2.setEmail("maria@gmail.com");
        cliente2.setPassword("maria");
        cliente2.setPassword(passwordEncoder.encode(cliente2.getPassword()));
        cliente2.setFechaRegistro(new Date());
        cliente2.setTipoUsuario(Usuario.TipoUsuario.CLIENTE);
        cliente2.setSaldo(200.0F);
        clienteRepositorio.save(cliente);
        clienteRepositorio.save(cliente2);

        Desarrollador desarrollador = new Desarrollador();
        desarrollador.setNombreUsuario("FernandoDev");
        desarrollador.setEmail("fernandodev@gmail.com");
        desarrollador.setPassword("fernando");
        desarrollador.setPassword(passwordEncoder.encode(desarrollador.getPassword()));
        desarrollador.setFechaRegistro(new Date());
        desarrollador.setTipoUsuario(Usuario.TipoUsuario.DESARROLLADOR);
        desarrollador.setNombreEstudio("Fernando Studios");
        desarrolladorRepositorio.save(desarrollador);

        Desarrollador desarrollador2 = new Desarrollador();
        desarrollador2.setNombreUsuario("AnaGames");
        desarrollador2.setEmail("anagames@gmail.com");
        desarrollador2.setPassword("ana");
        desarrollador2.setPassword(passwordEncoder.encode(desarrollador2.getPassword()));
        desarrollador2.setFechaRegistro(new Date());
        desarrollador2.setTipoUsuario(Usuario.TipoUsuario.DESARROLLADOR);
        desarrollador2.setNombreEstudio("Ana Games");
        desarrolladorRepositorio.save(desarrollador2);

        Desarrollador desarrollador3 = new Desarrollador();
        desarrollador3.setNombreUsuario("SandfallInteractive");
        desarrollador3.setEmail("sandfall@gmail.com");
        desarrollador3.setPassword("sandfall");
        desarrollador3.setPassword(passwordEncoder.encode(desarrollador3.getPassword()));
        desarrollador3.setFechaRegistro(new Date());
        desarrollador3.setTipoUsuario(Usuario.TipoUsuario.DESARROLLADOR);
        desarrollador3.setNombreEstudio("Sandfall Interactive");
        desarrolladorRepositorio.save(desarrollador3);

        Juego juego = new Juego();
        juego.setTitulo("Elden Ring");
        juego.setDescripcion("Un juego de rol y acción épico. ¿Tu rol? LA VICTIMA. Prepárate para sufrir como nunca antes en brutales peleas contra todo tipo de enemigos.");
        juego.setPrecio(59.99F);
        juego.setImagenUrl("/images/game-covers/elden_ring.jpg");
        juego.setDesarrollador(desarrollador);

        juegoRepositorio.save(juego);
        Juego juego2 = new Juego();
        juego2.setTitulo("Hollow Knight");
        juego2.setDescripcion("Un juego de acción y aventura en un mundo subterráneo lleno de insectos y héroes olvidados. Explora, lucha y descubre los secretos de Hallownest.");
        juego2.setPrecio(14.99F);
        juego2.setImagenUrl("/images/game-covers/hollow_knight.png");
        juego2.setDesarrollador(desarrollador2);
        juegoRepositorio.save(juego2);
        Juego juego3 = new Juego();
        juego3.setTitulo("Super Mario Odyssey");
        juego3.setDescripcion("Un juego de plataformas donde controlas a Mario en su misión para rescatar a la princesa Peach del malvado Bowser.");
        juego3.setPrecio(49.99F);
        juego3.setImagenUrl("/images/game-covers/super_mario_odyssey.jpg");
        juego3.setDesarrollador(desarrollador2);
        juegoRepositorio.save(juego3);
        Juego juego4 = new Juego();
        juego4.setTitulo("Call of Duty: Black Ops Cold War 3");
        juego4.setDescripcion("Un juego de disparos en primera persona que te sumerge en intensas misiones militares alrededor del mundo.");
        juego4.setPrecio(69.99F);
        juego4.setImagenUrl("/images/game-covers/cod_black_ops_cold_war.png");
        juego4.setDesarrollador(desarrollador);
        juegoRepositorio.save(juego4);
        Juego juego5 = new Juego();
        juego5.setTitulo("The Witcher 3: Wildly Hunted");
        juego5.setDescripcion("Un juego de rol de mundo abierto donde juegas como Geralt de Rivia, un cazador de monstruos en un mundo lleno de peligros y decisiones morales.");
        juego5.setPrecio(39.99F);
        juego5.setImagenUrl("/images/game-covers/witcher3.jpg");
        juego5.setDesarrollador(desarrollador);
        juegoRepositorio.save(juego5);
        Juego juego6 = new Juego();
        juego6.setTitulo("Cyberpunk 2077");
        juego6.setDescripcion("Un juego de rol y acción en un futuro distópico donde exploras la ciudad de Night City y te enfrentas a corporaciones corruptas y tecnología avanzada.");
        juego6.setPrecio(59.99F);
        juego6.setImagenUrl("/images/game-covers/cyberpunk2077.jpg");
        juego6.setDesarrollador(desarrollador2);
        juegoRepositorio.save(juego6);

        Juego juego7 = new Juego();
        juego7.setTitulo("Clair Obscur: Expedition 33");
        juego7.setDescripcion("GOTY del 2025. Una aventura RPG en un mundo oscuro lleno de maravillas por descubrir.");
        juego7.setPrecio(44.99F);
        juego7.setImagenUrl("/images/game-covers/Expedition33.png");
        juego7.setDesarrollador(desarrollador3);
        juego7.setCategoria("RPG");
        juego7.setFechaPublicacion(new Date());
        juegoRepositorio.save(juego7);

        Biblioteca biblioteca = new Biblioteca();
        biblioteca.setCliente(cliente);
        biblioteca.setJuego(juego);
        biblioteca.setFechaAdquisicion(new Date());
        bibliotecaRepositorio.save(biblioteca);
        Biblioteca biblioteca2 = new Biblioteca();
        biblioteca2.setCliente(cliente);
        biblioteca2.setJuego(juego3);
        biblioteca2.setFechaAdquisicion(new Date());
        bibliotecaRepositorio.save(biblioteca2);
        Biblioteca biblioteca3 = new Biblioteca();
        biblioteca3.setCliente(cliente2);
        biblioteca3.setJuego(juego2);
        biblioteca3.setFechaAdquisicion(new Date());
        bibliotecaRepositorio.save(biblioteca3);
        Biblioteca biblioteca4 = new Biblioteca();
        biblioteca4.setCliente(cliente2);
        biblioteca4.setJuego(juego5);
        biblioteca4.setFechaAdquisicion(new Date());
        bibliotecaRepositorio.save(biblioteca4);

        Resenha resenha = new Resenha();
        resenha.setCliente(cliente);
        resenha.setJuego(juego);
        resenha.setRecomendado(true);
        resenha.setTexto("¡Increíble juego! La historia es fascinante y el mundo abierto es enorme. Las batallas son desafiantes pero muy gratificantes.");
        resenhaRepositorio.save(resenha);
        Resenha resenha2 = new Resenha();
        resenha2.setCliente(cliente2);
        resenha2.setJuego(juego2);
        resenha2.setRecomendado(true);
        resenha2.setTexto("Me encantó la atmósfera del juego y el diseño de los niveles. La jugabilidad es fluida y los combates son emocionantes.");
        resenhaRepositorio.save(resenha2);
        Resenha resenha3 = new Resenha();
        resenha3.setCliente(cliente);
        resenha3.setJuego(juego3);
        resenha3.setRecomendado(false);
        resenha3.setTexto("No cumplió con mis expectativas. La historia es bastante simple y repetitiva. Aunque los gráficos son buenos, la jugabilidad se vuelve monótona rápidamente.");
        resenhaRepositorio.save(resenha3);


    }

    @Override
    public void run(String... args) throws Exception {
        cargarDatosIniciales();
    }
}
