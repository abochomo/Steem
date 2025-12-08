package com.es.unex.cum.mdai.Steem.Services;

import com.es.unex.cum.mdai.Steem.Modelo.Cliente;
import com.es.unex.cum.mdai.Steem.Modelo.Desarrollador;
import com.es.unex.cum.mdai.Steem.Modelo.Usuario;
import com.es.unex.cum.mdai.Steem.Repositorio.ClienteRepositorio;
import com.es.unex.cum.mdai.Steem.Repositorio.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    private Usuario usuarioActual;

    @Override
    public Usuario loginUsuario(String email, String contrasena) {
        Usuario usuario = usuarioRepositorio.findByEmail(email);
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        if (!contrasena.equals(usuario.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }
        return usuario;
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
    public Usuario findUser(long userID) {
        return usuarioRepositorio.findById(userID).orElse(null);
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
    @Override
    public Usuario findUserByUsername(String username) {
        return usuarioRepositorio.findByNombreUsuario(username);
    }

    @Override
    public void guardarUsuario(Usuario usuario) {
        // Al guardar un 'Usuario' que en realidad es un 'Cliente',
        // JPA lo maneja automáticamente.
        usuarioRepositorio.save(usuario);
    }

}
