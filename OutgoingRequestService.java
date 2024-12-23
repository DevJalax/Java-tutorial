import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class OutgoingRequestService {

    private final Bucket bucket;
    private final AtomicInteger requestCount = new AtomicInteger(0);
    private final AtomicInteger totalAmount = new AtomicInteger(0);

    public OutgoingRequestService() {
        // Configure the bucket to allow 10 requests in total after 3 mandatory requests
        Bandwidth limit = Bandwidth.classic(10, Refill.greedy(10, Duration.ofMinutes(1)));
        this.bucket = Bucket.builder()
                            .addLimit(limit)
                            .build();
    }

    public String makeOutgoingRequest(int amount) {
        int currentRequestCount = requestCount.get();
        int currentTotalAmount = totalAmount.get();

        // Check if mandatory 3 requests are completed
        if (currentRequestCount < 3) {
            // Automatically allow the first 3 requests
            requestCount.incrementAndGet();
            totalAmount.addAndGet(amount);
            return "Request allowed: " + (currentRequestCount + 1) + " with amount " + amount + " INR";
        } else {
            // After 3 mandatory requests, check if the bucket has tokens left and total amount is under 3000 INR
            if (bucket.tryConsume(1) && (currentTotalAmount + amount) <= 3000) {
                requestCount.incrementAndGet();
                totalAmount.addAndGet(amount);
                return "Request allowed: " + (currentRequestCount + 1) + " with amount " + amount + " INR. Total spent: " + (currentTotalAmount + amount) + " INR";
            } else if ((currentTotalAmount + amount) > 3000) {
                return "Request denied: Exceeds amount limit of 3000 INR. Total spent: " + currentTotalAmount + " INR";
            } else {
                return "Request denied: Rate limit exceeded. Total requests: " + currentRequestCount;
            }
        }
    }

    public int getRequestCount() {
        return requestCount.get();
    }

    public int getTotalAmount() {
        return totalAmount.get();
    }
}
