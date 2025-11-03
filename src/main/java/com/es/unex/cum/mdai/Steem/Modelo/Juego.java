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

    public Juego(int idJuego, String titulo, String descripcion, String sinopsis, double precio, Date fechaLanzamiento, Desarrollador desarrollador, Categoria categoria, boolean activo) {
        this.idJuego = idJuego;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.sinopsis = sinopsis;
        this.precio = precio;
        this.fechaLanzamiento = fechaLanzamiento;
        this.desarrollador = desarrollador;
        this.categoria = categoria;
        this.activo = activo;
    }

    public int getIdJuego() {
        return idJuego;
    }

    public void setIdJuego(int idJuego) {
        this.idJuego = idJuego;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public Date getFechaLanzamiento() {
        return fechaLanzamiento;
    }

    public void setFechaLanzamiento(Date fechaLanzamiento) {
        this.fechaLanzamiento = fechaLanzamiento;
    }

    public Desarrollador getDesarrollador() {
        return desarrollador;
    }

    public void setDesarrollador(Desarrollador desarrollador) {
        this.desarrollador = desarrollador;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
