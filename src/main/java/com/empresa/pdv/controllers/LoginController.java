package com.empresa.pdv.controllers;

import com.empresa.pdv.models.Usuario;
import com.empresa.pdv.services.AuthService;
import com.empresa.pdv.services.ServiceRegistry;
import javafx.fxml.FXML;

/**
 * Orquestra a autenticação (RNF6, RNF9, RNF10), delegando as regras para o {@link AuthService}.
 */
public class LoginController {

    private final AuthService authService = ServiceRegistry.authService();

    @FXML
    public void initialize() {
        // TODO: Inicializar componentes e carregar dados do módulo de login
    }

    /** RNF9: autenticação mediante login e senha previamente cadastrados. */
    public Usuario realizarLogin(String login, String senha) {
        return authService.login(login, senha);
    }

    /** RNF10: logout a qualquer momento. */
    public void realizarLogout() {
        authService.logout();
    }

    public Usuario getUsuarioAutenticado() {
        return authService.getUsuarioAutenticado();
    }
}
