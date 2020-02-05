package javaFX;

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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextInputDialog;
									
public class VennBase extends Application	 {
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override 
	public void start(Stage stage) {
		
		//sets window
		stage.setMaximized(true);
		StackPane root = new StackPane();
		Pane pane = new Pane();
		
		//Get primary screen bounds
	    Rectangle2D screenBounds = Screen.getPrimary().getBounds();
		
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
		
		
		
		
//		//A second text box
//				Button box2 = new Button("Another text here 12345678");
////				box2.layoutXProperty().bind(pane.widthProperty().divide(30.0/13.0));
////				box2.layoutYProperty().bind(pane.heightProperty().divide(2.0));
//				box2.prefWidthProperty().bind(pane.widthProperty().divide(8.0));
//				box2.prefHeightProperty().bind(pane.heightProperty().divide(15.0));
//				box2.setLayoutX(screenBounds.getWidth()-(screenBounds.getWidth()/8));
//				box2.setLayoutY(screenBounds.getHeight()/15);
//				box2.setStyle("-fx-background-color: #80b380");
//				box2.setOnMouseClicked(new EventHandler<MouseEvent>() {
//					@Override
//					public void handle(MouseEvent event) {
//						if (event.getButton() == MouseButton.SECONDARY) {
//							TextInputDialog dialog = new TextInputDialog("25 character limit");
//							dialog.setTitle("Change text");
//							dialog.setHeaderText("Enter to change text");
//							dialog.setContentText("Please enter some text:");
//							String result = dialog.showAndWait().get();
//							while (result.length()>25) {
//								dialog.setHeaderText("Character limit is 25!");
//								result = dialog.showAndWait().get();
//								}
//							box2.setText(result);
//						}
//					}
//				});
//				//changes cursor when moving text box and records distance moved
//				box2.setOnMousePressed(new EventHandler<MouseEvent>() {
//		            @Override
//		            public void handle(MouseEvent mouseEvent) {
//		                box2.setCursor(Cursor.MOVE);
//		                drag.x = box2.getLayoutX() - mouseEvent.getSceneX();
//		                drag.y = box2.getLayoutY() - mouseEvent.getSceneY();
//		            }
//		        });
//				//Moves text box when dragged
//				box2.setOnMouseDragged(new EventHandler<MouseEvent>() {
//					@Override
//					public void handle(MouseEvent mouseEvent) {
//						box2.setLayoutX(mouseEvent.getSceneX() + drag.x);
//						box2.setLayoutY(mouseEvent.getSceneY() + drag.y);
//					}
//				});
		
		
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
		textAdder.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				textAdder.setText("Not implemented yet..");
				
				
				//Text box prototype - 1st implementation
				Button box = new Button("Insert text here 123456789");
				box.prefWidthProperty().bind(pane.widthProperty().divide(8.0));
				box.prefHeightProperty().bind(pane.heightProperty().divide(15.0));
				box.setLayoutX(screenBounds.getWidth()-(screenBounds.getWidth()/8));
																				//		box1.layoutXProperty().bind(pane.widthProperty().divide(30.0/13.0));
																				//		box1.layoutYProperty().bind(pane.heightProperty().divide(2.0));

				box.setStyle("-fx-background-color: #80b380");

				//Change contents
				box.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						if (event.getButton() == MouseButton.SECONDARY) {
							TextInputDialog dialog = new TextInputDialog(box.getText());
							dialog.setTitle("Change text");
							dialog.setHeaderText("Enter to change text");
							dialog.setContentText("25 character limit");
							String result = dialog.showAndWait().get();
							while (result.length()>25) {
								dialog.setHeaderText("Character limit is 25!");
								result = dialog.showAndWait().get();
								}
							box.setText(result);
						}
					}
				});


				//changes cursor when moving text box and records distance moved
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
					}
				});

				pane.getChildren().add(box);
				
			}

		});
		
		
		//Texts
		Text title = new Text("Title");
		title.setFont(new Font(20));
		title.setStroke(Color.BLACK);
		title.layoutXProperty().bind(pane.widthProperty().divide(2.0));
		title.setLayoutY(25);
//		title.setTextAlignment(TextAlignment.CENTER);
		title.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getButton() == MouseButton.SECONDARY) {
					TextInputDialog dialog = new TextInputDialog("25 character limit");
					dialog.setTitle("Change title");
					dialog.setHeaderText("Enter to change title");
					dialog.setContentText("Please enter the new title:");
					String result = dialog.showAndWait().get();
					while (result.length()>25) {
						dialog.setHeaderText(title.getText());
						result = dialog.showAndWait().get();
						}
					title.setText(result);
				}
			}
		});
		
		Text left = new Text("left");
		left.setFont(new Font(12));
		left.setStroke(Color.BLACK);
		left.setTextAlignment(TextAlignment.CENTER);
		left.layoutXProperty().bind(pane.widthProperty().divide(5.0/2.0).subtract(25));
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
					while (result.length()>25) {
						dialog.setHeaderText(left.getText());
						result = dialog.showAndWait().get();
						}
					left.setText(result);
				}
			}
		});
		
		Text right = new Text("right");
		right.setFont(new Font(12));
		right.setStroke(Color.BLACK);
		right.setTextAlignment(TextAlignment.CENTER);
		right.layoutXProperty().bind(pane.widthProperty().divide(5.0/3.0).subtract(25));
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
					while (result.length()>25) {
						dialog.setHeaderText(right.getText());
						result = dialog.showAndWait().get();
						}
					right.setText(result);
				}
			}
		});
		
				
		//Adds items to the window
		pane.getChildren().add(circleR);
		pane.getChildren().add(circleL);
//		pane.getChildren().add(box1);
//		pane.getChildren().add(box2);
		pane.getChildren().addAll(cp1, cp2);
		pane.getChildren().add(slider);
		pane.getChildren().add(textAdder);
		
		//Adds titles to window
		pane.getChildren().add(title);
		pane.getChildren().add(left);
		pane.getChildren().add(right);
		
		
		root.getChildren().addAll(pane);
		Scene scene2 = new Scene(root);
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
