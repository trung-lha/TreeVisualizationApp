package tree.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

import tree.logic.Node;

public class TreePanel extends JPanel implements ActionListener{
	private Graphics2D g2;
	private Node root;
	private int done;
	public List<Node> listVisitedNode =new ArrayList();
	Timer tm = new Timer(25, this);
	
	public List<Node> findPath(Node root, int value) {
		List<Node> checkedNode = new ArrayList();
		findPath(root, value, checkedNode);
		return checkedNode;
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

	public void setNodePanel(Node node) {
		this.root = node;
	}

	public Node getNodePanal() {
		return this.root;
	}

	public TreePanel() {
	}

	public void setMoveAllNode() {
		this.setMoveAllNode(root);
	}
	public void setLocation() {
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
	public void drawTree(Node root) {
		if (root == null) {
			return;
		} else {
			root.setX(root.getX() + root.getMoveX());
			root.setY(root.getY() + root.getMoveY());
		}
		Node parent = root.getParent();
		// draw Line for Node
		if (parent != null) {
			g2.setColor(Color.BLACK);
			g2.drawLine((int) parent.getX() + 36 / 2, (int) parent.getY() + 36 - 1,
					(int) root.getX() + 17, (int) root.getY() + 17);
		}
		// set color for Node
		if (root.getStatus() == Node.nodeColor) {
			g2.setColor(new Color(33,184,191));
		}
		if (root.getStatus() == Node.addColor) {
			g2.setColor(Color.ORANGE);
		}
		if (root.getStatus() == Node.removeColor) {
			g2.setColor(new Color(239,219,0));
		}
		if (root.getStatus() == Node.degColor) {
			g2.setColor(Color.RED);
		}
		if (root.getStatus() == Node.searchColor) {
			g2.setColor(new Color(98,204,77));
		}
		if (root.getStatus() == Node.replaceColor) {
			g2.setColor(Color.PINK);
		}
		if (root.getStatus() == Node.nodePath) {
			g2.setColor(Color.BLUE);
		}
		g2.fillOval((int)root.getX(),(int) root.getY(), 36, 36);

		g2.setColor(Color.BLACK);
		if (root.getStatus() == 1) {
			g2.setColor(Color.WHITE);
		}
		String nodeString = root.getValue() + "";
		g2.drawString(nodeString, root.getX() + (36-13) / 2, root.getY() + 22);

		drawTree(root.getLeftChild());
		drawTree(root.getRightChild());
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		drawTree(this.root);
	}

	public void startAction() {
		this.done = 0;
		setMoveAllNode();
		tm.start();
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		this.done++;
		repaint();
		if (this.done == 100) {
			tm.stop();
		}
	}
}
