package com.es.unex.cum.mdai.Steem;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SteemApplicationTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void shouldFindTestUser() {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM usuario WHERE email = 'testuser@example.com'",
                Integer.class
        );
        assertNotNull(count);
        assertTrue(count > 0, "Test user should exist");
    }

    @Test
    void shouldFindJuegazoWithCategory() {
        String sql = "SELECT c.nombre FROM juego j JOIN categoria c ON j.id_categoria = c.id_categoria WHERE j.titulo = ?";
        String categoria = jdbcTemplate.queryForObject(sql, new Object[]{"Juegazo"}, String.class);
        assertEquals("RPG", categoria);
    }

    @Test
    void shouldFindDeveloperAndCliente() {
        int devCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM desarrollador WHERE id_desarrollador = 1 AND id_usuario = 1", Integer.class);
        int cliCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM cliente WHERE id_cliente = 1 AND id_usuario = 1", Integer.class);
        assertEquals(1, devCount);
        assertEquals(1, cliCount);
    }
}