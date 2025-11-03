package com.es.unex.cum.mdai.Steem.Modelo;

import java.util.List;

public class Desarrollador extends Usuario {
    private String nombreEstudio;
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
