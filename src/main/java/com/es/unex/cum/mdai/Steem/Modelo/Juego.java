package com.es.unex.cum.mdai.Steem.Modelo;

import jakarta.persistence.*;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "juego")
public class Juego {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idJuego;
    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = true)
    private Desarrollador desarrollador;
    private String titulo;
    private double precio;
    private String categoria;
    private String descripcion;
    private Date fechaPublicacion;

    @OneToMany(mappedBy = "juego")
    private Set<Biblioteca> bibliotecas = new HashSet<>();

    public double getPrecio() {
        return precio;
    }

    public long getIdJuego() {
        return idJuego;
    }

    public void setIdJuego(long idJuego) {
        this.idJuego = idJuego;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
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


    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    public Date getFechaPublicacion() {
        return fechaPublicacion;
    }
    public void setFechaPublicacion(Date fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }
    public Desarrollador getDesarrollador() {
        return desarrollador;
    }
    public void setDesarrollador(Desarrollador desarrollador) {
        this.desarrollador = desarrollador;
    }

    public long getId() {
        return idJuego;
    }
}


