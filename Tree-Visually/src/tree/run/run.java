package tree.run;

import javax.swing.SwingUtilities;
import tree.gui.GUI;

public class run {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new GUI();
			}
		});
	}
}
