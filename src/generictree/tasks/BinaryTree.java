package generictree.tasks;

import aiproject.TVChannel;

public class BinaryTree<T> {

	public Node<T> Root;

	public BinaryTree(T root) {

		this.Root = new Node<T>(null, root, null, null);

	}

	public Node<T> Find(Node<T> node, T key) {

		Node<T> result = node;
		if (node == null)
			return result;
		if (node.Data == key &&(node.Left ==null || node.Right ==null))
			return node;
		if (node.Left != null )
			return Find(node.Left, key);
		if (node.Right != null )
			return Find(node.Right, key);

		return result;
	}
	
	public Node<TVChannel> FindChannel(Node<TVChannel> node, String key) {

		Node<TVChannel> result = null;
		if (node == null)
			return result;
		if (node.Data.Category == key &&(node.Left ==null || node.Right ==null))
			return node;
		if (node.Right != null )
			return FindChannel(node.Right, key);

		return result;
	}

	public void InsertLeft(T parent, T value) {
		Node<T> node = Find(Root, parent);
		Node<T> left = new Node<T>(node, value, null, null);
		node.Left = left;
	}

	public void InsertRight(T parent, T value) {
		Node<T> node = Find(Root, parent);
		Node<T> right = new Node<T>(node, value, null, null);
		node.Right = right;
	}
	
	public void PrintTree(Node<T> node){
		if(node ==null)
			return;
		node.DisplayNode();
		PrintTree(node.Left);
		PrintTree(node.Right);
	}

}
