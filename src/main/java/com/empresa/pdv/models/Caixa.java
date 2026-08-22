package com.empresa.pdv.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import com.empresa.pdv.exceptions.RegraNegocioException;

public class Caixa {

    private final Long id;
    private final BigDecimal valorInicial;
    private final LocalDateTime dataHoraAbertura;
    private final Usuario usuarioAbertura;

    private StatusCaixa status;
    private LocalDateTime dataHoraFechamento;
    private Usuario usuarioFechamento;
    private BigDecimal valorContadoFechamento;
    private String justificativaDivergencia;

    public Caixa(Long id, BigDecimal valorInicial, LocalDateTime dataHoraAbertura, Usuario usuarioAbertura) {

        if (valorInicial == null || valorInicial.signum() < 0) {
            throw new RegraNegocioException(
                    "O valor inicial do caixa não pode ser nulo ou negativo.");
        }
        if (dataHoraAbertura == null) {
            throw new RegraNegocioException("A data e hora de abertura são obrigatórias.");
        }
        if (usuarioAbertura == null) {
            throw new RegraNegocioException("O usuário de abertura é obrigatório.");
        }

        this.id = id;
        this.valorInicial = valorInicial;
        this.dataHoraAbertura = dataHoraAbertura;
        this.usuarioAbertura = usuarioAbertura;
        this.status = StatusCaixa.ABERTO;
    }

    public boolean isAberto() {
        return status == StatusCaixa.ABERTO;
    }

    public void fechar(LocalDateTime dataHoraFechamento, Usuario usuarioFechamento, BigDecimal valorContadoFechamento,
            String justificativaDivergencia) {
        if (!isAberto()) {
            throw new IllegalStateException("O caixa já está fechado.");
        }
        this.status = StatusCaixa.FECHADO;
        this.dataHoraFechamento = dataHoraFechamento;
        this.usuarioFechamento = usuarioFechamento;
        this.valorContadoFechamento = valorContadoFechamento;
        this.justificativaDivergencia = justificativaDivergencia;
    }

    // Métodos getters
    public Long getId() {
        return id;
    }

    public BigDecimal getValorInicial() {
        return valorInicial;
    }

    public LocalDateTime getDataHoraAbertura() {
        return dataHoraAbertura;
    }

    public Usuario getUsuarioAbertura() {
        return usuarioAbertura;
    }

    public StatusCaixa getStatus() {
        return status;
    }

    public LocalDateTime getDataHoraFechamento() {
        return dataHoraFechamento;
    }

    public Usuario getUsuarioFechamento() {
        return usuarioFechamento;
    }

    public BigDecimal getValorContadoFechamento() {
        return valorContadoFechamento;
    }

    public String getJustificativaDivergencia() {
        return justificativaDivergencia;
    }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) {
            return true;
        }
        if (!(objeto instanceof Caixa outro)) {
            return false;
        }
        return Objects.equals(id, outro.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Caixa{" +
                "id=" + id +
                ", valorInicial=" + valorInicial +
                ", dataHoraAbertura=" + dataHoraAbertura +
                ", usuarioAbertura=" + usuarioAbertura +
                ", status=" + status +
                ", dataHoraFechamento=" + dataHoraFechamento +
                ", usuarioFechamento=" + usuarioFechamento +
                ", valorContadoFechamento=" + valorContadoFechamento +
                ", justificativaDivergencia='" + justificativaDivergencia + '\'' +
                '}';
    }

}
