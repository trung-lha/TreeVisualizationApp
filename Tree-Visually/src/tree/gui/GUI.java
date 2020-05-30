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
import javax.swing.JPanel;
import javax.swing.JTextField;

import tree.logic.AVLTree;
import tree.logic.BSTTree;
import tree.logic.Node;

public class GUI extends JFrame {

	String treeType[] = { "AVL Tree", "BST Tree" };
	JComboBox comboboxTree = new JComboBox(treeType);

	private BSTTree treeLogic;
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

		clearButton = new JButton("Remove tree");
		clearButtonSetting();
		searchButton = new JButton("Search node");
		searchButtonSetting();

		addButton = new JButton("Add number");
		addRandButton = new JButton("Add random number");
		removeButton = new JButton("Remove node");

		comboboxTree.setBackground(Param.COLOR_BUTT_CREATE);
		comboboxTree.setFont(Param.BUTTON_FONT);
		buttonPanel.add(comboboxTree);
		buttonPanel.setBackground(Param.COLOR_PANEL);
		treePanel.setBackground(Color.WHITE);

		getContentPane().add(buttonPanel, BorderLayout.NORTH);
		getContentPane().add(treePanel, BorderLayout.CENTER);
		getContentPane().add(logPanel, BorderLayout.SOUTH);

		if (comboboxTree.getSelectedIndex() == 0) {
			treeLogic = new AVLTree();
			AVLTree treeAVL = (AVLTree) treeLogic;
			treePanel.setTreePanel(treeAVL);
			addButtonSettingForAVL(treeAVL);
			addRandSettingForAVL(treeAVL);
			removeButtonSettingForAVL(treeAVL);
		} else {
			treeLogic = new BSTTree();
			treePanel.setTreePanel(treeLogic);
			addButtonSettingForBST(treeLogic);
			addRandSettingForBST(treeLogic);
			removeButtonSettingForBST(treeLogic);
		}
		setVisible(true);

	}

	public void paint(Graphics g) {
		// call superclass version of method paint
		super.paint(g);
	}

	public void addBST(BSTTree tree, int value) {
		try {
			enableComponents(buttonPanel, false);
			tree.addNode(value);
			treePanel.repaint();
			logField.setText("node " + value + " added");
			enableComponents(buttonPanel, true);
		} catch (Exception add) {
			logField.setText(add.getMessage());
		}
	}

	public void addAVL(AVLTree tree, int value) {
		try {
			Node newNode = tree.addNode(tree.getTree(), value);
			treePanel.repaint();
			logField.setText("node " + value + " added");

			Node degNode = tree.chekDeg(newNode);

			if (degNode != null) {
				degNode.setStatus(Node.degColor);
				logField.setText("node " + value + " added with ...");
				enableComponents(buttonPanel, false);
				new java.util.Timer().schedule(new java.util.TimerTask() {
					@Override
					public void run() {
						degNode.setStatus(Node.addColor);
						;
						String rotateType = tree.typeOfRotation(degNode);
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
//				System.out.println("CLEAR");
				treeLogic.clearTree();
				treePanel.repaint();
				logField.setText("cleared");
			}
		});
		buttonPanel.add(clearButton);
	}

	public void addRandSettingForBST(BSTTree tree) {
		addRandButton.setBackground(Param.COLOR_BUTT_CREATE);
		addRandButton.setFont(Param.BUTTON_FONT);
		addRandButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int value = (int) (Math.random() * 100 + 1);
				addBST(tree, value);
			}
		});
		buttonPanel.add(addRandButton);
	}

	public void addRandSettingForAVL(AVLTree tree) {
		addRandButton.setBackground(Param.COLOR_BUTT_CREATE);
		addRandButton.setFont(Param.BUTTON_FONT);
		addRandButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int value = (int) (Math.random() * 100 + 1);
				addAVL(tree, value);
			}
		});
		buttonPanel.add(addRandButton);
	}

	public void addButtonSettingForBST(BSTTree tree) {
		addButton.setBackground(Param.COLOR_BUTT_CREATE);
		addButton.setFont(Param.BUTTON_FONT);
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
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
				addBST(tree, value);
			}
		});
		buttonPanel.add(addButton);
	}

	public void addButtonSettingForAVL(AVLTree tree) {
		addButton.setBackground(Param.COLOR_BUTT_CREATE);
		addButton.setFont(Param.BUTTON_FONT);
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
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
				addAVL(tree, value);
			}
		});
		buttonPanel.add(addButton);
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

				Node forRemove = treeLogic.searchNode(value);
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
		buttonPanel.add(removeButton);
	}

	public void removeButtonSettingForAVL(AVLTree tree) {
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

				Node forRemove = treeLogic.searchNode(value);
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
		buttonPanel.add(removeButton);
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

				Node result = treeLogic.searchNode(value);
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
