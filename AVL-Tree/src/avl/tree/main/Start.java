package avl.tree.main;

import javax.swing.SwingUtilities;

import avl.tree.gui.UI;

public class Start {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new UI();
			}
		});
	}


}
