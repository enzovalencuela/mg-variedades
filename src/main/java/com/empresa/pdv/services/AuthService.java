package com.empresa.pdv.services;

import com.empresa.pdv.exceptions.RegraNegocioException;
import com.empresa.pdv.models.Usuario;

/**
 * RNF6, RNF9, RNF10: autenticação (login/logout) de administradores e vendedores.
 */
public class AuthService {

    private final UsuarioService usuarioService;
    private Usuario usuarioAutenticado;

    public AuthService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /** RNF9: autenticação mediante login e senha previamente cadastrados. */
    public Usuario login(String login, String senha) {
        Usuario usuario = usuarioService.autenticar(login, senha)
                .orElseThrow(() -> new RegraNegocioException("Login ou senha inválidos, ou usuário desativado."));
        this.usuarioAutenticado = usuario;
        return usuario;
    }

    /** RNF10: logout a qualquer momento. */
    public void logout() {
        this.usuarioAutenticado = null;
    }

    public boolean isAutenticado() {
        return usuarioAutenticado != null;
    }

    public Usuario getUsuarioAutenticado() {
        return usuarioAutenticado;
    }
}
