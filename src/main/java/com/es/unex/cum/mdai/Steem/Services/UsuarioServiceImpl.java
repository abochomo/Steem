package com.es.unex.cum.mdai.Steem.Services;

import com.es.unex.cum.mdai.Steem.Modelo.Cliente;
import com.es.unex.cum.mdai.Steem.Modelo.Usuario;
import com.es.unex.cum.mdai.Steem.Repositorio.ClienteRepositorio;
import com.es.unex.cum.mdai.Steem.Repositorio.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    private Usuario usuarioActual;

    @Override
    public void registrarUsuario(String email, String user, String contrasena) {
        if (usuarioRepositorio.findByEmail(email) != null) {
            throw new RuntimeException("El email ya está registrado");
        }
        Usuario nuevoUsuario = new Cliente();
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setNombreUsuario(user);
        nuevoUsuario.setPassword(contrasena);
        usuarioRepositorio.save(nuevoUsuario);
    }

    @Override
    public void loginUsuario(String email, String contrasena) {
        if (usuarioRepositorio.findByEmail(email) != null) {
            throw new RuntimeException("El email no está registrado");
        }

        Usuario usuario = usuarioRepositorio.findByEmail(email);

        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        if (!usuario.getPassword().equals(contrasena)) {
            throw new RuntimeException("Contraseña incorrecta");
        }
        this.usuarioActual = usuario;
    }

    @Override
    public void cambiarContrasena(String nuevaContrasena) {
        if (this.usuarioActual == null) {
            throw new RuntimeException("No hay ningún usuario logueado para cambiar la contraseña");
        }
        this.usuarioActual.setPassword(nuevaContrasena);
        usuarioRepositorio.save(this.usuarioActual);
    }

    @Override
    public Usuario findUser(long user) {
        return usuarioRepositorio.findById(user).orElse(null);
    }

    @Override
    public Usuario findUserByEmail(String email) {
        return usuarioRepositorio.findByEmail(email);
    }

    @Override
    public void setUser(Usuario usuario) {
        this.usuarioActual = usuario;
    }

    @Override
    public void eliminarUsuario(long user) {
        if (usuarioRepositorio.existsById(user)) {
            usuarioRepositorio.deleteById(user);
        } else {
            throw new RuntimeException("No se puede eliminar: El usuario no existe");
        }

    }
}
