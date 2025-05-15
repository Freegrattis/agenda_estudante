package service;

import model.Usuario;

public class UsuarioService {
    private Usuario usuarioLogado;

    public void cadastrarUsuario(String nome, String email, String cidade) {
        usuarioLogado = new Usuario(nome, email, cidade);
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }
}
