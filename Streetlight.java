import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Streetlight {
    private int id;
    private boolean isOn;
    private int ambientLightLevel; // Simulated ambient light level (0-100)

    public Streetlight(int id) {
        this.id = id;
        this.isOn = false;
        this.ambientLightLevel = 100; // Default to bright
    }

    public void updateAmbientLight(int lightLevel) {
        this.ambientLightLevel = lightLevel;
        controlLight();
    }

    private void controlLight() {
        if (ambientLightLevel < 50) {
            turnOn();
        } else {
            turnOff();
        }
    }

    public void turnOn() {
        isOn = true;
        logStatus("ON");
    }

    public void turnOff() {
        isOn = false;
        logStatus("OFF");
    }

    public boolean isOn() {
        return isOn;
    }

    private void logStatus(String status) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("streetlight_log.txt", true))) {
            writer.write("Streetlight " + id + " is " + status + " at ambient light level " + ambientLightLevel + "\n");
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }
}
