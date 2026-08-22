package com.empresa.pdv.services;

import com.empresa.pdv.exceptions.RegraNegocioException;
import com.empresa.pdv.models.Caixa;
import com.empresa.pdv.models.Categoria;
import com.empresa.pdv.models.Devolucao;
import com.empresa.pdv.models.ItemVenda;
import com.empresa.pdv.models.RelatorioDevolucoes;
import com.empresa.pdv.models.StatusProdutoDevolvido;
import com.empresa.pdv.models.TipoReembolso;
import com.empresa.pdv.models.Usuario;
import com.empresa.pdv.models.Venda;
import com.empresa.pdv.repository.DevolucaoRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * RF06-RF07, RF22: devolução de itens de vendas anteriores e relatório de devoluções.
 */
public class DevolucaoService {

    private final DevolucaoRepository repository;
    private final VendaService vendaService;
    private final CaixaService caixaService;

    public DevolucaoService(VendaService vendaService, CaixaService caixaService) {
        this(new DevolucaoRepository(), vendaService, caixaService);
    }

    public DevolucaoService(DevolucaoRepository repository, VendaService vendaService, CaixaService caixaService) {
        this.repository = repository;
        this.vendaService = vendaService;
        this.caixaService = caixaService;
    }

    /** RF06: localização da venda a ser devolvida, pelo número identificador. */
    public Venda localizarVendaPorId(Long vendaId) {
        return vendaService.buscarPorId(vendaId)
                .orElseThrow(() -> new RegraNegocioException("Venda não encontrada."));
    }

    /** RF06: localização alternativa por nome do cliente e data da venda. */
    public Venda localizarVendaPorClienteEData(String nomeCliente, LocalDate data) {
        return vendaService.buscarPorClienteEData(nomeCliente, data)
                .orElseThrow(() -> new RegraNegocioException("Venda não encontrada para o cliente e data informados."));
    }

    /** RF07. */
    public Devolucao registrarDevolucao(Usuario solicitante, Venda venda, Long produtoId,
                                         StatusProdutoDevolvido statusProduto, TipoReembolso tipoReembolso,
                                         String motivo) {
        ItemVenda item = venda.getItens().stream()
                .filter(i -> i.getProduto().getId().equals(produtoId))
                .findFirst()
                .orElseThrow(() -> new RegraNegocioException("Este produto não pertence à venda informada."));

        // RF07 regra 1: prazo limite determinado pela categoria do produto (RF05).
        Categoria categoria = item.getProduto().getCategoria();
        LocalDateTime limiteDevolucao = venda.getDataHora().plus(categoria.getPrazoDevolucao());
        if (LocalDateTime.now().isAfter(limiteDevolucao)) {
            throw new RegraNegocioException("O prazo de devolução deste produto já expirou.");
        }

        // RF07 regra 3: reembolso em dinheiro exige caixa aberto e gera saída no caixa.
        Caixa caixaAberto = caixaService.getCaixaAberto().orElse(null);
        if (tipoReembolso == TipoReembolso.DINHEIRO && caixaAberto == null) {
            throw new RegraNegocioException("Não é possível registrar devolução com reembolso em dinheiro sem um caixa em aberto.");
        }

        // RF07 regra 5: cada devolução refere-se a uma única unidade do produto vendido;
        // múltiplas unidades exigem uma chamada a este método por unidade devolvida.
        BigDecimal valorReembolso = item.getPrecoUnitario();
        Devolucao devolucao = new Devolucao(repository.proximoId(), venda, item.getProduto(), LocalDateTime.now(),
                statusProduto, tipoReembolso, valorReembolso, motivo, caixaAberto);
        return repository.salvar(devolucao);
    }

    public List<Devolucao> listarPorPeriodo(LocalDate inicio, LocalDate fim) {
        return repository.listarPorPeriodo(inicio, fim);
    }

    /** RF22: relatório de devoluções de um período. */
    public RelatorioDevolucoes relatorioDevolucoes(LocalDate inicio, LocalDate fim) {
        List<Devolucao> devolucoes = listarPorPeriodo(inicio, fim);

        BigDecimal valorTotalReembolsado = devolucoes.stream()
                .map(Devolucao::getValorReembolso)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Long> devolucoesPorCategoria = new LinkedHashMap<>();
        for (Devolucao devolucao : devolucoes) {
            String categoria = devolucao.getProdutoDevolvido().getCategoria().getNome();
            devolucoesPorCategoria.merge(categoria, 1L, Long::sum);
        }
        List<String> rankingCategorias = devolucoesPorCategoria.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .toList();

        Map<StatusProdutoDevolvido, Integer> quantidadePorCondicao = new EnumMap<>(StatusProdutoDevolvido.class);
        for (StatusProdutoDevolvido status : StatusProdutoDevolvido.values()) {
            quantidadePorCondicao.put(status, 0);
        }
        devolucoes.forEach(devolucao -> quantidadePorCondicao.merge(devolucao.getStatusProduto(), 1, Integer::sum));

        String motivoPredominante = motivoPredominante(devolucoes).orElse(null);

        return new RelatorioDevolucoes(devolucoes.size(), devolucoes.size(), valorTotalReembolsado, rankingCategorias,
                quantidadePorCondicao, motivoPredominante);
    }

    private Optional<String> motivoPredominante(List<Devolucao> devolucoes) {
        Map<String, Long> contagem = new LinkedHashMap<>();
        for (Devolucao devolucao : devolucoes) {
            if (devolucao.getMotivo() != null && !devolucao.getMotivo().isBlank()) {
                contagem.merge(devolucao.getMotivo(), 1L, Long::sum);
            }
        }
        return contagem.entrySet().stream()
                .max(Comparator.comparingLong(Map.Entry::getValue))
                .map(Map.Entry::getKey);
    }
}
