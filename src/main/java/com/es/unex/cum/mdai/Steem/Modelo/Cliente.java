// java
package com.es.unex.cum.mdai.Steem.Modelo;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cliente")
public class Cliente extends Usuario {

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "cliente_biblioteca",
            joinColumns = @JoinColumn(name = "cliente_id"),
            inverseJoinColumns = @JoinColumn(name = "juego_id")
    )
    private List<Juego> biblioteca = new ArrayList<>();

    @Transient
    private Carrito carrito = new Carrito();

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Compra> historialCompras = new ArrayList<>();

    public List<Compra> getHistorialCompras() {
        return historialCompras;
    }

    public List<Juego> getBiblioteca() {
        return biblioteca;
    }

    public void setBiblioteca(List<Juego> biblioteca) {
        this.biblioteca = biblioteca != null ? biblioteca : new ArrayList<>();
    }

    public Carrito getCarrito() {
        return carrito;
    }

    public void setCarrito(Carrito carrito) {
        this.carrito = carrito;
    }

    public void setHistorialCompras(List<Compra> historialCompras) {
        this.historialCompras = historialCompras != null ? historialCompras : new ArrayList<>();
    }

    @Override
    public String getTipoUsuario() {
        return "Cliente";
    }
}