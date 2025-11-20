package com.es.unex.cum.mdai.Steem.Repositorio;

import com.es.unex.cum.mdai.Steem.Modelo.Biblioteca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BibliotecaRepositorio extends JpaRepository<Biblioteca, Integer> {
}
