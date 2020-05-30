package tree.logic;

import javax.swing.JPanel;

public class Node {

	private int value;
	private int height;
	private int x;
	private int y;
	public static final int addColor = 1;
	public static final int removeColor = 2;
	public static final int degColor = 3;
	public static final int warningColor = 4;
	public static final int searchColor = 5;
	
	private Node parent;
	private Node leftChild;
	private Node rightChild;
	private int status;

	public Node() {
		value = 0;
		height = 1;
		parent = null;
		leftChild = null;
		rightChild = null;
	}

	public Node(int value) {
		this();
		this.value = value;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	public int getStatus() {
		return status;
	}
	public int getValue() {
		return value;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getX() {
		return x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getY() {
		return y;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	public void changeHeight(int number) {
		this.height += number;
	}
	

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public Node getLeftChild() {
		return leftChild;
	}

	public void setLeftChild(Node leftChild) {
		this.leftChild = leftChild;
	}

	public void addLeftChild(int value) {
		setLeftChild(new Node(value));
	}
	public void addLeftChild(Node newNode) {
		this.leftChild = newNode;
	}
	public Node getRightChild() {
		return rightChild;
	}

	public void setRightChild(Node rightChild) {
		this.rightChild = rightChild;
	}

	public void addRightChild(int value){
		setRightChild(new Node(value));
	}
	public void addRightChild(Node newNode){
		this.rightChild = newNode;
	}
}
