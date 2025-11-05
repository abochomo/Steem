package com.es.unex.cum.mdai.Steem.Modelo;

import java.util.List;

public class SeccionComunidad {
    private Juego juego;
    private List<Resenha> foro;

    public Juego getJuego() {
        return juego;
    }

    public void setJuego(Juego juego) {
        this.juego = juego;
    }

    public List<Resenha> getForo() {
        return foro;
    }

    public void setForo(List<Resenha> foro) {
        this.foro = foro;
    }
}
