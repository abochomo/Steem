package com.es.unex.cum.mdai.Steem.Services;

public interface ClienteService {
    public void registrarUsuario(String email, String user, String contrasena);
    public void loginUsuario(String email, String contrasena);
    public void cambiarContrasena(String nuevaContrasena);
}
