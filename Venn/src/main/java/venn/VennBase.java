package venn;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class VennBase extends Application	 {
	
	@Override 
	public void start(Stage primaryStage) {
		
		Stage stage = new Stage();
		

		Color blue = new Color(Color.BLUE.getRed(), Color.BLUE.getGreen(), Color.BLUE.getBlue(), 0.25);		
		Color red = new Color(Color.RED.getRed(), Color.RED.getGreen(), Color.RED.getBlue(), 0.25);

		
		Pane pane = new Pane();
		Circle circle = new Circle();
		circle.centerYProperty().bind(pane.heightProperty().divide(2.0));	
		circle.centerXProperty().bind(pane.widthProperty().divide(5.0/3.0));
		circle.radiusProperty().bind(pane.widthProperty().divide(5.0));
		circle.setStroke(Color.BLUE);
		circle.setFill(red);
		
		Pane pane2 = new Pane();
		Circle circle2 = new Circle();
		circle2.centerYProperty().bind(pane.heightProperty().divide(2.0));												
		circle2.centerXProperty().bind(pane.widthProperty().divide(5.0/2.0));
		circle2.radiusProperty().bind(pane.widthProperty().divide(5.0));
		circle2.setStroke(Color.RED);
		circle2.setFill(blue);
		
		Scene scene2 = new Scene(pane, 600, 500);
		stage.setTitle("Venn Demo");
		stage.setScene(scene2);
		
		final ColorPicker cp1 = new ColorPicker(red);
		final ColorPicker cp2 = new ColorPicker(blue);
		
		
		cp1.layoutYProperty().bind(pane.heightProperty().subtract(25));
		cp2.layoutYProperty().bind(pane.heightProperty().subtract(50));
		
		cp1.setOnAction(new EventHandler() {
			@Override
			public void handle(javafx.event.Event event) {
				Color col1 = new Color(cp1.getValue().getRed(), cp1.getValue().getGreen(), cp1.getValue().getBlue(), 0.25);
				circle.setFill(col1);
				}
        });
		
		cp2.setOnAction(new EventHandler() {
			@Override
			public void handle(javafx.event.Event event) {
				Color col2 = new Color(cp2.getValue().getRed(), cp2.getValue().getGreen(), cp2.getValue().getBlue(), 0.25);
				circle2.setFill(col2);
				}
        });
		
	
		

		pane.getChildren().addAll(circle, circle2, cp1, cp2);
		
		stage.show();
		
		
	}
	
	
	
	
	public static void main(String[] args) {
		Application.launch(args);
	}

}