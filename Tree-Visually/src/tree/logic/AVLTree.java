package tree.logic;

import javax.swing.JOptionPane;
import exception.ExceptionErrorAdd;

public class AVLTree extends BSTTree{
	
	public AVLTree() {
		super();
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
		int leftChildHeight = getLeftHeight(degTree); // chieu cao con trai
		int righChildtHeight = getRightHeight(degTree);	// chieu cao con phai
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
				JOptionPane.showMessageDialog(null, "Da vao day");
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
			tree = degTree.getParent();
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
			tree = rightGrandson;
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
			tree = degTree.getParent();
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
			tree = leftGrandson;
		}
	}
	
	public void removeNode(Node forRemove) {
		Node parent = forRemove.getParent();
		super.removeNode(forRemove);
		Node check = chekDeg(parent);
		if(check != null) {
			check.setStatus(Node.degColor);
			typeOfRotation(chekDeg(parent));
		}
	}
	public Node addNode(int value) throws Exception {
		this.setColorForTree();
		if (tree == null) {
			tree = new Node(value);
			return tree;
		} else {
			try {
				Node degNode = this.addNode(tree, value);
				return degNode;
			} catch (Exception e) {
				throw e;
			}
		}
	}
	
	public Node addNode (Node localNode, int value) throws Exception{
		try {
			Node newNode = super.addNode(localNode, value);
			return newNode;
		} catch (Exception ErrorAdd) {
			throw ErrorAdd;
		}
	}
}
