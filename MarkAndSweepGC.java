import java.util.HashSet;
import java.util.Set;

public class MarkAndSweepGC {
    static class Object {
        boolean marked; // Mark bit
        Set<Object> references; // References to other objects

        public Object() {
            this.marked = false;
            this.references = new HashSet<>();
        }

        public void addReference(Object obj) {
            references.add(obj);
        }
    }

    private Set<Object> heap; // Simulating the heap memory

    public MarkAndSweepGC() {
        heap = new HashSet<>();
    }

    // Mark phase
    public void mark(Object root) {
        if (!root.marked) {
            root.marked = true; // Mark the object as reachable
            for (Object ref : root.references) {
                mark(ref); // Recursively mark all referenced objects
            }
        }
    }

    // Sweep phase
    public void sweep() {
        Set<Object> toRemove = new HashSet<>();
        for (Object obj : heap) {
            if (!obj.marked) {
                toRemove.add(obj); // Collect objects to be removed
            } else {
                obj.marked = false; // Reset mark for next collection
            }
        }
        heap.removeAll(toRemove); // Remove unmarked objects from heap
    }

    // Method to add an object to the heap
    public void addObject(Object obj) {
        heap.add(obj);
    }

    public static void main(String[] args) {
        MarkAndSweepGC gc = new MarkAndSweepGC();

        // Creating objects
        Object obj1 = new Object();
        Object obj2 = new Object();
        Object obj3 = new Object();
        
        // Setting up references
        obj1.addReference(obj2);
        obj2.addReference(obj3);

        // Adding objects to the heap
        gc.addObject(obj1);
        gc.addObject(obj2);
        gc.addObject(obj3);

        // Simulating garbage collection
        System.out.println("Running Mark and Sweep...");
        gc.mark(obj1); // Starting from root
        gc.sweep(); // Sweep unmarked objects

        System.out.println("Garbage collection completed.");
        System.out.println("Remaining objects in heap: " + gc.heap.size());
    }
}
