import java.util.HashMap;
import java.util.Map;

public class Map {
    public static void main(String[] args) {
        // Create a map
        Map<String, Integer> map = new HashMap<>();

        // Add some elements to the map
        map.put("Alice", 24);
        map.put("Bob", 25);
        map.put("Charlie", 30);

        // Check if the map contains a key
        System.out.println(map.containsKey("Alice"));  // Output: true
        System.out.println(map.containsKey("Eve"));  // Output: false

        // Check if the map contains a value
        System.out.println(map.containsValue(24));  // Output: true
        System.out.println(map.containsValue(20));  // Output: false

        // Get the value for a key
        System.out.println(map.get("Alice"));  // Output: 24

        // Remove an entry from the map
        map.remove("Bob");

        // Print the size of the map
        System.out.println(map.size());  // Output: 2
    }
}
