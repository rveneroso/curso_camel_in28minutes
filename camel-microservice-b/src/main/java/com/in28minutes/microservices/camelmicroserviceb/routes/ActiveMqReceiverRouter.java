package com.in28minutes.microservices.camelmicroserviceb.routes;

import com.in28minutes.microservices.camelmicroserviceb.CurrencyExchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ActiveMqReceiverRouter extends RouteBuilder {

    MyCurrentExchangeProcessor myCurrentExchangeProcessor;
    MyCurrentExchangeTransformer myCurrentExchangeTransformer;



    public ActiveMqReceiverRouter(MyCurrentExchangeProcessor myCurrentExchangeProcessor, MyCurrentExchangeTransformer myCurrentExchangeTransformer) {
        this.myCurrentExchangeProcessor = myCurrentExchangeProcessor;
        this.myCurrentExchangeTransformer = myCurrentExchangeTransformer;
    }

    @Override
    public void configure() throws Exception {
        from("activemq:my-activemq-queue")
                .unmarshal().json(JsonLibrary.Jackson, CurrencyExchange.class)
                .bean(myCurrentExchangeProcessor)
                .bean(myCurrentExchangeTransformer)
                .to("log:received-message-from-activemq");
    }
}

@Component
class MyCurrentExchangeProcessor {

    Logger logger = LoggerFactory.getLogger(MyCurrentExchangeProcessor.class);
    public void processMessage(CurrencyExchange currencyExchange) {
        logger.info("Do some processing with message with conversionMultiple {}", currencyExchange.getConversionMultiple());
    }
}

@Component
class MyCurrentExchangeTransformer {

    public CurrencyExchange processMessage(CurrencyExchange currencyExchange) {
        currencyExchange.setConversionMultiple(currencyExchange.getConversionMultiple().multiply(BigDecimal.TEN));
        return currencyExchange;
    }
}
