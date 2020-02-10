package venn;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextInputDialog;
									
@SuppressWarnings("unused")
public class VennBase extends Application	 {
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override 
	public void start(Stage stage) {
		
		//sets window
		StackPane root = new StackPane();
		Pane pane = new Pane();
		
		//Get primary screen bounds
	    Rectangle2D screenBounds = Screen.getPrimary().getBounds();
		
		//Custom colors
		Color blue = new Color(Color.BLUE.getRed(), Color.BLUE.getGreen(), Color.BLUE.getBlue(), 0.5);		
		Color red = new Color(Color.RED.getRed(), Color.RED.getGreen(), Color.RED.getBlue(), 0.5);
		@SuppressWarnings("unused")
		Color green = new Color(Color.GREEN.getRed(), Color.GREEN.getGreen(), Color.GREEN.getBlue(), 0.5);
	    
		//Right venn circle
		Circle circleR = new Circle();
		circleR.centerYProperty().bind(pane.heightProperty().divide(2.0));
		circleR.centerXProperty().bind(pane.widthProperty().divide(5.0/3.0));
		circleR.radiusProperty().bind(pane.widthProperty().divide(5.0));
		circleR.setStroke(Color.BLUE);
		circleR.setFill(blue);
		
		//Left venn circle
		Circle circleL= new Circle();
		circleL.centerYProperty().bind(pane.heightProperty().divide(2.0));
		circleL.centerXProperty().bind(pane.widthProperty().divide(5.0/2.0));
		circleL.radiusProperty().bind(pane.widthProperty().divide(5.0));
		circleL.setStroke(Color.RED);
		circleL.setFill(red);
		
		
		//color picker setup
		final ColorPicker cp1 = new ColorPicker(Color.BLUE);
		final ColorPicker cp2 = new ColorPicker(Color.RED);
		final ColorPicker cp3 = new ColorPicker(Color.GREEN);
		cp1.layoutYProperty().bind(pane.heightProperty().subtract(25));
		cp2.layoutYProperty().bind(pane.heightProperty().subtract(50));
		cp3.layoutYProperty().bind(pane.heightProperty().subtract(75));
		
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
		
		cp3.setOnAction(new EventHandler() {
			@Override
			public void handle(javafx.event.Event event) {
				String textBo = Integer.toHexString(cp3.getValue().hashCode());//String.format("#%02X%02X%02X", ((int)cp3.getValue().getRed())*255, ((int)cp3.getValue().getGreen())*255, ((int)cp3.getValue().getBlue())*255);
//				textBo = "-fx-background-color: #" + textBo;
//				Record.textBox = textBo;
				}
        });
		
		
		//text box adder
		Button textAdder = new Button("Add new text box");		
		textAdder.setPrefWidth(200);
		textAdder.setPrefHeight(50);
		textAdder.setLayoutX(screenBounds.getWidth()-200);
		textAdder.setLayoutY(screenBounds.getHeight()-72);
		
		textAdder.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {		
				int stackable = Math.floorDiv((int) screenBounds.getHeight(), (int) textAdder.getHeight())-2;
				
				//Text box properties
				
				Button box = new Button("New Text Box");
//				boolean moved = false;
				box.prefWidthProperty().bind(pane.widthProperty().divide(7.0));
				box.prefHeightProperty().bind(textAdder.heightProperty().subtract(15));
				box.setLayoutX(textAdder.getLayoutX());
				box.setLayoutY(textAdder.getLayoutY()-(textAdder.getHeight()*(Record.numBoxes%stackable)+textAdder.getHeight()));
				Record.numBoxes++;
				
//				box.setStyle("-fx-background-color: "+Record.textBox);
				box.setStyle("-fx-background-color: #80b380");
				
				//Change contents
				box.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
//						box.setText(Record.textBox);
						if (event.getButton() == MouseButton.SECONDARY) {
							TextInputDialog dialog = new TextInputDialog(box.getText());
							dialog.setTitle("Change text");
							dialog.setHeaderText("Enter to change text\nLeave empty to delete text box");
							dialog.setContentText("25 character limit");
							String result = dialog.showAndWait().get();
							while (result.length()>25) {
								dialog.setHeaderText("Character limit is 25!");
								result = dialog.showAndWait().get();
								}
							box.setText(result);
							if (box.getText().equals("")) {pane.getChildren().remove(box);}
						}
					}
				});


				//changes cursor when moving text box  and  records distance moved
				final Delta drag = new Delta();
				box.setOnMousePressed(new EventHandler<MouseEvent>() {
				    @Override
				    public void handle(MouseEvent mouseEvent) {
				        box.setCursor(Cursor.MOVE);
				        drag.x = box.getLayoutX() - mouseEvent.getSceneX();
				        drag.y = box.getLayoutY() - mouseEvent.getSceneY();
				    }
				});

				//Moves text box when dragged
				box.setOnMouseDragged(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent mouseEvent) {
						box.setLayoutX(mouseEvent.getSceneX() + drag.x);
						box.setLayoutY(mouseEvent.getSceneY() + drag.y);
//						if (!moved) {moved=true;Record.numBoxes--;}
					}
				});

				pane.getChildren().add(box);
				
			}

		});
		
		
		//Texts
		Text title = new Text("Title");
		title.setFont(new Font(20));
		title.setStroke(Color.BLACK);
		title.setLayoutX(((int) screenBounds.getWidth()/2)-title.getText().length()*8/2);			//does not support window resizing
		title.setLayoutY(25);
		
//		title.setOnMouseMoved(new EventHandler<MouseEvent>() {
//			@Override
//			public void handle(MouseEvent event) {		
//				title.setText(""+textAdder.getHeight());
//			}
//		});
		
		title.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getButton() == MouseButton.SECONDARY) {
					TextInputDialog dialog = new TextInputDialog("25 character limit");
					dialog.setTitle("Change title");
					dialog.setHeaderText("Enter to change title");
					dialog.setContentText("Please enter the new title:");
					String result = dialog.showAndWait().get();
					title.setText(result);
					title.setLayoutX(((int) screenBounds.getWidth()/2)-title.getText().length()*8/2);
				}
			}
		});
		
		Text left = new Text("left");
		left.setFont(new Font(12));
		left.setStroke(Color.BLACK);
		left.setTextAlignment(TextAlignment.CENTER);
//		left.layoutXProperty().bind(pane.widthProperty().divide(5.0/2.0).subtract(25));
		left.setLayoutX(((int) screenBounds.getWidth()/2.5)-left.getText().length()*5/2);
		left.layoutYProperty().bind(pane.heightProperty().divide(2.0).subtract(circleL.radiusProperty()).subtract(5));
		left.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getButton() == MouseButton.SECONDARY) {
					TextInputDialog dialog = new TextInputDialog("25 character limit");
					dialog.setTitle("Change text");
					dialog.setHeaderText("Enter to change text");
					dialog.setContentText("Please enter some text:");
					String result = dialog.showAndWait().get();
					left.setText(result);
					left.setLayoutX(((int) screenBounds.getWidth()/2.5)-left.getText().length()*5/2);
				}
			}
		});
		
		Text right = new Text("right");
		right.setFont(new Font(12));
		right.setStroke(Color.BLACK);
		right.setTextAlignment(TextAlignment.CENTER);
//		right.layoutXProperty().bind(pane.widthProperty().divide(5.0/3.0).subtract(25));
		right.setLayoutX(((int) screenBounds.getWidth()/(5.0/3.0))-right.getText().length()*5/2);
		right.layoutYProperty().bind(pane.heightProperty().divide(2.0).subtract(circleL.radiusProperty()).subtract(5));
		right.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getButton() == MouseButton.SECONDARY) {
					TextInputDialog dialog = new TextInputDialog("25 character limit");
					dialog.setTitle("Change text");
					dialog.setHeaderText("Enter to change text");
					dialog.setContentText("Please enter some text:");
					String result = dialog.showAndWait().get();
					right.setText(result);
					right.setLayoutX(((int) screenBounds.getWidth()/(5.0/3.0))-right.getText().length()*5/2);
				}
			}
		});
		
		//Right circle color picker label
		Text cpR = new Text(": Right circle color");
		cpR.setFont(new Font(12));
		cpR.setStroke(Color.BLACK);
		cpR.setTextAlignment(TextAlignment.CENTER);
		cpR.layoutXProperty().bind(cp1.layoutXProperty().add(125));
		cpR.layoutYProperty().bind(cp1.layoutYProperty().add(15));
		
		//Right circle color picker label
		Text cpL = new Text(": Left circle color");
		cpL.setFont(new Font(12));
		cpL.setStroke(Color.BLACK);
		cpL.setTextAlignment(TextAlignment.CENTER);
		cpL.layoutXProperty().bind(cp2.layoutXProperty().add(125));
		cpL.layoutYProperty().bind(cp2.layoutYProperty().add(15));
		
				
		//Adds items to the window
		pane.getChildren().add(circleR);
		pane.getChildren().add(circleL);
		pane.getChildren().addAll(cp1, cp2);
		pane.getChildren().add(textAdder);
		
		//Adds titles to window
		pane.getChildren().add(title);
		pane.getChildren().add(left);
		pane.getChildren().add(right);
		pane.getChildren().addAll(cpR, cpL);
		
		//Center line - use when you want to center nodes
//		Line center = new Line(screenBounds.getWidth()/(5.0/3.0), 0, screenBounds.getWidth()/(5.0/3.0), 1000);
//		pane.getChildren().add(center);
		
		//resizes to maximize screen
		pane.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (!stage.isFullScreen()) {stage.setMaximized(true);}
			}
		});
		
		root.getChildren().addAll(pane);
		Scene scene2 = new Scene(root);
		stage.setTitle("Venn Application Demo");
		stage.setScene(scene2);
		stage.setResizable(false);
		stage.setMaximized(true);
		stage.show();
		
	}
	
	//Class for recording mouse distance
	static class Delta	{
		double x;
		double y;
	}
	
	//Class for recording
	static class Record {
		static int numBoxes;
		//Default text box color stored in this circle
//		static String textBox = null;
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}

}
