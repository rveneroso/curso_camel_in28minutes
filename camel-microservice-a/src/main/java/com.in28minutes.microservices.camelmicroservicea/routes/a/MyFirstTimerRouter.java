package com.in28minutes.microservices.camelmicroservicea.routes.a;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MyFirstTimerRouter extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // Queue -> endpoint
        // Transformation
        // Database -> endpoint

        // Nos primeiros estágios do curso serão usados um timer como entrada e log como saída.
        // Por padrão, o timer executa de 1 em 1 segundo
        from("timer:first-timer") // A palavra timer define o tipo de endpoint. O nome first-timer é de escolha do desenvolvedor
                .transform().constant("Time now is " + LocalDateTime.now()) // Adiciona a data e hora atuais à mensagem original recebida pelo timer
                .to("log:first-timer"); // O mesmo que acima: log é o tipo de endpoint, first-time é o nome dado a ele pelo desenvolvedor.

        // Sem uma implementação correta da mensagem, os logs monstram ininterruptamente a mensagem abaixo:
        // Exchange[ExchangePattern: InOnly, BodyType: null, Body: [Body is null]]

    }
}
