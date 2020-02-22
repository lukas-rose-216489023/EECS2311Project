package venn;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.TextField;
import java.util.Optional;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
									
@SuppressWarnings("unused")
public class VennBase extends Application	 {
	
	static boolean debug = false;
	static boolean anchor = false;
	ListView<String> listView;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override 
	public void start(Stage stage) {

		//Get primary screen bounds
	    Rectangle2D screenBounds = Screen.getPrimary().getBounds();
	    
		//sets window
		StackPane root = new StackPane();
		Pane pane = new Pane();
		root.getChildren().addAll(pane);
		stage.setTitle("Venn Application");
		Scene scene = new Scene(root, screenBounds.getWidth()-100, screenBounds.getHeight()-100);
		stage.setScene(scene);
		stage.setMaximized(true);
		stage.show();
		
		
		//Custom colors
		Color blue = new Color(Color.BLUE.getRed(), Color.BLUE.getGreen(), Color.BLUE.getBlue(), 0.5);		
		Color red = new Color(Color.RED.getRed(), Color.RED.getGreen(), Color.RED.getBlue(), 0.5);
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
		
		//Anchor points -----------------------------------------------------------------------------------------------------------------
		Anchor leftCircle = new Anchor();
		Anchor rightCircle = new Anchor();
		Anchor intersection = new Anchor();
		
		double paneWidth = screenBounds.getWidth();
		double paneHeight = screenBounds.getHeight()-62;
		double cirlx = screenBounds.getWidth()/(5.0/2.0);
		double cirly = (screenBounds.getHeight()-62)/2;
		double radius = screenBounds.getWidth()/5.0;
		
		Points p = new Points();
		
		p.l1 = new Point(cirlx-(radius*0.51172)-3, cirly-(radius*0.8359));
		p.l2 = new Point(cirlx-(radius*0.65625), p.l1.yValue+paneHeight*0.0608);
		p.l3 = new Point(cirlx-(radius*0.78125), p.l2.yValue+paneHeight*0.0608);
		p.l4 = new Point(cirlx-(radius*0.87890), p.l3.yValue+paneHeight*0.0608);
		p.l5 = new Point(cirlx-(radius*0.92969), p.l4.yValue+paneHeight*0.0608);
		p.l6 = new Point(cirlx-(radius*0.96875), p.l5.yValue+paneHeight*0.0608);
		p.l7 = new Point(cirlx-(radius*0.92969), p.l6.yValue+paneHeight*0.0608);
		p.l8 = new Point(cirlx-(radius*0.87890), p.l7.yValue+paneHeight*0.0608);
		p.l9 = new Point(cirlx-(radius*0.78125), p.l8.yValue+paneHeight*0.0608);
		p.l10 = new Point(cirlx-(radius*0.65625), p.l9.yValue+paneHeight*0.0608);
		p.l11 = new Point(cirlx-(radius*0.51172), p.l10.yValue+paneHeight*0.0608);
		
		p.i1 = new Point(paneWidth*0.41875, (paneHeight*0.33739));
		p.i2 = new Point(paneWidth*0.41875, p.i1.yValue+paneHeight*0.05471);
		p.i3 = new Point(paneWidth*0.41875, p.i2.yValue+paneHeight*0.05471);
		p.i4 = new Point(paneWidth*0.41875, p.i3.yValue+paneHeight*0.05471);
		p.i5 = new Point(paneWidth*0.41875, p.i4.yValue+paneHeight*0.05471);
		p.i6 = new Point(paneWidth*0.41875, p.i5.yValue+paneHeight*0.05471);
		
		p.r1 = new Point(p.l1.xValue+paneWidth*0.23984+10, p.l1.yValue);
		p.r2 = new Point(p.l2.xValue+paneWidth*0.29609+5, p.l2.yValue);
		p.r3 = new Point(p.l3.xValue+paneWidth*0.34375, p.l3.yValue);
		p.r4 = new Point(p.l4.xValue+paneWidth*0.378125, p.l4.yValue);
		p.r5 = new Point(p.l5.xValue+paneWidth*0.40156, p.l5.yValue);
		p.r6 = new Point(p.l6.xValue+paneWidth*0.42109, p.l6.yValue);
		p.r7 = new Point(p.l7.xValue+paneWidth*0.40156, p.l7.yValue);
		p.r8 = new Point(p.l8.xValue+paneWidth*0.378125, p.l8.yValue);
		p.r9 = new Point(p.l9.xValue+paneWidth*0.34375, p.l9.yValue);
		p.r10 = new Point(p.l10.xValue+paneWidth*0.29609, p.l10.yValue);
		p.r11 = new Point(p.l11.xValue+paneWidth*0.23984, p.l11.yValue);
		
		leftCircle.addPoint(p.l1);
		leftCircle.addPoint(p.l2);
		leftCircle.addPoint(p.l3);
		leftCircle.addPoint(p.l4);
		leftCircle.addPoint(p.l5);
		leftCircle.addPoint(p.l6);
		leftCircle.addPoint(p.l7);
		leftCircle.addPoint(p.l8);
		leftCircle.addPoint(p.l9);
		leftCircle.addPoint(p.l10);
		leftCircle.addPoint(p.l11);
		
		intersection.addPoint(p.i1);
		intersection.addPoint(p.i2);
		intersection.addPoint(p.i3);
		intersection.addPoint(p.i4);
		intersection.addPoint(p.i5);
		intersection.addPoint(p.i6);
		
		rightCircle.addPoint(p.r1);
		rightCircle.addPoint(p.r2);
		rightCircle.addPoint(p.r3);
		rightCircle.addPoint(p.r4);
		rightCircle.addPoint(p.r5);
		rightCircle.addPoint(p.r6);
		rightCircle.addPoint(p.r7);
		rightCircle.addPoint(p.r8);
		rightCircle.addPoint(p.r9);
		rightCircle.addPoint(p.r10);
		rightCircle.addPoint(p.r11);
				
		//color picker setup ------------------------------------------------------------------------------------------------------------
		final ColorPicker cp1 = new ColorPicker(Color.BLUE);
		final ColorPicker cp2 = new ColorPicker(Color.RED);
		final ColorPicker cp3 = new ColorPicker(Color.GREEN);
		cp1.layoutYProperty().bind(pane.heightProperty().subtract(25));
		cp2.layoutYProperty().bind(pane.heightProperty().subtract(50));
		cp1.prefWidthProperty().bind(pane.widthProperty().multiply(10.0/100.0));
		cp2.prefWidthProperty().bind(pane.widthProperty().multiply(10.0/100.0));
		cp1.layoutXProperty().bind(pane.widthProperty().multiply(90.0/100.0));
		cp2.layoutXProperty().bind(pane.widthProperty().multiply(90.0/100.0));
		
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
		
//		cp3.setOnAction(new EventHandler() {
//			@Override
//			public void handle(javafx.event.Event event) {
//				String textBo = Integer.toHexString(cp3.getValue().hashCode());//String.format("#%02X%02X%02X", ((int)cp3.getValue().getRed())*255, ((int)cp3.getValue().getGreen())*255, ((int)cp3.getValue().getBlue())*255);
////				textBo = "-fx-background-color: #" + textBo;
////				Record.textBox = textBo;
//				}
//        });
		
		
		//Anchor option button
		Button anchorOption = new Button("Anchoring off");
		anchorOption.prefWidthProperty().bind(pane.widthProperty().multiply(15.0/100.0));
		anchorOption.prefHeightProperty().bind(pane.heightProperty().multiply(5.0/100.0));
		anchorOption.layoutXProperty().bind(pane.widthProperty().multiply(85.0/100.0));
		anchorOption.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (VennBase.anchor) {VennBase.anchor = false;anchorOption.setText("Anchoring off");}
				else {VennBase.anchor = true;anchorOption.setText("Anchoring on");}
			}
		});
		
		
		//text box adder ------------------------------------------------------------------------------------------------------------
		Button textAdder = new Button("Add new text box");		
		textAdder.prefWidthProperty().bind(pane.widthProperty().multiply(20.0/100.0));
		textAdder.prefHeightProperty().bind(pane.heightProperty().multiply(10.0/100.0));
		textAdder.layoutXProperty().bind(pane.widthProperty().multiply(0));
		textAdder.layoutYProperty().bind(pane.heightProperty().multiply(0));
		
		textAdder.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getButton() == MouseButton.PRIMARY) {
					int stackable = (int) (pane.getHeight() / (textAdder.getHeight()-10)) -2;


					//Text box properties

					Button box = new Button("New Text Box");
//					box.prefWidthProperty().bind(textAdder.widthProperty().multiply(75.0/100.0));
					box.prefWidthProperty().bind(circleL.radiusProperty().subtract(50));
					box.prefHeightProperty().bind(pane.heightProperty().multiply(5.0/100.0));
					box.setLayoutX(15);
					box.setLayoutY(5+(textAdder.getPrefHeight()*2) + ((textAdder.getPrefHeight()-15)*(Record.numBoxes%stackable)));
					Record.numBoxes++;

					//				box.setStyle("-fx-background-color: "+Record.textBox);
					box.setStyle("-fx-background-color: #80b380");

					//Change contents
					box.setOnMouseClicked(new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent event) {
							if (event.getButton().equals(MouseButton.SECONDARY)) {
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

					//variables for use in resize detection and position detection
					Record record = new Record();
					record.percentX = box.getLayoutX() / pane.getWidth();
					record.percentY = box.getLayoutY() / pane.getHeight();
					record.inCircleL = false;
					record.inCircleR = false;

					box.setOnMousePressed(new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent mouseEvent) {
							box.setCursor(Cursor.MOVE);
							record.x = box.getLayoutX() - mouseEvent.getSceneX();
							record.y = box.getLayoutY() - mouseEvent.getSceneY();
						}
					});

					//Moves text box when dragged 
					box.setOnMouseDragged(new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent mouseEvent) {
							box.setLayoutX(mouseEvent.getSceneX() + record.x);
							box.setLayoutY(mouseEvent.getSceneY() + record.y);
							record.percentX = box.getLayoutX() / pane.getWidth();
							record.percentY = box.getLayoutY() / pane.getHeight();
						}
					});

					//Anchoring
					box.setOnMouseReleased(new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent mouseEvent) {
							if (VennBase.anchor) {
								//Within circle formula => x^2-2xa + y^2-2yb < r^2-a^2-b^2 where a is horizontal distance from 0 to mid circle and b is vertical distance from 0 to mid circle
								double x2 = box.getLayoutX()*box.getLayoutX();
								double y2 = box.getLayoutY()*box.getLayoutY();
								double La = circleL.getCenterX();
								double Lb = circleL.getCenterY();
								double Ra = circleR.getCenterX();
								double Rb = circleR.getCenterY();
								double Rr2 = circleR.getRadius()*circleR.getRadius();
								double Lr2 = Rr2;

								if ((x2-2*box.getLayoutX()*La)+(y2-2*box.getLayoutY()*Lb) < Lr2-La*La-Lb*Lb) {record.inCircleL=true;}
								else {record.inCircleL=false;}
								if ((x2-2*box.getLayoutX()*Ra)+(y2-2*box.getLayoutY()*Rb) < Rr2-Ra*Ra-Rb*Rb) {record.inCircleR=true;}
								else {record.inCircleR=false;}

								if (record.inCircleL && record.inCircleR) {
									//box x and y are closest anchor points in the intersection
									if (debug) {box.setText("Currently in intersection");}
									box.setLayoutX(intersection.closest(box.getLayoutY()).xValue);
									box.setLayoutY(intersection.closest(box.getLayoutY()).yValue);
									record.percentX = box.getLayoutX() / pane.getWidth();
									record.percentY = box.getLayoutY() / pane.getHeight();
								}
								else if (record.inCircleL) {
									//box x and y are closest anchor points in the left circle
									if (debug) {box.setText("Currently in left circle");}
									box.setLayoutX(leftCircle.closest(box.getLayoutY()).xValue);
									box.setLayoutY(leftCircle.closest(box.getLayoutY()).yValue);
									record.percentX = box.getLayoutX() / pane.getWidth();
									record.percentY = box.getLayoutY() / pane.getHeight();
								}
								else if (record.inCircleR) {
									//box x and y are closest anchor points in the right circle
									if (debug) {box.setText("Currently in right circle");}
									box.setLayoutX(rightCircle.closest(box.getLayoutY()).xValue);
									box.setLayoutY(rightCircle.closest(box.getLayoutY()).yValue);
									record.percentX = box.getLayoutX() / pane.getWidth();
									record.percentY = box.getLayoutY() / pane.getHeight();
								}
							}
						}
					});

					box.setOnMouseEntered(new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent mouseEvent) {
							if (VennBase.debug) {box.setText((int)box.getLayoutX()+", "+(int)box.getLayoutY());}
						}
					});

					//resize detection:
					pane.widthProperty().addListener(new ChangeListener<Number>() {
						@Override
						public void changed(ObservableValue<? extends Number> observable, Number oldX, Number newX) {
							////						System.out.println("oldX = "+ oldX+", newX = "+newX);
							//						double old = box.getLayoutX();
							//						box.setLayoutX(old + (newX.doubleValue() - oldX.doubleValue())/2.5);
							////						System.out.println("(newX-oldX)/2 = "+ ((newX.doubleValue()-oldX.doubleValue())/2.0)+"; old="+old+" ==> new="+box.getLayoutX());

							box.setLayoutX(pane.getWidth() * record.percentX);

							p.l1 = new Point(circleL.getLayoutX()-(circleL.getRadius()*0.51172), circleL.getLayoutY()-(circleL.getRadius()*0.8359));
							p.l2 = new Point(circleL.getLayoutX()-(circleL.getRadius()*0.65625), p.l1.yValue+pane.getHeight()*0.0608);
							p.l3 = new Point(circleL.getLayoutX()-(circleL.getRadius()*0.78125), p.l2.yValue+pane.getHeight()*0.0608);
							p.l4 = new Point(circleL.getLayoutX()-(circleL.getRadius()*0.87890), p.l3.yValue+pane.getHeight()*0.0608);
							p.l5 = new Point(circleL.getLayoutX()-(circleL.getRadius()*0.92969), p.l4.yValue+pane.getHeight()*0.0608);
							p.l6 = new Point(circleL.getLayoutX()-(circleL.getRadius()*0.96875), p.l5.yValue+pane.getHeight()*0.0608);
							p.l7 = new Point(circleL.getLayoutX()-(circleL.getRadius()*0.92969), p.l6.yValue+pane.getHeight()*0.0608);
							p.l8 = new Point(circleL.getLayoutX()-(circleL.getRadius()*0.87890), p.l7.yValue+pane.getHeight()*0.0608);
							p.l9 = new Point(circleL.getLayoutX()-(circleL.getRadius()*0.78125), p.l8.yValue+pane.getHeight()*0.0608);
							p.l10 = new Point(circleL.getLayoutX()-(circleL.getRadius()*0.65625), p.l9.yValue+pane.getHeight()*0.0608);
							p.l11 = new Point(circleL.getLayoutX()-(circleL.getRadius()*0.51172), p.l10.yValue+pane.getHeight()*0.0608);

							p.i1 = new Point((pane.getWidth()*0.41875), (pane.getHeight()*0.33739));
							p.i2 = new Point(pane.getWidth()*0.41875, p.i1.yValue+pane.getHeight()*0.05471);
							p.i3 = new Point(pane.getWidth()*0.41875, p.i2.yValue+pane.getHeight()*0.05471);
							p.i4 = new Point(pane.getWidth()*0.41875, p.i3.yValue+pane.getHeight()*0.05471);
							p.i5 = new Point(pane.getWidth()*0.41875, p.i4.yValue+pane.getHeight()*0.05471);
							p.i6 = new Point(pane.getWidth()*0.41875, p.i5.yValue+pane.getHeight()*0.05471);

							p.r1 = new Point(p.l1.xValue+pane.getWidth()*0.23984, p.l1.yValue);
							p.r2 = new Point(p.l2.xValue+pane.getWidth()*0.29609, p.l2.yValue);
							p.r3 = new Point(p.l3.xValue+pane.getWidth()*0.34375, p.l3.yValue);
							p.r4 = new Point(p.l4.xValue+pane.getWidth()*0.378125, p.l4.yValue);
							p.r5 = new Point(p.l5.xValue+pane.getWidth()*0.40156, p.l5.yValue);
							p.r6 = new Point(p.l6.xValue+pane.getWidth()*0.42109, p.l6.yValue);
							p.r7 = new Point(p.l7.xValue+pane.getWidth()*0.40156, p.l7.yValue);
							p.r8 = new Point(p.l8.xValue+pane.getWidth()*0.378125, p.l8.yValue);
							p.r9 = new Point(p.l9.xValue+pane.getWidth()*0.34375, p.l9.yValue);
							p.r10 = new Point(p.l10.xValue+pane.getWidth()*0.29609, p.l10.yValue);
							p.r11 = new Point(p.l11.xValue+pane.getWidth()*0.23984, p.l11.yValue);

						}
					});

					pane.heightProperty().addListener(new ChangeListener<Number>() {
						@Override
						public void changed(ObservableValue<? extends Number> observable, Number oldY, Number newY) {
							////						System.out.println("oldY = "+ oldY+", newY = "+newY);
							//						double old = box.getLayoutY();
							//						box.setLayoutY(old + (newY.doubleValue() - oldY.doubleValue())/2.5);
							////						System.out.println("(newY-oldY)/2 = "+ ((newY.doubleValue()-oldY.doubleValue())/2.0)+"; old="+old+" ==> new="+box.getLayoutY());

							box.setLayoutY(pane.getHeight() * record.percentY);						
						}
					});

					pane.getChildren().add(box);

				}
			}
		});
		
		//multiple text box adder ------------------------------------------------------------------------------------------------------------
		Button multAdder = new Button("Add mulitple new text boxes");		
		multAdder.prefWidthProperty().bind(pane.widthProperty().multiply(20.0/100.0));
		multAdder.prefHeightProperty().bind(pane.heightProperty().multiply(10.0/100.0));
		multAdder.layoutXProperty().bind(pane.widthProperty().multiply(0));
		multAdder.layoutYProperty().bind(pane.heightProperty().multiply(10.0/100.0));
		multAdder.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				final Stage dialog = new Stage();
				dialog.initModality(Modality.APPLICATION_MODAL);
				dialog.initOwner(stage);
				VBox layout = new VBox(20);

				listView = new ListView<>(FXCollections.observableArrayList());
				listView.setEditable(true);

				listView.setCellFactory(TextFieldListCell.forListView());		

				listView.setOnEditCommit(new EventHandler<ListView.EditEvent<String>>() {
					@Override
					public void handle(ListView.EditEvent<String> t) {
						listView.getItems().set(t.getIndex(), t.getNewValue());
					}

				});

				listView.setOnEditCancel(new EventHandler<ListView.EditEvent<String>>() {
					@Override
					public void handle(ListView.EditEvent<String> t) {
						//System.out.println("setOnEditCancel");
					}
				});
				listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

				//add cell to list button
				Button addText = new Button("Add");
				addText.setLayoutX(screenBounds.getMinX());
				addText.setLayoutY(screenBounds.getMaxY());

				addText.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						String c = new String("Enter Text");
						listView.getItems().add(listView.getItems().size(), c);
						listView.scrollTo(c);
						listView.edit(listView.getItems().size() - 1);
					}

				});

				//delete cell from list button
				Button deleteText = new Button("Delete Selected");

				deleteText.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						final int selectedIdx = listView.getSelectionModel().getSelectedIndex();
						if (selectedIdx != -1) {
							String itemToRemove = listView.getSelectionModel().getSelectedItem();

							final int newSelectedIdx =
									(selectedIdx == listView.getItems().size() - 1)
									? selectedIdx - 1
											: selectedIdx;

							listView.getItems().remove(selectedIdx);
							listView.getSelectionModel().select(newSelectedIdx);
						}
					}
				});

				//make all cells text boxes button
				Button finish = new Button("Finish");
				finish.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						ObservableList<String> topics;
						String list= "";
						topics = listView.getSelectionModel().getSelectedItems();

						for (int i = 0; i < topics.size(); i++) {
							int stackable = (int) (pane.getHeight() / (textAdder.getHeight()-10)) -1;


							//Text box properties

							Button box = new Button(topics.get(i));
//		            		box.prefWidthProperty().bind(textAdder.widthProperty().multiply(75.0/100.0));
							box.prefWidthProperty().bind(circleL.radiusProperty().subtract(50));
							box.prefHeightProperty().bind(pane.heightProperty().multiply(5.0/100.0));
							box.setLayoutX(15);
							box.setLayoutY(5+(textAdder.getPrefHeight()*2) + ((textAdder.getPrefHeight()-15)*(Record.numBoxes%stackable)));
							Record.numBoxes++;

//		            		box.setStyle("-fx-background-color: "+Record.textBox);
							box.setStyle("-fx-background-color: #80b380");

							//Change contents
							box.setOnMouseClicked(new EventHandler<MouseEvent>() {
								@Override
								public void handle(MouseEvent event) {
									if (event.getButton().equals(MouseButton.SECONDARY)) {
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
							//variables for use in resize detection and position detection
							Record record = new Record();
							record.percentX = box.getLayoutX() / pane.getWidth();
							record.percentY = box.getLayoutY() / pane.getHeight();
							record.inCircleL = false;
							record.inCircleR = false;

							box.setOnMousePressed(new EventHandler<MouseEvent>() {
								@Override
								public void handle(MouseEvent mouseEvent) {
									box.setCursor(Cursor.MOVE);
									record.x = box.getLayoutX() - mouseEvent.getSceneX();
									record.y = box.getLayoutY() - mouseEvent.getSceneY();
								}
							});

							//Moves text box when dragged 
							box.setOnMouseDragged(new EventHandler<MouseEvent>() {
								@Override
								public void handle(MouseEvent mouseEvent) {
									box.setLayoutX(mouseEvent.getSceneX() + record.x);
									box.setLayoutY(mouseEvent.getSceneY() + record.y);
									record.percentX = box.getLayoutX() / pane.getWidth();
									record.percentY = box.getLayoutY() / pane.getHeight();
								}
							});

							//Anchoring
							box.setOnMouseReleased(new EventHandler<MouseEvent>() {
								@Override
								public void handle(MouseEvent mouseEvent) {
									if (VennBase.anchor) {
										//Within circle formula => x^2-2xa + y^2-2yb < r^2-a^2-b^2 where a is horizontal distance from 0 to mid circle and b is vertical distance from 0 to mid circle
										double x2 = box.getLayoutX()*box.getLayoutX();
										double y2 = box.getLayoutY()*box.getLayoutY();
										double La = circleL.getCenterX();
										double Lb = circleL.getCenterY();
										double Ra = circleR.getCenterX();
										double Rb = circleR.getCenterY();
										double Rr2 = circleR.getRadius()*circleR.getRadius();
										double Lr2 = Rr2;
										//						System.out.println("x2+y2="+(x2+y2)+", Lr2="+Lr2+", Rr2="+Rr2);

										if ((x2-2*box.getLayoutX()*La)+(y2-2*box.getLayoutY()*Lb) < Lr2-La*La-Lb*Lb) {record.inCircleL=true;}
										else {record.inCircleL=false;}
										if ((x2-2*box.getLayoutX()*Ra)+(y2-2*box.getLayoutY()*Rb) < Rr2-Ra*Ra-Rb*Rb) {record.inCircleR=true;}
										else {record.inCircleR=false;}

										//						System.out.println("checking...\nL:"+record.inCircleL+", R:"+record.inCircleR);
										if (record.inCircleL && record.inCircleR) {
											//box x and y are closest anchor points in the intersection
											if (debug) {box.setText("Currently in intersection");}
											box.setLayoutX(intersection.closest(box.getLayoutY()).xValue);
											box.setLayoutY(intersection.closest(box.getLayoutY()).yValue);
											record.percentX = box.getLayoutX() / pane.getWidth();
											record.percentY = box.getLayoutY() / pane.getHeight();
										}
										else if (record.inCircleL) {
											//box x and y are closest anchor points in the left circle
											if (debug) {box.setText("Currently in left circle");}
											box.setLayoutX(leftCircle.closest(box.getLayoutY()).xValue);
											box.setLayoutY(leftCircle.closest(box.getLayoutY()).yValue);
											record.percentX = box.getLayoutX() / pane.getWidth();
											record.percentY = box.getLayoutY() / pane.getHeight();
										}
										else if (record.inCircleR) {
											//box x and y are closest anchor points in the right circle
											if (debug) {box.setText("Currently in right circle");}
											box.setLayoutX(rightCircle.closest(box.getLayoutY()).xValue);
											box.setLayoutY(rightCircle.closest(box.getLayoutY()).yValue);
											record.percentX = box.getLayoutX() / pane.getWidth();
											record.percentY = box.getLayoutY() / pane.getHeight();
										}
									}
								}
							});

							box.setOnMouseEntered(new EventHandler<MouseEvent>() {
								@Override
								public void handle(MouseEvent mouseEvent) {
									if (VennBase.debug) {box.setText((int)box.getLayoutX()+", "+(int)box.getLayoutY());}
								}
							});

							//resize detection:
							pane.widthProperty().addListener(new ChangeListener<Number>() {
								@Override
								public void changed(ObservableValue<? extends Number> observable, Number oldX, Number newX) {
									////		            						System.out.println("oldX = "+ oldX+", newX = "+newX);
									//		            						double old = box.getLayoutX();
									//		            						box.setLayoutX(old + (newX.doubleValue() - oldX.doubleValue())/2.5);
									////		            						System.out.println("(newX-oldX)/2 = "+ ((newX.doubleValue()-oldX.doubleValue())/2.0)+"; old="+old+" ==> new="+box.getLayoutX());

									box.setLayoutX(pane.getWidth() * record.percentX);

									p.l1 = new Point(circleL.getLayoutX()-(circleL.getRadius()*0.51172), circleL.getLayoutY()-(circleL.getRadius()*0.8359));
									p.l2 = new Point(circleL.getLayoutX()-(circleL.getRadius()*0.65625), p.l1.yValue+pane.getHeight()*0.0608);
									p.l3 = new Point(circleL.getLayoutX()-(circleL.getRadius()*0.78125), p.l2.yValue+pane.getHeight()*0.0608);
									p.l4 = new Point(circleL.getLayoutX()-(circleL.getRadius()*0.87890), p.l3.yValue+pane.getHeight()*0.0608);
									p.l5 = new Point(circleL.getLayoutX()-(circleL.getRadius()*0.92969), p.l4.yValue+pane.getHeight()*0.0608);
									p.l6 = new Point(circleL.getLayoutX()-(circleL.getRadius()*0.96875), p.l5.yValue+pane.getHeight()*0.0608);
									p.l7 = new Point(circleL.getLayoutX()-(circleL.getRadius()*0.92969), p.l6.yValue+pane.getHeight()*0.0608);
									p.l8 = new Point(circleL.getLayoutX()-(circleL.getRadius()*0.87890), p.l7.yValue+pane.getHeight()*0.0608);
									p.l9 = new Point(circleL.getLayoutX()-(circleL.getRadius()*0.78125), p.l8.yValue+pane.getHeight()*0.0608);
									p.l10 = new Point(circleL.getLayoutX()-(circleL.getRadius()*0.65625), p.l9.yValue+pane.getHeight()*0.0608);
									p.l11 = new Point(circleL.getLayoutX()-(circleL.getRadius()*0.51172), p.l10.yValue+pane.getHeight()*0.0608);

									p.i1 = new Point((pane.getWidth()*0.41875), (pane.getHeight()*0.33739));
									p.i2 = new Point(pane.getWidth()*0.41875, p.i1.yValue+pane.getHeight()*0.05471);
									p.i3 = new Point(pane.getWidth()*0.41875, p.i2.yValue+pane.getHeight()*0.05471);
									p.i4 = new Point(pane.getWidth()*0.41875, p.i3.yValue+pane.getHeight()*0.05471);
									p.i5 = new Point(pane.getWidth()*0.41875, p.i4.yValue+pane.getHeight()*0.05471);
									p.i6 = new Point(pane.getWidth()*0.41875, p.i5.yValue+pane.getHeight()*0.05471);

									p.r1 = new Point(p.l1.xValue+pane.getWidth()*0.23984, p.l1.yValue);
									p.r2 = new Point(p.l2.xValue+pane.getWidth()*0.29609, p.l2.yValue);
									p.r3 = new Point(p.l3.xValue+pane.getWidth()*0.34375, p.l3.yValue);
									p.r4 = new Point(p.l4.xValue+pane.getWidth()*0.378125, p.l4.yValue);
									p.r5 = new Point(p.l5.xValue+pane.getWidth()*0.40156, p.l5.yValue);
									p.r6 = new Point(p.l6.xValue+pane.getWidth()*0.42109, p.l6.yValue);
									p.r7 = new Point(p.l7.xValue+pane.getWidth()*0.40156, p.l7.yValue);
									p.r8 = new Point(p.l8.xValue+pane.getWidth()*0.378125, p.l8.yValue);
									p.r9 = new Point(p.l9.xValue+pane.getWidth()*0.34375, p.l9.yValue);
									p.r10 = new Point(p.l10.xValue+pane.getWidth()*0.29609, p.l10.yValue);
									p.r11 = new Point(p.l11.xValue+pane.getWidth()*0.23984, p.l11.yValue);

								}
							});

							pane.heightProperty().addListener(new ChangeListener<Number>() {
								@Override
								public void changed(ObservableValue<? extends Number> observable, Number oldY, Number newY) {
									////		            						System.out.println("oldY = "+ oldY+", newY = "+newY);
									//		            						double old = box.getLayoutY();
									//		            						box.setLayoutY(old + (newY.doubleValue() - oldY.doubleValue())/2.5);
									////		            						System.out.println("(newY-oldY)/2 = "+ ((newY.doubleValue()-oldY.doubleValue())/2.0)+"; old="+old+" ==> new="+box.getLayoutY());

									box.setLayoutY(pane.getHeight() * record.percentY);						
								}
							});

							pane.getChildren().add(box);

						}
						dialog.close();
					}

				});

				layout.setPadding(new Insets(20,20,20,20));
				layout.getChildren().addAll(listView, addText, deleteText, finish);
				Scene dialogScene = new Scene(layout, 300, 500);

				dialog.setScene(dialogScene);
				dialog.show();
			}
		});
	
		
		//Texts ------------------------------------------------------------------------------------------------------------
		Text title = new Text("Title");
		title.setFont(new Font(20));
		title.setStroke(Color.BLACK);
		//title.getText().length()*4 : text half length
		title.layoutXProperty().bind(pane.widthProperty().divide(2).subtract(title.getText().length()*4));
		title.setLayoutY(25);
		
		title.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getButton() == MouseButton.PRIMARY) {
					TextInputDialog dialog = new TextInputDialog("Enter Venn diagram title");
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
				if (event.getButton() == MouseButton.PRIMARY) {
					TextInputDialog dialog = new TextInputDialog("Enter left circle title");
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
				if (event.getButton() == MouseButton.PRIMARY) {
					TextInputDialog dialog = new TextInputDialog("Enter right circle title");
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
		Text cpR = new Text("Right circle color : ");
		cpR.setFont(new Font(12));
		cpR.setStroke(Color.BLACK);
		cpR.layoutXProperty().bind(cp1.layoutXProperty().subtract(100));
		cpR.layoutYProperty().bind(cp1.layoutYProperty().add(15));
		
		//Right circle color picker label
		Text cpL = new Text("Left circle color : ");
		cpL.setFont(new Font(12));
		cpL.setStroke(Color.BLACK);
		cpL.setTextAlignment(TextAlignment.CENTER);
		cpL.layoutXProperty().bind(cp2.layoutXProperty().subtract(100));
		cpL.layoutYProperty().bind(cp2.layoutYProperty().add(15));
		
		
		//Screen-shot implementation
		FlowPane flow = new FlowPane();
		ImageView display = new ImageView();
		Button capture = new Button("Take Screenshot of Venn Diagram!");
		flow.getChildren().addAll(display, capture);
		capture.layoutXProperty().bind(pane.widthProperty().multiply(0));
		capture.layoutYProperty().bind(pane.heightProperty().multiply(95.0/100.0));
		capture.prefWidthProperty().bind(pane.widthProperty().multiply(10.0/100.0));
		capture.prefHeightProperty().bind(pane.heightProperty().multiply(5.0/100.0));
		
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
		
		//Adds items to the window -----------------------------------------------------------------------------------------------
		pane.getChildren().add(circleR);
		pane.getChildren().add(circleL);
		pane.getChildren().addAll(cp1, cp2);
		pane.getChildren().add(textAdder);
		pane.getChildren().add(anchorOption);
		pane.getChildren().add(multAdder);
		
		//Adds titles to window
		pane.getChildren().add(title);
		pane.getChildren().add(left);
		pane.getChildren().add(right);
		pane.getChildren().addAll(cpR, cpL);
		
		//Adds screenshot to window
//		pane.getChildren().add(flow);
		
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
		Text captureButton = new Text();
		captureButton.setLayoutY(275);
		
		pane.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.F3) {
					if (VennBase.debug) {
						VennBase.debug=false;
						pane.getChildren().removeAll(screen_bounds, pane_bounds, cp1data, cp2data, textAdderData, titleData, rightData, leftData, cpLData, cpRData, captureButton);
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
						captureButton.setText("capture: "+capture.getLayoutX()+", "+capture.getLayoutY());
						
						VennBase.debug=true;
						pane.getChildren().addAll(screen_bounds, pane_bounds, cp1data, cp2data, textAdderData, titleData, rightData, leftData, cpLData, cpRData, captureButton);
					}
				}
			}
        });
		
		pane.setOnMouseMoved(new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent event) {		
			if (VennBase.anchor) {stage.setMaximized(true);}
		}
	});
		
		
	}
	
	
	//Class for recording variables for each box; created for each box
	static class Record {
		static int numBoxes;
		
		double x;
		double y;
		
		//Default text box color stored in this circle
//		static String textBox = null;
		
		//resize detection variables
		double percentX;
		double percentY;
		
		//position detection variables
		boolean inCircleR;
		boolean inCircleL;
		
	}
	
	//Anchor points in each circle
	private class Anchor {
		ArrayList<Point> points = new ArrayList<Point>();
		
		public void addPoint(Point p) {this.points.add(p);}
		
		public void removePoint(Point p) {if(this.points.contains(p)) {this.points.remove(p);}}
		
		public Point closest(double h) {
			double closest = Double.MAX_VALUE;
			Point found = null;
			for (int i=0;i<points.size();i++) {
				if (Math.abs(h-points.get(i).yValue)<closest) {closest = Math.abs(h-points.get(i).yValue);found=points.get(i);}
			}
			return found;
		}
		
		public void printAll() {
			for (Point p:points) {System.out.println(p.toString());}
		}
		
	}
	
	private class Point {
		double xValue;
		double yValue;
		
		public Point(double x, double y) {this.xValue=x;this.yValue=y;}
		
		public String toString() {
			return (this.xValue+", "+this.yValue);
			
		}
	}
	
	static class Points{
		Point l1;
		Point l2;
		Point l3;
		Point l4;
		Point l5;
		Point l6;
		Point l7;
		Point l8;
		Point l9;
		Point l10;
		Point l11;
		Point i1;
		Point i2;
		Point i3;
		Point i4;
		Point i5;
		Point i6;
		Point r1;
		Point r2;
		Point r3;
		Point r4;
		Point r5;
		Point r6;
		Point r7;
		Point r8;
		Point r9;
		Point r10;
		Point r11;
	}
	
	
	public static void main(String[] args) {
		Application.launch(args);
	}

}
