import java.util.Scanner;

public class SmartCityIoT {
    public static void main(String[] args) {
        String brokerUrl = "tcp://localhost:1883"; // Change this to your MQTT broker URL
        StreetlightController controller = new StreetlightController(5, brokerUrl); // Create 5 streetlights
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nSmart City IoT Controller");
            System.out.println("1. Simulate Ambient Light");
            System.out.println("2. Control Streetlights Manually");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    controller.simulateAmbientLight();
                    break;
                case 2:
                    controller.controlLightManually();
                    break;
                case 3:
                    System.out.println("Exiting...");
                    controller.disconnect();
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
