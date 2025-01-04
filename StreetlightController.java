import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class StreetlightController {
    private List<Streetlight> streetlights;
    private MQTTClient mqttClient;

    public StreetlightController(int numberOfLights, String brokerUrl) {
        streetlights = new ArrayList<>();
        mqttClient = new MQTTClient(brokerUrl, "SmartCityController");
        for (int i = 0; i < numberOfLights; i++) {
            streetlights.add(new Streetlight(i + 1));
        }
        mqttClient.subscribe("streetlight/control");
    }

    public void simulateAmbientLight() {
        Random random = new Random();
        for (Streetlight light : streetlights) {
            int lightLevel = random.nextInt(100); // Random ambient light level
            System.out.println("Ambient light level for Streetlight " + light.id + ": " + lightLevel);
            light.updateAmbientLight(lightLevel);
            // Publish the status to MQTT
            mqttClient.publish("streetlight/status/" + light.id, light.isOn() ? "ON" : "OFF");
        }
    }

    public void controlLightManually() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter streetlight ID to toggle (1-" + streetlights.size() + ") or 'exit' to quit:");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                break;
            }
            try {
                int id = Integer.parseInt(input);
                if (id >= 1 && id <= streetlights.size()) {
                    Streetlight light = streetlights.get(id - 1);
                    if (light.isOn()) {
                        light.turnOff();
                    } else {
                        light.turnOn();
                    }
                    // Publish the control command to MQTT
                    mqttClient.publish("streetlight/control", "Toggle " + light.id);
                } else {
                    System.out.println("Invalid ID. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number or 'exit'.");
            }
        }
        scanner.close();
    }

    public void disconnect() {
        mqttClient.disconnect();
    }
}
