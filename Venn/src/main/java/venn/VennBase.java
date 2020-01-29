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
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
									
public class VennBase extends Application	 {
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override 
	public void start(Stage stage) {
		
		//sets window
		stage.setMaximized(true);
		Pane pane = new Pane();
		
		//Custom colors
		Color blue = new Color(Color.BLUE.getRed(), Color.BLUE.getGreen(), Color.BLUE.getBlue(), 0.25);		
		Color red = new Color(Color.RED.getRed(), Color.RED.getGreen(), Color.RED.getBlue(), 0.25);
		@SuppressWarnings("unused")
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

	    
		//Right venn circle
		Circle circleR = new Circle();
		circleR.centerYProperty().bind(pane.heightProperty().divide(2.0));
		circleR.centerXProperty().bind(pane.widthProperty().divide(5.0/3.0));
		circleR.radiusProperty().bind(pane.widthProperty().divide(5.0));
		circleR.setStroke(Color.BLUE);
		circleR.setFill(red);
		
		//Left venn circle
		Circle circleL= new Circle();
		circleL.centerYProperty().bind(pane.heightProperty().divide(2.0));
		circleL.centerXProperty().bind(pane.widthProperty().divide(5.0/2.0));
		circleL.radiusProperty().bind(pane.widthProperty().divide(5.0));
		circleL.setStroke(Color.RED);
		circleL.setFill(blue);
		
		
		//Text box prototype - 1st implementation
		Button box1 = new Button("Insert text here 12345678");
//		box1.layoutXProperty().bind(pane.widthProperty().divide(30.0/13.0));
//		box1.layoutYProperty().bind(pane.heightProperty().divide(2.0));
		box1.setLayoutX(1100);
		box1.setLayoutY(200);
		box1.prefWidthProperty().bind(pane.widthProperty().divide(8.0));
		box1.prefHeightProperty().bind(pane.heightProperty().divide(15.0));
		box1.setStyle("-fx-background-color: #80b380");
		box1.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
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
//		cp1("Right circle color");
//		cp2.setAccessibleText("Left circle color");
		
		cp1.setOnAction(new EventHandler() {
			@Override
			public void handle(javafx.event.Event event) {
				Color col1 = new Color(cp1.getValue().getRed(), cp1.getValue().getGreen(), cp1.getValue().getBlue(), 0.5);
				circleR.setFill(col1);
				col1.saturate();
				col1.saturate();
				col1.saturate();
				circleR.setStroke(col1);
				}
        });
		
		cp2.setOnAction(new EventHandler() {
			@Override
			public void handle(javafx.event.Event event) {
				Color col2 = new Color(cp2.getValue().getRed(), cp2.getValue().getGreen(), cp2.getValue().getBlue(), 0.5);
				circleL.setFill(col2);
				col2.saturate();
				col2.saturate();
				col2.saturate();
				circleL.setStroke(col2);
				}
        });
		
		
		//text box adder
		Button textAdder = new Button("Add new text box");
		textAdder.layoutXProperty().bind(pane.widthProperty().subtract(200));
		textAdder.layoutYProperty().bind(pane.heightProperty().subtract(50));
		textAdder.setPrefWidth(200);
		textAdder.setPrefHeight(50);
		box1.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				
			}
		});
		
		
		//Texts
		Text title = new Text("Title");
		title.setFont(new Font(20));
		title.setStroke(Color.BLACK);
		title.setTextAlignment(TextAlignment.CENTER);
		title.layoutXProperty().bind(pane.widthProperty().divide(2));
		title.setLayoutY(25);
		
		Text left = new Text("left");
		left.setFont(new Font(12));
		left.setStroke(Color.BLACK);
		left.setTextAlignment(TextAlignment.CENTER);
		left.layoutXProperty().bind(pane.widthProperty().divide(5.0/2.0).subtract(25));
		left.layoutYProperty().bind(pane.heightProperty().divide(2.0).subtract(circleL.radiusProperty()).subtract(5));
		
		Text right = new Text("right");
		right.setFont(new Font(12));
		right.setStroke(Color.BLACK);
		right.setTextAlignment(TextAlignment.CENTER);
		right.layoutXProperty().bind(pane.widthProperty().divide(5.0/3.0).subtract(25));
		right.layoutYProperty().bind(pane.heightProperty().divide(2.0).subtract(circleL.radiusProperty()).subtract(5));
		
				
		//Adds items to the window
		pane.getChildren().add(circleR);
		pane.getChildren().add(circleL);
		pane.getChildren().add(box1);
		pane.getChildren().addAll(cp1, cp2);
		pane.getChildren().add(slider);
		pane.getChildren().add(textAdder);
		pane.getChildren().add(title);
		pane.getChildren().add(left);
		pane.getChildren().add(right);

		
		Scene scene2 = new Scene(pane);
		stage.setTitle("Venn Application Demo");
		stage.setScene(scene2);
		stage.show();
		
		
	}
	
	//for recording mouse distance
	static class Delta	{
		double x;
		double y;
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}

}