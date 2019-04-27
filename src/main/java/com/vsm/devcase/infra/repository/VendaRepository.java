package com.vsm.devcase.infra.repository;

import com.vsm.devcase.domain.model.Cliente;
import com.vsm.devcase.domain.model.SexoEnum;
import com.vsm.devcase.domain.model.Venda;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author junior.cantador
 */
@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {

    /**
     * Retorna o histórico de vendas entre as datas informadas
     *
     * @param dataInicial
     * @param DataFinal
     * @return Lista de vendas
     */
    @Query("SELECT v FROM Venda v WHERE v.data between :dataInicial and :dataFinal")
    List<Venda> findHistoricBetweenDates(Date dataInicial, Date dataFinal);

    /**
     * Retorna o historico de vendas de acordo com o sexo e entre as datas
     * informadas
     *
     * @param sexo
     * @param dataInicial
     * @param DataFinal
     * @return Lista de vendas
     */
    @Query("SELECT v FROM Venda v JOIN v.cliente c WHERE c.sexo = :sexo and v.data between :dataInicial and :dataFinal")
    List<Venda> findHistoricByGenderBetweenDates(SexoEnum sexo, Date dataInicial, Date dataFinal);    
}
