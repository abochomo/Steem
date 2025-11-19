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

    @Override
    public String getTipoUsuario() {
        return "Cliente";
    }
}