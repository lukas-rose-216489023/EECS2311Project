package venn;

import java.util.ArrayList;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.Pane;

//Class for recording variables for each box; created for each box
public class Record {

	static int numBoxes;

	static ArrayList<String> left = new ArrayList<String>();
	static ArrayList<String> intersection = new ArrayList<String>();
	static ArrayList<String> right = new ArrayList<String>();
	static ArrayList<String> universal = new ArrayList<String>();

	static ArrayList<TextBox> tBoxes = new ArrayList<TextBox>();

	boolean inSelectionX = false;
	boolean inSelectionY = false;

	double moveX,moveY;
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

	public static void addToLeft(String text) {left.add(text);}
	public static void addToIntersection(String text) {intersection.add(text);}
	public static void addToRight(String text) {right.add(text);}
	public static void addToUniversal(String text) {universal.add(text);}

	public static void removeFromLeft(String text) {if (left.contains(text)){left.remove(text);}}
	public static void removeFromIntersetion(String text) {if (intersection.contains(text)){intersection.remove(text);}}
	public static void removeFromRight(String text) {if (right.contains(text)){right.remove(text);}}
	public static void removeFromUniversal(String text) {if (universal.contains(text)){universal.remove(text);}}
	
	public static void printAll() {
		System.out.println("left:");
		for (String t:left) {System.out.println(" - "+t);}
		System.out.println("intersection:");
		for (String t:intersection) {System.out.println(" - "+t);}
		System.out.println("right:");
		for (String t:right) {System.out.println(" - "+t);}
	}

	public static void addTextBox(TextBox b) {tBoxes.add(b);}
	public static void removeTextBox(TextBox b) {tBoxes.remove(b);removeFromLeft(b.box.getText());removeFromIntersetion(b.box.getText());removeFromRight(b.box.getText());}

	public static void deleteSelection(Pane pane) {
		ArrayList<TextBox> iterate = new ArrayList<TextBox>(tBoxes);
		for (TextBox b:iterate) {
			if (b.record.inSelectionX&&b.record.inSelectionY) {b.removeFromList(pane);}
		}
	}
	
	public static boolean checkDataClash(Button box, String pos) {
		System.out.println("\nchecking..");
		boolean clash = false;
		String clashPos = "";
		if (pos.equals("intersection")&&Record.intersection.contains(box.getText())) {clash=true;clashPos="intersection";}
		else if (pos.equals("right")&&Record.right.contains(box.getText())) {clash=true;clashPos="right";}
		else if (pos.equals("left")&&Record.left.contains(box.getText())) {clash=true;clashPos="left";}
		if (clash&&!VennBase.debug) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Text Box Clash");
			alert.setHeaderText("There already exists a text box with the same text in this "+clashPos+" section of the diagram.");
			alert.setContentText("Would you like to continue placing the text box here?");

			ButtonType yes = new ButtonType("Yes");
			ButtonType no = new ButtonType("No", ButtonData.CANCEL_CLOSE);

			alert.getButtonTypes().setAll(yes, no);

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get().equals(yes)) {
				// ... user chose "yes"
				return true;
			}
			else if (result.get().equals(no)) {
				// ... user chose "no"
				return false;
			}
		}
		
		System.out.println("still checking...\n");printAll();
		clash=false;
		if (pos.equals("intersection")) {
			if (Record.right.contains(box.getText())){clash=true;clashPos="right";}
			if (Record.left.contains(box.getText())){clash=true;clashPos="left";}
		}
		else if (pos.equals("left")) {
			if (Record.right.contains(box.getText())){clash=true;clashPos="right";}
			if (Record.intersection.contains(box.getText())){clash=true;clashPos="intersection";}
		}
		else if (pos.equals("right")&&Record.left.contains(box.getText())) {
			if (Record.left.contains(box.getText())){clash=true;clashPos="left";}
			if (Record.intersection.contains(box.getText())){clash=true;clashPos="intersection";}
		}
		if (clash&&!VennBase.debug) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Text Box Clash");
			alert.setHeaderText("There already exists a text box with the same text in the "+clashPos+" section of the diagram.");
			alert.setContentText("Would you like to continue placing the text box here?");

			ButtonType yes = new ButtonType("Yes");
			ButtonType no = new ButtonType("No", ButtonData.CANCEL_CLOSE);

			alert.getButtonTypes().setAll(yes, no);

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get().equals(yes)) {
				// ... user chose "yes"
				return true;
			}
			else if (result.get().equals(no)) {
				// ... user chose "no"
				return false;
			}
		}
		
		return true;
	}

}
