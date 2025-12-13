// java
package com.es.unex.cum.mdai.Steem.Modelo;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "cliente")
public class Cliente extends Usuario {
    private Date fechaNacimiento;

    private float saldo;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Biblioteca> bibliotecas = new HashSet<>();

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Resenha> resenhas = new HashSet<>();

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Set<Biblioteca> getBibliotecas() {
        return bibliotecas;
    }
    public void setBibliotecas(Set<Biblioteca> bibliotecas) {
        this.bibliotecas = bibliotecas;
    }
    public Set<Resenha> getResenhas() {
        return resenhas;
    }
    public void setResenhas(Set<Resenha> resenhas) {
        this.resenhas = resenhas;
    }

    public Cliente () {
    }

    public float getSaldo() {
        return saldo;
    }

    public void setSaldo(float saldo) {
        this.saldo = saldo;
    }

    public void restarSaldo(float cantidad) {
        this.saldo -= cantidad;
    }
    public void sumarSaldo(float cantidad) {
        this.saldo += cantidad;
    }

}