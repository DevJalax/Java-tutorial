import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TimeoutClient {
    private static final int TIMEOUT = 5000; // 5 seconds
    private static final String API1_URL = "https://api1.example.com";
    private static final String API2_URL = "https://api2.example.com";

    public static void main(String[] args) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        try {
            Future<String> future = executor.submit(new APICallable(API1_URL));
            String response = future.get(TIMEOUT, TimeUnit.MILLISECONDS);
            System.out.println("API1 response: " + response);
        } catch (TimeoutException e) {
            System.out.println("API1 call timed out, calling API2...");
            try {
                Future<String> future = executor.submit(new APICallable(API2_URL));
                String response = future.get(TIMEOUT, TimeUnit.MILLISECONDS);
                System.out.println("API2 response: " + response);
            } catch (TimeoutException e2) {
                System.out.println("API2 call also timed out.");
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }

    private static class APICallable implements Callable<String> {
        private final String url;

        APICallable(String url) {
            this.url = url;
        }

        @Override
        public String call() throws Exception {
            URL apiUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setConnectTimeout(TIMEOUT);
            connection.setReadTimeout(TIMEOUT);
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return "Success";
            } else {
                throw new IOException("API call failed with response code: " + responseCode);
            }
        }
    }
}
