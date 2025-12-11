package com.es.unex.cum.mdai.Steem.Repositorio;

import com.es.unex.cum.mdai.Steem.Modelo.Juego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JuegoRepositorio extends JpaRepository<Juego, Long> {

    Optional<Juego> findJuegoByTitulo(String titulo);

    Juego findJuegoById(long id);

    List<Juego> findByTituloContainingIgnoreCase(String titulo);
}