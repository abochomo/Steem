package com.es.unex.cum.mdai.Steem.Modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "desarrollador")
public class Desarrollador extends Usuario {
    private String nombreEstudio;
    @OneToMany(mappedBy = "desarrollador", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Juego> juegosPublicados;

    public String getNombreEstudio() {
        return nombreEstudio;
    }

    public void setNombreEstudio(String nombreEstudio) {
        this.nombreEstudio = nombreEstudio;
    }

    public List<Juego> getJuegosPublicados() {
        return juegosPublicados;
    }

    public void setJuegosPublicados(List<Juego> juegosPublicados) {
        this.juegosPublicados = juegosPublicados;
    }
}
