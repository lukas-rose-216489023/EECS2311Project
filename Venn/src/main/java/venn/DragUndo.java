package venn;

import java.util.ArrayList;
import javafx.scene.control.Button;

public class DragUndo {

	ArrayList<Point> pointsList;
	int pointsCursor;
	Button b;
	
	public DragUndo(Button b, double x, double y) {
		this.b = b;
		this.pointsList = new ArrayList<Point>();
		this.pointsCursor = 0;
		this.pointsList.add(0, new Point(x, y));		
	}
	
	public void addPoint(double x, double y) {
		this.pointsList.add(++this.pointsCursor, new Point(x, y));
	}
	
}
