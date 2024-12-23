import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class FibonacciCalculator extends RecursiveTask<Long> {
    private final long n;

    public FibonacciCalculator(long n) {
        this.n = n;
    }

    @Override
    protected Long compute() {
        if (n <= 1) {
            return n;
        }
        // Create subtasks for n-1 and n-2
        FibonacciCalculator f1 = new FibonacciCalculator(n - 1);
        f1.fork(); // Start the first task in a separate thread
        FibonacciCalculator f2 = new FibonacciCalculator(n - 2);
        return f2.compute() + f1.join(); // Compute the second task and join the first
    }

    public static void main(String[] args) {
        long n = 50; // Change this value to compute a different Fibonacci number
        ForkJoinPool pool = new ForkJoinPool();
        long result = pool.invoke(new FibonacciCalculator(n));
        System.out.println("The " + n + "th Fibonacci number is: " + result);
    }
