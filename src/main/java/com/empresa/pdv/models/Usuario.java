package com.empresa.pdv.models;

import java.time.LocalDate;
import java.util.Objects;

/**
 * RF01-RF03: usuário do sistema (administrador ou vendedor).
 */
public class Usuario {

    private Long id;
    private String nome;
    private String login;
    private String senha;
    private Cargo cargo;
    private LocalDate dataCadastro;
    private boolean ativo;
    private boolean seed;

    public Usuario() {
    }

    public Usuario(Long id, String nome, String login, String senha, Cargo cargo, LocalDate dataCadastro) {
        this.id = id;
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.cargo = cargo;
        this.dataCadastro = dataCadastro;
        this.ativo = true;
        this.seed = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    /**
     * Indica se é a conta de administrador padrão criada na primeira execução (RNF8),
     * que não pode ser excluída/desativada, apenas ter a senha alterada.
     */
    public boolean isSeed() {
        return seed;
    }

    public void setSeed(boolean seed) {
        this.seed = seed;
    }

    public boolean isAdministrador() {
        return cargo == Cargo.ADMINISTRADOR;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario usuario)) return false;
        return Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Usuario{id=%d, login='%s', cargo=%s, ativo=%s}".formatted(id, login, cargo, ativo);
    }
}
