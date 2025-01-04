import java.util.BitSet;

public class BitSet {
    public static void main(String[] args) {
        // Create a BitSet with capacity 100
        BitSet bits = new BitSet(100);

        // Set the value of a bit
        bits.set(5);
        bits.set(7);
        bits.set(10);

        // Check the value of a bit
        System.out.println(bits.get(5));  // Output: true
        System.out.println(bits.get(6));  // Output: false

        // Clear the value of a bit
        bits.clear(5);
        System.out.println(bits.get(5));  // Output: false

        // Flip the value of a bit
        bits.flip(7);
        System.out.println(bits.get(7));  // Output: false

        // Check the number of bits set to true
        System.out.println(bits.cardinality());  // Output: 2
    }
}
