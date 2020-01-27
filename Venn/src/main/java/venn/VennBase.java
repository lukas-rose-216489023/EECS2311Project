package venn;

import java.awt.*;
import java.awt.Event;

import javax.swing.event.ChangeListener;

import javafx.application.Application;
import javafx.beans.*;
import javafx.animation.*;
import javafx.event.*;
import javafx.collections.*;
import javafx.stage.*;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.scene.paint.*;
import javafx.scene.paint.Color;

public class VennBase extends Application	 {
	
	@Override 
	public void start(Stage primaryStage) {
		
		Stage stage = new Stage();
		
		Pane pane = new Pane();
		
		Color col = new Color(Color.RED.getRed(), Color.RED.getGreen(), Color.RED.getBlue(), 0.25);
		
		Circle circle = new Circle();
		circle.centerYProperty().bind(pane.heightProperty().divide(2.0));	
		circle.centerXProperty().bind(pane.widthProperty().divide(5.0/3.0));
		circle.radiusProperty().bind(pane.widthProperty().divide(5.0));
		circle.setStroke(Color.BLACK);
		circle.setFill(col);
		
		Circle circle2 = new Circle();
		circle2.centerYProperty().bind(pane.heightProperty().divide(2.0));												
		circle2.centerXProperty().bind(pane.widthProperty().divide(5.0/2.0));
		circle2.radiusProperty().bind(pane.widthProperty().divide(5.0));
		circle2.setStroke(Color.BLACK);
		circle2.setFill(col);
		
		
		Scene scene2 = new Scene(pane, 600, 500);
		stage.setTitle("Venn Demo");
		stage.setScene(scene2);
		stage.show();
		
	/*	Slider slider = new Slider();
		slider.setMin(0);
	    slider.setMax(100);
	    slider.setValue(80);
	    slider.setShowTickLabels(true);
	    slider.setShowTickMarks(true);
	    slider.setBlockIncrement(10);
	    
	    pane.getChildren().add(slider);
	*/   
	   
		final ColorPicker cp1 = new ColorPicker(col);
		final ColorPicker cp2 = new ColorPicker(col);
		
		
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
		
		
		
		pane.getChildren().addAll(circle,circle2,cp1,cp2);
		
		
	}
	

	
	
	
	
	public static void main(String[] args) {
		Application.launch(args);
	}

}
