package com.example.camel.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

@Component
public class AdvancedRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // Error handling: Retry 3 times, then send to DLQ
        errorHandler(deadLetterChannel("jms:queue:deadLetterQueue")
            .maximumRedeliveries(3)
            .redeliveryDelay(2000)
            .retryAttemptedLogLevel(org.apache.camel.LoggingLevel.WARN));

        // REST Configuration
        restConfiguration()
            .component("restlet")
            .host("api.example.com")
            .bindingMode(RestBindingMode.auto);

        // Main route: Read from JMS, call REST API, write to file
        from("jms:queue:incomingOrders")
            .routeId("OrderProcessingRoute")
            .log("Received message: ${body}")
            .process("customProcessor")
            .to("rest:get:/external-service/orders/{orderId}?orderId=${body}")
            .log("REST response: ${body}")
            .to("file:output?fileName=order-${header.CamelFileName}.txt")
            .log("Order processed and written to file");
    }
}
