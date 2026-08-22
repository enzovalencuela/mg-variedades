package com.empresa.pdv.repository;

import com.empresa.pdv.models.Devolucao;

import java.time.LocalDate;
import java.util.List;

public class DevolucaoRepository extends InMemoryRepository<Devolucao> {

    public DevolucaoRepository() {
        super(Devolucao::getId);
    }

    public List<Devolucao> listarPorPeriodo(LocalDate inicio, LocalDate fim) {
        return listarTodos().stream()
                .filter(devolucao -> {
                    LocalDate data = devolucao.getDataHora().toLocalDate();
                    return !data.isBefore(inicio) && !data.isAfter(fim);
                })
                .toList();
    }
}
