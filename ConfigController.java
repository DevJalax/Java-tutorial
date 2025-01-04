@RefreshScope
@RestController
public class ConfigController {

    @Value("${app.message}")
    private String message;

    @GetMapping("/config-message")
    public String getMessage() {
        return message;
    }
}
