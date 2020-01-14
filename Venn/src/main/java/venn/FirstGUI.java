package venn;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.*;

public class FirstGUI {
	
	private JFrame f;
	private JPanel p;
	private JButton b1;
	private JLabel lab;
	
	public FirstGUI() {
		
		gui();
	}
	
	public void gui() {
		
		f = new JFrame("First GUI");
		f.setVisible(true);
		f.setSize(600, 400);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		p = new JPanel();
		p.setBackground(Color.yellow);
		
		b1 = new JButton("O");
		lab = new JLabel("Create a Circle");
		
		p.add(b1);
		p.add(lab);
		
		f.add(p);
		f.add(p,BorderLayout.SOUTH);
	}
	
	public static void main (String args[]) {
		FirstGUI a = new FirstGUI();
	}
}
