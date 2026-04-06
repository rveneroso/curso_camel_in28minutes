package com.in28minutes.microservices.camelmicroservicea.routes.patterns;

import org.apache.camel.ExchangeProperties;
import org.apache.camel.Headers;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class EipPatternsRouter extends RouteBuilder {

    private DynamicRouterBean dynamicRouterBean;

    public EipPatternsRouter(DynamicRouterBean dynamicRouterBean) {
        this.dynamicRouterBean = dynamicRouterBean;
    }

    @Override
    public void configure() throws Exception {

        Logger logger = LoggerFactory.getLogger(EipPatternsRouter.class);

        // Tracing eleva ainda mais o nível de informações que o Camel loga durante as ações, independente do nível de log definido para a aplicação.
        getContext().setTracing(true);

        // Em caso de erro, é possível direcionar as mensagens que falharam para uma DLQ - Dead letter queue
        errorHandler(deadLetterChannel("activemq:general-dead-letter-queue"));

        from("timer:routingSlip?period=10000")
                .transform().constant("This message is hardcoded, something that you definitely should avoid")
                // Camel também oferece um recurso para o envio da mensagem para um endpoint alternativo. Esse envio é assíncrono e não afeta nem o fluxo nem a mensagem original.
                // É um recurso útil principalmente para auditoria. No exemplo abaixo, além da mensagem ser enviada ao endpoint definido dinamicamente, uma cópia da mensagem será enviada à fila audit-queue no ActiveMQ.
                // Importante: wireTap só executa 1 vez para cada Exchange.
                .wireTap("activemq:audit-queue")
                .dynamicRouter(method(dynamicRouterBean));

    }
}

@Component
class DynamicRouterBean {

    Logger logger = LoggerFactory.getLogger(DynamicRouterBean.class);

    private int invocations;

    public String decideNextEndpoint(@ExchangeProperties Map<String, String> properties, @Headers Map<String, String> headers, String body) {

        logger.info("Properties: {}, Headers: {}, Body: {}", properties, headers, body);
        invocations++;

        if(invocations>10) return null;

        // O Camel tem uma sintaxe própria que permite ler diretatmente os valores das propriedades definidas em arquivos yaml, sem a necessidade de usar ConfigurationProperties ou anotações @value.
        // Basta usar a sintaxe {{}} como no código abaixo.
        if(invocations%2==0) {
            return "{{app.dynamic-routes.even}}";
        } else {
            return "{{app.dynamic-routes.odd}}";
        }
    }
}
