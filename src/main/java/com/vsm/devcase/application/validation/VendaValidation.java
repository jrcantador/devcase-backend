package com.vsm.devcase.application.validation;

import java.math.BigDecimal;
import org.springframework.stereotype.Service;

/**
 *
 * @author junior.cantador
 */
@Service
public class VendaValidation {

    public VendaValidation() {
    }

    public Long calcularPontuacao(BigDecimal valor) {
        return Math.round(valor.doubleValue() / 50);
    }

}
