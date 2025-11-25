package com.es.unex.cum.mdai.Steem.Services;

import com.es.unex.cum.mdai.Steem.Modelo.Usuario;

public interface UsuarioService {
    public void registrarUsuario(String email, String user, String contrasena);
    public void loginUsuario(String email, String contrasena);
    public void cambiarContrasena(String nuevaContrasena);
    public Usuario findUser(long user);
    public Usuario findUserByEmail(String email);
    public void setUser(Usuario usuario);
    public void eliminarUsuario(long user);
}
