package application;
	
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.event.*;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.layout.FlowPane;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextInputDialog;
									
@SuppressWarnings("unused")
public class Main extends Application	 {
	
	
	static boolean debug = false;
	@SuppressWarnings({ "unchecked", "rawtypes" })
	
	//Main method
	public static void main(String[] args) {
		launch(args);   //launch method calls start method
	}
	
	
	@Override 
	public void start(Stage stage) {    //Actual implementation method
		
		//Screen-shot implementation
		FlowPane flow = new FlowPane();
		ImageView display = new ImageView();
		Button capture = new Button("Take Screenshot of Venn Diagram!");
		flow.getChildren().addAll(display, capture);
		
		capture.setOnAction(event -> {
			try {
				Robot robot = new Robot();
				Rectangle rect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
				BufferedImage image = robot.createScreenCapture(rect);
				Image myImage = SwingFXUtils.toFXImage(image, null);
				ImageIO.write(image, "jpg", new File("VennScreenShot.jpg"));
				//display.setImage(myImage);
			} catch(Exception e) {
				e.printStackTrace();
			}
		});
		
		
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
		circleR.centerXProperty().bind(pane.widthProperty().divide(5.0/3.0));
		circleR.centerYProperty().bind(pane.heightProperty().divide(2.0));
		circleR.radiusProperty().bind(pane.widthProperty().divide(5.0));
		circleR.setStroke(Color.BLUE);
		circleR.setFill(blue);
		
		//Left venn circle
		Circle circleL= new Circle();
		circleL.centerXProperty().bind(pane.widthProperty().divide(5.0/2.0));
		circleL.centerYProperty().bind(pane.heightProperty().divide(2.0));
		circleL.radiusProperty().bind(pane.widthProperty().divide(5.0));
		circleL.setStroke(Color.RED);
		circleL.setFill(red);
		
		
		//color picker setup ------------------------------------------------------------------------------------------------------------
		final ColorPicker cp1 = new ColorPicker(Color.BLUE);
		final ColorPicker cp2 = new ColorPicker(Color.RED);
		final ColorPicker cp3 = new ColorPicker(Color.GREEN);
		cp1.layoutYProperty().bind(pane.heightProperty().subtract(25));
		cp2.layoutYProperty().bind(pane.heightProperty().subtract(50));
		cp3.layoutYProperty().bind(pane.heightProperty().subtract(75));
		cp1.prefWidthProperty().bind(pane.widthProperty().multiply(10.0/100.0));
		cp2.prefWidthProperty().bind(pane.widthProperty().multiply(10.0/100.0));
		
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
		
		
		//text box adder ------------------------------------------------------------------------------------------------------------
		Button textAdder = new Button("Add new text box");		
		textAdder.prefWidthProperty().bind(pane.widthProperty().multiply(20.0/100.0));
		textAdder.prefHeightProperty().bind(pane.heightProperty().multiply(10.0/100.0));
		textAdder.layoutXProperty().bind(pane.widthProperty().multiply(80.0/100.0));
		textAdder.layoutYProperty().bind(pane.heightProperty().multiply(90.0/100.0));
		
		textAdder.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {		
				int stackable = (int) (pane.getHeight() / (textAdder.getHeight()-10));
				
				//Text box properties
				
				Button box = new Button("New Text Box");
				box.prefWidthProperty().bind(textAdder.widthProperty().multiply(75.0/100.0));
				box.prefHeightProperty().bind(textAdder.heightProperty().subtract(25));
				box.setLayoutX(textAdder.getLayoutX());
				box.setLayoutY(textAdder.getLayoutY()-((textAdder.getHeight()-15)*(Record.numBoxes%stackable)+(textAdder.getHeight()-15)));
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
					}
				});
				
				box.setOnMouseEntered(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent mouseEvent) {
						if (Main.debug) {box.setText((int)box.getLayoutX()+", "+(int)box.getLayoutY());}
					}
				});
				
				pane.getChildren().add(box);
				
			}

		});
		
		
		//Texts ------------------------------------------------------------------------------------------------------------
		Text title = new Text("Title");
		title.setFont(new Font(20));
		title.setStroke(Color.BLACK);
		//title.getText().length()*4 : text half length
		title.layoutXProperty().bind(pane.widthProperty().divide(2).subtract(title.getText().length()*4));
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
					title.layoutXProperty().bind(pane.widthProperty().divide(2).subtract(title.getText().length()*4));
				}
			}
		});
		
		Text left = new Text("left");
		left.setFont(new Font(12));
		left.setStroke(Color.BLACK);
		left.setTextAlignment(TextAlignment.CENTER);
		//left.getText().length()*5/2 : text half length
		left.layoutXProperty().bind(pane.widthProperty().divide(5.0/2.0).subtract(left.getText().length()*5/2));
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
					left.layoutXProperty().bind(pane.widthProperty().divide(5.0/2.0).subtract(left.getText().length()*5/2));
				}
			}
		});
		
		Text right = new Text("right");
		right.setFont(new Font(12));
		right.setStroke(Color.BLACK);
		right.setTextAlignment(TextAlignment.CENTER);
		//right.getText().length()*5/2 : text half length
		right.layoutXProperty().bind(pane.widthProperty().divide(5.0/3.0).subtract(right.getText().length()*5/2));
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
					right.layoutXProperty().bind(pane.widthProperty().divide(5.0/3.0).subtract(right.getText().length()*5/2));
				}
			}
		});
		
		//Right circle color picker label
		Text cpR = new Text(" : Right circle color");
		cpR.setFont(new Font(12));
		cpR.setStroke(Color.BLACK);
		cpR.layoutXProperty().bind(cp1.widthProperty());
		cpR.layoutYProperty().bind(cp1.layoutYProperty().add(15));
		
		//Right circle color picker label
		Text cpL = new Text(" : Left circle color");
		cpL.setFont(new Font(12));
		cpL.setStroke(Color.BLACK);
		cpL.setTextAlignment(TextAlignment.CENTER);
		cpL.layoutXProperty().bind(cp2.widthProperty());
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
		
		
		//debug data -------------------------------------------------------------------------------------------------------
		Text screen_bounds = new Text();
		screen_bounds.setLayoutY(25);
		Text pane_bounds = new Text();
		pane_bounds.setLayoutY(50);
		Text cp1data = new Text();
		cp1data.setLayoutY(75);
		Text cp2data = new Text();
		cp2data.setLayoutY(100);
		Text textAdderData = new Text();
		textAdderData.setLayoutY(125);
		Text titleData = new Text();
		titleData.setLayoutY(150);
		Text rightData = new Text();
		rightData.setLayoutY(175);
		Text leftData = new Text();
		leftData.setLayoutY(200);
		Text cpLData = new Text();
		cpLData.setLayoutY(225);
		Text cpRData = new Text();
		cpRData.setLayoutY(250);
		pane.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.F3) {
					if (Main.debug) {
						Main.debug=false;
						pane.getChildren().removeAll(screen_bounds, pane_bounds, cp1data, cp2data, textAdderData, titleData, rightData, leftData, cpLData, cpRData);
					}
					else {
						screen_bounds.setText("screenBounds: "+screenBounds.getWidth()+", "+screenBounds.getHeight());
						pane_bounds.setText("pane bounds: "+pane.widthProperty().doubleValue()+", "+pane.heightProperty().doubleValue());
						cp1data.setText("cp1: "+cp1.getLayoutX()+", "+cp1.getLayoutY()+"; wh: "+cp1.getPrefWidth()+", "+cp1.getPrefHeight());
						cp2data.setText("cp2: "+cp2.getLayoutX()+", "+cp2.getLayoutY()+"; wh: "+cp2.getPrefWidth()+", "+cp2.getPrefHeight());
						textAdderData.setText("textAdder: "+(int)textAdder.getLayoutX()+", "+(int)textAdder.getLayoutY()+"; wh: "+(int)textAdder.getPrefWidth()+", "+(int)textAdder.getPrefHeight());
						titleData.setText("title: "+title.getLayoutX()+", "+title.getLayoutY());
						rightData.setText("right: "+right.getLayoutX()+", "+right.getLayoutY());
						leftData.setText("left: "+left.getLayoutX()+", "+left.getLayoutY());
						cpLData.setText("cpL: "+cpL.getLayoutX()+", "+cpL.getLayoutY());
						cpRData.setText("cpR: "+cpR.getLayoutX()+", "+cpR.getLayoutY());
						
						Main.debug=true;
						pane.getChildren().addAll(screen_bounds, pane_bounds, cp1data, cp2data, textAdderData, titleData, rightData, leftData, cpLData, cpRData);
					}
				}
			}
        });
		
		//Center line - use when you want to center nodes
//		Line center = new Line(screenBounds.getWidth()/(5.0/3.0), 0, screenBounds.getWidth()/(5.0/3.0), 1000);
//		pane.getChildren().add(center);
		
		
		pane.getChildren().add(flow); //Adding screenshot feature to pane
		
		root.getChildren().addAll(pane);
		Scene scene2 = new Scene(root);
		stage.setTitle("Venn Application Demo");
		stage.setScene(scene2);
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
	
	

}
