package com.vsm.devcase.infra.repository;

import com.vsm.devcase.domain.model.SexoEnum;
import com.vsm.devcase.domain.model.Venda;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author junior.cantador
 */
public interface VendaRepository extends JpaRepository<Venda, Long> {

    /**
     * Retorna o histórico de vendas entre as datas informadas
     *
     * @param dataInicial
     * @param DataFinal
     * @return Lista de vendas
     */
    @Query("SELECT v FROM Venda v WHERE v.data bewtween ?1 and ?2")
    List<Venda> findHistoricBetweenDates(Date dataInicial, Date DataFinal);

    /**
     * Retorna o historico de vendas de acordo com o sexo e entre as datas
     * informadas
     *
     * @param sexo
     * @param dataInicial
     * @param DataFinal
     * @return Lista de vendas
     */
    @Query("SELECT v FROM Venda v WHERE v.sexo = ?1 v.data bewtween ?2 and ?3")
    List<Venda> findHistoricByGenderBetweenDates(SexoEnum sexo, Date dataInicial, Date DataFinal);

}
