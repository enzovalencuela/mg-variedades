package com.empresa.pdv.controllers;

import com.empresa.pdv.models.DashboardResumo;
import com.empresa.pdv.models.StatusCaixa;
import com.empresa.pdv.services.CaixaService;
import com.empresa.pdv.services.ProdutoService;
import com.empresa.pdv.services.ServiceRegistry;
import com.empresa.pdv.services.VendaService;
import javafx.fxml.FXML;

/**
 * Orquestra o painel inicial com as métricas do dia corrente (RF17), combinando dados de
 * {@link VendaService}, {@link ProdutoService} e {@link CaixaService}.
 */
public class DashboardController {

    private final VendaService vendaService = ServiceRegistry.vendaService();
    private final ProdutoService produtoService = ServiceRegistry.produtoService();
    private final CaixaService caixaService = ServiceRegistry.caixaService();

    @FXML
    public void initialize() {
        // TODO: Inicializar componentes e carregar dados do dashboard
    }

    /** RF17: faturamento do dia, produto mais vendido do dia e quantidade de produtos em falta. */
    public DashboardResumo carregarResumo() {
        return new DashboardResumo(
                vendaService.faturamentoDoDia(),
                vendaService.produtoMaisVendidoDoDia().orElse(null),
                produtoService.listarComEstoqueBaixo().size(),
                caixaService.getStatusAtual() == StatusCaixa.ABERTO
        );
    }
}
