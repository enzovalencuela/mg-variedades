package com.empresa.pdv.controllers;

import com.empresa.pdv.models.Cargo;
import com.empresa.pdv.models.Usuario;
import com.empresa.pdv.services.ServiceRegistry;
import com.empresa.pdv.services.UsuarioService;
import javafx.fxml.FXML;

import java.util.List;

/**
 * Orquestra o cadastro e a manutenção de usuários (RF01-RF03), delegando as regras
 * para o {@link UsuarioService}.
 */
public class UsuariosController {

    private final UsuarioService usuarioService = ServiceRegistry.usuarioService();

    @FXML
    public void initialize() {
        // TODO: Inicializar componentes e carregar dados do módulo de usuários
    }

    /** RF01. */
    public Usuario cadastrarUsuario(Usuario solicitante, String nome, String login, String senha, Cargo cargo) {
        return usuarioService.cadastrarUsuario(solicitante, nome, login, senha, cargo);
    }

    /** RF02: edição de dados cadastrais de um vendedor. */
    public Usuario editarUsuario(Usuario solicitante, Long id, String nome, String login) {
        return usuarioService.editarUsuario(solicitante, id, nome, login);
    }

    /** RF02: desativação de um vendedor. */
    public Usuario desativarUsuario(Usuario solicitante, Long id) {
        return usuarioService.desativarUsuario(solicitante, id);
    }

    /** RF02: promoção de um vendedor a administrador. */
    public Usuario promoverAAdministrador(Usuario solicitante, Long id) {
        return usuarioService.promoverAAdministrador(solicitante, id);
    }

    /** RF03: redefinição da própria senha mediante autenticação prévia. */
    public void redefinirSenha(Usuario solicitante, String senhaAtual, String novaSenha) {
        usuarioService.redefinirSenha(solicitante, senhaAtual, novaSenha);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioService.listarTodos();
    }
}
