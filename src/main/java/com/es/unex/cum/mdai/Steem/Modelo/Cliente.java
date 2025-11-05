package com.es.unex.cum.mdai.Steem.Modelo;

import java.util.List;

public class Cliente extends Usuario{
    private List<Juego> biblioteca;
    private Carrito carrito;
    private List<Compra> historialCompras;

    public List<Compra> getHistorialCompras() {
        return (List<Compra>) historialCompras;
    }

    public List<Juego> getBiblioteca() {
        return biblioteca;
    }

    public void setBiblioteca(List<Juego> biblioteca) {
        this.biblioteca = biblioteca;
    }

    public Carrito getCarrito() {
        return carrito;
    }

    public void setCarrito(Carrito carrito) {
        this.carrito = carrito;
    }

    public void setHistorialCompras(List<Compra> historialCompras) {
        this.historialCompras = historialCompras;
    }

    @Override
    public String getTipoUsuario() {
        return "Cliente";
    }

}
