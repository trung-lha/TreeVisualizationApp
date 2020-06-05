package tree.logic;

import javax.swing.JOptionPane;

import exception.ExceptionErrorAdd;

public class BSTTree {
	protected Node tree;
	
	public BSTTree() {
		tree = null;
	}
	public BSTTree(int value) {
		tree = new Node();
		tree.setValue(value);
	}
	public Node getTree() {
		return tree;
	}
	public void setTree(Node tree) {
		this.tree = tree;
	}
	public void setColorForTree() {
		this.setColorForTree(tree);
	}
	public void setColorForTree(Node localNode) {
		if (localNode == null) {
			return;
		}
		else {
			localNode.setStatus(Node.nodeColor);
			setColorForTree(localNode.getLeftChild());
			setColorForTree(localNode.getRightChild());
		}
	}

	public Node addNode(int value) throws Exception {
		this.setColorForTree();
		Node node = new Node(value);
		if (tree == null) {
			tree = node;
			return tree;
		} else {
			try {
				node = addNode(tree, value);
				return node;
			} catch (Exception e) {
				throw e;
			}
		}
	}

	// cap nhat lai chieu cao cua Node hien tai
	public void recalcHeight(Node localNode) {
		if(localNode == null) {
			return;
		}
		int leftChildHeight = getLeftHeight(localNode);
		int righChildtHeight = getRightHeight(localNode);

		localNode.setHeight(Math.max(leftChildHeight, righChildtHeight) + 1);
		recalcHeight(localNode.getParent());
	}

	public Node addNode(Node localNode, int value) throws Exception {

		Node newNode = new Node(value);
		if (localNode == null) {
			localNode = newNode;
			return localNode;
		} else {
			if (value < localNode.getValue()) {
				
				if (localNode.getLeftChild() == null) {
					localNode.addLeftChild(newNode);
					newNode.setParent(localNode);
					recalcHeight(newNode);
					return newNode;
				} else {
					addNode(localNode.getLeftChild(), value);
				}
			} else if (value > localNode.getValue()) {
				
				if (localNode.getRightChild() == null) {
					localNode.addRightChild(newNode);
					newNode.setParent(localNode);
					recalcHeight(newNode);
					return newNode;
				} else {
					addNode(localNode.getRightChild(), value);
				}
			} else {
				throw new ExceptionErrorAdd("Can't add cause the value " + value + " is existed");
			}
			return null;
		}
	}

	
	public int getLeftHeight(Node localNode) {
		int leftChildHeight = localNode.getLeftChild() == null ? 0 : localNode.getLeftChild().getHeight();
		return leftChildHeight;
	}

	public int getRightHeight(Node localNode) {
		int righChildtHeight = localNode.getRightChild() == null ? 0 : localNode.getRightChild().getHeight();
		return righChildtHeight;
	}

	// search Node
	public Node searchNode(int value) {
		this.setColorForTree();
		Node result = searchNode(tree, value);
		if (result != null) {
			return result;
		} 
		return result;
	}

	public Node searchNode(Node localNode, int value) {
		Node searchResult = null;

		if (localNode == null) {
			return null;
		}
		else if (localNode.getValue() == value) {
			searchResult = localNode;
		} else {
			if (value < localNode.getValue()) {
				searchResult = searchNode(localNode.getLeftChild(), value);
			} else {
				searchResult = searchNode(localNode.getRightChild(), value);
			}
		}
		return searchResult;
	}

	public void clearTree() {
		tree = null;
	}

	public void removeNode(Node forRemove) {
		Node parent = forRemove.getParent(); // cha cua node can xoa
		Node leftChild = forRemove.getLeftChild();
		Node rightChild = forRemove.getRightChild();

		// get highest child from removable tree
		Node highestTree = getLeftHeight(forRemove) > getRightHeight(forRemove) ? leftChild : rightChild;

		if (highestTree == null) {
			// no children
			if (parent.getLeftChild() != null && parent.getLeftChild().getValue() == forRemove.getValue()) {
				parent.setLeftChild(null);
			} else {
				parent.setRightChild(null);
			}
			// cap nhat lai chieu cao cua cac node cha bi anh huong do xoa node
			Node tmpTree = parent;
			while (tmpTree != null) {
				recalcHeight(tmpTree);
				tmpTree = tmpTree.getParent();
			}

		} else {
			Node forReplace = highestTree;
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
