package com.empresa.pdv.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * RF06-RF07: devolução de um item de uma venda anteriormente registrada.
 */
public class Devolucao {

    private final Long id;
    private final Venda venda;
    private final Produto produtoDevolvido;
    private final LocalDateTime dataHora;
    private final StatusProdutoDevolvido statusProduto;
    private final TipoReembolso tipoReembolso;
    private final BigDecimal valorReembolso;
    private final String motivo;
    private final Caixa caixaVinculado;

    public Devolucao(Long id, Venda venda, Produto produtoDevolvido, LocalDateTime dataHora,
                      StatusProdutoDevolvido statusProduto, TipoReembolso tipoReembolso, BigDecimal valorReembolso,
                      String motivo, Caixa caixaVinculado) {
        this.id = id;
        this.venda = venda;
        this.produtoDevolvido = produtoDevolvido;
        this.dataHora = dataHora;
        this.statusProduto = statusProduto;
        this.tipoReembolso = tipoReembolso;
        this.valorReembolso = valorReembolso;
        this.motivo = motivo;
        this.caixaVinculado = caixaVinculado;
    }

    public Long getId() {
        return id;
    }

    public Venda getVenda() {
        return venda;
    }

    public Produto getProdutoDevolvido() {
        return produtoDevolvido;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public StatusProdutoDevolvido getStatusProduto() {
        return statusProduto;
    }

    public TipoReembolso getTipoReembolso() {
        return tipoReembolso;
    }

    public BigDecimal getValorReembolso() {
        return valorReembolso;
    }

    public String getMotivo() {
        return motivo;
    }

    public Caixa getCaixaVinculado() {
        return caixaVinculado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Devolucao devolucao)) return false;
        return Objects.equals(id, devolucao.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Devolucao{id=%d, produto='%s', dataHora=%s, tipoReembolso=%s}"
                .formatted(id, produtoDevolvido.getNome(), dataHora, tipoReembolso);
    }
}
