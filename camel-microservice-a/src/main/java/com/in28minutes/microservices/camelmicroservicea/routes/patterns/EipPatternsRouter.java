package com.in28minutes.microservices.camelmicroservicea.routes.patterns;

import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class EipPatternsRouter extends RouteBuilder {

    private SplitterComponent splitterComponent;

    public EipPatternsRouter(SplitterComponent splitterComponent) {
        this.splitterComponent = splitterComponent;
    }

    @Override
    public void configure() throws Exception {
        from("timer:multicast?period=10000")
                .multicast()
                .to("log:something1", "activemq:my-activemq-xml-queue");

//        from("file:camel-microservice-a/files/csv")
//                .unmarshal().csv()
//                .split(body())
//                .convertBodyTo(String.class)
//                .to("activemq:split-queue");

        from("file:camel-microservice-a/files/csv")
                .convertBodyTo(String.class)
                // O arquivo usado na aula desse assunto é um csv de uma única linha onde os campos são separados por vírgula.
                // Como o delimitador é justamente a vírgula, então cada campo da linha se torna uma mensagem enviada para split-queue.
                //.split(body(),",")

                // A sintaxe abaixo chama o método split do bean SplitterComponent passando a vírgula como delimitador.
                // Importante: todo o conteúdo do arquivo é considerado a mensagem. O arquivo data.csv tem 5 linhas: 1 cabeçalho e 4 linhas de dados. A variável body contém todas as 5 linhas.
                .bean("splitterComponent", "splitInput(${body}, ',')")
                .to("activemq:split-queue");
    }
}

@Component
class SplitterComponent {

    Logger logger = LoggerFactory.getLogger(SplitterComponent.class);
    public List<String> splitInput(String body, String delimiter) {
        logger.info("CVS file received: {}", body);
        return Arrays.asList(body.split(delimiter));
    }
}
