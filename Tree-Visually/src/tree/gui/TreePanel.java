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
	private BSTTree root;
	public void setTreePanel(BSTTree root) {
		this.root = root;
	}
	public BSTTree getTreePanel() {
		return this.root;
	}
	public TreePanel(){
	}
	
	public void drawTree(Node node) {
		if (node == null) {
			return;
		}
		Node parent = node.getParent();
		
		// set locate for Node
		if (parent == null) {
			// 1st Level
			node.setX(Param.WIDTH/2);
			node.setY(Param.TOP_MARGIN);

		} else if (parent.getParent() == null) {
			// 2nd Level
			if (node.getValue() < parent.getValue()){
				node.setX(Param.WIDTH/4);
				node.setY(parent.getY() + Param.NODE_MARGIN);		
			} else {
				node.setX(Param.WIDTH - Param.WIDTH/4);
				node.setY(parent.getY() + Param.NODE_MARGIN);	
			}
		} else {
			// other Levels
			if (node.getValue() < parent.getValue()){
				node.setX(parent.getX() - Math.abs(parent.getX() - parent.getParent().getX())/2);
				node.setY(parent.getY() + Param.NODE_MARGIN);
			} else {
				node.setX(parent.getX() + Math.abs(parent.getX() - parent.getParent().getX())/2);
				node.setY(parent.getY() + Param.NODE_MARGIN);
			}
			
		}
		// draw Line for Node
		if (parent != null) {
			g2.setColor(Param.COLOR_LINE);
			g2.drawLine(parent.getX()+Param.DIAMETR/2, parent.getY()+Param.DIAMETR-1, node.getX() + 17, node.getY() + 17);
		}
		//set color for Node
		g2.setColor(Param.COLOR_NODE);
		if (node.getStatus() == Node.addColor) {
			g2.setColor(Param.COLOR_ADDED);
		} 
		if (node.getStatus() == Node.removeColor) {
			g2.setColor(Param.COLOR_REMOVED);
		}
		if (node.getStatus() == Node.degColor) {
			g2.setColor(Param.COLOR_DEG);
		}
		if (node.getStatus() == Node.warningColor) {
			g2.setColor(Param.COLOR_WARNIGN);
		}
		g2.fillOval(node.getX(), node.getY(), Param.DIAMETR, Param.DIAMETR);
		
		g2.setColor(Param.COLOR_VALUE);
		if (node.getStatus() == 1) {
			g2.setColor(Color.WHITE);
		}
		g2.setFont(Param.NODE_FONT);
		String nodeString = node.getValue() + "";
		FontSize fontSize = getStringSize(nodeString);
		g2.drawString(nodeString, node.getX() + (Param.DIAMETR-fontSize.getWidth())/2, node.getY() + 22);
		
		drawTree(node.getLeftChild());
		drawTree(node.getRightChild());
	}

	protected void paintComponent(Graphics g) {  
		super.paintComponent(g);  
		g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		drawTree(this.root.getTree());
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
