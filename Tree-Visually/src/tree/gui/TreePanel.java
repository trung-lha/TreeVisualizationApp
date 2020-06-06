package tree.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import javax.swing.JPanel;

import tree.logic.BSTTree;
import tree.logic.Node;

public class TreePanel extends JPanel{
	private Graphics2D g2; 
	private Node root;
	public void setNodePanel(Node node) {
		this.root = node;
	}
	public Node getNodePanal() {
		return this.root;
	}
	public TreePanel(){
	}
	
	public void drawTree(Node root) {
		if (root == null) {
//			System.out.println("Node want to paint is null");
			return;
		}
		Node parent = root.getParent();
		
		// set locate for Node
		if (parent == null) {
			// 1st Level
			root.setX(Param.WIDTH/2);
			root.setY(Param.TOP_MARGIN);

		} else if (parent.getParent() == null) {
			// 2nd Level
			if (root.getValue() < parent.getValue()){
				root.setX(Param.WIDTH/4);
				root.setY(parent.getY() + Param.NODE_MARGIN);		
			} else {
				root.setX(Param.WIDTH - Param.WIDTH/4);
				root.setY(parent.getY() + Param.NODE_MARGIN);	
			}
		} else {
			// other Levels
			if (root.getValue() < parent.getValue()){
				root.setX(parent.getX() - Math.abs(parent.getX() - parent.getParent().getX())/2);
				root.setY(parent.getY() + Param.NODE_MARGIN);
			} else {
				root.setX(parent.getX() + Math.abs(parent.getX() - parent.getParent().getX())/2);
				root.setY(parent.getY() + Param.NODE_MARGIN);
			}
			
		}
		// draw Line for Node
		if (parent != null) {
			g2.setColor(Param.COLOR_LINE);
			g2.drawLine(parent.getX()+Param.DIAMETR/2, parent.getY()+Param.DIAMETR-1, root.getX() + 17, root.getY() + 17);
		}
		//set color for Node
		
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
		g2.fillOval(root.getX(), root.getY(), Param.DIAMETR, Param.DIAMETR);
		
		g2.setColor(Param.COLOR_VALUE);
		if (root.getStatus() == 1) {
			g2.setColor(Color.WHITE);
		}
		g2.setFont(Param.NODE_FONT);
		String nodeString = root.getValue() + "";
		FontSize fontSize = getStringSize(nodeString);
		g2.drawString(nodeString, root.getX() + (Param.DIAMETR-fontSize.getWidth())/2, root.getY() + 22);
		
		drawTree(root.getLeftChild());
		drawTree(root.getRightChild());
	}

	protected void paintComponent(Graphics g) {  
		super.paintComponent(g);  
		g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		drawTree(this.root);
	}  
	
	private FontSize getStringSize (String str) {
		AffineTransform affinetransform = new AffineTransform();     
		FontRenderContext frc = new FontRenderContext(affinetransform,true,true);     
		Font font = Param.NODE_FONT;
		FontSize fontSize = new FontSize();
		fontSize.setWidth((int)(font.getStringBounds(str, frc).getWidth()));
		fontSize.setHeight((int)(font.getStringBounds(str, frc).getHeight()));
		
		return fontSize;
	}
}
