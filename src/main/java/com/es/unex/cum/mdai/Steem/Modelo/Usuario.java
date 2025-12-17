package com.es.unex.cum.mdai.Steem.Modelo;

import jakarta.persistence.*;

import java.util.Date;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "usuario")
public abstract class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;

    @Column(nullable = false, unique = true)
    protected String nombreUsuario;
    protected String email;
    protected String password;
    protected Date fechaRegistro;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoUsuario tipo;

    public enum TipoUsuario {
        CLIENTE, DESARROLLADOR
    }

    public long getIdUsuario() {
        return id;
    }

    public long getId() {return id;}

    public void setIdUsuario(long idUsuario) {
        this.id = idUsuario;
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

    public String getTipoUsuario(){
        return tipo.name();
    }

    public TipoUsuario getTipo() {
        return tipo;
    }

    public void setTipoUsuario(TipoUsuario tipo) {
        this.tipo = tipo;
    }

    public void setTipo(TipoUsuario tipo) {
        this.tipo = tipo;
    }

    public Usuario (){
    }
}
