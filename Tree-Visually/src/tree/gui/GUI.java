package tree.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
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

import exception.ExceptionForProject;
import tree.logic.AVLTree;
import tree.logic.BSTTree;
import tree.logic.Node;

public class GUI extends JFrame {

	String treeType[] = { "None", "AVL Tree", "BST Tree" };
	private JComboBox comboboxTree = new JComboBox(treeType);

	private BSTTree tree;
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
		setSize(1250, 800);
		setResizable(true);
		treePanel = new TreePanel();
		logPanel = new JPanel();
		logField = new JLabel("Please select kind of Algorithm");
		logField.setFont(new Font("Tahoma", Font.PLAIN, 20));
		logPanel.add(logField);

		// setup button for button Panel
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		valueField = new JTextField("");
		valueField.setColumns(7);
		valueField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		valueField.setHorizontalAlignment(JTextField.CENTER);
		buttonPanel.add(valueField);
		
		// ****************** ADD Setting ************************
		addButton = new JButton("Add number");
		addButton.setBackground(new Color(227,242,240));
		addButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		buttonPanel.add(addButton);

		addRandButton = new JButton("Add random number");
		addRandButton.setBackground(new Color(227,242,240));
		addRandButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		buttonPanel.add(addRandButton);
		
		// ****************** Search Setting *********************
		searchButton = new JButton("Search node");
		searchButton.setBackground(new Color(227,242,240));
		searchButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		buttonPanel.add(searchButton);
		
		// ****************** Remove Node Setting *****************
		removeButton = new JButton("Remove node");
		removeButton.setBackground(new Color(227,242,240));
		removeButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		buttonPanel.add(removeButton);
		
		// ************ clear tree setting ***********************
		clearButton = new JButton("Remove tree");
		clearButton.setBackground(new Color(227,242,240));
		clearButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		buttonPanel.add(clearButton);
		
		// ************ Select option box setting *****************
		comboboxTree.setBackground(new Color(227,242,240));
		comboboxTree.setFont(new Font("Tahoma", Font.PLAIN, 14));
		buttonPanel.add(comboboxTree);
		buttonPanel.setBackground(new Color(49,106,99));
		treePanel.setBackground(new Color(49,106,99));

		getContentPane().add(buttonPanel, BorderLayout.NORTH);
		getContentPane().add(treePanel, BorderLayout.CENTER);
		getContentPane().add(logPanel, BorderLayout.SOUTH);

		comboboxTree.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String str = (String) comboboxTree.getSelectedItem();
				switch (str) {
				case "BST Tree":
					tree = new BSTTree();
					removeActionListenerForButton();
					addButtonSettingForBST();
					addRandSettingForBST();
					searchButtonSetting();
					removeButtonSettingForBST();
					clearButtonSetting();
					buttonPanel.add(comboboxTree);
					logField.setText("Initialize BST tree successfully");
					break;
				case "AVL Tree":
					tree = new AVLTree();
					removeActionListenerForButton();
					AVLTree treeAVL = (AVLTree) tree;
					addButtonSettingForAVL(treeAVL);
					addRandSettingForAVL(treeAVL);
					searchButtonSetting();
					removeButtonSettingForAVL(treeAVL);
					clearButtonSetting();
					buttonPanel.add(comboboxTree);
					logField.setText("Initialize AVL tree successfully");
					break;
				default:
					removeActionListenerForButton();
					logField.setText("Please select kind of Algorithm");
					break;
				}
			}

		});
		
		setVisible(true);
	}

// ********************************************** Setting for AddButton ********************************************
	
	private void addButtonSettingForAVL(AVLTree tree) {
		addButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				treePanel.setTreePanel(tree);
				int value;
				try {
					value = Integer.parseInt(valueField.getText());
				} catch (NumberFormatException number) {
					System.out.println("not a number");
					logField.setText("'" + valueField.getText() + "' is not a number");
					return;
				}
				// find path
				enableComponents(buttonPanel, false);
				tree.findPath(value);
				if (tree.listVisitedNode.size() != 0) {
					Thread thread = new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								for (Node i : tree.listVisitedNode) {
									tree.setColorForTree();
									tree.setLocationForAllNode();
									treePanel.startAction();
									int last = i.getValue();
									if (last > value) {
										logField.setText(
												"Adding " + value + ": Go to left child cause " + value + " < " + last);
									} else if (last < value) {
										logField.setText("Adding " + value + ": Go to right child cause " + value
												+ " > " + last);
									} else {
										logField.setText("Adding " + value + ": Stop! Found the node with same value!");
									}
									Node highLightNode = tree.searchNode(i.getValue());
									highLightNode.setStatus(Node.nodePath);
									treePanel.repaint();
									Thread.sleep(1000);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
					thread.start();
				}
				// add Node
				new java.util.Timer().schedule(new java.util.TimerTask() {
					@Override
					public void run() {
						try {
							tree.addNode(value);
//							tree.setColorForTree();
							treePanel.setTreePanel(tree);
							logField.setText("Node " + value + " has added. ");
							treePanel.startAction();
							new java.util.Timer().schedule(new java.util.TimerTask() {
								@Override
								public void run() {
									logField.setText(" Check new Tree is balance ?");
								}
							}, 1000);
							// check degNode
							new java.util.Timer().schedule(new java.util.TimerTask() {
								@Override
								public void run() {
									try {
										Node nodeAdded = tree.searchNode(value);
										Node degNode = tree.chekDeg(nodeAdded);
										if (degNode != null) {
											logField.setText("Found an unbalance node in new tree. Rotating ...");
											repaint();
										} else {
											enableComponents(buttonPanel, true);
											logField.setText("Not found unbalance node in new tree.");
										}
										if (degNode != null) {
											enableComponents(buttonPanel, false);
											new java.util.Timer().schedule(new java.util.TimerTask() {
												@Override
												public void run() {
													tree.setColorForTree();
													String rotateType = tree.typeOfRotation(degNode);
													treePanel.setTreePanel(tree);
													treePanel.startAction();
													enableComponents(buttonPanel, true);
													logField.setText("Rotating node with " + rotateType);
												}
											}, 1500);
										}
									} catch (ExceptionForProject e) {
										enableComponents(buttonPanel, true);
										logField.setText(e.notification());
									}
								}
							}, 2300);
						} catch (ExceptionForProject e) {
							enableComponents(buttonPanel, true);
							logField.setText(e.notification());
						}
					}
				}, tree.listVisitedNode.size() * 1000);
			}

		});
		buttonPanel.add(addButton);
	}

	private void addButtonSettingForBST() {

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

				treePanel.setTreePanel(tree);
				enableComponents(buttonPanel, false);
				// find Path
				tree.findPath(value);

				if (tree.listVisitedNode.size() != 0) {
					Thread thread = new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								for (Node i : tree.listVisitedNode) {
									tree.setColorForTree();
									tree.setLocationForAllNode();
									treePanel.startAction();
									int last = i.getValue();
									if (last > value) {
										logField.setText(
												"Adding " + value + ": Go to left child cause " + value + " < " + last);
									} else if (last < value) {
										logField.setText("Adding " + value + ": Go to right child cause " + value
												+ " > " + last);
									} else {
										logField.setText("Adding " + value + ": Stop! Found the node with same value!");
									}
									Node highLightNode = tree.searchNode(i.getValue());
									highLightNode.setStatus(Node.nodePath);
									treePanel.repaint();
									Thread.sleep(1000);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
					thread.start();
				}
				// add node
				new java.util.Timer().schedule(new java.util.TimerTask() {
					@Override
					public void run() {
						try {
							tree.addNode(value);
							treePanel.setTreePanel(tree);
							treePanel.startAction();
							logField.setText("Node " + value + " added");
							enableComponents(buttonPanel, true);
						} catch (ExceptionForProject addBST) {
							enableComponents(buttonPanel, true);
							logField.setText(addBST.notification());
						}
					}
				}, (tree.listVisitedNode.size()) * 1000);

			}
		});
		buttonPanel.add(addButton);
	}

// ********************************************* Setting for clearButton ************************************	

	private void clearButtonSetting() {

		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				tree.clearTree();
				treePanel.setTreePanel(tree);
				treePanel.repaint();
				tree = null;
				logField.setText("cleared");
				comboboxTree.setSelectedIndex(0);
			}
		});
		buttonPanel.add(clearButton);
	}

// ********************************************* Setting for AddRand Button  ************************************
	private void addRandSettingForAVL(AVLTree tree) {
		addRandButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int value = (int) (Math.random() * 100 + 1);
				enableComponents(buttonPanel, false);
				// find path
				tree.findPath(value);
				if (tree.listVisitedNode.size() != 0) {
					Thread thread = new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								for (Node i : tree.listVisitedNode) {
									tree.setColorForTree();
									tree.setLocationForAllNode();
									treePanel.startAction();
									int last = i.getValue();
									if (last > value) {
										logField.setText(
												"Adding " + value + ": Go to left child cause " + value + " < " + last);
									} else if (last < value) {
										logField.setText("Adding " + value + ": Go to right child cause " + value
												+ " > " + last);
									} else {
										logField.setText("Adding " + value + ": Stop! Found the node with same value!");
									}
									Node highLightNode = tree.searchNode(i.getValue());
									highLightNode.setStatus(Node.nodePath);
									treePanel.repaint();
									Thread.sleep(1000);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
					thread.start();
				}
				// add Node
				new java.util.Timer().schedule(new java.util.TimerTask() {
					@Override
					public void run() {
						try {
							tree.addNode(value);
							treePanel.setTreePanel(tree);
//							tree.setColorForTree();
							logField.setText("Node " + value + " has added. ");
							treePanel.startAction();
							new java.util.Timer().schedule(new java.util.TimerTask() {
								@Override
								public void run() {
									logField.setText(" Check new Tree is balance ?");
								}
							}, 1000);
							// check degNode
							new java.util.Timer().schedule(new java.util.TimerTask() {
								@Override
								public void run() {
									try {
										Node nodeAdded = tree.searchNode(value);
										Node degNode = tree.chekDeg(nodeAdded);
										if (degNode != null) {
											logField.setText("Found an unbalance node in new tree. Rotating ...");
											repaint();
										} else {
											enableComponents(buttonPanel, true);
											logField.setText("Not found unbalance node in new tree.");
										}
										if (degNode != null) {
											enableComponents(buttonPanel, false);
											new java.util.Timer().schedule(new java.util.TimerTask() {
												@Override
												public void run() {
													tree.setColorForTree();
													String rotateType = tree.typeOfRotation(degNode);
													treePanel.setTreePanel(tree);
													treePanel.startAction();
													enableComponents(buttonPanel, true);
													logField.setText("Rotating node with " + rotateType);
												}
											}, 1500);
										}
									} catch (ExceptionForProject e) {
										enableComponents(buttonPanel, true);
										logField.setText(e.notification());
									}
								}
							}, 2300);
							
						} catch (ExceptionForProject e) {
							enableComponents(buttonPanel, true);
							logField.setText(e.notification());
						}
					}
				}, tree.listVisitedNode.size() * 1000);

			}

		});
		buttonPanel.add(addRandButton);
	}

	private void addRandSettingForBST() {

		addRandButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int value = (int) (Math.random() * 100 + 1);

				treePanel.setTreePanel(tree);
				enableComponents(buttonPanel, false);
				// FindPath
				tree.findPath(value);
				if (tree.listVisitedNode.size() != 0) {
					Thread thread = new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								for (Node i : tree.listVisitedNode) {
									tree.setColorForTree();
									tree.setLocationForAllNode();
									treePanel.startAction();
									int last = i.getValue();
									if (last > value) {
										logField.setText(
												"Adding " + value + ": Go to left child cause " + value + " < " + last);
									} else if (last < value) {
										logField.setText("Adding " + value + ": Go to right child cause " + value
												+ " > " + last);
									} else {
										logField.setText("Adding " + value + ": Stop! Found the node with same value!");
									}
									Node highLightNode = tree.searchNode(i.getValue());
									highLightNode.setStatus(Node.nodePath);
									treePanel.repaint();
									Thread.sleep(1000);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
					thread.start();
				}
				// add Node
				new java.util.Timer().schedule(new java.util.TimerTask() {
					@Override
					public void run() {
						try {
							tree.addNode(value);
							treePanel.setTreePanel(tree);
							treePanel.startAction();
							logField.setText("Node " + value + " has added.");
							enableComponents(buttonPanel, true);
						} catch (ExceptionForProject addBST) {
							enableComponents(buttonPanel, true);
							logField.setText(addBST.notification());
						}
					}
				}, tree.listVisitedNode.size() * 1000);

			}
		});
		buttonPanel.add(addRandButton);
	}

//********************************************* Setting for Remove Button  **********************************
	private void removeButtonSettingForAVL(AVLTree tree) {
		removeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int value;
				try {
					value = Integer.parseInt(valueField.getText());
				} catch (NumberFormatException number) {
					logField.setText(valueField.getText() + " is not number.");
					return;
				}
				enableComponents(buttonPanel, false);
				// find Path
				tree.findPath(value);
				if (tree.listVisitedNode.size() != 0) {
					tree.setColorForTree();
					Thread thread = new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								logField.setText("Searching " + value + " to remove .....");
								for (Node i : tree.listVisitedNode) {
									tree.setColorForTree();
									tree.setLocationForAllNode();
									treePanel.startAction();
									Node highLightNode = tree.searchNode(i.getValue());
									highLightNode.setStatus(Node.nodePath);
									treePanel.repaint();
									Thread.sleep(1000);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
					thread.start();
				}
				
				// Start Remove
				new java.util.Timer().schedule(new java.util.TimerTask() {
					@Override
					public void run() {
						try {
							
							Node forRemove = tree.searchNode(value);
							if (forRemove != null) {
								logField.setText("Find a replace node for remove ...");
								tree.findReplaceNodeForRemove(forRemove);
								repaint();
							} 
							
							new java.util.Timer().schedule(new java.util.TimerTask() {
								@Override
								public void run() {
									tree.setColorForTree();
									try {
										tree.removeNode(value);
										treePanel.setTreePanel(tree);
										tree.setLocationForAllNode();
										treePanel.startAction();
										logField.setText("Node " + value + " removed");
									} catch (ExceptionForProject rm) {
										enableComponents(buttonPanel, true);
										logField.setText(rm.notification());
										return;
									}
									
								}
							}, 1000);
							new java.util.Timer().schedule(new java.util.TimerTask() {
								@Override
								public void run() {
									logField.setText("Check the tree is Unbalance ?");
								}
							}, 2000);
							// check degNode
							new java.util.Timer().schedule(new java.util.TimerTask() {
								@Override
								public void run() {
									Node degNode = tree.checkDeg();

									if (degNode == null)
										logField.setText("Not found unbalanced node.");
									else {
										logField.setText("Found an unbalanced node. Rotating tree ...");
										repaint();
									}
									if (degNode != null) {
										new java.util.Timer().schedule(new java.util.TimerTask() {
											@Override
											public void run() {
												degNode.setStatus(Node.nodeColor);
												String rotateType = tree.typeOfRotation(degNode);
												treePanel.setTreePanel(tree);
												treePanel.startAction();
												enableComponents(buttonPanel, true);
												logField.setText("Tree is rotated with " + rotateType);
											}
										}, 2000);
									}
									enableComponents(buttonPanel, true);
								}
							}, 3000);

						} catch (ExceptionForProject rv2) {
							enableComponents(buttonPanel, true);
							logField.setText(rv2.notification());
						}
					}
				}, 1000 * tree.listVisitedNode.size());

			}
		});
		buttonPanel.add(removeButton);
	}

	private void removeButtonSettingForBST() {

		removeButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				int value;
				try {
					value = Integer.parseInt(valueField.getText());
				} catch (NumberFormatException e) {
					logField.setText(valueField.getText() + " is not number.");
					return;
				}
				enableComponents(buttonPanel, false);
				// find Path
				tree.findPath(value);
				if (tree.listVisitedNode.size() != 0) {
					Thread thread = new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								logField.setText("Searching " + value + " to remove...");
								for (Node i : tree.listVisitedNode) {
									tree.setColorForTree();
									tree.setLocationForAllNode();
									treePanel.startAction();
									Node highLightNode = tree.searchNode(i.getValue());
									highLightNode.setStatus(Node.nodePath);
									treePanel.repaint();
									Thread.sleep(1000);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
					thread.start();
				}

				new java.util.Timer().schedule(new java.util.TimerTask() {
					@Override
					public void run() {
						try {
							tree.setColorForTree();
							treePanel.repaint();
							Node forRemove = tree.searchNode(value);
							if (forRemove != null){
								forRemove.setStatus(Node.removeColor);
								treePanel.setTreePanel(tree);
								logField.setText("Node " + value + " has found");
								repaint();
								new java.util.Timer().schedule(new java.util.TimerTask() {
									@Override
									public void run() {
										logField.setText("Find a replace node for remove ...");
										tree.findReplaceNodeForRemove(forRemove);
										repaint();
									}
								}, 600);
							}
							
							new java.util.Timer().schedule(new java.util.TimerTask() {
								@Override
								public void run() {
									tree.setColorForTree();
									try {
										tree.removeNode(value);
										treePanel.setTreePanel(tree);
										treePanel.startAction();
										enableComponents(buttonPanel, true);
										logField.setText("Node " + value + " removed");
									} catch (ExceptionForProject rv) {
										enableComponents(buttonPanel, true);
										logField.setText(rv.notification());
									}
								}
							}, 2100);
						} catch (ExceptionForProject rv2) {
							enableComponents(buttonPanel, true);
							logField.setText(rv2.notification());
						}
					}
				}, tree.listVisitedNode.size() * 1000);
			}
		});
		buttonPanel.add(removeButton);
	}

// ********************************************* Setting for Search Button  **************************************

	private void searchButtonSetting() {

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

				tree.setColorForTree();
				repaint();
				tree.findPath(value);
				if (tree.listVisitedNode.size() != 0) {
					Thread thread = new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								logField.setText("Searching " + value + ".....");
								// them node can xoa vao list Highlight
								for (Node i : tree.listVisitedNode) {
									tree.setColorForTree();
									tree.setLocationForAllNode();
									treePanel.startAction();
									int last = i.getValue();
									if (last > value) {
										logField.setText(
												"Adding " + value + ": Go to left child cause " + value + " < " + last);
									} else {
										logField.setText("Adding " + value + ": Go to right child cause " + value
												+ " > " + last);
									}
									Node highLightNode = tree.searchNode(i.getValue());
									highLightNode.setStatus(Node.nodePath);
									treePanel.repaint();
									Thread.sleep(1000);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
					thread.start();
				}

				new java.util.Timer().schedule(new java.util.TimerTask() {

					@Override
					public void run() {
						try {
							tree.setColorForTree();
							treePanel.repaint();
							Node result;
							result = tree.searchNode(value);
							treePanel.setTreePanel(tree);
							if (result == null) {
								logField.setText("node " + value + " is not exist");
							} else {
								result.setStatus(Node.searchColor);
								repaint();
								logField.setText("node " + value + " is found");
							}
						} catch (ExceptionForProject e) {
							logField.setText(e.notification());
						}

					}

				}, tree.listVisitedNode.size() * 1000 + 1000);

			}
		});
		buttonPanel.add(searchButton);
	}
	private void removeActionListenerForButton() {
		for (ActionListener al : addButton.getActionListeners()) {
			addButton.removeActionListener(al);
		}
		for (ActionListener al : addRandButton.getActionListeners()) {
			addRandButton.removeActionListener(al);
		}
		for (ActionListener al : searchButton.getActionListeners()) {
			searchButton.removeActionListener(al);
		}
		for (ActionListener al : removeButton.getActionListeners()) {
			removeButton.removeActionListener(al);
		}
		for (ActionListener al : clearButton.getActionListeners()) {
			addButton.removeActionListener(al);
		}
	}
//
//
//
//	public void paint(Graphics g) {
//		// call superclass version of method paint
//		super.paint(g);
//	}

	private void enableComponents(Container container, boolean enable) {
		Component[] components = container.getComponents();

		for (Component component : components) {
			component.setEnabled(enable);
		}
	}
}
