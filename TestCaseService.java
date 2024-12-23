import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class TestCaseService {

    private final RestTemplate restTemplate;

    public TestCaseService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void runTestCases(MultipartFile file) throws IOException {
        // Read the Excel file
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0); // Assuming first sheet has the test cases

        // Iterate through the rows (skipping the header row)
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                String url = row.getCell(0).getStringCellValue();  // Controller URL
                String method = row.getCell(1).getStringCellValue(); // HTTP Method (GET, POST, etc.)
                String requestPayload = row.getCell(2).getStringCellValue(); // Request payload (as JSON)
                String expectedResponse = row.getCell(3).getStringCellValue(); // Expected response (as JSON)

                // Send request to the controller
                ResponseEntity<String> response = invokeController(url, method, requestPayload);

                // Compare the response with the expected response
                if (response.getBody().equals(expectedResponse)) {
                    System.out.println("Test case " + (i + 1) + " passed.");
                } else {
                    System.out.println("Test case " + (i + 1) + " failed. Expected: " + expectedResponse + ", but got: " + response.getBody());
                }
            }
        }

        workbook.close();
    }

    private ResponseEntity<String> invokeController(String url, String method, String payload) {
        switch (method.toUpperCase()) {
            case "GET":
                return restTemplate.getForEntity(url, String.class);
            case "POST":
                return restTemplate.postForEntity(url, payload, String.class);
            case "PUT":
                return restTemplate.exchange(url, org.springframework.http.HttpMethod.PUT, null, String.class);
            case "DELETE":
                return restTemplate.exchange(url, org.springframework.http.HttpMethod.DELETE, null, String.class);
            default:
                throw new IllegalArgumentException("Unsupported method: " + method);
        }
    }
}
