package com.empresa.pdv.repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

/**
 * Base de persistência mockada em memória, usada no lugar de um banco de dados real
 * enquanto o projeto não possui uma camada JDBC. Mantém a mesma responsabilidade de um
 * repositório (armazenar, buscar, listar, remover), apenas sem SQL por trás.
 *
 * @param <T> tipo da entidade armazenada, sempre identificada por um {@code Long}.
 */
public abstract class InMemoryRepository<T> {

    protected final Map<Long, T> registros = new LinkedHashMap<>();
    private final AtomicLong sequenciaId = new AtomicLong(0);
    private final Function<T, Long> extratorId;

    protected InMemoryRepository(Function<T, Long> extratorId) {
        this.extratorId = extratorId;
    }

    /** Gera o próximo identificador único da entidade, análogo a uma sequence/auto-increment. */
    public Long proximoId() {
        return sequenciaId.incrementAndGet();
    }

    public T salvar(T entidade) {
        registros.put(extratorId.apply(entidade), entidade);
        return entidade;
    }

    public Optional<T> buscarPorId(Long id) {
        return Optional.ofNullable(registros.get(id));
    }

    public List<T> listarTodos() {
        return new ArrayList<>(registros.values());
    }

    public void remover(Long id) {
        registros.remove(id);
    }

    public boolean existePorId(Long id) {
        return registros.containsKey(id);
    }
}
