package tree.logic;

import exception.ExceptionForProject;
import exception.ExceptionSearch;

public class AVLTree extends BSTTree{
	
	public AVLTree() {
		super();
	}
	public Node checkDeg() {
		Node deg = null;
		checkDegAndBookMark(root);
		deg =  this.searchNodeDeg(root);
		return deg;
	}
	public Node searchNodeDeg(Node localNode) {
		if (localNode == null)
			return null;
		Node result = null;
		if (localNode.getStatus() == Node.degColor) {
			return result = localNode;
		} else {
			result = searchNodeDeg(localNode.getLeftChild());
			if (result != null)
				return result;
			result = searchNodeDeg(localNode.getRightChild());
		}
		return result;
	}
	public void checkDegAndBookMark(Node localNode) {
		if (localNode == null) {
			return;
		}
		int leftHeight, rightHeight;
		if (localNode.getLeftChild() == null) {
			leftHeight = 0;
		} else {
			leftHeight = localNode.getLeftChild().getHeight();
		}
		if (localNode.getRightChild() == null) {
			rightHeight = 0;
		} else {
			rightHeight = localNode.getRightChild().getHeight();
		}
		if (Math.abs(leftHeight - rightHeight) > 1) {
			localNode.setStatus(Node.degColor);
			return;
		}
		checkDegAndBookMark(localNode.getRightChild());
		checkDegAndBookMark(localNode.getLeftChild());
	}

	public Node chekDeg (Node localTree){
		
		int leftChildHeight = getLeftHeight(localTree);
		int righChildtHeight = getRightHeight(localTree);
		
		if (Math.abs(leftChildHeight - righChildtHeight) > 1){
			localTree.setStatus(Node.degColor);
			return localTree;
		} else if (localTree.getParent() != null) {
			return chekDeg(localTree.getParent());
		} else {
			return null;
		}
	}

	public String typeOfRotation(Node degTree) {
		String rotate;
		int leftChildHeight = getLeftHeight(degTree); 
		int righChildtHeight = getRightHeight(degTree);	
		if (leftChildHeight > righChildtHeight) {
			Node tmpLeftChild = degTree.getLeftChild();

			leftChildHeight = getLeftHeight(tmpLeftChild);
			righChildtHeight = getRightHeight(tmpLeftChild);
			
			if (leftChildHeight > righChildtHeight) {
				// right rotate
				rightRotate(degTree);
				rotate = "right rotate";
			} else {
				// right double rotation
				doubleRightRotate(degTree);
				rotate = "double right rotate";
			}
		} else {
			// left rotation
			Node tmpRightChild = degTree.getRightChild();
			leftChildHeight = getLeftHeight(tmpRightChild);
			righChildtHeight = getRightHeight(tmpRightChild);
			if (righChildtHeight > leftChildHeight) {
				// left rotation
				leftRotate(degTree);
				rotate = "left rotate";
			} else {
				// double left rotation
				doubleLeftRotate(degTree);
				rotate = "double left rotate";
			}
		}
		
		return rotate;
	}
	
	private void rightRotate (Node degTree) {
		// right rotation
		Node leftChild = degTree.getLeftChild(),
			 parent = degTree.getParent(),
			 rightGrandson = leftChild.getRightChild();

		degTree.setParent(leftChild);
		degTree.setLeftChild(rightGrandson);
		
		if (rightGrandson != null) {
			rightGrandson.setParent(degTree);
		}
		
		leftChild.setParent(parent);
		leftChild.setRightChild(degTree);

		if (parent != null){
			if (parent.getValue() > leftChild.getValue()) {
				parent.setLeftChild(leftChild);
			} else {
				parent.setRightChild(leftChild);
			}
		}

		Node tmpTree = degTree; 
		while (tmpTree != null) {
			recalcHeight(tmpTree);
			tmpTree = tmpTree.getParent();
		}

		if (parent == null) {
			root = degTree.getParent();
		}
	}
	
	private void doubleRightRotate (Node degTree) {
		
		Node leftChild = degTree.getLeftChild(),
			 parent = degTree.getParent(),
			 rightGrandson = leftChild.getRightChild();
		
		leftChild.setParent(rightGrandson);
		leftChild.setRightChild(rightGrandson.getLeftChild());
		
		if (rightGrandson.getLeftChild() != null) {
			rightGrandson.getLeftChild().setParent(leftChild);
		}
		
		if (rightGrandson.getRightChild() != null) {
			rightGrandson.getRightChild().setParent(degTree);
		}
		
		degTree.setParent(rightGrandson);
		degTree.setLeftChild(rightGrandson.getRightChild());
		
		rightGrandson.setParent(parent);
		rightGrandson.setLeftChild(leftChild);
		rightGrandson.setRightChild(degTree);
		
		if (parent != null){
			if (parent.getValue() > rightGrandson.getValue()) {
				parent.setLeftChild(rightGrandson);
			} else {
				parent.setRightChild(rightGrandson);
			}
		}
		
		recalcHeight(degTree);
		Node tmpTree = leftChild;
		while (tmpTree != null) {
			recalcHeight(tmpTree);
			tmpTree = tmpTree.getParent();
		}
		
		if (parent == null) {
			root = rightGrandson;
		}
	}
	
	private void leftRotate (Node degTree) {
		// right rotation
		Node rightChild = degTree.getRightChild(),
			 parent = degTree.getParent(),
			 leftGrandson = rightChild.getLeftChild();

		degTree.setParent(rightChild);
		degTree.setRightChild(rightChild.getLeftChild());
		
		if (leftGrandson != null) {
			leftGrandson.setParent(degTree);
		}

		rightChild.setParent(parent);
		rightChild.setLeftChild(degTree);

		if (parent != null){
			if (parent.getValue() > rightChild.getValue()) {
				parent.setLeftChild(rightChild);
			} else {
				parent.setRightChild(rightChild);
			}
		}


		//	degTree.setHeight(1);
		Node tmpTree = degTree; 
		while (tmpTree != null) {
			recalcHeight(tmpTree);
			tmpTree = tmpTree.getParent();
		}

		if (parent == null) {
			root = degTree.getParent();
		}
	}
	
	
	private void doubleLeftRotate (Node degTree) {
		Node rightChild = degTree.getRightChild(),
			 parent = degTree.getParent(),
			 leftGrandson = rightChild.getLeftChild();
		
		rightChild.setParent(leftGrandson);
		rightChild.setLeftChild(leftGrandson.getRightChild());
		
		if (leftGrandson.getRightChild() != null) {
			leftGrandson.getRightChild().setParent(rightChild);
		}
		
		if (leftGrandson.getLeftChild() != null) {
			leftGrandson.getLeftChild().setParent(degTree);
		}
		
		degTree.setParent(leftGrandson);
		degTree.setRightChild(leftGrandson.getLeftChild());
		
		leftGrandson.setParent(parent);
		leftGrandson.setRightChild(rightChild);
		leftGrandson.setLeftChild(degTree);
		
		if (parent != null){
			if (parent.getValue() > leftGrandson.getValue()) {
				parent.setLeftChild(leftGrandson);
			} else {
				parent.setRightChild(leftGrandson);
			}
		}
		
		recalcHeight(rightChild);
		Node tmpTree = degTree;
		while (tmpTree != null) {
			recalcHeight(tmpTree);
			tmpTree = tmpTree.getParent();
		}
		
		if (parent == null) {
			root = leftGrandson;
		}
	}
	public Node searchNode(int value) throws ExceptionForProject {
//		this.setColorForTree();
		if (root == null)
			throw new ExceptionSearch();
		Node result = searchNode(root, value);
		if (result != null) {
			return result;
		} 
		return result;
	}
	public Node addNode (Node localNode, int value) throws ExceptionForProject{
		try {
			Node newNode = super.addNode(localNode, value);
			checkDeg();
			return newNode;
		} catch (ExceptionForProject ErrorAdd) {
			throw ErrorAdd;
		}
	}
	public void removeNode(int value) throws ExceptionForProject {
		try {
			super.removeNode(value);
			checkDeg();
		} catch (ExceptionForProject rv) {
			throw rv;
		}
	}
	public void removeNode(Node forRemove) {
		super.removeNode(forRemove);
		checkDeg();
	}
}
