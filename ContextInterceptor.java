import io.micrometer.context.ContextRegistry;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.Collections;
import java.util.UUID;

@Component
@Slf4j
public class ContextInterceptor implements WebGraphQlInterceptor {

    public static final String TID_HEADER_KEY = "tid";
    private static final String TRANSACTION_ID_KEY = "txId";

    @Override
    public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {

        //Passing txId from gateway in context and MDC
        String tid = request.getHeaders().getFirst(TID_HEADER_KEY);
        if (tid == null) {
            log.warn("tid not found in request header, generating new tid");
            tid = UUID.randomUUID().toString();
        }
        //Register a ThreadLocalAccessor that will help transferring
        //the transaction id from Log4j's ThreadContext
        //from and to various context objects
        ContextRegistry.getInstance().registerThreadLocalAccessor(
                TRANSACTION_ID_KEY,
                () -> MDC.get(TRANSACTION_ID_KEY),
                value1 -> MDC.put(TRANSACTION_ID_KEY, value1),
                () -> MDC.remove(TRANSACTION_ID_KEY));
        //Store the transaction id in MDC
        MDC.put(TRANSACTION_ID_KEY,tid);
        return chain.next(request).contextWrite(Context.of(TRANSACTION_ID_KEY,tid));
    }
}
