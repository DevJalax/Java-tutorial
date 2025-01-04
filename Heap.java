import java.util.PriorityQueue;

public class Heap {
    public static void main(String[] args) {
        // Create a heap
        PriorityQueue<Integer> heap = new PriorityQueue<>();

        // Add elements to the heap
        heap.offer(10);
        heap.offer(3);
        heap.offer(5);
        heap.offer(1);
        heap.offer(4);

        // Print the top element of the heap
        System.out.println(heap.peek());  // Output: 1

        // Remove the top element of the heap
        heap.poll();

        // Print the top element of the heap again
        System.out.println(heap.peek());  // Output: 3

        // Print the size of the heap
        System.out.println(heap.size());  // Output: 4
    }
}
