package com.es.unex.cum.mdai.Steem.Services;

import com.es.unex.cum.mdai.Steem.Modelo.Resenha;

import java.util.List;

public interface ResenhaService {
    void publicarResenha(Resenha resenha, Long idJuego, String emailUsuario);
    void borrarResenha(Long idResenha, String emailUsuario);

    List<Resenha> listarTodas();

    // 2. BORRADO DE ADMIN (Sin comprobar el email del dueño)
    void borrarResenhaAdmin(Long idResenha);
}