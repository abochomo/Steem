// java
package com.es.unex.cum.mdai.Steem.Modelo;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cliente")
public class Cliente extends Usuario {



    @Override
    public String getTipoUsuario() {
        return "Cliente";
    }
}