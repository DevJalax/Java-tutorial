import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class Backpressure {

    @GetMapping("/data")
    public Flux<String> getData() {
        return Flux.range(1, 100)
                   .map(i -> {
                       // Simulate processing
                       return "Data " + i;
                   })
                   .onBackpressureBuffer(10); // Buffer size
    }
}
