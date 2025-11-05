package com.es.unex.cum.mdai.Steem.Modelo;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import java.util.Date;


@MappedSuperclass
public abstract class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int idUsuario;

    @Column(nullable = false, unique = true)
    protected String nombreUsuario;
    protected String email;
    protected String password;
    protected Date fechaRegistro;


}
