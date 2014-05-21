package generictree.tasks;

public class Test {

	public static void main(String[] args) {
		
		BinaryTree<Integer> tree = 
				new BinaryTree<Integer>(4);
		
		tree.InsertLeft(4, 5);
		tree.InsertRight(4, 6);
		
		tree.InsertLeft(5, 4);
		tree.InsertRight(5, 6);
		
		tree.InsertLeft(4, 3);
		tree.InsertRight(4, 2);
		
		tree.PrintTree(tree.Root);
		

	}

}
