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

	String treeType[] = { "AVL Tree", "BST Tree" };
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
		treeBST = new BSTTree();
		treeAVL = new AVLTree();
		addButton = new JButton("Add number");
		addButtonSetting();
		buttonPanel.add(addButton);
		addRandButton = new JButton("Add random number");
		buttonPanel.add(addRandButton);
		addRandSetting();
		// ****************** Search Setting *********************
		searchButton = new JButton("Search node");
		searchButtonSetting();
		// ****************** Remove Node Setting *****************
		removeButton = new JButton("Remove node");
		buttonPanel.add(removeButton);
		removeButtonSetting();
		// ************ clear tree setting ***********************
		clearButton = new JButton("Remove tree");
		clearButtonSetting();

		// ************ Select option box setting *****************
		comboboxTree.setBackground(new Color(33, 184, 191));
		comboboxTree.setFont(new Font("Tahoma", Font.PLAIN, 14));
		buttonPanel.add(comboboxTree);
		buttonPanel.setBackground(new Color(226, 245, 251));
		treePanel.setBackground(Color.WHITE);

		getContentPane().add(buttonPanel, BorderLayout.NORTH);
		getContentPane().add(treePanel, BorderLayout.CENTER);
		getContentPane().add(logPanel, BorderLayout.SOUTH);

		setVisible(true);

	}

// ********************************************** Setting for AddButton ********************************************

	private void addButtonSetting() {
		addButton.setBackground(new Color(33, 184, 191));
		addButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
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
					// find Path
					treePanel.listVisitedNode = treePanel.findPath(treeBST.getTree(), value);

					if (treePanel.listVisitedNode.size() != 0) {
						Thread thread = new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									for (Node i : treePanel.listVisitedNode) {
										treeBST.setColorForTree();
										treePanel.repaint();
										int last = i.getValue();
										if (last > value) {
											logField.setText("Adding " + value + ": Go to left child cause " + value
													+ " < " + last);
										} else {
											logField.setText("Adding " + value + ": Go to right child cause " + value
													+ " > " + last);
										}
										Node highLightNode = treeBST.searchNode(i.getValue());
										highLightNode.setStatus(Node.nodePath);
										treePanel.repaint();
										Thread.sleep(2000);
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
								treeBST.addNode(value);
								treePanel.setNodePanel(treeBST.getTree());
								treePanel.setLocation();
								treePanel.startAction();
								logField.setText("node " + value + " added");
								enableComponents(buttonPanel, true);
							} catch (ExceptionForProject addBST) {
								enableComponents(buttonPanel, true);
								logField.setText(addBST.notification());
							}
						}
					}, (treePanel.listVisitedNode.size()) * 2000 + 1000);
				}
				// ******************************** Add with AVL **************************
				else {
					treePanel.setNodePanel(treeAVL.getTree());
					int value;
					try {
						value = Integer.parseInt(valueField.getText());
					} catch (NumberFormatException e) {
						System.out.println("not a number");
						logField.setText("" + valueField.getText() + "is not a number");
						return;
					}
					// find path
					enableComponents(buttonPanel, false);
					treePanel.listVisitedNode = treePanel.findPath(treeAVL.getTree(), value);
					if (treePanel.listVisitedNode.size() != 0) {
						Thread thread = new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									for (Node i : treePanel.listVisitedNode) {
										treeAVL.setColorForTree();
										treePanel.repaint();
										int last = i.getValue();
										if (last > value) {
											logField.setText("Adding " + value + ": Go to left child cause " + value
													+ " < " + last);
										} else {
											logField.setText("Adding " + value + ": Go to right child cause " + value
													+ " > " + last);
										}
										Node highLightNode = treeAVL.searchNode(i.getValue());
										highLightNode.setStatus(Node.nodePath);
										treePanel.repaint();
										Thread.sleep(2000);
									}
									logField.setText("node " + value + " added");
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
								treeAVL.addNode(value);
								treePanel.setNodePanel(treeAVL.getTree());
								treePanel.setLocation();
								logField.setText("Node " + value + " has added. ");
								treePanel.startAction();
								new java.util.Timer().schedule(new java.util.TimerTask() {
									@Override
									public void run() {
										logField.setText(" Check new Tree is balance ?");
									}
								}, 2000);
								// check degNode
								new java.util.Timer().schedule(new java.util.TimerTask() {
									@Override
									public void run() {
										try {
											Node nodeAdded = treeAVL.searchNode(value);
											Node degNode = treeAVL.chekDeg(nodeAdded);
											if (degNode != null) {
												logField.setText("Found an unbalance node in new tree. Rotating ...");
												repaint();
											} else {
												logField.setText("Not found unbalance node in new tree.");
											}
											if (degNode != null) {
												enableComponents(buttonPanel, false);
												new java.util.Timer().schedule(new java.util.TimerTask() {
													@Override
													public void run() {
														degNode.setStatus(Node.nodeColor);
														String rotateType = treeAVL.typeOfRotation(degNode);
														treePanel.setNodePanel(treeAVL.getTree());
														treePanel.startAction();
														enableComponents(buttonPanel, true);
														logField.setText("Rotating node with " + rotateType);
													}
												}, 2000);
											}
										} catch (ExceptionForProject e) {
											enableComponents(buttonPanel, true);
											logField.setText(e.notification());
										}
									}
								}, 4000);
								enableComponents(buttonPanel, true);
							} catch (ExceptionForProject e) {
								enableComponents(buttonPanel, true);
								logField.setText(e.notification());
							}
						}
					}, treePanel.listVisitedNode.size() * 2000 + 1000);
				}
			}
		});

	}

// ********************************************* Setting for clearButton ************************************	

	private void clearButtonSetting() {
		clearButton.setBackground(new Color(242, 96, 39));
		clearButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
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

// ********************************************* Setting for AddRand Button  ************************************

	public void addRandSetting() {
		addRandButton.setBackground(new Color(33, 184, 191));
		addRandButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		addRandButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int value = (int) (Math.random() * 100 + 1);

				// *************** Add rand with BST *************************

				if (comboboxTree.getSelectedItem() == "BST Tree") {

					treePanel.setNodePanel(treeBST.getTree());
					enableComponents(buttonPanel, false);
					// FindPath
					treePanel.listVisitedNode = treePanel.findPath(treeBST.getTree(), value);
					if (treePanel.listVisitedNode.size() != 0) {
						Thread thread = new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									for (Node i : treePanel.listVisitedNode) {
										treeBST.setColorForTree();
										treePanel.repaint();
										int last = i.getValue();
										if (last > value) {
											logField.setText("Adding " + value + ": Go to left child cause " + value
													+ " < " + last);
										} else {
											logField.setText("Adding " + value + ": Go to right child cause " + value
													+ " > " + last);
										}
										Node highLightNode = treeBST.searchNode(i.getValue());
										highLightNode.setStatus(Node.nodePath);
										treePanel.repaint();
										Thread.sleep(2000);
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
								treeBST.addNode(value);
								treePanel.setNodePanel(treeBST.getTree());
								treePanel.setLocation();
								treePanel.startAction();
								logField.setText("Node " + value + "has added.");
								enableComponents(buttonPanel, true);
							} catch (ExceptionForProject addBST) {
								enableComponents(buttonPanel, true);
								logField.setText(addBST.notification());
							}
						}
					}, treePanel.listVisitedNode.size() * 2000 + 1000);
				}
				// ******************* Add rand with AVL *******************
				else {
					enableComponents(buttonPanel, false);
					// find path
					treePanel.listVisitedNode = treePanel.findPath(treeAVL.getTree(), value);
					if (treePanel.listVisitedNode.size() != 0) {
						Thread thread = new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									for (Node i : treePanel.listVisitedNode) {
										treeAVL.setColorForTree();
										treePanel.repaint();
										int last = i.getValue();
										if (last > value) {
											logField.setText("Adding " + value + ": Go to left child cause " + value
													+ " < " + last);
										} else {
											logField.setText("Adding " + value + ": Go to right child cause " + value
													+ " > " + last);
										}
										Node highLightNode = treeAVL.searchNode(i.getValue());
										highLightNode.setStatus(Node.nodePath);
										treePanel.repaint();
										Thread.sleep(2000);
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
								treeAVL.addNode(value);
								treePanel.setNodePanel(treeAVL.getTree());
								treePanel.setLocation();
								logField.setText("Node " + value + " has added. ");
								treePanel.startAction();
								new java.util.Timer().schedule(new java.util.TimerTask() {
									@Override
									public void run() {
										logField.setText(" Check new Tree is balance ?");
									}
								}, 2000);
								// check degNode
								new java.util.Timer().schedule(new java.util.TimerTask() {
									@Override
									public void run() {
										try {
											Node nodeAdded = treeAVL.searchNode(value);
											Node degNode = treeAVL.chekDeg(nodeAdded);
											if (degNode != null) {
												logField.setText("Found an unbalance node in new tree. Rotating ...");
												repaint();
											} else {
												logField.setText("Not found unbalance node in new tree.");
											}
											if (degNode != null) {
												enableComponents(buttonPanel, false);
												new java.util.Timer().schedule(new java.util.TimerTask() {
													@Override
													public void run() {
														degNode.setStatus(Node.nodeColor);
														String rotateType = treeAVL.typeOfRotation(degNode);
														treePanel.setNodePanel(treeAVL.getTree());
														treePanel.startAction();
														enableComponents(buttonPanel, true);
														logField.setText("Rotating node with " + rotateType);
													}
												}, 2000);
											}
										} catch (ExceptionForProject e) {
											enableComponents(buttonPanel, true);
											logField.setText(e.notification());
										}
									}
								}, 4000);
								enableComponents(buttonPanel, true);
							} catch (ExceptionForProject e) {
								enableComponents(buttonPanel, true);
								logField.setText(e.notification());
							}
						}
					}, treePanel.listVisitedNode.size() * 2000 + 1000);

				}
			}
		});
	}

//********************************************* Setting for Remove Button  **********************************

	public void removeButtonSetting() {
		removeButton.setBackground(new Color(239, 219, 0));
		removeButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		removeButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				int value;
				try {
					value = Integer.parseInt(valueField.getText());
				} catch (NumberFormatException e) {
					logField.setText(valueField.getText() + " is not number.");
					return;
				}
				// ******************************** Remove for BST
				// ******************************************

				if (comboboxTree.getSelectedItem() == "BST Tree") {
					enableComponents(buttonPanel, false);
					// find Path
					treePanel.listVisitedNode = treePanel.findPath(treeBST.getTree(), value);
					if (treePanel.listVisitedNode.size() != 0) {
						Thread thread = new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									logField.setText("Searching " + value + " to remove...");
									for (Node i : treePanel.listVisitedNode) {
										treeBST.setColorForTree();
										treePanel.repaint();
										Node highLightNode = treeBST.searchNode(i.getValue());
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
								treeBST.setColorForTree();
								treePanel.repaint();
								Node forRemove = treeBST.searchNode(value);
								if (forRemove == null) {
									enableComponents(buttonPanel, true);
									logField.setText("node " + value + " is not exist");
									return;
								} else {
									forRemove.setStatus(Node.removeColor);
									treePanel.setNodePanel(treeBST.getTree());
									repaint();
								}
								new java.util.Timer().schedule(new java.util.TimerTask() {
									@Override
									public void run() {
										logField.setText("Find a replace node for remove ...");
										treeBST.findReplaceNodeForRemove(forRemove);
										repaint();
									}
								}, 2000);
								new java.util.Timer().schedule(new java.util.TimerTask() {
									@Override
									public void run() {
										treeBST.setColorForTree();
										treeBST.removeNode(forRemove);
										treePanel.setNodePanel(treeBST.getTree());
										treePanel.startAction();
										enableComponents(buttonPanel, true);
										logField.setText("node " + value + " removed");
									}
								}, 4000);
							} catch (ExceptionForProject rv) {
								enableComponents(buttonPanel, true);
								logField.setText(rv.notification());
							}
						}
					}, (treePanel.listVisitedNode.size() + 1) * 1000);
					logField.setText("node " + value + " removed");

				}
				// ******************************** Remove for AVL
				// ******************************************
				else {
					enableComponents(buttonPanel, false);
					// find Path
					treePanel.listVisitedNode = treePanel.findPath(treeAVL.getTree(), value);
					if (treePanel.listVisitedNode.size() != 0) {
						Thread thread = new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									logField.setText("Searching " + value + " to remove .....");
//									treePanel.listVisitedNode.add(new Node(value));
									for (Node i : treePanel.listVisitedNode) {
										treeAVL.setColorForTree();
										treePanel.repaint();
										Node highLightNode = treeAVL.searchNode(i.getValue());
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
								treeAVL.setColorForTree();
								findRoot();
								Node forRemove = treeAVL.searchNode(value);
								if (forRemove == null) {
									enableComponents(buttonPanel, true);
									logField.setText("node " + value + " is not exist");
									return;
								} else {
									forRemove.setStatus(Node.removeColor);
									treePanel.repaint();
								}
								new java.util.Timer().schedule(new java.util.TimerTask() {
									@Override
									public void run() {
										logField.setText("Find a replace node for remove ...");
										treeAVL.findReplaceNodeForRemove(forRemove);
										repaint();
									}
								}, 2000);
								new java.util.Timer().schedule(new java.util.TimerTask() {
									@Override
									public void run() {
										treeAVL.removeNode(forRemove);
										treePanel.setNodePanel(treeAVL.getTree());
										treePanel.setLocation();
										repaint();
										logField.setText("node " + value + " removed");
									}
								}, 4000);
								new java.util.Timer().schedule(new java.util.TimerTask() {
									@Override
									public void run() {
										logField.setText("Check the tree is Unbalance ?");
									}
								}, 6000);
								// check degNode
								new java.util.Timer().schedule(new java.util.TimerTask() {
									@Override
									public void run() {
										Node degNode = treeAVL.checkDeg();

										if (degNode == null)
											logField.setText("Not found unbalanced node.");
										else {
											logField.setText("Found an unbalanced node. Rotating tree ...");
											repaint();
										}
										if (degNode != null) {
//											treeAVL.setColorForTree();
											new java.util.Timer().schedule(new java.util.TimerTask() {
												@Override
												public void run() {
													degNode.setStatus(Node.nodeColor);
													treeAVL.typeOfRotation(degNode);
													findRoot();
													treePanel.setNodePanel(treeAVL.getTree());
													treePanel.startAction();
													enableComponents(buttonPanel, true);
													logField.setText("Tree is rotated.");
												}
											}, 2000);
										}
										enableComponents(buttonPanel, true);

									}
								}, 8000);

							} catch (ExceptionForProject rv2) {
								enableComponents(buttonPanel, true);
								logField.setText(rv2.notification());
							}
						}
					}, 1000 * (treePanel.listVisitedNode.size() + 1));

				}
			}
		});
	}

// ********************************************* Setting for Search Button  **************************************

	private void searchButtonSetting() {
		searchButton.setBackground(new Color(33, 184, 191));
		searchButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
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

				// ******************************* AVL ****************************************
				if (comboboxTree.getSelectedIndex() == 0) {
					treeAVL.setColorForTree();
					repaint();
					treePanel.listVisitedNode = treePanel.findPath(treeAVL.getTree(), value);
					if (treePanel.listVisitedNode.size() != 0) {
						Thread thread = new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									logField.setText("Searching " + value + ".....");
									// them node can xoa vao list Highlight
									for (Node i : treePanel.listVisitedNode) {
										treeAVL.setColorForTree();
										treePanel.repaint();
										Node highLightNode = treeAVL.searchNode(i.getValue());
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
								treeAVL.setColorForTree();
								treePanel.repaint();
								Node result;
								result = treeAVL.searchNode(value);
								treePanel.setNodePanel(treeAVL.getTree());
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

					}, treePanel.listVisitedNode.size() * 1000 + 1000);

				}

				// ******************************* BST ****************************************
				else {
					treeBST.setColorForTree();
					repaint();
					treePanel.listVisitedNode = treePanel.findPath(treeBST.getTree(), value);
					if (treePanel.listVisitedNode.size() != 0) {
						Thread thread2 = new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									logField.setText("Searching " + value + ".....");
									// them node can xoa vao list Highlight
									for (Node i : treePanel.listVisitedNode) {
										treeBST.setColorForTree();
										treePanel.repaint();
										Node highLightNode = treeBST.searchNode(i.getValue());
										highLightNode.setStatus(Node.nodePath);
										treePanel.repaint();
										Thread.sleep(1000);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
						thread2.start();
					}

					new java.util.Timer().schedule(new java.util.TimerTask() {

						@Override
						public void run() {
							try {
								treeBST.setColorForTree();
								treePanel.repaint();
								Node result;
								result = treeBST.searchNode(value);
								treePanel.setNodePanel(treeBST.getTree());
								if (result == null) {
									logField.setText("node " + value + " is not exist");
								} else {
									result.setStatus(Node.searchColor);
									repaint();
									logField.setText("node " + value + " is found");
								}
							} catch (ExceptionForProject e1) {
								logField.setText(e1.notification());
							}

						}

					}, treePanel.listVisitedNode.size() * 1000 + 1000);
				}
			}
		});
		buttonPanel.add(searchButton);
	}

	public void findRoot() {
		if (treeAVL.getTree() != null) {
			Node root = treeAVL.getTree();
			while (root.getParent() != null) {
				root = root.getParent();
			}
			treeAVL.setTree(root);
		}
		if (treeBST.getTree() != null) {
			Node root = treeBST.getTree();
			while (root.getParent() != null) {
				root = root.getParent();
			}
			treeBST.setTree(root);
		}
	}

	public void paint(Graphics g) {
		// call superclass version of method paint
		super.paint(g);
	}

	private void enableComponents(Container container, boolean enable) {
		Component[] components = container.getComponents();

		for (Component component : components) {
			component.setEnabled(enable);
		}
	}
}
