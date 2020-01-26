package venn;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class Main extends Application {

	public void start(Stage primaryStage) throws Exception{
		
		Pane mainPane = (Pane) FXMLLoader.load(getClass().getResource("SceneVenn.fxml"));
		Scene scene = new Scene(mainPane);
		
		Circle circle1 = new Circle();
		mainPane.getChildren().add(circle1);
		circle1.setNodeOrientation(null);
		
		primaryStage.setScene(scene);
		
		primaryStage.show();
		
		mainPane.heightProperty().addListener(new ChangeListener() {

			@Override
			public void changed(ObservableValue arg0, Object arg1, Object arg2) {
				double height = (double) arg2;
				circle1.setRadius(height/2);	
				
			}
			
		});
		

		primaryStage.show();
				
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}