import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
public class CustomInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(CustomInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Read the request body
        String requestBody = getRequestBody(request);
        
        // Strip malicious scripts
        String cleanedBody = stripMaliciousScripts(requestBody);
        
        // Check for null values
        if (containsNullValues(cleanedBody)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Null values not allowed. Please pass valid values.");
            return false; // Stop further processing
        }

        // Log the cleaned request for auditing
        logger.info("Cleaned Request Body: {}", cleanedBody);
        
        // You can further process the cleanedBody if needed
        // For now, we'll just continue the request
        return true;
    }

    private String getRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
        return stringBuilder.toString();
    }

    private String stripMaliciousScripts(String body) {
        // Simple implementation to remove scripts (you can enhance this)
        return body.replaceAll("<script.*?>.*?</script>", ""); // Remove script tags
    }

    private boolean containsNullValues(String body) {
        // Check if the body contains any null values (you can enhance this logic)
        // Assuming JSON format, you can parse it and check for nulls
        // Here, we will just check for the string "null" for simplicity
        return body.contains(":null");
    }
}
