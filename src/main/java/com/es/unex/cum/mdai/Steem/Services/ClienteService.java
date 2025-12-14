package com.es.unex.cum.mdai.Steem.Services;

import com.es.unex.cum.mdai.Steem.Modelo.Cliente;
import com.es.unex.cum.mdai.Steem.Modelo.Usuario;
import com.es.unex.cum.mdai.Steem.Repositorio.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;

public interface ClienteService {
    public Cliente findClienteById(long id);
    public Cliente findClienteByEmail(String email);
    public void registroCliente(Cliente cliente);
    public void cargarSaldo(float cantidad);
    public void descontarSaldo(float cantidad);
}
