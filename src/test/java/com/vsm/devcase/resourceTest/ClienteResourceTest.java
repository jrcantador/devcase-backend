package com.vsm.devcase.resourceTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vsm.devcase.domain.model.Cliente;
import com.vsm.devcase.domain.model.SexoEnum;
import com.vsm.devcase.domain.model.Venda;
import com.vsm.devcase.infra.repository.ClienteRepository;
import com.vsm.devcase.infra.repository.VendaRepository;
import com.vsm.devcase.infra.service.TokenAuthenticationService;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import org.junit.After;
import org.junit.Assert;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author junior.cantador
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(properties = {"server.port=8888"})
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ClienteResourceTest {

    final String BASE_PATH = "http://localhost:8888";
    final String TOKEN = TokenAuthenticationService.createToken("admin");

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private VendaRepository vendaRepository;

    private RestTemplate restTemplate;
    private HttpHeaders headers;
    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void SetUp() throws Exception {
        restTemplate = new RestTemplate();
        headers = new HttpHeaders();
        headers.setBearerAuth(TOKEN);
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * Caso de teste 1 - Listar clientes cadastrados
     *
     * 1 - Cadastrado um cliente 2 - Executada rotida de listar clientes 3 -
     * Testado informações retornadas da lista
     *
     * @throws JsonProcessingException
     * @throws IOException
     */
    @Test
    public void test01() throws JsonProcessingException, IOException {
        clienteRepository.save(new Cliente("Cliente caso teste 1", SexoEnum.MASCULINO));
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<String> response = restTemplate.exchange(BASE_PATH + "/clientes/", HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        List<Cliente> clientes = mapper.readValue(response.getBody(), mapper.getTypeFactory().constructCollectionType(List.class, Cliente.class));

        Assert.assertNotNull(clientes);
        Assert.assertEquals(1, clientes.size());
        Assert.assertEquals("Cliente caso teste 1", clientes.get(0).getNome());
        Assert.assertEquals(SexoEnum.MASCULINO, clientes.get(0).getSexo());
    }

    /**
     * Caso de teste 2 - Cadastrar Cliente
     *
     * 1 - Cadastrado um cliente 2 - Executada rotina de cadastrar cliente 3 -
     * Teste status da requisição
     *
     * @throws JsonProcessingException
     * @throws IOException
     */
    @Test
    public void test02() throws JsonProcessingException, IOException {
        Cliente cliente = (new Cliente("Cliente caso teste 2", SexoEnum.MASCULINO));
        HttpEntity<Cliente> entity = new HttpEntity<>(cliente, headers);
        ResponseEntity<String> response = restTemplate.exchange(BASE_PATH + "/clientes/", HttpMethod.POST, entity, String.class);

        Cliente clienteSalvo = clienteRepository.findByName("Cliente caso teste 2");
        Assert.assertNotNull(clienteSalvo);
        Assert.assertEquals("Cliente caso teste 2", clienteSalvo.getNome());
        Assert.assertEquals(SexoEnum.MASCULINO, clienteSalvo.getSexo());

        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
    }

    /**
     * Caso de teste 4 - Pontuação total do cliente
     *
     * 1 - Cadastra um cliente 2 - Cadastra duas vendas para o mesmo cliente 3 -
     * Verifica se a pontuação do clinte esta de acordo com a soma da pontuação
     * das duas vendas
     *
     * @throws JsonProcessingException
     * @throws IOException
     */
    @Test
    public void test03() throws JsonProcessingException, IOException {
        Cliente clienteInterno01 = new Cliente("Cliente caso teste 3", SexoEnum.FEMININO);
        HttpEntity<Cliente> entity = new HttpEntity<>(clienteInterno01, headers);
        ResponseEntity<String> response = restTemplate.exchange(BASE_PATH + "/clientes/", HttpMethod.POST, entity, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));

        Cliente clienteSalvo = clienteRepository.findByName(clienteInterno01.getNome());

        Venda vendaInterna01 = new Venda();
        vendaInterna01.setCliente(clienteSalvo);
        vendaInterna01.setValorTotal(new BigDecimal(1000.00));
        vendaInterna01.setData(new Date());

        Venda vendaInterna02 = new Venda();
        vendaInterna02.setCliente(clienteSalvo);
        vendaInterna02.setValorTotal(new BigDecimal(2000.00));
        vendaInterna02.setData(new Date());

        HttpEntity<Venda> entityPost = new HttpEntity<>(vendaInterna01, headers);
        restTemplate.exchange(BASE_PATH + "/vendas/", HttpMethod.POST, entityPost, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        entityPost = new HttpEntity<>(vendaInterna02, headers);
        restTemplate.exchange(BASE_PATH + "/vendas/", HttpMethod.POST, entityPost, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));

        clienteSalvo = clienteRepository.findByName(clienteInterno01.getNome());

        Assert.assertNotNull(clienteSalvo);
        Assert.assertEquals(new BigDecimal(60.00).setScale(2), clienteSalvo.getPontuacao());
    }
}
