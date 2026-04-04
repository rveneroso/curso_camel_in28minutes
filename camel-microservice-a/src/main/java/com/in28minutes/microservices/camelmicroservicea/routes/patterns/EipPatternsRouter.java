package com.in28minutes.microservices.camelmicroservicea.routes.patterns;

import com.in28minutes.microservices.camelmicroservicea.CurrencyExchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
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


        from("file:camel-microservice-a/files/aggregate-json")
                .unmarshal().json(JsonLibrary.Jackson, CurrencyExchange.class)
                .aggregate(simple("${body.to}"), new ArrayListAggregationStrategy())
                // A linha abaixo diz: “Só finalize o grupo quando tiver 3 mensagens daquele grupo”
                .completionSize(3)
                // Para garantir a exibição dos logs, usar completionTimeout, que diz:
                // “Se não chegar mais mensagens em 5s, finalize mesmo incompleto”
                .completionTimeout(5000)
                .to("log:aggregate-json");
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
