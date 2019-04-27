package com.vsm.devcase.application.resource;

import com.vsm.devcase.application.validation.VendaValidation;
import com.vsm.devcase.domain.dto.VendaDTO;
import com.vsm.devcase.domain.model.Venda;
import com.vsm.devcase.infra.repository.VendaRepository;
import io.swagger.annotations.ApiOperation;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 *
 * @author junior.cantador
 */
@RestController
@RequestMapping("/vendas")
public class VendaResource {

    private VendaRepository vendaRepository;
    private VendaValidation validation;

    @Autowired
    public VendaResource(VendaRepository vendaRepository, VendaValidation validation) {
        this.vendaRepository = vendaRepository;
        this.validation = validation;
    }

    /**
     * Recurso que retorna todas as vendas cadastradas
     *
     * @return Json com todas a vendas
     */
    @ApiOperation(value = "Lista todas as vendas", notes = "Lista todas as vendas", response = Venda.class, responseContainer = "List")  
    @GetMapping("/")
    public List<Venda> list() {
        return vendaRepository.findAll();
    }

    /**
     * Recurso que retorna o historico de vendas entre um periodo de datas
     *
     * @param venda Json com as informaçõe de data inicial e data final
     * @return Lista com o historico de vendas
     */
    @ApiOperation(value = "Recurso que retorna o historico de vendas entre um periodo de datas", notes = "Recurso que retorna o historico de vendas entre um periodo de datas", response = Venda.class, responseContainer = "List")    
    @PostMapping("/historic-dates")
    public List<Venda> historicBetweenDates(@RequestBody VendaDTO venda) {
        return vendaRepository.findHistoricBetweenDates(venda.getDataInicial(), venda.getDataFinal());
    }

    /**
     * Recurso que retorna o histoorico de vendas por sexo e entre um periodo de
     * datas
     *
     * @param venda Json com as informações de sexo, data inicial e data final
     * @return Lista com o historico de vendas
     */
    @ApiOperation(value = "Recurso que retorna o histórico de vendas por sexo e entre um periodo de datas", notes = "Recurso que retorna o histoorico de vendas por sexo e entre um periodo de datas", response = Venda.class, responseContainer = "List")    
    @PostMapping("/historic-gender-dates")
    public List<Venda> historicByGenderBetweenDates(@RequestBody VendaDTO venda) {
        return vendaRepository.findHistoricByGenderBetweenDates(venda.getSexo(), venda.getDataInicial(), venda.getDataFinal());
    }

    /**
     * Recurso para cadastra Venda
     *
     * @param venda Json de Venda
     * @return 201 - Created
     */
    @ApiOperation(value = "Recurso para cadastrar Venda", notes = "Recurso para cadastra Venda", response = ResponseEntity.class, responseContainer = "create")    
    @PostMapping("/")
    public ResponseEntity<Object> create(@RequestBody Venda venda) {

        Long pontuacao = validation.calcularPontuacao(venda.getValorTotal());
        venda.setPontuacao(pontuacao);
            Venda vendaSalva = vendaRepository.save(venda);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(vendaSalva.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

}
