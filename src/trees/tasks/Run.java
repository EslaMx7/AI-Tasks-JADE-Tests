package trees.tasks;

public class Run {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		BinaryTree tree = new BinaryTree();
		
		tree.insertRoot(4);
		tree.insertLeft(4, 5);
		tree.insertRight(4, 6);
		tree.insertLeft(5, 4);
		tree.insertRight(5, 6);
		tree.insertLeft(6, 7);
		tree.insertRight(6, 8);
		
		//tree.PostOrder(tree.root);
		
		tree.Breadth(tree.root);

		treeGUI gui = new treeGUI(tree);

	}

}
