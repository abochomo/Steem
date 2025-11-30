package com.es.unex.cum.mdai.Steem.Services;

import com.es.unex.cum.mdai.Steem.Modelo.Biblioteca;
import com.es.unex.cum.mdai.Steem.Repositorio.BibliotecaRepositorio;
import com.es.unex.cum.mdai.Steem.Repositorio.JuegoRepositorio;
import com.es.unex.cum.mdai.Steem.Repositorio.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BibliotecaServiceImpl implements BibliotecaService {

    @Autowired
    private BibliotecaRepositorio bibliotecaRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private JuegoRepositorio juegoRepositorio;

    private Biblioteca bibliotecaActual;

    @Override
    public void comprarJuego(long user, long juego) {

    }

    @Override
    public void reembolsarJuego(long user, long juego) {

    }

    @Override
    public List<Biblioteca> getBiblioteca(long user) {
        return List.of();
    }

    @Override
    public void setBiblioteca(Biblioteca biblioteca) {

    }

    @Override
    public boolean tieneJuego(long userId, long juegoId) {
        Optional<Biblioteca> biblioteca = bibliotecaRepositorio.findBibliotecaByClienteIdAndJuegoId(userId, juegoId);
        if (biblioteca.isPresent()) {
            return true;
        } else {
            return false;
        }
    }
}
