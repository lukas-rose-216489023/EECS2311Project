package venn;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class Main extends Application {

	public void start(Stage primaryStage) throws Exception{
		Parent root = FXMLLoader.load(getClass().getResource("SceneVenn.fxml"));
		primaryStage.setTitle("Venn Diagram");
		primaryStage.setScene(new Scene(root, 800, 500));
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}