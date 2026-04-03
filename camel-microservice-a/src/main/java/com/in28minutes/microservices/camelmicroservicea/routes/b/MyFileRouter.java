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
                .routeId("Files-Input-Route")
                .choice()
                    .when(simple("${file:ext} ends with 'xml'"))
                        .log("XML file")
                    .when(simple("${file:ext} ends with 'json'"))
                        .log("JSON file")
                    .otherwise()
                        .log("Not xml or json file")
                .end()

                .convertBodyTo(String.class)
                // Outra maneira de converter o corpo da mensagem para String é .transform().body(String.class)

                .choice()
                    .when(simple("${body} contains 'USD'"))
                        .log("File is a USD conversion")
                .end()
                .log("${messageHistory} ${file:absolute.path}")
                .to("file:camel-microservice-a/files/output");
    }
}
