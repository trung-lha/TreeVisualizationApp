package tree.logic;

import javax.swing.JPanel;

import exception.ExceptionErrorAdd;

public class BSTTree {
	protected Node tree = null;

	public Node getTree() {
		return tree;
	}

	public void addNode(int value) throws Exception {
		if (tree == null) {
			tree = new Node(value);
		} else {
			try {
				addNode(tree, value);
			} catch (Exception e) {
				throw e;
			}
		}
	}

	public Node addNode(Node localNode, int value) throws Exception {

		if (value < localNode.getValue()) {
			if (localNode.getLeftChild() == null) {
				Node newNode = new Node(value);
				localNode.addLeftChild(newNode);
				newNode.setParent(localNode);
				newNode.setStatus(Node.addColor);
				recalcHeight(newNode);
				return newNode;
			} else {
				addNode(localNode.getLeftChild(), value);
			}
		} else if (value > localNode.getValue()) {
			if (localNode.getRightChild() == null) {
				Node newNode = new Node(value);
				localNode.addRightChild(newNode);
				newNode.setParent(localNode);
				newNode.setStatus(Node.addColor);
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

	// cap nhat lai chieu cao cua Node hien tai
	public void recalcHeight(Node localNode) {

		int leftChildHeight = getLeftHeight(localNode);
		int righChildtHeight = getRightHeight(localNode);

		localNode.setHeight(Math.max(leftChildHeight, righChildtHeight) + 1);
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
		Node result = searchNode(tree, value);
		;
		if (result != null) {
			result.setStatus(1);
		} else {
			result.setStatus(-1);
		}
		return result;
	}

	public Node searchNode(Node localNode, int value) {
		Node searchResult = new Node();

		if (localNode == null) {
			return null;
		}

		if (localNode.getValue() == value) {
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
