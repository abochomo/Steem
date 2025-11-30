package com.es.unex.cum.mdai.Steem.Services;

import com.es.unex.cum.mdai.Steem.Modelo.Cliente;
import com.es.unex.cum.mdai.Steem.Modelo.Usuario;
import com.es.unex.cum.mdai.Steem.Repositorio.ClienteRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteServiceImpl implements ClienteService {
    @Autowired
    private ClienteRepositorio clienteRepositorio;

    private Cliente clienteActual;

    @Override
    public void registroCliente(Cliente cliente) {
        if (cliente.getEmail() != null && clienteRepositorio.findByEmail(cliente.getEmail()) != null) {
            throw new RuntimeException("El email ya está registrado");
        }
        if (cliente.getNombreUsuario() == null) {
            throw new RuntimeException("El nombre de usuario no puede ser nulo");
        }
        cliente.setTipo(Usuario.TipoUsuario.CLIENTE);

        cliente.setFechaRegistro(new java.util.Date());
        clienteRepositorio.save(cliente);
    }
}
