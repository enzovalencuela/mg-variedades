package com.empresa.pdv.controllers;

import com.empresa.pdv.exceptions.RegraNegocioException;
import com.empresa.pdv.models.FormatoExportacao;
import com.empresa.pdv.models.Produto;
import com.empresa.pdv.models.ProdutoProcurado;
import com.empresa.pdv.models.RelatorioDevolucoes;
import com.empresa.pdv.models.RelatorioProdutosPeriodo;
import com.empresa.pdv.models.RelatorioVendasCategoria;
import com.empresa.pdv.models.RelatorioVendasPeriodo;
import com.empresa.pdv.models.RelatorioVendasVendedor;
import com.empresa.pdv.models.Usuario;
import com.empresa.pdv.services.DevolucaoService;
import com.empresa.pdv.services.ProdutoService;
import com.empresa.pdv.services.ReportExportService;
import com.empresa.pdv.services.ServiceRegistry;
import com.empresa.pdv.services.VendaService;
import javafx.fxml.FXML;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Orquestra a geração e exportação dos relatórios de vendas, produtos e devoluções
 * (RF15-RF23), delegando as regras para {@link VendaService}, {@link ProdutoService},
 * {@link DevolucaoService} e {@link ReportExportService}.
 */
public class RelatoriosController {

    private final VendaService vendaService = ServiceRegistry.vendaService();
    private final ProdutoService produtoService = ServiceRegistry.produtoService();
    private final DevolucaoService devolucaoService = ServiceRegistry.devolucaoService();
    private final ReportExportService reportExportService = ServiceRegistry.reportExportService();

    @FXML
    public void initialize() {
        // TODO: Inicializar componentes e carregar dados do módulo de relatórios
    }

    /** RF15. */
    public RelatorioVendasPeriodo relatorioVendasPeriodo(LocalDate inicio, LocalDate fim) {
        return vendaService.relatorioVendasPeriodo(inicio, fim);
    }

    /** RF16: consulta restrita ao administrador. */
    public List<RelatorioVendasCategoria> relatorioVendasPorCategoria(Usuario solicitante, LocalDate inicio, LocalDate fim) {
        return vendaService.relatorioVendasPorCategoria(solicitante, inicio, fim);
    }

    /** RF18: produtos em falta (quantidade mínima atingida), filtrável por período — restrito ao administrador. */
    public List<Produto> relatorioProdutosEmFalta(Usuario solicitante, LocalDate inicio, LocalDate fim) {
        return produtoService.relatorioProdutosEmFalta(solicitante, inicio, fim);
    }

    /** RF19: listagem dos produtos procurados por clientes e não encontrados na loja. */
    public List<ProdutoProcurado> listarProdutosProcurados(LocalDate inicio, LocalDate fim) {
        return produtoService.listarProdutosProcurados(inicio, fim);
    }

    /** RF20. */
    public RelatorioProdutosPeriodo relatorioProdutosPeriodo(LocalDate inicio, LocalDate fim) {
        return vendaService.relatorioProdutosPeriodo(inicio, fim);
    }

    /** RF21. */
    public List<RelatorioVendasVendedor> relatorioVendasPorVendedor(LocalDate inicio, LocalDate fim) {
        return vendaService.relatorioVendasPorVendedor(inicio, fim);
    }

    /** RF22. */
    public RelatorioDevolucoes relatorioDevolucoes(LocalDate inicio, LocalDate fim) {
        return devolucaoService.relatorioDevolucoes(inicio, fim);
    }

    /**
     * RF23: exportação de um relatório já gerado, preservando os filtros aplicados na
     * consulta em tela. Disponível sempre para o administrador; para o vendedor, apenas
     * quando {@code restritoAoAdmin} for {@code false} para o relatório em questão.
     */
    public byte[] exportarRelatorio(Usuario solicitante, String nomeRelatorio, Map<String, String> filtrosAplicados,
                                     List<String> cabecalhos, List<List<String>> linhas, FormatoExportacao formato,
                                     boolean restritoAoAdmin) {
        if (solicitante == null || (restritoAoAdmin && !solicitante.isAdministrador())) {
            throw new RegraNegocioException("Este relatório não está disponível para exportação pelo seu perfil de usuário.");
        }
        return reportExportService.exportar(nomeRelatorio, filtrosAplicados, cabecalhos, linhas, formato);
    }
}
