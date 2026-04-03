package com.in28minutes.microservices.camelmicroservicea.routes.b;

import org.apache.camel.Headers;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MyFileRouter extends RouteBuilder {

    private DeciderBean deciderBean;

    public MyFileRouter(DeciderBean deciderBean) {
        this.deciderBean = deciderBean;
    }
    /*
     * Observação: quando o código abaixo é executado, os arquivos que se encontravam em files/input são movidos (não são copiados) para a pasta output.
     * Porém, o Camel cria dentro de files/input, uma pasta chamada .camel e coloca nela todos os arquivos que originalmente estavam em files/input.
     * Isso é para controle interno do Camel, para que ele saiba o que já foi e o que ainda não foi processado.
     */
    @Override
    public void configure() throws Exception {
        from("file:camel-microservice-a/files/input")
                .routeId("Files-Input-Route")
                // choice é um exemplo do EIP Content Based Routing
                .choice()
                    .when(method(deciderBean))
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
                .to("direct:log-file-values")
                .to("file:camel-microservice-a/files/output");

        from("direct:log-file-values")
                .log("File values:\n")
                .log("${messageHistory} ${file:absolute.path}")
                .log("${file:name} ${file:name.ext} ${file:name.noext} $file:onlyname}")
                .log("${file:onlyname.noext} ${file:parent} ${file:path} ${file:absolute}")
                .log("${file:size} ${file:modified}")
                .log("${routeId} ${camelId} ${body}");
    }
}

@Component
class DeciderBean {
    Logger logger = LoggerFactory.getLogger(DeciderBean.class);

    public boolean isConditionMet(String body,
        @Headers Map<String,String> headers) {
        logger.info("Inside DeciderBean. Message body is {}. Headers are: {}", body,headers);
        return true;
    }
}
