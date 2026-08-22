package com.empresa.pdv.services;

import com.empresa.pdv.repository.DevolucaoRepository;
import com.empresa.pdv.repository.VendaRepository;

/**
 * Registro simples dos serviços da aplicação, compartilhado pelos Controllers.
 *
 * <p>Substitui, de forma mínima, um container de injeção de dependências: garante que todos
 * os Controllers enxerguem o mesmo estado mockado em memória (mesmos usuários, categorias,
 * produtos, vendas, caixa, etc.), já que o projeto ainda não possui banco de dados nem um
 * framework de DI.</p>
 */
public final class ServiceRegistry {

    private static final VendaRepository VENDA_REPOSITORY = new VendaRepository();
    private static final DevolucaoRepository DEVOLUCAO_REPOSITORY = new DevolucaoRepository();

    private static final UsuarioService USUARIO_SERVICE = new UsuarioService();
    private static final AuthService AUTH_SERVICE = new AuthService(USUARIO_SERVICE);
    private static final CategoriaService CATEGORIA_SERVICE = new CategoriaService();
    private static final ProdutoService PRODUTO_SERVICE = new ProdutoService(CATEGORIA_SERVICE);
    private static final CaixaService CAIXA_SERVICE = new CaixaService(VENDA_REPOSITORY, DEVOLUCAO_REPOSITORY);
    private static final VendaService VENDA_SERVICE =
            new VendaService(VENDA_REPOSITORY, PRODUTO_SERVICE, CAIXA_SERVICE, CATEGORIA_SERVICE);
    private static final DevolucaoService DEVOLUCAO_SERVICE =
            new DevolucaoService(DEVOLUCAO_REPOSITORY, VENDA_SERVICE, CAIXA_SERVICE);
    private static final ReportExportService REPORT_EXPORT_SERVICE = new ReportExportService();

    private ServiceRegistry() {
    }

    public static UsuarioService usuarioService() {
        return USUARIO_SERVICE;
    }

    public static AuthService authService() {
        return AUTH_SERVICE;
    }

    public static CategoriaService categoriaService() {
        return CATEGORIA_SERVICE;
    }

    public static ProdutoService produtoService() {
        return PRODUTO_SERVICE;
    }

    public static CaixaService caixaService() {
        return CAIXA_SERVICE;
    }

    public static VendaService vendaService() {
        return VENDA_SERVICE;
    }

    public static DevolucaoService devolucaoService() {
        return DEVOLUCAO_SERVICE;
    }

    public static ReportExportService reportExportService() {
        return REPORT_EXPORT_SERVICE;
    }
}
