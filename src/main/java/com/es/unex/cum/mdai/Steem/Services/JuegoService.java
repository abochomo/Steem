package com.es.unex.cum.mdai.Steem.Services;

import com.es.unex.cum.mdai.Steem.Modelo.Juego;

import java.util.List;

public interface JuegoService {
    public void addJuego(Juego juego);

    public Juego getJuegoById(long id);

    public void updateJuego(Juego juego);

    public Juego buscarJuegoPorTitulo(String titulo);

    public List<Juego> getAllJuegos();

    public void guardarVariosJuegos(List<Juego> juegos);
}
