package com.empresa.pdv.models;

import java.math.BigDecimal;

/**
 * RF17: métricas do painel inicial referentes ao dia corrente.
 */
public record DashboardResumo(
        BigDecimal faturamentoDia,
        String produtoMaisVendidoDia,
        int quantidadeProdutosEstoqueBaixo,
        boolean caixaAberto
) {
}
