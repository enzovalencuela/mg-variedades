package com.empresa.pdv.repository;

import com.empresa.pdv.models.Usuario;

import java.util.Optional;

public class UsuarioRepository extends InMemoryRepository<Usuario> {

    public UsuarioRepository() {
        super(Usuario::getId);
    }

    public Optional<Usuario> buscarPorLogin(String login) {
        return listarTodos().stream()
                .filter(usuario -> usuario.getLogin().equalsIgnoreCase(login))
                .findFirst();
    }

    public boolean existeLogin(String login) {
        return buscarPorLogin(login).isPresent();
    }
}
