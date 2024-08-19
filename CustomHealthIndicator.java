import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class CustomHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        // Logic to check health
        boolean isHealthy = checkServiceHealth();
        if (isHealthy) {
            return Health.up().build();
        }
        return Health.down().withDetail("Error", "Service is down").build();
    }

    private boolean checkServiceHealth() {
        // Implement health check logic
        return true; // or false based on health check
    }
}
