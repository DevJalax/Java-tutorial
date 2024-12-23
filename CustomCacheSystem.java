import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CustomCacheSystem {
    
    // LRU Cache class with expiration and thread safety
    static class LRUCache<K, V> {
        private class Node {
            K key;
            V value;
            long timestamp; // To track the entry time
            Node prev;
            Node next;

            Node(K key, V value) {
                this.key = key;
                this.value = value;
                this.timestamp = System.currentTimeMillis(); // Set timestamp on creation
            }
        }

        private final int capacity;
        private final long expirationTimeMillis;
        private final HashMap<K, Node> cacheMap;
        private final Node head;
        private final Node tail;

        // Constructor to initialize cache with a capacity and expiration time
        public LRUCache(int capacity, long expirationTimeMillis) {
            this.capacity = capacity;
            this.expirationTimeMillis = expirationTimeMillis;
            this.cacheMap = new HashMap<>();
            this.head = new Node(null, null); // Dummy head
            this.tail = new Node(null, null); // Dummy tail
            head.next = tail;
            tail.prev = head;
        }

        // Synchronized get method for thread-safe access
        public synchronized V get(K key) {
            if (!cacheMap.containsKey(key)) {
                return null;
            }

            Node node = cacheMap.get(key);

            // Check for expiration
            if (isExpired(node)) {
                removeNode(node);
                cacheMap.remove(key);
                return null;  // The value has expired
            }

            // Move accessed node to the front (most recently used)
            removeNode(node);
            addToFront(node);

            return node.value;
        }

        // Synchronized put method for thread-safe access
        public synchronized void put(K key, V value) {
            if (cacheMap.containsKey(key)) {
                Node existingNode = cacheMap.get(key);
                existingNode.value = value;
                existingNode.timestamp = System.currentTimeMillis();  // Reset timestamp
                removeNode(existingNode);   // Move to front as recently used
                addToFront(existingNode);
            } else {
                if (cacheMap.size() == capacity) {
                    // Evict least recently used node (tail.prev)
                    cacheMap.remove(tail.prev.key);
                    removeNode(tail.prev);
                }
                Node newNode = new Node(key, value);
                cacheMap.put(key, newNode);
                addToFront(newNode);
            }

            // Remove any expired entries
            removeExpiredEntries();
        }

        // Helper method to check if a node is expired
        private boolean isExpired(Node node) {
            return System.currentTimeMillis() - node.timestamp > expirationTimeMillis;
        }

        // Helper method to remove expired entries
        private void removeExpiredEntries() {
            Iterator<Map.Entry<K, Node>> iterator = cacheMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<K, Node> entry = iterator.next();
                Node node = entry.getValue();
                if (isExpired(node)) {
                    removeNode(node);
                    iterator.remove();  // Remove entry from the HashMap
                }
            }
        }

        // Helper method to remove a node from the linked list
        private void removeNode(Node node) {
            Node prevNode = node.prev;
            Node nextNode = node.next;
            prevNode.next = nextNode;
            nextNode.prev = prevNode;
        }

        // Helper method to add a node to the front (just after the head)
        private void addToFront(Node node) {
            Node nextNode = head.next;
            head.next = node;
            node.prev = head;
            node.next = nextNode;
            nextNode.prev = node;
        }

        // Method to display cache contents for testing
        public synchronized void displayCache() {
            Node current = head.next;
            while (current != tail) {
                System.out.print(current.key + " ");
                current = current.next;
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // Cache capacity = 3, Expiration time = 5000ms (5 seconds)
        LRUCache<Integer, String> cache = new LRUCache<>(3, 5000);

        cache.put(1, "Data1");
        cache.put(2, "Data2");
        cache.put(3, "Data3");

        System.out.println("Cache after adding 3 elements:");
        cache.displayCache();

        Thread.sleep(3000);  // Sleep for 3 seconds
        cache.get(1);  // Access key 1, making it most recently used
        cache.put(4, "Data4");  // Should evict key 2

        System.out.println("Cache after accessing key 1 and adding key 4:");
        cache.displayCache();

        Thread.sleep(3000);  // Sleep for another 3 seconds (Total 6 seconds, key 1 should expire)
        cache.put(5, "Data5");  // Key 1 should be expired by now, key 4 and 3 remain

        System.out.println("Cache after adding key 5 (key 1 should be expired):");
        cache.displayCache();
    }
}
