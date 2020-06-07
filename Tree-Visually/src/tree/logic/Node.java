package tree.logic;

public class Node {

	private int value;
	private int height;
	private float x;
	private float y;
	private float moveX;
	private float moveY;
	private float newX;
	private float newY;
	public static final int addColor = 1;
	public static final int removeColor = 2;
	public static final int degColor = 3;
	public static final int replaceColor = 4;
	public static final int searchColor = 5;
	public static final int nodeColor = 6;
	
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
		this.status = Node.addColor;
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
	
	public void setX(float x) {
		this.x = x;
	}
	
	public float getX() {
		return x;
	}
	public float getNewX() {
		return newX;
	}
	public void setNewX(float newX) {
		this.newX = newX;
	}
	public float getNewY() {
		return newY;
	}
	public void setNewY(float newY) {
		this.newY = newY;
	}
	public void setMoveX(float x) {
		this.moveX = x;
	}
	
	public float getMoveX() {
		return moveX;
	}
	public void setMoveY(float y) {
		this.moveY = y;
	}
	
	public float getMoveY() {
		return moveY;
	}
	public void setY(float y) {
		this.y = y;
	}
	
	public float getY() {
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
