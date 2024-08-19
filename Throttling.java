import net.jodah.expiringmap.ExpiringMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class Throttling {

    private final ExpiringMap<String, Integer> requests = ExpiringMap.builder()
            .expiration(1, TimeUnit.MINUTES)
            .build();

    @GetMapping("/throttle")
    public String throttleRequest() {
        String clientId = "client-id"; // Example client identifier
        requests.put(clientId, requests.getOrDefault(clientId, 0) + 1);
        if (requests.get(clientId) > 5) {
            return "Too many requests";
        }
        return "Request successful";
    }
}
