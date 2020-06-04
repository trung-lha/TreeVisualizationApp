package tree.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import tree.logic.AVLTree;
import tree.logic.BSTTree;
import tree.logic.Node;

public class GUI extends JFrame {

	String treeType[] = {"AVL Tree", "BST Tree" };
	JComboBox comboboxTree = new JComboBox(treeType);

	private BSTTree treeBST;
	private AVLTree treeAVL;
	private TreePanel treePanel;
	private JPanel buttonPanel;
	private JPanel logPanel;
	private JLabel logField;

	private JButton addRandButton;
	private JButton addButton;
	private JButton clearButton;
	private JButton removeButton;
	private JButton searchButton;

	private JTextField valueField;

	public GUI() {
		setTitle("BST Tree,AVL Tree visualization Application");
		setSize(Param.WIDTH + 50, Param.HEIGHT);
		setResizable(true);
		treePanel = new TreePanel();
		logPanel = new JPanel();
		logField = new JLabel("Please add a number");
		logField.setFont(Param.BUTTON_FONT);
		logPanel.add(logField);

		// setup button for button Panel
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		valueField = new JTextField("");
		valueField.setColumns(7);
		valueField.setFont(Param.TEXT_FIELD_FONT);
		valueField.setHorizontalAlignment(JTextField.CENTER);
		buttonPanel.add(valueField);
		//****************** ADD Setting ************************
		treeBST = new BSTTree();
		treeAVL = new AVLTree();
		addButton = new JButton("Add number");
		addButtonSetting();
		buttonPanel.add(addButton);
		addRandButton = new JButton("Add random number");
		buttonPanel.add(addRandButton);
		
		//****************** Remove Setting **********************
		clearButton = new JButton("Remove tree");
		clearButtonSetting();
		removeButton = new JButton("Remove node");
		buttonPanel.add(removeButton);
		
		// ************ clear setting ****************************
		searchButton = new JButton("Search node");
		searchButtonSetting();
		

		comboboxTree.setBackground(Param.COLOR_BUTT_CREATE);
		comboboxTree.setFont(Param.BUTTON_FONT);
		buttonPanel.add(comboboxTree);
		buttonPanel.setBackground(Param.COLOR_PANEL);
		treePanel.setBackground(Color.WHITE);

		getContentPane().add(buttonPanel, BorderLayout.NORTH);
		getContentPane().add(treePanel, BorderLayout.CENTER);
		getContentPane().add(logPanel, BorderLayout.SOUTH);

			
		
		setVisible(true);

	}
	private void addButtonSetting() {
		addButton.setBackground(Param.COLOR_BUTT_CREATE);
		addButton.setFont(Param.BUTTON_FONT);
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				// *************** Add with BST *************************
				if (comboboxTree.getSelectedItem() == "BST Tree") {
					
					treePanel.setNodePanel(treeBST.getTree());
					int value;
					try {
						value = Integer.parseInt(valueField.getText());
					} catch (NumberFormatException e) {
						System.out.println("not a number");
						logField.setText("'" + valueField.getText() + "' is not a number");
						return;
					}
					enableComponents(buttonPanel, false);
					try {
						treeBST.addNode(value);
						treePanel.setNodePanel(treeBST.getTree());
						treePanel.repaint();
						logField.setText("node " + value + " added");
						enableComponents(buttonPanel, true);
					} catch (Exception addBST) {
						enableComponents(buttonPanel, true);
						logField.setText(addBST.getMessage());
					}
				}
				// ******************* Add with AVL *******************
				else {
//					JOptionPane.showMessageDialog(null, "Vao AVL tree");
					treePanel.setNodePanel(treeAVL.getTree());
					int value;
					try {
						value = Integer.parseInt(valueField.getText());
					} catch (NumberFormatException e) {
						System.out.println("not a number");
						logField.setText("" + valueField.getText() + "is not a number");
						return;
					}
					try {
						treeAVL.addNode(value);
						treePanel.setNodePanel(treeAVL.getTree());
						repaint();
						logField.setText("node " + value + " added");
						Node nodeAdded = treeAVL.searchNode(value);
						Node degNode = treeAVL.chekDeg(nodeAdded);
						repaint();
						if (degNode != null) {
							logField.setText("node " + value + " added with ...");
							enableComponents(buttonPanel, false);
							new java.util.Timer().schedule(new java.util.TimerTask() {
								@Override
								public void run() {
									degNode.setStatus(Node.nodeColor);
									String rotateType = treeAVL.typeOfRotation(degNode);
									Node root = nodeAdded;
									while (root.getParent() != null) {
										root = nodeAdded.getParent();
									}
									treePanel.setNodePanel(root);
									repaint();
									enableComponents(buttonPanel, true);
									logField.setText("node " + value + " added with " + rotateType);
								}
							}, 2000);
						}
					} catch (Exception avl) {
						logField.setText(avl.getMessage());
					}
				} 
			}
		});
		
	}

	public void paint(Graphics g) {
		// call superclass version of method paint
		super.paint(g);
	}

	public void addBST(BSTTree tree, int value) {
		try {
			
		} catch (Exception add) {
			logField.setText(add.getMessage());
		}
	}
	
	private void enableComponents(Container container, boolean enable) {
		Component[] components = container.getComponents();

		for (Component component : components) {
			component.setEnabled(enable);
		}
	}

	private void clearButtonSetting() {
		clearButton.setBackground(Param.COLOR_BUTT_CLEAR);
		clearButton.setFont(Param.BUTTON_FONT);
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				treeAVL.clearTree();
				treeBST.clearTree();
				treePanel.setNodePanel(null);
				treePanel.repaint();
				logField.setText("cleared");
			}
		});
		buttonPanel.add(clearButton);
	}


	public void addRandSettingForAVL() {
		addRandButton.setBackground(Param.COLOR_BUTT_CREATE);
		addRandButton.setFont(Param.BUTTON_FONT);
		addRandButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int value = (int) (Math.random() * 100 + 1);
				try {
					treeAVL.addNode(value);
					Node nodeAdded = treeAVL.searchNode(value);
					if (nodeAdded != null) 
						System.out.println(nodeAdded.getStatus());
					treePanel.setNodePanel(treeAVL.getTree());
					repaint();
					logField.setText("node " + value + " added");
					Node degNode = treeAVL.chekDeg(treeAVL.getTree());
					repaint();
					if (degNode != null && degNode.getStatus() == Node.degColor) {
						logField.setText("node " + value + " added with ...");
						enableComponents(buttonPanel, false);
						new java.util.Timer().schedule(new java.util.TimerTask() {
							@Override
							public void run() {
								degNode.setStatus(Node.nodeColor);
								String rotateType = treeAVL.typeOfRotation(degNode);
								treePanel.repaint();
								enableComponents(buttonPanel, true);
								logField.setText("node " + value + " added with " + rotateType);
							}
						}, 2000);
					}
				} catch (Exception avl) {
					logField.setText(avl.getMessage());
				}
			}
		});
	}

	public void removeButtonSettingForBST(BSTTree tree) {
		removeButton.setBackground(Param.COLOR_BUTT_REMOVE);
		removeButton.setFont(Param.BUTTON_FONT);
		removeButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				int value;
				try {
					value = Integer.parseInt(valueField.getText());
				} catch (NumberFormatException e) {
					System.out.println("not a number");
					return;
				}

				Node forRemove = treeBST.searchNode(value);
				if (forRemove == null) {
					logField.setText("node " + value + " is not exist");
					return;
				}
				forRemove.setStatus(Node.removeColor);
				treePanel.repaint();

				enableComponents(buttonPanel, false);
				new java.util.Timer().schedule(new java.util.TimerTask() {
					@Override
					public void run() {
						tree.removeNode(forRemove);
						forRemove.setStatus(Node.addColor);
						treePanel.repaint();
						enableComponents(buttonPanel, true);
						logField.setText("node " + value + " removed");
					}
				}, 1000);
			}
		});
	}

	public void removeButtonSettingForAVL() {
		removeButton.setBackground(Param.COLOR_BUTT_REMOVE);
		removeButton.setFont(Param.BUTTON_FONT);
		removeButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				int value;
				try {
					value = Integer.parseInt(valueField.getText());
				} catch (NumberFormatException e) {
					System.out.println("not a number");
					return;
				}

				Node forRemove = treeAVL.searchNode(value);
				if (forRemove == null) {
					logField.setText("node " + value + " is not exist");
					return;
				}
				forRemove.setStatus(Node.removeColor);
				treePanel.repaint();

				enableComponents(buttonPanel, false);
				new java.util.Timer().schedule(new java.util.TimerTask() {
					@Override
					public void run() {
						treeAVL.removeNode(forRemove);
						forRemove.setStatus(Node.addColor);
						treePanel.repaint();
						enableComponents(buttonPanel, true);
						logField.setText("node " + value + " removed");
					}
				}, 1000);
			}
		});
	}

	private void searchButtonSetting() {
		searchButton.setBackground(Param.COLOR_BUTT_CREATE);
		searchButton.setFont(Param.BUTTON_FONT);
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
				Node result;
				if (comboboxTree.getSelectedIndex() == 0) {
					result = treeAVL.searchNode(value);
				}
				else {
					result = treeBST.searchNode(value);
				}
				if (result == null) {
					logField.setText("node " + value + " is not exist");
				} else {
					logField.setText("node " + value + " is found");
				}
				treePanel.repaint();
			}
		});
		buttonPanel.add(searchButton);
	}
}
