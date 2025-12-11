package com.es.unex.cum.mdai.Steem.Services;

import com.es.unex.cum.mdai.Steem.Modelo.Biblioteca;
import com.es.unex.cum.mdai.Steem.Modelo.Cliente;
import com.es.unex.cum.mdai.Steem.Modelo.Juego;
import com.es.unex.cum.mdai.Steem.Repositorio.BibliotecaRepositorio;
import com.es.unex.cum.mdai.Steem.Repositorio.JuegoRepositorio;
import com.es.unex.cum.mdai.Steem.Repositorio.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public void comprarJuego(long userId, long juegoId) {
        Cliente cliente = (Cliente) usuarioRepositorio.findById(userId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Juego juegoComprado = juegoRepositorio.findById(juegoId).orElseThrow(() -> new RuntimeException("Juego no encontrado"));

        if (cliente == null || juegoComprado == null) {
            throw new RuntimeException("Usuario o juego no encontrado");
        }

        if (tieneJuego(userId,juegoId)){
            throw new RuntimeException("El usuario ya posee este juego");
        }

        if (cliente.getSaldo() < juegoComprado.getPrecio()) {
            throw new RuntimeException("Saldo insuficiente para comprar el juego");
        }

        cliente.restarSaldo(juegoComprado.getPrecio());
        usuarioRepositorio.save(cliente);

        Biblioteca nuevaEntrada = new Biblioteca();
        nuevaEntrada.setCliente(cliente);
        nuevaEntrada.setJuego(juegoComprado);
        nuevaEntrada.setFechaAdquisicion(new java.util.Date());
        bibliotecaRepositorio.save(nuevaEntrada);

    }

    @Override
    public void reembolsarJuego(long user, long juego) {

    }

    @Override
    public List<Biblioteca> getBiblioteca(long user) {
        return List.of();
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
