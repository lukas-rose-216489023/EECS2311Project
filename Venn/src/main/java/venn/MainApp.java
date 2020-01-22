package venn;

import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.StackPane;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.stage.Stage;

import javafx.fxml.FXMLLoader;

 
 
public class MainApp extends Application{   
	
	Stage window;
	Scene scene1;
    
    public static void main(String[] args) {
    	Application.launch(args);
    }
     
    public void start(Stage primaryStage) throws Exception{
    	window = primaryStage;
        
    	Button but1 = new Button("Venn Diagram");
    	but1.setOnAction(e -> window.setScene(scene1));
    	but1.isVisible();
        window.show();
    }
    
    public void scene(Scene scene1) throws Exception{
    	
        
    }
    
    
   
}
