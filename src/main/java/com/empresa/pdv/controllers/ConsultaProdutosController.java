package com.empresa.pdv.controllers;

import com.empresa.pdv.models.Produto;
import com.empresa.pdv.models.ProdutoProcurado;
import com.empresa.pdv.models.Usuario;
import com.empresa.pdv.services.ProdutoService;
import com.empresa.pdv.services.ServiceRegistry;
import javafx.fxml.FXML;

import java.util.List;

/**
 * Orquestra a busca rápida de produtos (RF14) e o registro de produtos procurados e não
 * encontrados (RF19), delegando as regras para o {@link ProdutoService}.
 */
public class ConsultaProdutosController {

    private final ProdutoService produtoService = ServiceRegistry.produtoService();

    @FXML
    public void initialize() {
        // TODO: Inicializar componentes e carregar dados do módulo de consulta de produtos
    }

    /** RF14: busca rápida por nome ou código de barras. */
    public List<Produto> buscarProduto(String termo) {
        return produtoService.buscarPorNomeOuCodigo(termo);
    }

    /** RF19: produto procurado por cliente e não encontrado na loja. */
    public ProdutoProcurado registrarProdutoProcurado(Usuario vendedor, String nomeProduto) {
        return produtoService.registrarProdutoProcurado(vendedor, nomeProduto);
    }
}
