package com.in28minutes.microservices.camelmicroservicea.routes.patterns;

import org.apache.camel.ExchangeProperties;
import org.apache.camel.Headers;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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


        from("timer:routingSlip?period=10000")
                .transform().constant("This message is hardcoded, something that you definitely should avoid")
                .dynamicRouter(method(dynamicRouterBean));

    }
}

@Component
class DynamicRouterBean {

    Logger logger = LoggerFactory.getLogger(DynamicRouterBean.class);
    public String decideNextEndpoint(@ExchangeProperties Map<String, String> properties, @Headers Map<String, String> headers, String body) {
        logger.info("Properties: {}, Headers: {}, Body: {}", properties, headers, body);
        return "activemq:dynamic-router-queue";
    }
}
