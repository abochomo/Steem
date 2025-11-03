package com.es.unex.cum.mdai.Steem.Modelo;

public class Compra {
    private int idCompra;
    private Cliente cliente;
    private Juego juego;

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setJuego(Juego juego) {
        this.juego = juego;
    }
}
