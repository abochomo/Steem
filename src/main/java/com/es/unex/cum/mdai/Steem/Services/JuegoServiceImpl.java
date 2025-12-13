package com.es.unex.cum.mdai.Steem.Services;

import com.es.unex.cum.mdai.Steem.Modelo.Juego;
import com.es.unex.cum.mdai.Steem.Repositorio.JuegoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JuegoServiceImpl implements JuegoService {

    @Autowired
    private JuegoRepositorio juegoRepositorio;

    private Juego juego;

    @Override
    public void addJuego(Juego juego) {
        if (juego != null) {
            if (juegoRepositorio.findJuegoById(juego.getId()) == null) {
                juegoRepositorio.save(juego);
            }
        }
    }

    @Override
    public Juego getJuegoById(long id) {
        if (id <= 0) {
            return null;
        }
        return juegoRepositorio.findJuegoById(id);
    }

    @Override
    public void updateJuego(Juego juego) {
        if (juego!=null){
            if (juegoRepositorio.findJuegoById(juego.getId())!=null)
            {
                juegoRepositorio.save(juego);
            }
        }
    }

    @Override
    public Juego buscarJuegoPorTitulo(String titulo) {
        if (titulo == null || titulo.isEmpty()) {
            return null;
        }
        Optional<Juego> juego = juegoRepositorio.findJuegoByTitulo(titulo);
        if (!juego.isPresent()) {
            return null;
        }
        else{
            return juego.get();
        }
    }

    @Override
    public List<Juego> getAllJuegos() {
        return juegoRepositorio.findAll();
    }

    @Override
    public void guardarVariosJuegos(List<Juego> juegos) {
        if (juegos != null && !juegos.isEmpty()) {
            juegoRepositorio.saveAll(juegos);
        }
    }

    @Override
    public List<Juego> buscarJuegos(String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            // Usa el método nuevo del repositorio para buscar coincidencias parciales
            return juegoRepositorio.findByTituloContainingIgnoreCase(keyword);
        }
        // Si el usuario buscó "nada" (vacío), devolvemos todos los juegos
        return juegoRepositorio.findAll();
    }

    @Override
    public List<Juego> getJuegosByDesarrollador(Long idDesarrollador) {
        // ACTUALIZACIÓN: Llamamos al nuevo nombre del método
        return juegoRepositorio.findByDesarrollador_Id(idDesarrollador);
    }

}
