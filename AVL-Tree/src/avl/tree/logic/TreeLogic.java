package avl.tree.logic;
public class TreeLogic {

	private static Tree tree = null;
	private static int added;
	private static int removed;
	private static int degenerated;

	public static Tree getTree() {
		return tree;
	}
	
	public static int getAdded() {
		return added;
	}
	
	public static void setAdded(int val) {
		added = val;
	}
	
	public static int getRemoved() {
		return removed;
	}
	
	public static void setDegenerated(int val) {
		degenerated = val;
	}
	
	public static int getDegenerated() {
		return degenerated;
	}
	
	public static void setRemoved(int val) {
		removed = val;
	}
	

	public void addNode (int value) {
		System.out.println(value);

		if (tree == null) {
			tree = new Tree(value);		
		} else {
			addNode(tree, value);
		}
		
		added = value;
	}

	public void addNode (Tree localTree, int value) {
		if (value < localTree.getValue()) {
			if (localTree.getLeftChild() == null) {
				//deepest dimension
				localTree.addLeftChild(value);
				localTree.getLeftChild().setParent(localTree);
			} else {
				addNode(localTree.getLeftChild(), value);
			}
		} else if (value > localTree.getValue()) {
			if (localTree.getRightChild() == null){
				//deepest dimension
				localTree.addRightChild(value);
				localTree.getRightChild().setParent(localTree);
			} else {
				addNode(localTree.getRightChild(), value);
			}
		} else {
			System.out.println("can't be added");
			return;
		}
		recalcHeight(localTree);
	}

	public void recalcHeight (Tree localTree){

		int leftChildHeight = getLeftHeight(localTree);
		int righChildtHeight = getRightHeight(localTree);

		localTree.setHeight(Math.max(leftChildHeight, righChildtHeight) + 1);
	}
	
	public int getLeftHeight (Tree localTree){
		int leftChildHeight = localTree.getLeftChild() == null ? 0 : localTree.getLeftChild().getHeight();
		return leftChildHeight;
	}
	
	public int getRightHeight (Tree localTree){
		int righChildtHeight = localTree.getRightChild() == null ? 0 : localTree.getRightChild().getHeight();
		return righChildtHeight;
	}
	
	
	
	public Tree searchNode(int value) {
		Tree result = searchNode(tree, value);;
		if (result != null) {
			added = result.getValue();
		} else {
			added = -1000;
		}
		return result;
	}

	public Tree searchNode(Tree localTree, int value) {
		Tree searchResult = new Tree();

		if (localTree == null) {
			return null;
		}
		
		if (localTree.getValue() == value) {
			searchResult = localTree;
		} else {
			if (value < localTree.getValue()) {
				searchResult = searchNode(localTree.getLeftChild(), value);
			} else {
				searchResult = searchNode(localTree.getRightChild(), value);
			}
		}
		return searchResult;
	}

	public Tree chekDeg (Tree localTree){
		
		int leftChildHeight = getLeftHeight(localTree);
		int righChildtHeight = getRightHeight(localTree);
		if (Math.abs(leftChildHeight - righChildtHeight) > 1){
			return localTree;
		} else if (localTree.getParent() != null) {
			return chekDeg(localTree.getParent());
		} else {
			return null;
		}
	}


	public String typeOfRotation(Tree degTree) {
		String rotate;
		int leftChildHeight = getLeftHeight(degTree);
		int righChildtHeight = getRightHeight(degTree);

		if (leftChildHeight > righChildtHeight) {
			Tree tmpLeftChild = degTree.getLeftChild();

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
			Tree tmpRightChild = degTree.getRightChild();
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
	
	private void rightRotate (Tree degTree) {
		// right rotation
		Tree leftChild = degTree.getLeftChild(),
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

		Tree tmpTree = degTree; 
		while (tmpTree != null) {
			recalcHeight(tmpTree);
			tmpTree = tmpTree.getParent();
		}

		if (parent == null) {
			tree = degTree.getParent();
		}
	}
	
	private void doubleRightRotate (Tree degTree) {
		
		Tree leftChild = degTree.getLeftChild(),
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
		Tree tmpTree = leftChild;
		while (tmpTree != null) {
			recalcHeight(tmpTree);
			tmpTree = tmpTree.getParent();
		}
		
		if (parent == null) {
			tree = rightGrandson;
		}
	}
	
	private void leftRotate (Tree degTree) {
		// right rotation
		Tree rightChild = degTree.getRightChild(),
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
		Tree tmpTree = degTree; 
		while (tmpTree != null) {
			recalcHeight(tmpTree);
			tmpTree = tmpTree.getParent();
		}

		if (parent == null) {
			tree = degTree.getParent();
		}
	}
	
	private void doubleLeftRotate (Tree degTree) {
		Tree rightChild = degTree.getRightChild(),
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
		Tree tmpTree = degTree;
		while (tmpTree != null) {
			recalcHeight(tmpTree);
			tmpTree = tmpTree.getParent();
		}
		
		if (parent == null) {
			tree = leftGrandson;
		}
	}
	
	public void clearTree () {
		tree = null;
	}
	
	
	public void removeNode (Tree forRemove) {
		Tree parent = forRemove.getParent();
		Tree leftChild = forRemove.getLeftChild();
		Tree rightChild = forRemove.getRightChild();
		
		// get highest child from removable tree
		Tree highestTree = getLeftHeight(forRemove) > getRightHeight(forRemove) ? leftChild : rightChild;
		
		if (highestTree == null) {
			// no children
			if (parent.getLeftChild() != null && parent.getLeftChild().getValue() == forRemove.getValue()) {
				parent.setLeftChild(null);
			} else {
				parent.setRightChild(null);
			}
			
			Tree tmpTree = parent;
			while (tmpTree != null) {
				recalcHeight(tmpTree);
				tmpTree = tmpTree.getParent();
			}
			
			Tree degTree = chekDeg(parent);
			if (degTree != null){
				typeOfRotation(degTree);
			}
			
			removed = -1000;
		} else {
			Tree forReplace = highestTree;
			if (highestTree.getValue() > forRemove.getValue()) {
				while (forReplace.getLeftChild() != null) {
					forReplace = forReplace.getLeftChild();
				}
			} else {
				while (forReplace.getRightChild() != null) {
					forReplace = forReplace.getRightChild();
				}
			}
			forRemove.setValue(forReplace.getValue());
			removeNode(forReplace);
		}
	}
	
}
