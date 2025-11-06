package com.es.unex.cum.mdai.Steem.Modelo;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "administrador")
public class Administrador extends Usuario{

    @Column(name = "nivel_acceso", nullable = false)
    private int nivelAcceso;

    public int getNivelAcceso() {
        return nivelAcceso;
    }
    public void setNivelAcceso(int nivelAcceso) {
        this.nivelAcceso = nivelAcceso;
    }

    @Override
    public String getTipoUsuario() {
        return "Administrador";
    }
}
