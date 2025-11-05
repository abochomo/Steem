package com.es.unex.cum.mdai.Steem.Modelo;

public class Resenha {
    private int idResenha;
    private Cliente cliente;
    private Juego juego;
    private String comentario;
    private int valoracion; //(1-10)

    public int getIdResenha() {
        return idResenha;
    }

    public void setIdResenha(int idResenha) {
        this.idResenha = idResenha;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Juego getJuego() {
        return juego;
    }

    public void setJuego(Juego juego) {
        this.juego = juego;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public int getValoracion() {
        return valoracion;
    }

    public void setValoracion(int valoracion) {
        this.valoracion = valoracion;
    }
}
