package tree.logic;

import java.util.ArrayList;
import java.util.List;

import exception.ExceptionAdd;
import exception.ExceptionForProject;
import exception.ExceptionSearch;

public class BSTTree {
	protected Node root;
	public List<Node> listVisitedNode;
	
	public BSTTree() {
		root = null;
	}
	public BSTTree(int value) {
		root = new Node();
		root.setValue(value);
	}
	public Node getTree() {
		return root;
	}
	public void setTree(Node tree) {
		this.root = tree;
	}
	public void setColorForTree() {
		this.setColorForTree(root);
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

	public Node addNode(int value) throws ExceptionForProject {
		this.setColorForTree();
		Node node = new Node(value);
		if (root == null) {
			root = node;
			setLocationForAllNode();
			return root;
		} else {
			try {
				node = addNode(root, value);
				return node;
			} catch (ExceptionForProject e) {
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

	public Node addNode(Node localNode, int value) throws ExceptionForProject {

		Node newNode = new Node(value);
		if (localNode == null) {
			localNode = newNode;
			return localNode;
		} else {
			if (value < localNode.getValue()) {

				if (localNode.getLeftChild() == null) {
					localNode.addLeftChild(newNode);
					newNode.setParent(localNode);
					newNode.setX(newNode.getParent().getX());
					newNode.setY(newNode.getParent().getY());
					recalcHeight(newNode);
					return newNode;
				} else {
					addNode(localNode.getLeftChild(), value);
				}
			} else if (value > localNode.getValue()) {
				
				if (localNode.getRightChild() == null) {
					localNode.addRightChild(newNode);
					newNode.setParent(localNode);
					newNode.setX(newNode.getParent().getX());
					newNode.setY(newNode.getParent().getY());
					recalcHeight(newNode);
					return newNode;
				} else {
					addNode(localNode.getRightChild(), value);
				}
			} else {
				throw new ExceptionAdd();
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
	public Node searchNode(int value) throws ExceptionForProject {
		this.setColorForTree();
		if (root == null) {
			throw new ExceptionSearch();
		}
		Node result = searchNode(root, value);
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
		root = null;
	}

	public boolean checkLeaf(Node localNode) {
		if (localNode.getRightChild() == null & localNode.getLeftChild() == null) {
			return true;
		}
		else return false;
	}
	
	public Node findReplaceNodeForRemove(Node forRemove) {

		Node leftChild = forRemove.getLeftChild();
		Node rightChild = forRemove.getRightChild();
		Node higherChild = getLeftHeight(forRemove) > getRightHeight(forRemove) ? leftChild : rightChild;
		if (higherChild == null ){
			return null;
		}
		Node forReplace = higherChild;
		if (higherChild.getValue() > forRemove.getValue()) {
			while (forReplace.getLeftChild() != null) {
				forReplace = forReplace.getLeftChild();
			}
			forReplace.setStatus(Node.replaceColor);
			return forReplace;
		} else {
			while (forReplace.getRightChild() != null) {
				forReplace = forReplace.getRightChild();
			}
			forReplace.setStatus(Node.replaceColor);
			return forReplace;
		}
		
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
	
	// set location for each node in tree
	public void setMoveAllNode() {
		this.setMoveAllNode(root);
	}
	public void setLocationForAllNode() {
		this.setXY(root);
	}
	
	public void setXY(Node localNode) {
		if (localNode == null) {
			return;
		}
		Node parent = localNode.getParent();
		if (parent == null) {
			localNode.setX(1200 / 2);
			localNode.setY(10);
		} else {
			if (parent.getParent() == null) {
				if (localNode.getValue() < parent.getValue()) {
					localNode.setX(1200/ 4);
					localNode.setY(70 + parent.getY());
				} else {
					localNode.setX(1200 - 1200 / 4);
					localNode.setY(70 + parent.getY());
				}
			} else {

				if (localNode.getValue() < parent.getValue()) {
					localNode.setX(parent.getX() - Math.abs(parent.getX() - parent.getParent().getX()) / 2);
					localNode.setY(70 + parent.getY());
				} else {
					localNode.setX(parent.getX() + Math.abs(parent.getX() - parent.getParent().getX()) / 2);
					localNode.setY(70 + parent.getY());
				}
			}
		}
		setXY(localNode.getRightChild());
		setXY(localNode.getLeftChild());
	}
	public void setMove(Node localNode, float x2, float y2) {
		localNode.setMoveX((x2 - localNode.getX()) / 100);
		localNode.setMoveY((y2 - localNode.getY()) / 100);
		localNode.setNewX(x2);
		localNode.setNewY(y2);
	}

	public void setMoveAllNode(Node localNode) {
		if (localNode == null) {
			return;
		}
		Node parent = localNode.getParent();
		if (parent == null) {
			setMove(localNode, 1200 / 2, 10);
		} else {
			if (parent.getParent() == null) {
				if (localNode.getValue() < parent.getValue()) {
					setMove(localNode, 1200 / 4, 70 + parent.getNewY());
				} else {
					setMove(localNode, 1200 - 1200 / 4, 70 + parent.getNewY());
				}
			} else {
				if (localNode.getValue() < parent.getValue()) {
					setMove(localNode, parent.getNewX() - Math.abs(parent.getNewX() - parent.getParent().getNewX()) / 2,
							parent.getNewY() + 70);
				} else {
					setMove(localNode, parent.getNewX() + Math.abs(parent.getNewX() - parent.getParent().getNewX()) / 2,
							parent.getNewY() + 70);
				}
			}
		}
		setMoveAllNode(localNode.getLeftChild());
		setMoveAllNode(localNode.getRightChild());
	}
	
	// find path node
	public void findPath(int value) {
		listVisitedNode = null;
		listVisitedNode = new ArrayList();
		findPath(root, value, listVisitedNode);
	}

	public void findPath(Node root, int value, List<Node> checkedNode) {
		if (root == null) {
			return;
		}
		if (root.getValue() == value) {
			return;
		} else if (root.getValue() > value) {
			checkedNode.add(new Node(root.getValue()));
			findPath(root.getLeftChild(), value, checkedNode);
		} else {
			checkedNode.add(new Node(root.getValue()));
			findPath(root.getRightChild(), value, checkedNode);
		}
	}
}
