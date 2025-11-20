package com.es.unex.cum.mdai.Steem;

import com.es.unex.cum.mdai.Steem.Modelo.*;
import com.es.unex.cum.mdai.Steem.Repositorio.*; // Asegúrate de importar tus repositorios
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class SteemApplicationTests {

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Autowired
    private DesarrolladorRepositorio desarrolladorRepositorio;

    @Autowired
    private JuegoRepositorio juegoRepositorio;

    @Autowired
    private BibliotecaRepositorio bibliotecaRepositorio;

    // Variables para reusar en los tests
    private Desarrollador dev;
    private Juego juego1;

    @BeforeEach
    void setUp() {
        // Preparamos un desarrollador y un juego base para las pruebas
        dev = new Desarrollador();
        dev.setNombreUsuario("ValveLover");
        dev.setEmail("gabe@valve.com");
        dev.setPassword("1234");
        dev.setFechaRegistro(new Date());
        dev.setNombreEstudio("Valve");
        desarrolladorRepositorio.save(dev);

        juego1 = new Juego();
        juego1.setTitulo("Half-Life 3");
        juego1.setDescripcion("The legend");
        juego1.setPrecio(59.99);
        juego1.setCategoria("Shooter");
        juego1.setFechaPublicacion(new Date());
        juego1.setDesarrollador(dev);
        juegoRepositorio.save(juego1);
    }

    @Test
    @DisplayName("Test de Herencia: Verificar que Cliente y Desarrollador se guardan y distinguen")
    void testHerenciaUsuarios() {
        Cliente cliente = new Cliente();
        cliente.setNombreUsuario("Gamer1");
        cliente.setEmail("gamer@test.com");
        cliente.setPassword("pass");
        cliente.setFechaRegistro(new Date());
        cliente.setFechaNacimiento(new Date());

        clienteRepositorio.save(cliente);

        // Verificamos que se guardó
        assertNotNull(cliente.getIdUsuario());

        // Verificamos polimorfismo
        assertEquals("Cliente", cliente.getTipoUsuario());
        assertEquals("Desarrollador", dev.getTipoUsuario());
    }

    @Test
    @DisplayName("Edge Case: Restricción de Nombre de Usuario Único")
    void testUniqueUsernameConstraint() {
        // Creamos un cliente con un nombre que ya existe (el del desarrollador creado en setUp)
        Cliente clona = new Cliente();
        clona.setNombreUsuario("ValveLover"); // Nombre duplicado
        clona.setEmail("otro@email.com");
        clona.setPassword("1234");

        // Intentar guardar debería lanzar una excepción de integridad de datos
        assertThrows(DataIntegrityViolationException.class, () -> {
            clienteRepositorio.saveAndFlush(clona);
        });
    }

    @Test
    @DisplayName("Edge Case: Borrado en Cascada (Cliente -> Biblioteca)")
    void testCascadeDeleteClienteBiblioteca() {
        // 1. Crear Cliente
        Cliente cliente = new Cliente();
        cliente.setNombreUsuario("Borrable");
        cliente.setEmail("borrable@test.com");
        cliente.setPassword("1234");
        // Asegúrate de inicializar la lista si tu constructor no lo hace
        if (cliente.getBibliotecas() == null) {
            cliente.setBibliotecas(new java.util.HashSet<>());
        }
        clienteRepositorio.save(cliente);

        // 2. Crear entrada en Biblioteca
        Biblioteca entradaBiblio = new Biblioteca();
        entradaBiblio.setCliente(cliente);
        entradaBiblio.setJuego(juego1);
        entradaBiblio.setFechaAdquisicion(new Date());

        // --- CRUCIAL: MANTENER LA COHERENCIA BIDIRECCIONAL ---
        // Añadimos la biblioteca a la lista del cliente para que JPA sepa que existe
        // y pueda aplicar el borrado en cascada.
        cliente.getBibliotecas().add(entradaBiblio);
        // -----------------------------------------------------

        // Guardamos. Al tener CascadeType.ALL en Cliente, esto a menudo guarda
        // la biblioteca automáticamente, pero guardar explícitamente tampoco daña.
        bibliotecaRepositorio.save(entradaBiblio);

        Long idBiblioteca = (long) entradaBiblio.getId();
        Long idCliente = cliente.getIdUsuario();

        assertTrue(bibliotecaRepositorio.existsById(Math.toIntExact(idBiblioteca)));

        // 3. BORRAR CLIENTE
        clienteRepositorio.deleteById(idCliente);

        // Forzamos el flush
        clienteRepositorio.flush();

        // 4. VERIFICACIONES
        assertFalse(clienteRepositorio.existsById(idCliente), "El cliente debería haber sido borrado");
        assertFalse(bibliotecaRepositorio.existsById(Math.toIntExact(idBiblioteca)), "La entrada de biblioteca debería haber sido borrada en cascada");
        assertTrue(juegoRepositorio.existsById((int) juego1.getIdJuego()), "El juego NO debería borrarse");
    }

    @Test
    @DisplayName("Lógica de Negocio: Carrito de Compras")
    void testLogicaCarrito() {
        // Este test no toca la base de datos, prueba la lógica de la clase POJO Carrito
        Carrito carrito = new Carrito();
        carrito.setJuegosEnCarrito(new java.util.ArrayList<>()); // Inicializar lista

        // Agregar juego
        carrito.agregarJuego(juego1);

        assertEquals(1, carrito.getJuegosEnCarrito().size());
        assertEquals(59.99, carrito.getPrecioTotal(), 0.01);

        // Edge Case: Vaciar carrito
        carrito.vaciarCarrito();
        assertEquals(0, carrito.getJuegosEnCarrito().size());
        assertEquals(0.0, carrito.getPrecioTotal());
    }
}
