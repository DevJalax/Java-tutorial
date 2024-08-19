import org.springframework.stereotype.Service;

@Service
public class FallbackService {

    public String primaryServiceCall() {
        // Simulate failure
        throw new RuntimeException("Primary service failed");
    }

    public String fallbackMethod() {
        return "Fallback response";
    }
}
