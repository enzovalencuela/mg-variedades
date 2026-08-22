package com.empresa.pdv.models;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * RF22: relatório de devoluções de um período.
 */
public record RelatorioDevolucoes(
        int quantidadeTotalDevolucoes,
        int quantidadeTotalItensDevolvidos,
        BigDecimal valorTotalReembolsado,
        List<String> rankingCategoriasPorDevolucao,
        Map<StatusProdutoDevolvido, Integer> quantidadePorCondicao,
        String motivoPredominante
) {
}
