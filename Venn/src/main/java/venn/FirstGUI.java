package venn;

import javax.swing.JFrame;
public class FirstGUI extends JFrame {
	public static void main (String args[]) {
		FirstGUI gui = new FirstGUI();
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gui.setSize(1000, 1000);
		gui.setVisible(true);
		gui.setTitle("First GUI");
	}
}
