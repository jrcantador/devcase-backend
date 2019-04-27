package com.vsm.devcase.domain.dto;

import com.vsm.devcase.domain.model.SexoEnum;
import java.util.Date;

/**
 *
 * @author junior.cantador
 */
public class VendaDTO {

    private SexoEnum sexo;
    private Date dataInicial;
    private Date dataFinal;

    public VendaDTO() {
    }

    public SexoEnum getSexo() {
        return sexo;
    }

    public void setSexo(SexoEnum sexo) {
        this.sexo = sexo;
    }

    public Date getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial;
    }

    public Date getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }

}
