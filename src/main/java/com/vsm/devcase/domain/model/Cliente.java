package com.vsm.devcase.domain.model;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author junior.cantador
 */
@Entity
public class Cliente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    @ApiModelProperty(notes = "Identificador único do cliente", required = true)
    private Long id;

    @Column(length = 100)
    @ApiModelProperty(notes = "Nome do cliente", required = true)
    private Long nome;

    @Column
    @Enumerated(EnumType.STRING)    
    @ApiModelProperty(notes = "Sexo do cliente")
    private SexoEnum sexo;

    @Column
    @ApiModelProperty(notes = "Pontuação total do cliente")
    private BigDecimal pontuacao;

    public Cliente() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNome() {
        return nome;
    }

    public void setNome(Long nome) {
        this.nome = nome;
    }

    public SexoEnum getSexo() {
        return sexo;
    }

    public void setSexo(SexoEnum sexo) {
        this.sexo = sexo;
    }

    public BigDecimal getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(BigDecimal pontuacao) {
        this.pontuacao = pontuacao;
    }

}
