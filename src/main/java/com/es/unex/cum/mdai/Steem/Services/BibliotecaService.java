package com.es.unex.cum.mdai.Steem.Services;

import com.es.unex.cum.mdai.Steem.Modelo.Biblioteca;

import java.util.List;

public interface BibliotecaService {
    public void comprarJuego(long user, long juego);
    public void reembolsarJuego(long user, long juego);
    public List<Biblioteca> getBiblioteca(long user);
    public boolean tieneJuego(long juego, long user);
}
