package com.es.unex.cum.mdai.Steem.Repositorio;

import com.es.unex.cum.mdai.Steem.Modelo.Resenha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResenhaRepositorio extends JpaRepository<Resenha, Long> {

    List<Resenha> findByJuego_IdJuego(long juegoId);

    // CORRECCIÓN: Cambiamos 'Cliente_IdUsuario' por 'Cliente_Id'
    // Spring busca: Entidad Resenha -> Campo 'cliente' -> Entidad Cliente -> Campo 'id'
    Optional<Resenha> findByCliente_IdAndJuego_IdJuego(Long idUsuario, Long idJuego);
}