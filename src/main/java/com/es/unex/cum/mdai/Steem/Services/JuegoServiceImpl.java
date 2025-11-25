package com.es.unex.cum.mdai.Steem.Services;

import com.es.unex.cum.mdai.Steem.Modelo.Juego;
import com.es.unex.cum.mdai.Steem.Repositorio.JuegoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JuegoServiceImpl implements JuegoService {

    @Autowired
    private JuegoRepositorio juegoRepositorio;

    private Juego juego;

    @Override
    public void addJuego(Juego juego) {
        if (juego != null) {
            if (juegoRepositorio.getJuegoById(juego.getId()) == null) {
                juegoRepositorio.save(juego);
            }
        }
    }

    @Override
    public Juego getJuegoById(long id) {
        if (id <= 0) {
            return null;
        }
        return juegoRepositorio.getJuegoById(id);
    }

    @Override
    public void updateJuego(Juego juego) {
        if (juego!=null){
            if (juegoRepositorio.getJuegoById(juego.getId())!=null)
            {
                juegoRepositorio.save(juego);
            }
        }
    }

    @Override
    public void deleteJuego(Juego juego) {
        if (juego!=null){
            if (juegoRepositorio.getJuegoById(juego.getId())!=null)
            {
                juegoRepositorio.delete(juego);
            }
        }
    }

    @Override
    public Juego buscarJuegoPorTitulo(String titulo) {
        if (titulo == null || titulo.isEmpty()) {
            return null;
        }
        return juegoRepositorio.findByTitulo(titulo);
    }

    @Override
    public List<Juego> getAllJuegos() {
        return juegoRepositorio.findAll();
    }
}
