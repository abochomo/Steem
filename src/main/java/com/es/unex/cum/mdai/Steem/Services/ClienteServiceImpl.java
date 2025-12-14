package com.es.unex.cum.mdai.Steem.Services;

import com.es.unex.cum.mdai.Steem.Modelo.Cliente;
import com.es.unex.cum.mdai.Steem.Modelo.Usuario;
import com.es.unex.cum.mdai.Steem.Repositorio.ClienteRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ClienteServiceImpl implements ClienteService {
    @Autowired
    private ClienteRepositorio clienteRepositorio;

    private Cliente clienteActual;

    @Autowired
    private PasswordEncoder passwordEncoder;
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

        cliente.setPassword(passwordEncoder.encode(cliente.getPassword()));
        clienteRepositorio.save(cliente);
    }

    @Override
    public void cargarSaldo(float cantidad) {
        if (this.clienteActual == null) {
            throw new RuntimeException("No hay ningún cliente logueado para cargar saldo");
        }
        this.clienteActual.setSaldo(this.clienteActual.getSaldo() + cantidad);
        clienteRepositorio.save(this.clienteActual);
    }

    @Override
    public void descontarSaldo(float cantidad) {
        if (this.clienteActual == null) {
            throw new RuntimeException("No hay ningún cliente logueado para cargar saldo");
        }
        this.clienteActual.setSaldo(this.clienteActual.getSaldo() - cantidad);
        clienteRepositorio.save(this.clienteActual);
    }

    @Override
    public Cliente findClienteById(long id) {
        clienteActual = clienteRepositorio.findById(id).orElse(null);
        return clienteActual;
    }

    @Override
    public Cliente findClienteByEmail(String email) {
        clienteActual = clienteRepositorio.findByEmail(email);
        return clienteActual;
    }
}
