package venn;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class VennBase extends Application	 {
	
	@Override 
	public void start(Stage primaryStage) {
		
		Pane pane = new Pane();
		Stage stage2 = new Stage();
		Circle circle = new Circle();
		circle.centerYProperty().bind(pane.heightProperty().divide(2.0));	
		circle.setCenterX(250);
		circle.setRadius(100);
		circle.setStroke(Color.BLUE);
		circle.setFill(Color.RED);

		Circle circle2 = new Circle();
		circle2.centerYProperty().bind(pane.heightProperty().divide(2.0));												
		circle2.setCenterX(350);
		circle2.setRadius(100);
		circle2.setStroke(Color.RED);
		circle2.setFill(Color.BLUE);
		
		pane.getChildren().add(circle);
		pane.getChildren().add(circle2);
		
		Scene scene2 = new Scene(pane, 600, 500);
		stage2.setTitle("Venn Demo");
		stage2.setScene(scene2);
		stage2.show();
		
		
	}
	
	
	
	
	public static void main(String[] args) {
		Application.launch(args);
	}

}
