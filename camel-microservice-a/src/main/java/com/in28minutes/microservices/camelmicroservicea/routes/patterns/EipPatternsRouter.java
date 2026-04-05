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

        Logger logger = LoggerFactory.getLogger(EipPatternsRouter.class);

        String dynamicRoutingSlip = "direct:endpoint1,direct:endpoint2,direct:i-can-put-any-name-here";

        from("timer:routingSlip?period=10000")
                .transform().constant("This message is hardcoded, something that you definitely should avoid")
                .routingSlip(simple(dynamicRoutingSlip));

        from("direct:endpoint1")
                .to("log:this-is-endpoint1");

        from("direct:endpoint2")
                .to("log:this-is-endpoint2");

        from("direct:i-can-put-any-name-here")
                .log("Running i-can-put-any-name-here endpoint")
                .to("activemq:routing-slip-queue");
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
