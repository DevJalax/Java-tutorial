import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api")
public class RequestValidationController {

    @Autowired
    private AuditService auditService;

    @PostMapping("/request")
    public ResponseEntity<String> processRequest(@RequestBody String request) {
        try {
            // Check if the request is in JSON format
            if (isJSON(request)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Please send the request in XML format.");
            }

            // Validate the request based on requestXSD
            boolean isValid = validateRequest(request);
            if (!isValid) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid request format.");
            }

            // Store the valid request in the audit table
            auditService.storeRequest(request);

            // Generate the response based on responseXSD
            String response = generateResponse(request);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing the request.");
        }
    }

   private boolean isJSON(String input) {
    if (input == null || input.isEmpty()) {
        return false; // Empty or null strings are not valid JSON
    }

    ObjectMapper objectMapper = new ObjectMapper();
    try {
        // Attempt to parse the input as JSON
        objectMapper.readTree(input);
        return true; // If parsing is successful, it's valid JSON
    } catch (JsonProcessingException e) {
        return false; // If an exception is thrown, it's not valid JSON
    }
}

    private boolean validateRequest(String request) {
        try {
            // Load the requestXSD schema
            SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
            Schema schema = schemaFactory.newSchema(new ByteArrayInputStream(requestXSD.getBytes(StandardCharsets.UTF_8)));

            // Create a validator
            Validator validator = schema.newValidator();

            // Validate the request against the schema
            validator.validate(new StreamSource(new ByteArrayInputStream(request.getBytes(StandardCharsets.UTF_8))));
            return true;
        } catch (Exception e) {
            // Handle validation errors
            return false;
        }
    }

    private String generateResponse(String request) {
        // Implement logic to generate the response based on responseXSD
        // ...
        return "Response generated successfully.";
    }
}
