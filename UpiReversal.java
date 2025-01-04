import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UpiReversal {
    public static void main(String[] args) throws Exception {
        // Replace with your UPI gateway API endpoint
        String apiUrl = "https://api.upigateway.com/v1/reverse";

        // Replace with your UPI gateway API key
        String apiKey = "YOUR_API_KEY_HERE";

        // Replace with the transaction ID to be reversed
        String transactionId = "TRANSACTION_ID_HERE";

        // Replace with the reason for reversal
        String reason = "Transaction failed";

        // Create a new OkHttp client
        OkHttpClient client = new OkHttpClient();

        // Create a new request
        Request request = new Request.Builder()
               .url(apiUrl)
               .post(RequestBody.create(MediaType.get("application/json"), getRequestBody(transactionId, reason)))
               .header("Authorization", "Bearer " + apiKey)
               .header("Content-Type", "application/json")
               .build();

        // Execute the request
        Response response = client.newCall(request).execute();

        // Check if the response was successful
        if (!response.isSuccessful()) {
            throw new Exception("UPI reversal failed: " + response.code());
        }

        // Print the response
        System.out.println("UPI reversal successful: " + response.body().string());
    }

    private static String getRequestBody(String transactionId, String reason) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("transaction_id", transactionId);
        requestBody.put("reason", reason);

        return new JSONObject(requestBody).toString();
    }
}
