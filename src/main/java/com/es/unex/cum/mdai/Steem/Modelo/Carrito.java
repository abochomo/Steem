package com.es.unex.cum.mdai.Steem.Modelo;


import java.util.List;

/**
 * Clase que representa un carrito de compras en la plataforma Steem.
 * Maneja la lógica relacionada con los juegos añadidos al carrito por un cliente. Así como gestionar la compra de los
 * mismos y generar el cargo correspondiente. También crea las compras asociadas a los juegos comprados. Como la clase Compra
 * solo almacena un juego, el carrito se encarga de crear una compra por cada juego que contiene y añadirlas al historial de compras.
 */

public class Carrito {
    private List<Juego> juegosEnCarrito;
    private double precioTotal;


    public List<Juego> getJuegosEnCarrito() {
        return juegosEnCarrito;
    }
    public void setJuegosEnCarrito(List<Juego> juegosEnCarrito) {
        this.juegosEnCarrito = juegosEnCarrito;
    }
    public double getPrecioTotal() {
        return precioTotal;
    }
    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }

    public void agregarJuego(Juego juego) {
        juegosEnCarrito.add(juego);
        precioTotal += juego.getPrecio();
    }

    public void eliminarJuego(Juego juego) {
        if (juegosEnCarrito.remove(juego)) {
            precioTotal -= juego.getPrecio();
        }
    }

    public void vaciarCarrito() {
        juegosEnCarrito.clear();
        precioTotal = 0.0;
    }

    public void procesarCompra(Cliente cliente) {
        for (Juego juego : juegosEnCarrito) {
            Compra compra = new Compra();
            compra.setCliente(cliente);
            compra.setJuego(juego);
            // Aquí se podría añadir lógica para generar un ID de compra, fecha, etc.
            cliente.getHistorialCompras().add(compra);
        }
        vaciarCarrito();
    }


}
