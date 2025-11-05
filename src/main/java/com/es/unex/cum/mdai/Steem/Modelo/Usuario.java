package com.es.unex.cum.mdai.Steem.Modelo;

import java.util.Date;

public abstract class Usuario {
    protected int idUsuario;
    protected String nombreUsuario;
    protected String email;
    protected String password;
    protected Date fechaRegistro;

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public boolean validarPassword(String password) {
        return this.password.equals(password);
    }

    public abstract String getTipoUsuario();
}
