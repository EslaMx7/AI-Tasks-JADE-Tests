package generictree.tasks;

import java.util.ArrayList;

public class Node<T> {

	public T Data;
	public Node<T> Parent;
	public Node<T> Left;
	public Node<T> Right;
	
	public ArrayList<Node<T>> Nodes;
	
	public Node(Node<T> parent,T data,Node<T> left,Node<T> right){
		this.Parent=parent;
		this.Data = data;
		this.Left=left;
		this.Right=right;
		this.Nodes = new ArrayList<Node<T>>();
	}
	
	public void DisplayNode(){
		System.out.println("Data : "+this.Data);
	}
}
