package com.es.unex.cum.mdai.Steem.Modelo;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "juego")
public class Juego {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idJuego;
    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = true)
    @JsonIgnore
    private Desarrollador desarrollador;
    @NotBlank(message = "El título no puede estar vacío ni contener solo espacios")
    @Size(min = 3, max = 50, message = "El título debe tener entre 3 y 50 caracteres")
    private String titulo;
    @NotNull(message = "El precio es obligatorio")
    @Min(value = 0, message = "El precio no puede ser negativo")
    @Max(value = 999, message = "El precio no puede superar los 999€")
    private float precio;
    private String categoria;
    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 10, max = 300, message = "La descripción debe tener entre 10 y 300 caracteres")
    private String descripcion;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaPublicacion;
    private String imagenUrl;

    @OneToMany(mappedBy = "juego")
    @JsonIgnore
    private Set<Biblioteca> bibliotecas = new HashSet<>();

    public float getPrecio() {
        return precio;
    }

    public long getIdJuego() {
        return idJuego;
    }

    public void setIdJuego(long idJuego) {
        this.idJuego = idJuego;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }


    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    public Date getFechaPublicacion() {
        return fechaPublicacion;
    }
    public void setFechaPublicacion(Date fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }
    public Desarrollador getDesarrollador() {
        return desarrollador;
    }
    public void setDesarrollador(Desarrollador desarrollador) {
        this.desarrollador = desarrollador;
    }

    public long getId() {
        return idJuego;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }
    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }
}


