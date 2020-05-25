package avl.tree.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

import avl.tree.logic.Tree;
import avl.tree.logic.TreeLogic;

public class TreePanel extends JPanel{
	private static final long serialVersionUID = 1L;
	
	private Graphics2D g2; 
	
	public TreePanel(){
		
	}
	
	public void drawTree(Tree localTree) {
		if (localTree == null) {
			return;
		}
		Tree parent = localTree.getParent();
		
		if (parent == null) {
			// 1st Level
			localTree.setX(Param.WIDTH/2);
			localTree.setY(Param.TOP_MARGIN);

		} else if (parent.getParent() == null) {
			// 2nd Level
			if (localTree.getValue() < parent.getValue()){
				localTree.setX(Param.WIDTH/4);
				localTree.setY(parent.getY() + Param.NODE_MARGIN);		
			} else {
				localTree.setX(Param.WIDTH - Param.WIDTH/4);
				localTree.setY(parent.getY() + Param.NODE_MARGIN);	
			}
		} else {
			// other Levels
			if (localTree.getValue() < parent.getValue()){
				localTree.setX(parent.getX() - Math.abs(parent.getX() - parent.getParent().getX())/2);
				localTree.setY(parent.getY() + Param.NODE_MARGIN);
			} else {
				localTree.setX(parent.getX() + Math.abs(parent.getX() - parent.getParent().getX())/2);
				localTree.setY(parent.getY() + Param.NODE_MARGIN);
			}
			
		}
		if (parent != null) {
			g2.setColor(Param.COLOR_LINE);
			g2.drawLine(parent.getX()+Param.DIAMETR/2, parent.getY()+Param.DIAMETR-1, localTree.getX() + 17, localTree.getY() + 17);
		}
		
		g2.setColor(Param.COLOR_NODE);
		if (localTree.getValue() == TreeLogic.getAdded()) {
			g2.setColor(Param.COLOR_ADDED);
		} 
		if (localTree.getValue() == TreeLogic.getRemoved()) {
			g2.setColor(Param.COLOR_REMOVED);
		}
		if (localTree.getValue() == TreeLogic.getDegenerated()) {
			g2.setColor(Param.COLOR_DEG);
		}
		
		g2.fillOval(localTree.getX(), localTree.getY(), Param.DIAMETR, Param.DIAMETR);
		
		g2.setColor(Param.COLOR_VALUE);
		if (localTree.getValue() == TreeLogic.getAdded()) {
			g2.setColor(Color.WHITE);
		}
		g2.setFont(Param.NODE_FONT);
		String nodeString = localTree.getValue() + "";
		FontSize fontSize = getStringSize(nodeString);
		g2.drawString(nodeString, localTree.getX() + (Param.DIAMETR-fontSize.getWidth())/2, localTree.getY() + 22);
		
		drawTree(localTree.getLeftChild());
		drawTree(localTree.getRightChild());
	}

	protected void paintComponent(Graphics g) {  
		super.paintComponent(g);  
		g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		drawTree(TreeLogic.getTree());
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
