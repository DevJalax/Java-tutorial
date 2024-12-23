import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOException;
import org.jpos.iso.channel.NACChannel;
import org.jpos.iso.packager.GenericPackager;
import org.jpos.util.Logger;
import org.jpos.util.SimpleLogListener;

import java.io.IOException;

public class ISO8583Server {

    private static final int PORT = 5454; // Change as needed
    private static final String HOST = "localhost"; // Change to your host

    public static void main(String[] args) {
        Logger logger = new Logger();
        logger.addListener(new SimpleLogListener());

        try {
            // Load the packager configuration
            GenericPackager packager = new GenericPackager("path/to/your/packager.xml");

            // Create a channel to listen for incoming messages
            NACChannel channel = new NACChannel(HOST, PORT, packager, null);
            channel.connect();

            while (true) {
                try {
                    ISOMsg isoMsg = channel.receive(); // Receive ISO message
                    processMessage(isoMsg); // Process the received message
                    ISOMsg responseMsg = createResponse(isoMsg); // Create a response message
                    channel.send(responseMsg); // Send the response back
                } catch (ISOException e) {
                    logger.error("ISOException while processing message: " + e.getMessage(), e);
                } catch (IOException e) {
                    logger.error("IOException while receiving/sending message: " + e.getMessage(), e);
                    break; // Exit the loop if there's a serious network issue
                } catch (Exception e) {
                    logger.error("Unexpected error occurred: " + e.getMessage(), e);
                }
            }
        } catch (IOException e) {
            logger.error("Failed to connect to the channel: " + e.getMessage(), e);
        } catch (ISOException e) {
            logger.error("Failed to initialize packager: " + e.getMessage(), e);
        }
    }

    private static void processMessage(ISOMsg isoMsg) {
        try {
            System.out.println("Received Message: " + isoMsg);

            // Example of checking for specific message types (MTI)
            String mti = isoMsg.getMTI();
            switch (mti) {
                case "0200": // Authorization request
                    handleAuthorizationRequest(isoMsg);
                    break;
                case "0210": // Authorization response
                    handleAuthorizationResponse(isoMsg);
                    break;
                default:
                    throw new ISOException("Unsupported MTI: " + mti);
            }
        } catch (ISOException e) {
            System.err.println("Error processing ISO message: " + e.getMessage());
        }
    }

    private static void handleAuthorizationRequest(ISOMsg isoMsg) throws ISOException {
        // Extract required fields from the request
        String transactionAmount = isoMsg.getString(4); // Transaction amount
        String cardNumber = isoMsg.getString(2); // Card number
        String terminalId = isoMsg.getString(41); // Terminal ID

        System.out.println("Processing Authorization Request:");
        System.out.println("Card Number: " + cardNumber);
        System.out.println("Transaction Amount: " + transactionAmount);
        System.out.println("Terminal ID: " + terminalId);

        // Here you would add your business logic to authorize the transaction
        boolean isAuthorized = authorizeTransaction(cardNumber, transactionAmount);

        if (isAuthorized) {
            System.out.println("Transaction Authorized");
        } else {
            System.out.println("Transaction Declined");
            throw new ISOException("Transaction Declined");
        }
    }

    private static boolean authorizeTransaction(String cardNumber, String amount) {
        // Simulate authorization logic. In a real application, you'd check against a database or payment gateway.
        return Integer.parseInt(amount) <= 1000; // Example condition for authorization
    }

    private static void handleAuthorizationResponse(ISOMsg isoMsg) throws ISOException {
        // Handle authorization response logic here if needed
        System.out.println("Processing Authorization Response: " + isoMsg);
    }

    private static ISOMsg createResponse(ISOMsg request) throws ISOException {
        ISOMsg response = new ISOMsg();
        response.setMTI("0210"); // Set MTI for response
        response.set(11, request.getString(11)); // Echo the STAN from the request

        // Set response code based on processing result
        if ("00".equals(request.getString(39))) {  // Assuming 39 is set during processing
            response.set(39, "00"); // Success code
        } else {
            response.set(39, "05"); // Decline code for example
        }

        return response;
    }
}
