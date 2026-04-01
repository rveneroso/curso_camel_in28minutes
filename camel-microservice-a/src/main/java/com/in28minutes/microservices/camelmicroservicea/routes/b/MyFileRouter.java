package com.in28minutes.microservices.camelmicroservicea.routes.b;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class MyFileRouter extends RouteBuilder {

    /*
     * Observação: quando o código abaixo é executado, os arquivos que se encontravam em files/input são movidos (não são copiados) para a pasta output.
     * Porém, o Camel cria dentro de files/input, uma pasta chamada .camel e coloca nela todos os arquivos que originalmente estavam em files/input.
     * Isso é para controle interno do Camel, para que ele saiba o que já foi e o que ainda não foi processado.
     */
    @Override
    public void configure() throws Exception {
        from("file:camel-microservice-a/files/input")
                .log("${body}")
                .to("file:camel-microservice-a/files/output");
    }
}
