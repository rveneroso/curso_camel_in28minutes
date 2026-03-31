package com.in28minutes.microservices.camelmicroservicea.routes.a;

import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class MyFirstTimerRouter extends RouteBuilder {

    @Autowired
    private GetCurrentTimeBean getCurrentTimeBean;

    @Autowired
    private SimpleLoggingProcessingComponent loggingComponent;

    @Override
    public void configure() throws Exception {
        // Queue -> endpoint
        // Transformation
        // Database -> endpoint

        // Nos primeiros estágios do curso serão usados um timer como entrada e log como saída.
        // Por padrão, o timer executa de 1 em 1 segundo

        // Essa nova versão adiciona a saída gerada pelo método getCurrentTime do bean GetCurrentTimeBean ao body da mensagem.
        // O Camel usa automaticamente o retorno do bean como corpo da mensagem (body) — e é isso que o log está imprimindo.
        // O bean GetCurrentTimeBean tem apenas um método público. Por isso o Camel o utiliza como default.
        // É possível também especificar qual método deve ser chamado para compor o body da mensagem: bean("getCurrentTimeBean", "getCurrentTime").

        // No Apache Camel, processing é qualquer processamento que seja faça utilizando a mensagem recebida SEM ALTERÁ-LA. Já o Transformation,
        // como o próprio nome indica, é o processamento que modifica a mensagem recebida.

        // O corpo da mensagem pode ser acessado com {$body}
        from("timer:first-timer") // A palavra timer define o tipo de endpoint. O nome first-timer é de escolha do desenvolvedor
                .log("${body}")
                .bean(getCurrentTimeBean)
                .log("${body}")
                .bean(loggingComponent)
                .log("${body}")
                .to("log:first-timer"); // O mesmo que acima: log é o tipo de endpoint, first-time é o nome dado a ele pelo desenvolvedor.

    }
}

@Component
class GetCurrentTimeBean {
    public String getCurrentTime() {
        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        return now.format(formatter);
    }
}

@Component
class SimpleLoggingProcessingComponent {
    Logger logger = LoggerFactory.getLogger(SimpleLoggingProcessingComponent.class);
    public void process(String message) {
        logger.info("SimpleLoggingProcessingComponent {}", message); //
    }
}
