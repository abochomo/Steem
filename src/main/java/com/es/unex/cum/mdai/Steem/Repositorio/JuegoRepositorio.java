package com.es.unex.cum.mdai.Steem.Repositorio;

import com.es.unex.cum.mdai.Steem.Modelo.Juego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JuegoRepositorio extends JpaRepository<Juego, Integer> {
    // Ejemplo: Buscar juegos por categoría automáticamente
    // List<Juego> findByCategoria(String categoria);
}