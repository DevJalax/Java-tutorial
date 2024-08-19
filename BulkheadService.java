import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class BulkheadService {

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Async
    public void executeTask() {
        executorService.submit(() -> {
            // Task logic here
        });
    }
}
