package com.es.unex.cum.mdai.Steem.Services;

import com.es.unex.cum.mdai.Steem.Modelo.Desarrollador;
import com.es.unex.cum.mdai.Steem.Modelo.Usuario;
import com.es.unex.cum.mdai.Steem.Repositorio.DesarrolladorRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DesarrolladorServiceImpl implements DesarrolladorService {

    @Autowired
    private DesarrolladorRepositorio desarrolladorRepositorio;

    private Desarrollador desarrolladorActual;

    @Override
    public void registroDesarrollador(Desarrollador dev) {
        if (dev.getEmail() != null && desarrolladorRepositorio.findByEmail(dev.getEmail()) != null) {
            throw new RuntimeException("El email ya está registrado");
        }
        if (dev.getNombreUsuario() == null) {
            throw new RuntimeException("El nombre de usuario no puede ser nulo");
        }
        dev.setTipo(Usuario.TipoUsuario.DESARROLLADOR);
        dev.setFechaRegistro(new java.util.Date());

        desarrolladorRepositorio.save(dev);
    }
}
