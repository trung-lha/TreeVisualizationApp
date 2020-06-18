package tree.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

import tree.logic.BSTTree;
import tree.logic.Node;

public class TreePanel extends JPanel implements ActionListener{
	private Graphics2D g2;
	private BSTTree tree;
	private int step;
	private Timer tm = new Timer(25, this);
	
	public void setTreePanel(BSTTree tree) {
		this.tree = tree;
	}

	public BSTTree getTreePanal() {
		return this.tree;
	}

	public TreePanel() {
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
			g2.setColor(new Color(227,242,240));
			g2.drawLine((int) parent.getX() + 36 / 2, (int) parent.getY() + 36 - 1,
					(int) root.getX() + 17, (int) root.getY() + 17);
		}
		// set color for Node
		if (root.getStatus() == Node.nodeColor) {
			g2.setColor(new Color(227,242,240));
		}
		if (root.getStatus() == Node.addColor) {
			g2.setColor(new Color(250,238,178));
		}
		if (root.getStatus() == Node.removeColor) {
			g2.setColor(new Color(212,174,217));
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
			g2.setColor(new Color(250,238,178));
		}
		g2.fillOval((int)root.getX(),(int) root.getY(), 36, 36);

		g2.setColor(Color.BLACK);
		String nodeString = root.getValue() + "";
		g2.drawString(nodeString, root.getX() + (36-13) / 2, root.getY() + 22);

		drawTree(root.getLeftChild());
		drawTree(root.getRightChild());
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if (tree != null)
			drawTree(tree.getTree());
	}

	public void startAction() {
		this.step = 0;
		tree.setMoveAllNode();
		tm.start();
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		this.step++;
		repaint();
		if (this.step == 100) {
			tm.stop();
		}
	}
}
