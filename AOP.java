// Import necessary Spring and Spring Cloud dependencies
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.rabbit.inbound.RabbitInboundChannelAdapter;
import org.springframework.integration.rabbit.outbound.RabbitMessagingTemplate;
import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.messaging.MessageChannel;

@SpringBootApplication
@EnableEurekaClient
@EnableHystrix
@RibbonClient(name = "my-service")
public class AOP {

    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }

    // Define a message channel for Spring Integration
    @Bean
    public MessageChannel inputChannel() {
        return new DirectChannel();
    }

    // Define an integration flow with transformer and logging
    @Bean
    public IntegrationFlow integrationFlow() {
        return IntegrationFlows.from(inputChannel())
                .transform(new GenericTransformer<String, String>() {
                    @Override
                    public String transform(String source) {
                        return "Transformed: " + source; // Simple transformation logic
                    }
                })
                .handle("messageHandler", "handleMessage") // Service activator for handling messages
                .get();
    }

    // RabbitMQ inbound adapter to receive messages from a queue
    @Bean
    public RabbitInboundChannelAdapter rabbitInboundAdapter(ConnectionFactory connectionFactory) {
        RabbitInboundChannelAdapter adapter = new RabbitInboundChannelAdapter(connectionFactory);
        adapter.setOutputChannel(inputChannel());
        adapter.setQueueNames("myQueue");
        return adapter;
    }

    // RabbitMQ messaging template for sending messages
    @Bean
    public RabbitMessagingTemplate rabbitMessagingTemplate(ConnectionFactory connectionFactory) {
        return new RabbitMessagingTemplate(connectionFactory);
    }

    // Define a route locator for Spring Cloud Gateway
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("my-service", r -> r.path("/service/**")
                        .uri("lb://my-service")) // Load balancing with Ribbon
                .build();
    }

    // Logging aspect for method execution time (AOP)
    @ServiceActivator(inputChannel = "inputChannel")
    public void logMessage(String message) {
        System.out.println("Received message: " + message);
    }
}
