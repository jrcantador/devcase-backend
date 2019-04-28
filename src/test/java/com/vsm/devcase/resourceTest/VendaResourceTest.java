package com.vsm.devcase.resourceTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vsm.devcase.domain.dto.VendaDTO;
import com.vsm.devcase.domain.model.Cliente;
import com.vsm.devcase.domain.model.SexoEnum;
import com.vsm.devcase.domain.model.Venda;
import com.vsm.devcase.infra.repository.ClienteRepository;
import com.vsm.devcase.infra.repository.VendaRepository;
import com.vsm.devcase.infra.service.TokenAuthenticationService;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
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
public class VendaResourceTest {

    final String BASE_PATH = "http://localhost:8888/vendas/";
    final String TOKEN = TokenAuthenticationService.createToken("admin");

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    private RestTemplate restTemplate;
    private HttpHeaders headers;
    private ObjectMapper mapper = new ObjectMapper();
    private Date dataAtual = new Date();

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
     * Caso de Teste 1 - Listar vendas
     * 
     * 1 - Cadastrado um cliente
     * 2 - Cadastrada uma venda
     * 3 - Executada rotina de listar vendas
     * 4 - Testado informações retornadas na lista
     * 
     * 
     * @throws JsonProcessingException
     * @throws IOException
     */
    @Test
    public void test01() throws JsonProcessingException, IOException {
        Cliente cliente = new Cliente("Cliente Teste 1", SexoEnum.FEMININO);
        Venda venda = new Venda();
        venda.setCliente(cliente);
        venda.setValorTotal(new BigDecimal(500.00));
        venda.setPontuacao(10L);
        venda.setData(dataAtual);

        cliente = clienteRepository.save(cliente);
        vendaRepository.save(venda);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<String> response = restTemplate.exchange(BASE_PATH, HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        List<Venda> vendas = mapper.readValue(response.getBody(), mapper.getTypeFactory().constructCollectionType(List.class, Venda.class));

        Assert.assertNotNull(vendas);
        Assert.assertEquals(1, vendas.size());
        Assert.assertEquals("Cliente Teste 1", vendas.get(0).getCliente().getNome());
        Assert.assertEquals(SexoEnum.FEMININO, vendas.get(0).getCliente().getSexo());
        Assert.assertEquals(new BigDecimal(500.00).setScale(2), vendas.get(0).getValorTotal());
        Assert.assertEquals(Long.valueOf(10), vendas.get(0).getPontuacao());
        Assert.assertEquals(dataAtual, vendas.get(0).getData());
    }

    /**
     * Caso de teste 2 - Cadastrar uma venda
     * 
     * 1 - Cadastrado um cliente
     * 2 - Cadastrada uma venda 
     * 3 - Verifica se venda foi cadastrada
     * 4 - Teste informações da venda
     * 5 - Testa pontuação do cliente
     *
     * @throws JsonProcessingException
     * @throws IOException
     */
    @Test
    public void test02() throws JsonProcessingException, IOException {
        Cliente cliente = new Cliente("Cliente Teste 2", SexoEnum.MASCULINO);
        cliente = clienteRepository.save(cliente);
      
        Venda venda = new Venda();
        venda.setCliente(cliente);
        venda.setValorTotal(new BigDecimal(1000.00));
        venda.setData(dataAtual);

        HttpEntity<Venda> entity = new HttpEntity<>(venda, headers);
        ResponseEntity<String> response = restTemplate.exchange(BASE_PATH, HttpMethod.POST, entity, String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        Venda vendaSalva = vendaRepository.findByCliente(cliente.getId()).get(0);
        
        Assert.assertNotNull(vendaSalva);                        
        Assert.assertEquals("Cliente Teste 2", vendaSalva.getCliente().getNome());
        Assert.assertEquals(SexoEnum.MASCULINO, vendaSalva.getCliente().getSexo());
        Assert.assertEquals(new BigDecimal(20.00).setScale(2), vendaSalva.getCliente().getPontuacao());
        Assert.assertEquals(new BigDecimal(1000.00).setScale(2), vendaSalva.getValorTotal());
        Assert.assertEquals(Long.valueOf(20), vendaSalva.getPontuacao());
        Assert.assertEquals(dataAtual, vendaSalva.getData());                       
    }

    /**
     * Caso de teste 3 - Lista com todas as vendas no intervalo de duas datas
     * 
     * 1 - Cadastrado dois clientes
     * 2 - Cadastrada duas vendas 
     * 3 - Testado se as duas vendas foram cadastradas
     * 4 - Testado informações das vendas cadastradas
     * 5 - Testado informções dos clientes
     * 6 - Teatado a quantidade de informaçoes que a rotina retorna as informações
     *     de acordo com as datas informadas
     * 
     * @throws JsonProcessingException
     * @throws IOException
     */
    @Test
    public void test03() throws JsonProcessingException, IOException {

        Calendar dataInicial = Calendar.getInstance();
        dataInicial.setTime(dataAtual);
        dataInicial.add(Calendar.DATE, +10);

        Calendar dataFinal = Calendar.getInstance();
        dataFinal.setTime(dataAtual);
        dataFinal.add(Calendar.DATE, +20);

        Cliente clienteInterno01 = new Cliente("José Pereira", SexoEnum.MASCULINO);
        Cliente clienteInterno02 = new Cliente("Marcio Silva", SexoEnum.MASCULINO);
        clienteInterno01 = clienteRepository.save(clienteInterno01);
        clienteInterno02 = clienteRepository.save(clienteInterno02);

        Venda vendaInterna01 = new Venda();
        vendaInterna01.setCliente(clienteInterno01);
        vendaInterna01.setValorTotal(new BigDecimal(1000.00));
        vendaInterna01.setData(dataInicial.getTime());

        Venda vendaInterna02 = new Venda();
        vendaInterna02.setCliente(clienteInterno02);
        vendaInterna02.setValorTotal(new BigDecimal(2000.00));
        vendaInterna02.setData(dataInicial.getTime());

        HttpEntity<Venda> entityPost = new HttpEntity<>(vendaInterna01, headers);
        restTemplate.exchange(BASE_PATH, HttpMethod.POST, entityPost, String.class);
        entityPost = new HttpEntity<>(vendaInterna02, headers);
        restTemplate.exchange(BASE_PATH, HttpMethod.POST, entityPost, String.class);

        VendaDTO dto = new VendaDTO();
        dto.setDataInicial(dataInicial.getTime());
        dto.setDataFinal(dataFinal.getTime());

        HttpEntity<VendaDTO> entity = new HttpEntity<>(dto, headers);
        ResponseEntity<String> response = restTemplate.exchange(BASE_PATH + "historic-dates", HttpMethod.POST, entity, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        List<Venda> vendas = mapper.readValue(response.getBody(), mapper.getTypeFactory().constructCollectionType(List.class, Venda.class));

        Assert.assertNotNull(vendas);
        Assert.assertEquals(2, vendas.size());
        if (vendas.get(0).getCliente().getId() == clienteInterno01.getId()) {
            Assert.assertEquals("José Pereira", vendas.get(0).getCliente().getNome());
            Assert.assertEquals(SexoEnum.MASCULINO, vendas.get(0).getCliente().getSexo());
            Assert.assertEquals(new BigDecimal(1000.00).setScale(2), vendas.get(0).getValorTotal());
            Assert.assertEquals(Long.valueOf(20), vendas.get(0).getPontuacao());
        } else if (vendas.get(0).getCliente().getId() == clienteInterno01.getId()) {
            Assert.assertEquals("Marcio Silva", vendas.get(0).getCliente().getNome());
            Assert.assertEquals(SexoEnum.MASCULINO, vendas.get(0).getCliente().getSexo());
            Assert.assertEquals(new BigDecimal(2000.00).setScale(2), vendas.get(0).getValorTotal());
            Assert.assertEquals(Long.valueOf(40), vendas.get(0).getPontuacao());
        } else {
            Assert.fail("Esperado <2> resultado, retornado <" + vendas.size() + ">");
        }
    }

    
    /**
     * Caso de teste 4 - Lista com todas as vendas de determinado sexo e no intervalo de duas datas
     * 
     * 1 - Cadastrado dois clientes
     * 2 - Cadastrada duas vendas 
     * 3 - Testado se as duas vendas foram cadastradas
     * 4 - Testado informações das vendas cadastradas
     * 5 - Testado informções dos clientes
     * 6 - Teatado a quantidade de informaçoes que a rotina retorna as informações
     *     de acordo com o sexo e as datas informadas 
     *     
     **/    
    @Test
    public void test04() throws JsonProcessingException, IOException {

        Calendar dataInicial = Calendar.getInstance();
        dataInicial.setTime(dataAtual);
        dataInicial.add(Calendar.DATE, +30);

        Calendar dataFinal = Calendar.getInstance();
        dataFinal.setTime(dataAtual);
        dataFinal.add(Calendar.DATE, +60);

        Cliente clienteInterno01 = new Cliente("Ana Aparecida", SexoEnum.FEMININO);
        Cliente clienteInterno02 = new Cliente("Roberto Lopes", SexoEnum.MASCULINO);
        clienteInterno01 = clienteRepository.save(clienteInterno01);
        clienteInterno02 = clienteRepository.save(clienteInterno02);

        Venda vendaInterna01 = new Venda();
        vendaInterna01.setCliente(clienteInterno01);
        vendaInterna01.setValorTotal(new BigDecimal(1000.00));
        vendaInterna01.setData(dataInicial.getTime());

        Venda vendaInterna02 = new Venda();
        vendaInterna02.setCliente(clienteInterno02);
        vendaInterna02.setValorTotal(new BigDecimal(2000.00));
        vendaInterna02.setData(dataInicial.getTime());

        HttpEntity<Venda> entityPost = new HttpEntity<>(vendaInterna01, headers);
        restTemplate.exchange(BASE_PATH, HttpMethod.POST, entityPost, String.class);
        entityPost = new HttpEntity<>(vendaInterna02, headers);
        restTemplate.exchange(BASE_PATH, HttpMethod.POST, entityPost, String.class);

        VendaDTO dto = new VendaDTO();
        dto.setSexo(SexoEnum.FEMININO);
        dto.setDataInicial(dataInicial.getTime());
        dto.setDataFinal(dataFinal.getTime());

        HttpEntity<VendaDTO> entity = new HttpEntity<>(dto, headers);
        ResponseEntity<String> response = restTemplate.exchange(BASE_PATH + "historic-gender-dates", HttpMethod.POST, entity, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        List<Venda> vendas = mapper.readValue(response.getBody(), mapper.getTypeFactory().constructCollectionType(List.class, Venda.class));

        Assert.assertNotNull(vendas);
        Assert.assertEquals(1, vendas.size());
        if (vendas.get(0).getCliente().getId() == clienteInterno01.getId()) {
            Assert.assertEquals("Ana Aparecida", vendas.get(0).getCliente().getNome());
            Assert.assertEquals(SexoEnum.FEMININO, vendas.get(0).getCliente().getSexo());
            Assert.assertEquals(new BigDecimal(1000.00).setScale(2), vendas.get(0).getValorTotal());
            Assert.assertEquals(Long.valueOf(20), vendas.get(0).getPontuacao());
        } else {
            Assert.fail("Esperado <1> resultado, retornado <" + vendas.size() + ">");
        }
    }
}
