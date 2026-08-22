package com.empresa.pdv.controllers;

import com.empresa.pdv.models.Devolucao;
import com.empresa.pdv.models.StatusProdutoDevolvido;
import com.empresa.pdv.models.TipoReembolso;
import com.empresa.pdv.models.Usuario;
import com.empresa.pdv.models.Venda;
import com.empresa.pdv.services.DevolucaoService;
import com.empresa.pdv.services.ServiceRegistry;
import javafx.fxml.FXML;

import java.time.LocalDate;

/**
 * Orquestra a localização de vendas e o registro de devoluções (RF06-RF07), delegando as
 * regras para o {@link DevolucaoService}.
 */
public class DevolucoesController {

    private final DevolucaoService devolucaoService = ServiceRegistry.devolucaoService();

    @FXML
    public void initialize() {
        // TODO: Inicializar componentes e carregar dados do módulo de devoluções
    }

    /** RF06: localização da venda pelo número identificador. */
    public Venda localizarVendaPorId(Long vendaId) {
        return devolucaoService.localizarVendaPorId(vendaId);
    }

    /** RF06: localização alternativa por nome do cliente e data da venda. */
    public Venda localizarVendaPorClienteEData(String nomeCliente, LocalDate data) {
        return devolucaoService.localizarVendaPorClienteEData(nomeCliente, data);
    }

    /** RF07. */
    public Devolucao registrarDevolucao(Usuario solicitante, Venda venda, Long produtoId,
                                         StatusProdutoDevolvido statusProduto, TipoReembolso tipoReembolso,
                                         String motivo) {
        return devolucaoService.registrarDevolucao(solicitante, venda, produtoId, statusProduto, tipoReembolso, motivo);
    }
}
