package com.es.unex.cum.mdai.Steem.Modelo;

import java.util.List;

public class Cliente extends Usuario{
    private List<Juego> biblioteca;
    private Carrito carrito;
    private List<Compra> historialCompras;

    public List<Compra> getHistorialCompras() {
        return (List<Compra>) historialCompras;
    }
}
