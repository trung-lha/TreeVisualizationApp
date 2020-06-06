package tree.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;
import tree.logic.Node;

public class TreePanel extends JPanel implements ActionListener{
	private Graphics2D g2;
	private Node root;
	private int done;
	Timer tm = new Timer(20, this);

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
			localNode.setX(Param.WIDTH / 2);
			localNode.setY(Param.TOP_MARGIN);
		} else {
			if (parent.getParent() == null) {
				if (localNode.getValue() < parent.getValue()) {
					localNode.setX(Param.WIDTH / 4);
					localNode.setY(Param.NODE_MARGIN + parent.getY());
				} else {
					localNode.setX(Param.WIDTH - Param.WIDTH / 4);
					localNode.setY(Param.NODE_MARGIN + parent.getY());
				}
			} else {

				if (localNode.getValue() < parent.getValue()) {
					localNode.setX(parent.getX() - Math.abs(parent.getX() - parent.getParent().getX()) / 2);
					localNode.setY(Param.NODE_MARGIN + parent.getY());
				} else {
					localNode.setX(parent.getX() + Math.abs(parent.getX() - parent.getParent().getX()) / 2);
					localNode.setY(Param.NODE_MARGIN + parent.getY());
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
			setMove(localNode, Param.WIDTH / 2, Param.TOP_MARGIN);
		} else {
			if (parent.getParent() == null) {
				if (localNode.getValue() < parent.getValue()) {
					setMove(localNode, Param.WIDTH / 4, Param.NODE_MARGIN + parent.getNewY());
				} else {
					setMove(localNode, Param.WIDTH - Param.WIDTH / 4, Param.NODE_MARGIN + parent.getNewY());
				}
			} else {
				if (localNode.getValue() < parent.getValue()) {
					setMove(localNode, parent.getNewX() - Math.abs(parent.getNewX() - parent.getParent().getNewX()) / 2,
							parent.getNewY() + Param.NODE_MARGIN);
				} else {
					setMove(localNode, parent.getNewX() + Math.abs(parent.getNewX() - parent.getParent().getNewX()) / 2,
							parent.getNewY() + Param.NODE_MARGIN);
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
			g2.setColor(Param.COLOR_LINE);
			g2.drawLine((int) parent.getX() + Param.DIAMETR / 2, (int) parent.getY() + Param.DIAMETR - 1,
					(int) root.getX() + 17, (int) root.getY() + 17);
		}
		// set color for Node
		if (root.getStatus() == Node.nodeColor) {
			g2.setColor(Param.COLOR_NODE);
		}
		if (root.getStatus() == Node.addColor) {
			g2.setColor(Param.COLOR_ADDED);
		}
		if (root.getStatus() == Node.removeColor) {
			g2.setColor(Param.COLOR_REMOVED);
		}
		if (root.getStatus() == Node.degColor) {
			g2.setColor(Param.COLOR_DEG);
		}
		if (root.getStatus() == Node.searchColor) {
			g2.setColor(Param.COLOR_SEARCH);
		}
		g2.fillOval((int)root.getX(),(int) root.getY(), Param.DIAMETR, Param.DIAMETR);

		g2.setColor(Param.COLOR_VALUE);
		if (root.getStatus() == 1) {
			g2.setColor(Color.WHITE);
		}
		String nodeString = root.getValue() + "";
		g2.drawString(nodeString, root.getX() + (Param.DIAMETR-13) / 2, root.getY() + 22);

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
		if (this.done == (100 + 1)) {
			tm.stop();
		}
	}
}
