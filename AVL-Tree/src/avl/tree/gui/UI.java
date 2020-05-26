package avl.tree.gui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.*;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JTextField;

import avl.tree.logic.Tree;
import avl.tree.logic.TreeLogic;

import java.awt.Component;
import javax.swing.JLabel;
import java.awt.BorderLayout;

import java.awt.FlowLayout;
import java.awt.Font;

public class UI extends JFrame{

	private static final long serialVersionUID = 1L;
	//	private JButton btnAddNumber;
	//	private JTextField textField;
	String treeType[]={"AVLTree","BSTTree"}; 
	JComboBox comboboxTree=new JComboBox(treeType); 
	
	
	private JButton addRandButton;
	private TreeLogic treeLogic;

	private TreePanel treePanel;
	private JTextField valueField;
	private JButton addButton;

	private JButton clearButton;

	private JButton removeButton;

	private JButton searchButton;
	private JPanel buttonPanel;
	private JPanel logPanel;

	private JLabel logField;

	public UI(){
		JFrame frame = new JFrame("AVL Tree");
		frame.setSize(Param.WIDTH + 50, Param.HEIGHT);
		frame.setResizable(true);
		treeLogic = new TreeLogic();

		treePanel = new TreePanel();
		
		logPanel = new JPanel();

		logField = new JLabel("add new value, please");
		logField.setFont(Param.BUTTON_FONT);
		logPanel.add(logField);
		
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		addButton = new JButton("Add Value");
		addButton.setBackground(Param.COLOR_BUTT_CREATE);
		addButton.setFont(Param.BUTTON_FONT);
		
		removeButton = new JButton("remove");
		removeButton.setBackground(Param.COLOR_BUTT_REMOVE);
		removeButton.setFont(Param.BUTTON_FONT);
		
		searchButton = new JButton("search");
		searchButton.setBackground(Param.COLOR_BUTT_CREATE);
		searchButton.setFont(Param.BUTTON_FONT);
		
		addRandButton = new JButton("Add Rand Value");
		addRandButton.setBackground(Param.COLOR_BUTT_CREATE);
		addRandButton.setFont(Param.BUTTON_FONT);
		
		
		clearButton = new JButton("clear");
		clearButton.setBackground(Param.COLOR_BUTT_CLEAR);
		clearButton.setFont(Param.BUTTON_FONT);
		
		comboboxTree.setBackground(Param.COLOR_BUTT_CREATE);
		comboboxTree.setFont(Param.BUTTON_FONT);

		valueField = new JTextField("");
		valueField.setColumns(7);
		valueField.setFont(Param.TEXT_FIELD_FONT);
		valueField.setHorizontalAlignment(JTextField.CENTER);
		
		buttonPanel.add(valueField);
		buttonPanel.add(addButton);
		buttonPanel.add(addRandButton);
		buttonPanel.add(searchButton);
		buttonPanel.add(removeButton);
		buttonPanel.add(clearButton);
		
		buttonPanel.add(comboboxTree);
		buttonPanel.setBackground(Param.COLOR_PANEL);
		
		treePanel.setBackground(Color.WHITE);
		
		frame.getContentPane().add(buttonPanel, BorderLayout.NORTH);
		frame.getContentPane().add(treePanel, BorderLayout.CENTER);
		frame.getContentPane().add(logPanel, BorderLayout.SOUTH);

		addEvents();
		frame.setVisible(true);
	
	}

	public void paint(Graphics g){
		// call superclass version of method paint
		super.paint(g);
	}

	public void addEvents () {
		addRandButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// EVENT FOR THE ADD BUTTON
				int value = (int) (Math.random()*100+1);

				addProceed(value);
			}
		});

		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// EVENT FOR THE ADD Value BUTTON
//				string 
				
				int value; 
				
				try {
					value = Integer.parseInt(valueField.getText());
				} catch (NumberFormatException e) {
					System.out.println("not a number");
					logField.setText("'" + valueField.getText() + "' is not a number");
					return;
				}
				
				if (value > 999 || value < -99) {
					System.out.println("to many symbols");
					logField.setText("proposed rage: from -99 to 999");
					return;
				}
				
				addProceed(value);

			}
		});
		
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int value;
				
				try {
					value = Integer.parseInt(valueField.getText());
				} catch (NumberFormatException e) {
					System.out.println("not a number");
					logField.setText("'" + valueField.getText() + "' is not a number");
					return;
				}
				
				Tree result = treeLogic.searchNode(value);
				if (result == null) {
					logField.setText("node " + value + " is not exist");
				} else {
					logField.setText("node " + value + " is found");
				}
				treePanel.repaint();
			}
		});
		
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int value;
				
				try {
					value = Integer.parseInt(valueField.getText());
				} catch (NumberFormatException e) {
					System.out.println("not a number");
					return;
				}
				
				TreeLogic.setAdded(-1000);
				TreeLogic.setRemoved(value);
				
				Tree forRemove = treeLogic.searchNode(value);
				if (forRemove == null) {
					logField.setText("node " + value + " is not exist");
					return;
				}
				
				TreeLogic.setRemoved(value);
				treePanel.repaint();
				
				enableComponents(buttonPanel, false);
				new java.util.Timer().schedule( 
			        new java.util.TimerTask() {
			            @Override
			            public void run() {
			            	if(comboboxTree.getSelectedIndex()==0) {
			            	    treeLogic.removeNode(forRemove); //xoa o avltree
			            	}
			            	if(comboboxTree.getSelectedIndex()==1) {
			            		treeLogic.removeNodeBST(forRemove); //xoa o bsttree
				            }
							treePanel.repaint();
							enableComponents(buttonPanel, true);
							logField.setText("node " + value + " removed");
			            }
			        }, 
			        1000 
				);				
			}
		});
		
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
//				System.out.println("CLEAR");
				treeLogic.clearTree();
				treePanel.repaint();
				logField.setText("cleared");
			}
		});


	}
	
	private void addProceed (int value) {		
		treeLogic.addNode(value);
		treePanel.repaint();
		logField.setText("node " + value + " added" );
		
		if(comboboxTree.getSelectedIndex()==0) { // nếu thuật toán được chọn là AVLtree
			Tree localTree =  treeLogic.searchNode(TreeLogic.getTree(), value);
			Tree degTree = treeLogic.chekDeg(localTree);
		
			if (degTree != null){
				TreeLogic.setDegenerated(degTree.getValue());
				logField.setText("node " + value + " added with ...");
				enableComponents(buttonPanel, false);
				new java.util.Timer().schedule( 
					new java.util.TimerTask() {
						@Override
						public void run() {
							TreeLogic.setDegenerated(-1000);
							String rotateType = treeLogic.typeOfRotation(degTree);
							treePanel.repaint();							
							enableComponents(buttonPanel, true);
							logField.setText("node " + value + " added with " + rotateType);
						}
					}, 
					1000 
				);		
			}
		} //end add AVLTree
		
				
	}
	
	private void enableComponents(Container container, boolean enable) {
        Component[] components = container.getComponents();
        
        for (Component component : components) {
            component.setEnabled(enable);
        }
    }

}
