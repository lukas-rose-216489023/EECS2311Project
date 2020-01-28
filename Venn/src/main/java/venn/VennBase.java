package venn;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.event.Event;
									
public class VennBase extends Application	 {
	
	@Override 
	public void start(Stage stage) {
		
		//sets window
		stage.setMaximized(true);
		Pane pane = new Pane();
		
		//Custom colors
		Color blue = new Color(Color.BLUE.getRed(), Color.BLUE.getGreen(), Color.BLUE.getBlue(), 0.25);		
		Color red = new Color(Color.RED.getRed(), Color.RED.getGreen(), Color.RED.getBlue(), 0.25);
		Color green = new Color(Color.GREEN.getRed(), Color.GREEN.getGreen(), Color.GREEN.getBlue(), 0.5);
		
		
		//Size slider
		//slider sets sizes small, medium, large, extra large	
		Slider slider = new Slider();
		slider.setMin(0);
		slider.setMax(4);
		slider.setValue(3);
	    slider.setShowTickLabels(true);
	    slider.setShowTickMarks(true);
	    slider.setBlockIncrement(1);

	    
		//First venn circle
		Circle circle = new Circle();
		circle.centerYProperty().bind(pane.heightProperty().divide(2.0));
		circle.centerXProperty().bind(pane.widthProperty().divide(5.0/3.0));
		circle.radiusProperty().bind(pane.widthProperty().divide(4.0));
		circle.setStroke(Color.BLUE);
		circle.setFill(red);
		
		//Second venn circle
		Circle circle2 = new Circle();
		circle2.centerYProperty().bind(pane.heightProperty().divide(2.0));
		circle2.centerXProperty().bind(pane.widthProperty().divide(5.0/2.0));
		circle2.radiusProperty().bind(pane.widthProperty().divide(4.0));
		circle2.setStroke(Color.RED);
		circle2.setFill(blue);
		
		//Text box within first circle - 1st implementation
//		int sGreen = green();
		Button box1 = new Button("Insert text here");
//		box1.layoutXProperty().bind(pane.widthProperty().divide(30.0/13.0));
//		box1.layoutYProperty().bind(pane.heightProperty().divide(2.0));
		box1.setLayoutX(500);
		box1.setLayoutY(0);
		box1.prefWidthProperty().bind(pane.widthProperty().divide(8.0));
		box1.prefHeightProperty().bind(pane.heightProperty().divide(10.0));
//		box1.setStyle("-fx-background-color: #"+sGreen);
		box1.setOnMouseClicked(new EventHandler() {
			@Override
			public void handle(javafx.event.Event event) {
				box1.setText("It works!");
			}
		});
		//changes cursor when moving text box and records distance moved
		final Delta drag = new Delta();
		box1.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                box1.setCursor(Cursor.MOVE);
                drag.x = box1.getLayoutX() - mouseEvent.getSceneX();
                drag.y = box1.getLayoutY() - mouseEvent.getSceneY();
            }
        });
		//Moves text box when dragged
		box1.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				box1.setLayoutX(mouseEvent.getSceneX() + drag.x);
				box1.setLayoutY(mouseEvent.getSceneY() + drag.y);
			}
		});
		
		
		//color picker setup
		final ColorPicker cp1 = new ColorPicker(red);
		final ColorPicker cp2 = new ColorPicker(blue);
		
		cp1.layoutYProperty().bind(pane.heightProperty().subtract(25));
		cp2.layoutYProperty().bind(pane.heightProperty().subtract(50));
		
		cp1.setOnAction(new EventHandler() {
			@Override
			public void handle(javafx.event.Event event) {
				Color col1 = new Color(cp1.getValue().getRed(), cp1.getValue().getGreen(), cp1.getValue().getBlue(), 0.5);
				circle.setFill(col1);
				}
        });
		
		cp2.setOnAction(new EventHandler() {
			@Override
			public void handle(javafx.event.Event event) {
				Color col2 = new Color(cp2.getValue().getRed(), cp2.getValue().getGreen(), cp2.getValue().getBlue(), 0.5);
				circle2.setFill(col2);
				}
        });
		
		
		//text box adder
		Button textAdder = new Button("Add new text box");
		textAdder.layoutXProperty().bind(pane.widthProperty().subtract(200));
		textAdder.layoutYProperty().bind(pane.heightProperty().subtract(50));
		textAdder.setPrefWidth(200);
		textAdder.setPrefHeight(50);
		box1.setOnMouseClicked(new EventHandler() {
			@Override
			public void handle(javafx.event.Event event) {
				
			}
		});
		
		
				
		//Adds items to the window
		pane.getChildren().add(circle);
		pane.getChildren().add(circle2);
		pane.getChildren().add(box1);
		pane.getChildren().addAll(cp1, cp2);
		pane.getChildren().add(slider);
		pane.getChildren().add(textAdder);
		
		Scene scene2 = new Scene(pane);
		stage.setTitle("Venn Application Demo");
		stage.setScene(scene2);
		stage.show();
		
		
	}
	
	//for recording mouse distance
	static class Delta	{
		static double x,y;
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}

}