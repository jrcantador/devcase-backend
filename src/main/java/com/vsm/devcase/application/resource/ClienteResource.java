package com.vsm.devcase.application.resource;

import com.vsm.devcase.domain.model.Cliente;
import com.vsm.devcase.infra.repository.ClienteRepository;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 *
 * @author junior.cantador
 */
@Controller
@RequestMapping("clientes")
public class ClienteResource {

    private ClienteRepository clienteRepository;

    @Autowired
    public ClienteResource(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    /**
     * Recurso que retorna todas os clientes cadastrados
     *
     * @return Json com todas a vendas
     */
    @GetMapping("/")
    public List<Cliente> list() {
        return clienteRepository.findAll();
    }

    /**
     * Recurso para cadastrar Cliente
     *
     * @param cliente Json de Cliente
     * @return 201 - Created
     */
    @PostMapping("/")
    public ResponseEntity<Object> create(@RequestBody Cliente cliente) {
        Cliente clienteSalvo = clienteRepository.save(cliente);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(clienteSalvo.getId()).toUri();

        return ResponseEntity.created(location).build();
    }
}
