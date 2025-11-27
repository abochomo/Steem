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
    private boolean activo=true;


    @OneToMany(mappedBy = "desarrollador", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Juego> juegosPublicados = new ArrayList<>();

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

    @Override
    public String getTipoUsuario() {
        return "Desarrollador";
    }

    public boolean isActivo() {
        return activo;
    }
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    public void desactivarDesarrollador(){
        this.activo=false;
    }
}
