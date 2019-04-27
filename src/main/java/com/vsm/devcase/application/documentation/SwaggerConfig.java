package com.vsm.devcase.application.documentation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *
 * @author junior.cantador
 */
@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket detalheApi() {

        Docket docket = new Docket(DocumentationType.SWAGGER_2);

        docket
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.vsm.devcase"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(this.informacoesApi().build());

        return docket;
    }

    private ApiInfoBuilder informacoesApi() {

        ApiInfoBuilder apiInfoBuilder = new ApiInfoBuilder();

        apiInfoBuilder.title("Api");
        apiInfoBuilder.description("Api Documentação");
        apiInfoBuilder.version("1.0");
        apiInfoBuilder.licenseUrl("www.vsm.com.br");
        apiInfoBuilder.contact(this.contato());

        return apiInfoBuilder;

    }

    private Contact contato() {
        return new Contact(
                "Junior de Menezes Cantador",
                "https://github.com/jrcantador",
                "jrcantador@gmail.com");
    }
}
