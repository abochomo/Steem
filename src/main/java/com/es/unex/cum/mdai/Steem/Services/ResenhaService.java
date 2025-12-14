package com.es.unex.cum.mdai.Steem.Services;

import com.es.unex.cum.mdai.Steem.Modelo.Resenha;

public interface ResenhaService {
    void publicarResenha(Resenha resenha, Long idJuego, String emailUsuario);
}