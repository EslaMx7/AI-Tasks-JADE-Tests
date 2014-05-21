package trees.tasks;

public class Node {
	int data;
	Node left;
	Node right;
	
	public Node(int ddata, Node left, Node right) {
		this.data = ddata;
		this.left = null;
		this.right = null;
	}

	public void displayNode(Node n) {
		System.out.print(n.data + " ");
	}
}
