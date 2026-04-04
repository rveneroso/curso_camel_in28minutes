package com.in28minutes.microservices.camelmicroservicea.routes.patterns;

import com.in28minutes.microservices.camelmicroservicea.CurrencyExchange;
import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import java.util.*;

public class ArrayListAggregationStrategy implements AggregationStrategy {

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {

        CurrencyExchange newBody =
                newExchange.getMessage().getBody(CurrencyExchange.class);

        if (oldExchange == null) {
            List<CurrencyExchange> list = new ArrayList<>();
            list.add(newBody);

            newExchange.getMessage().setBody(list);
            return newExchange;
        }

        List<CurrencyExchange> list =
                oldExchange.getMessage().getBody(List.class);

        list.add(newBody);

        return oldExchange;
    }
}
