import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RateLimiting {

    private final RateLimiter rateLimiter = RateLimiter.create(5.0); // 5 requests per second

    @GetMapping("/rate-limit")
    public String rateLimit() {
        if (rateLimiter.tryAcquire()) {
            return "Request successful";
        }
        return "Rate limit exceeded";
    }
}
