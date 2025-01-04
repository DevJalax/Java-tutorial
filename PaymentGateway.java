import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PaymentGateway {
    private String apiKey;
    private String apiSecret;

    public PaymentGateway(String apiKey, String apiSecret) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }

    public void refundPayment(String paymentId, double amount, String reason) throws PaymentGatewayException {
        // Replace with your payment gateway API endpoint
        String apiUrl = "https://api.paymentgateway.com/v1/refund";

        // Create a new OkHttp client
        OkHttpClient client = new OkHttpClient();

        // Create a new request
        Request request = new Request.Builder()
               .url(apiUrl)
               .post(RequestBody.create(MediaType.get("application/json"), getRequestBody(paymentId, amount, reason)))
               .header("Authorization", "Bearer " + apiKey)
               .header("Content-Type", "application/json")
               .build();

        // Execute the request
        Response response = client.newCall(request).execute();

        // Check if the response was successful
        if (!response.isSuccessful()) {
            throw new PaymentGatewayException("Refund failed: " + response.code());
        }
    }

    private String getRequestBody(String paymentId, double amount, String reason) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("payment_id", paymentId);
        requestBody.put("amount", amount);
        requestBody.put("reason", reason);

        return new JSONObject(requestBody).toString();
    }
}

class PaymentGatewayException extends Exception {
    public PaymentGatewayException(String message) {
        super(message);
    }
}
