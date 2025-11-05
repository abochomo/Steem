package com.es.unex.cum.mdai.Steem.Modelo;

public class Administrador extends Usuario{
    private int idAdministrador;
    private int nivelAcceso;

    public int getIdAdministrador() {
        return idAdministrador;
    }
    public void setIdAdministrador(int idAdministrador) {
        this.idAdministrador = idAdministrador;
    }
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
