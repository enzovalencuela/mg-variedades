package com.empresa.pdv.services;

import com.empresa.pdv.exceptions.RegraNegocioException;
import com.empresa.pdv.models.Cargo;
import com.empresa.pdv.models.Usuario;
import com.empresa.pdv.repository.UsuarioRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * RF01-RF03: cadastro, edição e autenticação de usuários (administradores e vendedores).
 */
public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService() {
        this(new UsuarioRepository());
    }

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
        semearAdministradorPadrao();
    }

    /** RNF8: conta de administrador padrão (seed) criada na primeira execução do sistema. */
    private void semearAdministradorPadrao() {
        Usuario admin = new Usuario(repository.proximoId(), "Administrador", "admin", "admin123",
                Cargo.ADMINISTRADOR, LocalDate.now());
        admin.setSeed(true);
        repository.salvar(admin);
    }

    private void validarAdministrador(Usuario solicitante) {
        if (solicitante == null || !solicitante.isAdministrador()) {
            throw new RegraNegocioException("Apenas o administrador pode realizar esta operação.");
        }
    }

    /** RF01. */
    public Usuario cadastrarUsuario(Usuario solicitante, String nome, String login, String senha, Cargo cargo) {
        validarAdministrador(solicitante);
        if (repository.existeLogin(login)) {
            throw new RegraNegocioException("Já existe um usuário cadastrado com este login.");
        }
        Usuario novoUsuario = new Usuario(repository.proximoId(), nome, login, senha, cargo, LocalDate.now());
        return repository.salvar(novoUsuario);
    }

    /** RF02: edição de dados cadastrais de um vendedor. */
    public Usuario editarUsuario(Usuario solicitante, Long id, String nome, String login) {
        validarAdministrador(solicitante);
        Usuario usuario = buscarPorIdOuFalhar(id);
        usuario.setNome(nome);
        usuario.setLogin(login);
        return repository.salvar(usuario);
    }

    /** RF02: desativação de um vendedor. */
    public Usuario desativarUsuario(Usuario solicitante, Long id) {
        validarAdministrador(solicitante);
        Usuario usuario = buscarPorIdOuFalhar(id);
        if (usuario.isSeed()) {
            throw new RegraNegocioException("A conta de administrador padrão não pode ser desativada.");
        }
        usuario.setAtivo(false);
        return repository.salvar(usuario);
    }

    /** RF02: promoção de um vendedor a administrador. */
    public Usuario promoverAAdministrador(Usuario solicitante, Long id) {
        validarAdministrador(solicitante);
        Usuario usuario = buscarPorIdOuFalhar(id);
        usuario.setCargo(Cargo.ADMINISTRADOR);
        return repository.salvar(usuario);
    }

    /** RF03: redefinição da própria senha mediante autenticação prévia (senha atual). */
    public void redefinirSenha(Usuario solicitante, String senhaAtual, String novaSenha) {
        if (solicitante == null || !solicitante.getSenha().equals(senhaAtual)) {
            throw new RegraNegocioException("Senha atual inválida.");
        }
        solicitante.setSenha(novaSenha);
        repository.salvar(solicitante);
    }

    /** Usado pelo AuthService: valida credenciais e retorna o usuário se ativo. */
    public Optional<Usuario> autenticar(String login, String senha) {
        return repository.buscarPorLogin(login)
                .filter(usuario -> usuario.getSenha().equals(senha))
                .filter(Usuario::isAtivo);
    }

    public Usuario buscarPorIdOuFalhar(Long id) {
        return repository.buscarPorId(id)
                .orElseThrow(() -> new RegraNegocioException("Usuário não encontrado."));
    }

    public List<Usuario> listarTodos() {
        return repository.listarTodos();
    }

    public List<Usuario> listarAtivos() {
        return repository.listarTodos().stream().filter(Usuario::isAtivo).toList();
    }
}
