import java.util.Stack;

public class Stack {
    public static void main(String[] args) {
        // Create a stack
        Stack<Integer> stack = new Stack<>();

        // Push elements onto the stack
        stack.push(10);
        stack.push(20);
        stack.push(30);

        // Check the element at the top of the stack
        System.out.println(stack.peek());  // Output: 30

        // Pop the top element from the stack
        stack.pop();

        // Check the element at the top of the stack again
        System.out.println(stack.peek());  // Output: 20

        // Check if the stack is empty
        System.out.println(stack.empty());  // Output: false
    }
}
