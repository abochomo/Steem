package com.es.unex.cum.mdai.Steem.Modelo;

import java.util.Date;
import java.util.List;

public class Juego {
    private int idJuego;
    private String titulo;
    private String descripcion;
    private String sinopsis;
    private double precio;
    private Date fechaLanzamiento;
    private Desarrollador desarrollador;
    private Categoria categoria;
    private boolean activo;

    public double getPrecio() {
        return precio;
    }
}
