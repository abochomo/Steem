package com.es.unex.cum.mdai.Steem.Repositorio;

import com.es.unex.cum.mdai.Steem.Modelo.Desarrollador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DesarrolladorRepositorio extends JpaRepository<Desarrollador, Long> {
}
