import java.util.Properties;

public class Properties {
    public static void main(String[] args) {
        // Create a Properties object
        Properties properties = new Properties();

        // Set some properties
        properties.setProperty("color", "blue");
        properties.setProperty("size", "large");

        // Get a property
        System.out.println(properties.getProperty("color"));  // Output: blue
        System.out.println(properties.getProperty("weight"));  // Output: null

        // Get a property with a default value
        System.out.println(properties.getProperty("weight", "0"));  // Output: 0

        // Print all properties
        properties.forEach((key, value) -> System.out.println(key + ": " + value));

        // Remove a property
        properties.remove("size");
        System.out.println(properties.getProperty("size"));  // Output: null
    }
}
