package com.es.unex.cum.mdai.Steem.Repositorio;

import com.es.unex.cum.mdai.Steem.Modelo.Resenha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List; // <--- Importante

@Repository
public interface ResenhaRepositorio extends JpaRepository<Resenha, Long> { // Nota: Long, no Integer si tu ID es long

    List<Resenha> findByJuegoId(long juegoId);
}