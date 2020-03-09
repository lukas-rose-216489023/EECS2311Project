package venn;

import java.util.ArrayList;

import javafx.scene.layout.Pane;

//Class for recording variables for each box; created for each box
public class Record {

	static int numBoxes;

	static ArrayList<String> left = new ArrayList<String>();
	static ArrayList<String> intersection = new ArrayList<String>();
	static ArrayList<String> right = new ArrayList<String>();
	
	static ArrayList<TextBox> tBoxes = new ArrayList<TextBox>();

	boolean inSelectionX = false;
	boolean inSelectionY = false;
	
	static double moveX;
	static double moveY;
	static double selectX, selectY;

	double x;
	double y;

	//Default text box color stored in this circle
	//		static String textBox = null;

	//resize detection variables
	double percentX;
	double percentY;

	//position detection variables
	boolean inCircleR;
	boolean inCircleL;

	public void addToLeft(String text) {left.add(text);}
	public void addToIntersection(String text) {intersection.add(text);}
	public void addToRight(String text) {right.add(text);}

	public static void removeFromLeft(String text) {if (left.contains(text)){left.remove(text);}}
	public static void removeFromIntersetion(String text) {if (intersection.contains(text)){intersection.remove(text);}}
	public static void removeFromRight(String text) {if (right.contains(text)){right.remove(text);}}
	
	public static void addTextBox(TextBox b) {tBoxes.add(b);}
	public static void removeTextBox(TextBox b) {tBoxes.remove(b);removeFromLeft(b.box.getText());removeFromIntersetion(b.box.getText());removeFromRight(b.box.getText());}
	
	public static void deleteSelection(Pane pane) {
		ArrayList<TextBox> iterate = new ArrayList<TextBox>(tBoxes);
		for (TextBox b:iterate) {
			if (b.record.inSelectionX&&b.record.inSelectionY) {b.removeFromList(pane);}
		}
	}


}
