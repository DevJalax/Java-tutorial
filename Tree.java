class Node {
  int data;
  Node left;
  Node right;

  public Node(int data) {
    this.data = data;
  }
}

class BinaryTree {
  Node root;

  public void addNode(int data) {
    root = addNodeRecursive(root, data);
  }

  private Node addNodeRecursive(Node current, int data) {
    if (current == null) {
      return new Node(data);
    }

    if (data < current.data) {
      current.left = addNodeRecursive(current.left, data);
    } else if (data > current.data) {
      current.right = addNodeRecursive(current.right, data);
    } else {
      // value already exists
      return current;
    }

    return current;
  }

  public void preOrderTraversal() {
    preOrderTraversal(root);
  }

  private void preOrderTraversal(Node current) {
    if (current != null) {
      System.out.print(current.data + " ");
      preOrderTraversal(current.left);
      preOrderTraversal(current.right);
    }
  }
}

public class Tree {
  public static void main(String[] args) {
    BinaryTree tree = new BinaryTree();
    tree.addNode(1);
    tree.addNode(2);
    tree.addNode(3);
    tree.addNode(4);
    tree.addNode(5);

    tree.preOrderTraversal();  // prints 1 2 4 5 3
  }
}
