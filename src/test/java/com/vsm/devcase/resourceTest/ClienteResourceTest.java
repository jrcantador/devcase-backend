package com.vsm.devcase.resourceTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vsm.devcase.domain.model.Cliente;
import com.vsm.devcase.domain.model.SexoEnum;
import com.vsm.devcase.infra.repository.ClienteRepository;
import com.vsm.devcase.infra.service.TokenAuthenticationService;
import java.io.IOException;
import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import org.junit.After;
import org.junit.Assert;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class ClienteResourceTest {

    final String BASE_PATH = "http://localhost:8888/clientes/";
    final String TOKEN = TokenAuthenticationService.createToken("admin");

    @Autowired
    private ClienteRepository clienteRepository;

    private RestTemplate restTemplate;
    private HttpHeaders headers;
    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void SetUp() throws Exception {
        clienteRepository.deleteAll();
        clienteRepository.save(new Cliente("Pedro Alves", SexoEnum.MASCULINO));

        restTemplate = new RestTemplate();
        headers = new HttpHeaders();
        headers.setBearerAuth(TOKEN);
    }

    @After
    public void tearDown() throws Exception {
        clienteRepository.delete(new Cliente("Pedro Alves", SexoEnum.MASCULINO));;
        clienteRepository.delete(new Cliente("João Medeiros", SexoEnum.MASCULINO));
    }

    @Test
    public void testList() throws JsonProcessingException, IOException {
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<String> response = restTemplate.exchange(BASE_PATH, HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        List<Cliente> clientes = mapper.readValue(response.getBody(), mapper.getTypeFactory().constructCollectionType(List.class, Cliente.class));

        Assert.assertNotNull(clientes);
        Assert.assertEquals(1, clientes.size());
        Assert.assertEquals("Pedro Alves", clientes.get(0).getNome());
        Assert.assertEquals(SexoEnum.MASCULINO, clientes.get(0).getSexo());
    }

    @Test
    public void testCreate() throws JsonProcessingException, IOException {
        HttpEntity<Cliente> entity = new HttpEntity<>(new Cliente("João Medeiros", SexoEnum.MASCULINO), headers);
        ResponseEntity<String> response = restTemplate.exchange(BASE_PATH, HttpMethod.POST, entity, String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
    }
}
