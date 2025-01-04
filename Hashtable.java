import java.util.Hashtable;

public class Hashtable {
    public static void main(String[] args) {
        // Create a Hashtable
        Hashtable<String, Integer> table = new Hashtable<>();

        // Add some elements to the Hashtable
        table.put("Alice", 24);
        table.put("Bob", 25);
        table.put("Charlie", 30);

        // Check if the Hashtable contains a key
        System.out.println(table.containsKey("Alice"));  // Output: true
        System.out.println(table.containsKey("Eve"));  // Output: false

        // Check if the Hashtable contains a value
        System.out.println(table.containsValue(24));  // Output: true
        System.out.println(table.containsValue(20));  // Output: false

        // Get the value for a key
        System.out.println(table.get("Alice"));  // Output: 24

        // Remove an entry from the Hashtable
        table.remove("Bob");

        // Print the size of the Hashtable
        System.out.println(table.size());  // Output: 2
    }
}
