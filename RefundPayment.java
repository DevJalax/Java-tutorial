import java.util.HashMap;
import java.util.Map;

import com.example.paymentgateway.PaymentGateway;
import com.example.paymentgateway.PaymentGatewayException;

public class RefundPayment {
    public static void main(String[] args) {
        // Replace with your payment gateway API credentials
        String apiKey = "YOUR_API_KEY_HERE";
        String apiSecret = "YOUR_API_SECRET_HERE";

        // Replace with the payment ID to be refunded
        String paymentId = "PAYMENT_ID_HERE";

        // Replace with the amount to be refunded
        double amount = 100.0;

        // Replace with the reason for refund
        String reason = "Customer requested refund";

        // Create a new payment gateway instance
        PaymentGateway paymentGateway = new PaymentGateway(apiKey, apiSecret);

        try {
            // Refund the payment
            paymentGateway.refundPayment(paymentId, amount, reason);

            System.out.println("Refund successful");
        } catch (PaymentGatewayException e) {
            System.out.println("Refund failed: " + e.getMessage());
        }
    }
}
