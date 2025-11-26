package com.es.unex.cum.mdai.Steem.Repositorio;

import com.es.unex.cum.mdai.Steem.Modelo.Biblioteca;
import org.springframework.boot.context.properties.bind.validation.BindValidationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BibliotecaRepositorio extends JpaRepository<Biblioteca, Integer> {
    public Biblioteca getBibliotecaById(Long id);
    public Biblioteca findBibliotecaByClienteIdAndJuegoId(Long clienteId, Long juegoId);
    List<Biblioteca> findAllByClienteId(Long idUsuario);
}
