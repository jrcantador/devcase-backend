package com.vsm.devcase.domain.model;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author junior.cantador
 */
@Entity
public class Venda implements Serializable {

    @Id
    @GeneratedValue
    @ApiModelProperty(notes = "Identificador único da venda", required = true)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    @ApiModelProperty(notes = "Identificador único do cliente relacionado a venda", required = true)
    private Cliente cliente;

    @Column(precision = 12, scale = 2)
    @ApiModelProperty(notes = "Valor total da venda")
    private BigDecimal valorTotal;

    @Column
    @ApiModelProperty(notes = "Pontuação da venda")
    private Long pontuacao;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(notes = "Data a quala venda foi realizada")
    private Date data;

    public Venda() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente Cliente) {
        this.cliente = Cliente;
    }

    public Long getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(Long pontuacao) {
        this.pontuacao = pontuacao;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

}
