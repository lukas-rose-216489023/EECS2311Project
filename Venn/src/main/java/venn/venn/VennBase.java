package venn.venn;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class VennBase extends Application	 {
	
	@Override 
	public void start(Stage primaryStage) {
		StackPane pane = new StackPane();
		pane.getChildren().add(new Button("Stack Pane"));
		Scene scene = new Scene(pane, 600, 500);
		primaryStage.setTitle("StackPane Demo");
		primaryStage.setScene(scene);
//		primaryStage.show();
		
		Stage stage = new Stage();
		stage.setTitle("Second Stage");
		stage.setScene(new Scene(new Button("New Stage"), 600, 500));
//		stage.show();
		
		Pane npane = new Pane();
		Stage stage2 = new Stage();
		Circle circle = new Circle();
		circle.setCenterY(100);													//stationary/ can only set the center once
		circle.centerXProperty().bind(npane.widthProperty().divide(1.5));		//adjusts according to panel data
		circle.setRadius(50);
		circle.setStroke(Color.BLUE);
		circle.setFill(Color.RED);

		Circle circle2 = new Circle();
		circle2.setCenterY(100);													//stationary/ can only set the center once
		circle2.centerXProperty().bind((npane.widthProperty().divide(2.5)));		//adjusts according to panel data
		circle2.setRadius(50);
		circle2.setStroke(Color.RED);
		circle2.setFill(Color.BLUE);
		
		npane.getChildren().add(circle);
		npane.getChildren().add(circle2);
		
		Scene scene2 = new Scene(npane, 200, 200);
		stage2.setTitle("Venn Demo");
		stage2.setScene(scene2);
		stage2.show();
		
		
	}
	
	
	
	
	public static void main(String[] args) {
		Application.launch(args);
	}

}
