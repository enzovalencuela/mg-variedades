package com.empresa.pdv.exceptions;

/**
 * Lançada quando uma operação viola uma regra de negócio descrita na especificação
 * de requisitos (ex: sangria maior que o saldo em caixa, devolução fora do prazo).
 */
public class RegraNegocioException extends RuntimeException {

    public RegraNegocioException(String message) {
        super(message);
    }
}
