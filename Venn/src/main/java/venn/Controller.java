package venn;

import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Controller {

	private Circle circle1;
	private ColorPicker cp1, cp2;
	
	public void insertClicked() {
		System.out.println("Hi");
	}
	
	
	
	public void colorSelected1() {
		cp1.valueProperty().addListener(observable, oldValue, newValue) -> {
			
			int red = (int) (255 * newValue.getRed());
			int green = (int) (255 * newValue.getGreen());
			int blue = (int) (255 * newValue.getBlue());
			
			circle1.setFill(Color.rgb(red, green, blue,0.2));
	}
	
}
