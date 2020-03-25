package venn;

import java.util.ArrayList;
import javafx.scene.layout.Pane;

//Class for recording variables for each box; created for each box
public class Record {

	static int numBoxes;
	
	int recordNum;

	static ArrayList<TextBox> left = new ArrayList<TextBox>();
	static ArrayList<TextBox> intersection = new ArrayList<TextBox>();
	static ArrayList<TextBox> right = new ArrayList<TextBox>();
	static ArrayList<TextBox> universal = new ArrayList<TextBox>();
	static ArrayList<TextBox> tBoxes = new ArrayList<TextBox>();
	
	boolean inSelectionX = false;
	boolean inSelectionY = false;
	boolean ctrlSelected = false;

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
	
	public Record(FileHandling autoSaveFile) {
		recordNum=numBoxes;
		autoSaveFile.WriteToFile("Record"+recordNum+" "+percentX+" "+percentY+" "+inCircleR+" "+inCircleL);
	}

	public static void addToLeft(TextBox tbox) {if (!left.contains(tbox)){left.add(tbox);tbox.pos="left";}}
	public static void addToIntersection(TextBox tbox) {if (!intersection.contains(tbox)){intersection.add(tbox);tbox.pos="intersection";}}
	public static void addToRight(TextBox tbox) {if (!right.contains(tbox)){right.add(tbox);tbox.pos="right";}}
	public static void addToUniversal(TextBox tbox) {if (!universal.contains(tbox)){universal.add(tbox);tbox.pos="universal";}}

	public static void removeFromLeft(TextBox tbox) {if (left.contains(tbox)){left.remove(tbox);}}
	public static void removeFromIntersetion(TextBox tbox) {if (intersection.contains(tbox)){intersection.remove(tbox);}}
	public static void removeFromRight(TextBox tbox) {if (right.contains(tbox)){right.remove(tbox);}}
	public static void removeFromUniversal(TextBox tbox) {if (universal.contains(tbox)){universal.remove(tbox);}}
	
	public static void printAll() {
		System.out.println("==============================================================================");
		System.out.println("left:");
		for (TextBox t:left) {System.out.println(" - "+t.box.getText());}
		System.out.println("intersection:");
		for (TextBox t:intersection) {System.out.println(" - "+t.box.getText());}
		System.out.println("right:");
		for (TextBox t:right) {System.out.println(" - "+t.box.getText());}
		System.out.println("universal:");
		for (TextBox t:universal) {System.out.println(" - "+t.box.getText());}
		System.out.println("==============================================================================");
		System.out.println("total:");
		for (TextBox t:tBoxes) {System.out.println(" - "+t.box.getText());}
	}

	public static void addTextBox(TextBox b) {tBoxes.add(b);}
	public static void removeTextBox(TextBox b) {tBoxes.remove(b);removeFromLeft(b);removeFromIntersetion(b);removeFromRight(b);}
	
	public static void deleteSelection(Pane pane, FileHandling autoSaveFile) {
		ArrayList<TextBox> iterate = new ArrayList<TextBox>(tBoxes);
		for (TextBox b:iterate) {
			if (b.record.inSelectionX&&b.record.inSelectionY) {
				b.removeFromList(pane);
				FileHandling.saveChanges(autoSaveFile, ("Box"+b.boxNum), "");
				FileHandling.saveChanges(autoSaveFile, ("Record"+b.boxNum), "");
			}
		}
	}

	public static void deleteAll(Pane pane, FileHandling autoSaveFile) {
		ArrayList<TextBox> iterate = new ArrayList<TextBox>(tBoxes);
		for (TextBox b:iterate) {
			b.removeFromList(pane);
			FileHandling.saveChanges(autoSaveFile, ("Box"+b.boxNum), "");
			FileHandling.saveChanges(autoSaveFile, ("Record"+b.record.recordNum), "");
		}
	}

}
