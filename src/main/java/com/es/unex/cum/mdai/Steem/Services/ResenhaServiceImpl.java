package com.es.unex.cum.mdai.Steem.Services;

import com.es.unex.cum.mdai.Steem.Modelo.Cliente;
import com.es.unex.cum.mdai.Steem.Modelo.Juego;
import com.es.unex.cum.mdai.Steem.Modelo.Resenha;
import com.es.unex.cum.mdai.Steem.Modelo.Usuario;
import com.es.unex.cum.mdai.Steem.Repositorio.JuegoRepositorio;
import com.es.unex.cum.mdai.Steem.Repositorio.ResenhaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ResenhaServiceImpl implements ResenhaService {

    @Autowired
    private ResenhaRepositorio resenhaRepositorio;

    @Autowired
    private JuegoRepositorio juegoRepositorio;

    @Autowired
    private UsuarioService usuarioService; // Usamos tu servicio existente

    @Override
    public void publicarResenha(Resenha resenha, Long idJuego, String emailUsuario) {
        // 1. Buscamos el juego
        Juego juego = juegoRepositorio.findById(idJuego)
                .orElseThrow(() -> new RuntimeException("Juego no encontrado"));

        // 2. Buscamos el usuario (Cliente)
        Usuario usuario = usuarioService.findUserByEmail(emailUsuario);
        if (!(usuario instanceof Cliente)) {
            throw new RuntimeException("Solo los clientes pueden escribir reseñas");
        }

        // 3. Configuramos la reseña
        resenha.setJuego(juego);
        resenha.setCliente((Cliente) usuario);
        resenha.setFechaPublicacion(new Date());

        // 4. Guardamos
        resenhaRepositorio.save(resenha);
    }

    @Override
    public void borrarResenha(Long idResenha, String emailUsuario) {
        // 1. Buscamos la reseña
        Resenha resenha = resenhaRepositorio.findById(idResenha)
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada"));

        // 2. SEGURIDAD: Verificamos que quien quiere borrar es el dueño
        if (!resenha.getCliente().getEmail().equals(emailUsuario)) {
            throw new RuntimeException("No tienes permiso para borrar esta reseña");
        }

        // 3. Borramos
        resenhaRepositorio.delete(resenha);
    }

    @Override
    public List<Resenha> listarTodas() {
        return resenhaRepositorio.findAll();
    }

    // 2. BORRADO DE ADMIN (Sin comprobar el email del dueño)
    @Override
    public void borrarResenhaAdmin(Long idResenha) {
        if (resenhaRepositorio.existsById(idResenha)) {
            resenhaRepositorio.deleteById(idResenha);
        } else {
            throw new RuntimeException("La reseña no existe");
        }
    }
}