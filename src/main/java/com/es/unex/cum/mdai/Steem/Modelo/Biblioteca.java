package com.es.unex.cum.mdai.Steem.Modelo;

import java.util.List;

public class Biblioteca {
    private int Cliente;
    private List<Juego> juegos;

    public int getCliente() {
        return Cliente;
    }

    public void setCliente(int cliente) {
        Cliente = cliente;
    }

    public List<Juego> getJuegos() {
        return juegos;
    }

    public void setJuegos(List<Juego> juegos) {
        this.juegos = juegos;
    }

    private boolean contieneJuego(Juego juego) {
        return juegos.contains(juego);
    }

    private void agregarJuego(Juego juego) {
        if (!contieneJuego(juego)) {
            juegos.add(juego);
        }
    }
}
