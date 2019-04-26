package com.vsm.devcase.application.validation;

import java.math.BigDecimal;

/**
 *
 * @author junior.cantador
 */
public class VendaValidation {

    public Long calcularPontuacao(BigDecimal valor) {
        return Math.round(valor.doubleValue() / 50);
    }

}
