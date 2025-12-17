package com.es.unex.cum.mdai.Steem.Repositorio;

import com.es.unex.cum.mdai.Steem.Modelo.Biblioteca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BibliotecaRepositorio extends JpaRepository<Biblioteca, Long> {
    Optional<Biblioteca> findBibliotecaByClienteIdAndJuegoId(Long clienteId, Long juegoId);
    Optional<List<Biblioteca>> findAllByClienteId(Long idUsuario);
    List<Biblioteca> findByCliente_Id(Long idCliente);
    boolean existsByJuego_IdJuego(long idJuego);}
