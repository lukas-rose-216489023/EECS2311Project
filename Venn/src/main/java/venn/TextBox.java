package venn;

import java.util.ArrayList;
import java.util.Optional;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class TextBox {
	static boolean ctrlSelection = false;
	
	int stackable;
	Button box;
	Record record;
	String pos;
	int boxNum;


	//Text box constructor
	public TextBox(Pane pane, Button textAdder, String text, Circle circleL, Circle circleR, Anchor intersection, Anchor leftCircle, Anchor rightCircle, Points p, Rectangle selection, FileHandling autoSaveFile){

		this.stackable = (int) (pane.getHeight() / (textAdder.getHeight()-10)) -2;		

		//Text box properties
		box = new Button(text);
		box.prefWidthProperty().bind(circleL.radiusProperty().subtract(50));
		box.prefHeightProperty().bind(pane.heightProperty().multiply(5.0/100.0));
		box.setLayoutX(15);
		box.setLayoutY(10+(textAdder.getPrefHeight()*2) + ((textAdder.getPrefHeight()-15)*(Record.numBoxes%stackable)));
		boxNum=Record.numBoxes;
		pos="universal";

		//variables for use in resize detection and position detection
		record = new Record(autoSaveFile);
		record.percentX = box.getLayoutX() / pane.getWidth();
		record.percentY = box.getLayoutY() / pane.getHeight();
		record.inCircleL = false;
		record.inCircleR = false;
		Record.addToUniversal(box.getText());

		Record.numBoxes++;


		autoSaveFile.WriteToFile("Box"+boxNum+" "+box.getText().length()+" "+box.getText()+" "+pos+" "+box.getLayoutX()+" "+box.getLayoutY());

		//box.setStyle("-fx-background-color: "+Record.textBox);
		box.setStyle("-fx-background-color: #80b380");
		
		EventHandler<MouseEvent> customize = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (ctrlSelection) {
					if (record.inSelectionX&&record.inSelectionY) {
						record.inSelectionX=false;record.inSelectionY=false;
						box.setStyle("-fx-background-color: #80b380; -fx-border-width: 0px; -fx-border-color: #0000ff50");
					}
					else {
						record.inSelectionX=true;record.inSelectionY=true;
						box.setStyle("-fx-background-color: #80b380; -fx-border-width: 5px; -fx-border-color: #0000ff50");
					}
				}

				else if (event.getButton().equals(MouseButton.PRIMARY)) {
					if (event.getClickCount()==2) {
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
						FileHandling.saveChanges(autoSaveFile, ("Box"+boxNum), ("Box"+boxNum+" "+box.getText().length()+" "+box.getText()+" "+pos+" "+box.getLayoutX()+" "+box.getLayoutY()));
						FileHandling.saveChanges(autoSaveFile, ("Record"+record.recordNum), ("Record"+record.recordNum+" "+record.percentX+" "+record.percentY+" "+record.inCircleR+" "+record.inCircleL));
					}
				}
				else if (event.getButton().equals(MouseButton.SECONDARY)) {
					pane.getChildren().remove(selection);
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Delete Text Box");
					alert.setHeaderText("Are you sure you want to delete this text box?");

					ButtonType deleteButton = new ButtonType("Delete");
					ButtonType cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

					alert.getButtonTypes().setAll(deleteButton, cancel);

					Optional<ButtonType> result = alert.showAndWait();
					if (result.get() == deleteButton) {
						// ... user chose "delete"
						removeFromList(pane);
						FileHandling.saveChanges(autoSaveFile, ("Box"+boxNum), "");
						FileHandling.saveChanges(autoSaveFile, ("Record"+record.recordNum), "");
					}
					else if (result.get() == cancel) {
						// ... user chose "cancel"
					}
					
					
					selection.setWidth(0);selection.setHeight(0);
				}
			}
		};
		
		//Text box action options
		box.setOnMouseClicked(customize);

		//changes cursor when moving text box  and  records distance moved
		box.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (ctrlSelection) {TextBox.prepMoveSelection(mouseEvent.getSceneX(), mouseEvent.getSceneY());}
				else {
					box.setCursor(Cursor.MOVE);
					record.x = box.getLayoutX() - mouseEvent.getSceneX();
					record.y = box.getLayoutY() - mouseEvent.getSceneY();
					if (pos.equals("intersection")) {Record.removeFromIntersetion(text);}
					if (pos.equals("left")) {Record.removeFromLeft(text);}
					if (pos.equals("right")) {Record.removeFromRight(text);}
				}
			}
		});

		//Moves text box when dragged 
		box.setOnMouseDragged(boxMovementHandler(pane));

		//Anchoring
		box.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
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
					if (VennBase.debug) {box.setText("Currently in intersection");}
					if (VennBase.anchor) {box.setLayoutX(intersection.closest(box.getLayoutY()).xValue);}
					if (VennBase.anchor) {box.setLayoutY(intersection.closest(box.getLayoutY()).yValue);}
					record.percentX = box.getLayoutX() / pane.getWidth();
					record.percentY = box.getLayoutY() / pane.getHeight();
					if (pos.equals("right")) {Record.removeFromRight(text);}
					if (pos.equals("left")) {Record.removeFromLeft(text);}
					if (Record.checkDataClash(box, "intersection")) {Record.addToIntersection(box.getText());pos="intersection";}
					else {
						box.setLayoutX(15);
						box.setLayoutY(5+(textAdder.getPrefHeight()*2) + ((textAdder.getPrefHeight()-15)*(Record.numBoxes%stackable)));
						record.percentX = box.getLayoutX() / pane.getWidth();
						record.percentY = box.getLayoutY() / pane.getHeight();
					}
				}
				else if (record.inCircleL) {
					//box x and y are closest anchor points in the left circle
					if (VennBase.debug) {box.setText("Currently in left circle");}
					if (VennBase.anchor) {box.setLayoutX(leftCircle.closest(box.getLayoutY()).xValue);}
					if (VennBase.anchor) {box.setLayoutY(leftCircle.closest(box.getLayoutY()).yValue);}
					record.percentX = box.getLayoutX() / pane.getWidth();
					record.percentY = box.getLayoutY() / pane.getHeight();
					if (pos.equals("right")) {Record.removeFromRight(text);}
					if (pos.equals("intersection")) {Record.removeFromIntersetion(text);}
					if (Record.checkDataClash(box, "left")) {Record.addToLeft(box.getText());pos="left";}
					else {
						box.setLayoutX(15);
						box.setLayoutY(5+(textAdder.getPrefHeight()*2) + ((textAdder.getPrefHeight()-15)*(Record.numBoxes%stackable)));
						record.percentX = box.getLayoutX() / pane.getWidth();
						record.percentY = box.getLayoutY() / pane.getHeight();
					}

				}
				else if (record.inCircleR) {
					//box x and y are closest anchor points in the right circle
					if (VennBase.debug) {box.setText("Currently in right circle");}
					if (VennBase.anchor) {box.setLayoutX(rightCircle.closest(box.getLayoutY()).xValue);}
					if (VennBase.anchor) {box.setLayoutY(rightCircle.closest(box.getLayoutY()).yValue);}
					record.percentX = box.getLayoutX() / pane.getWidth();
					record.percentY = box.getLayoutY() / pane.getHeight();
					if (pos.equals("intersection")) {Record.removeFromIntersetion(text);}
					if (pos.equals("left")) {Record.removeFromLeft(text);}
					if (Record.checkDataClash(box, "right")) {Record.addToRight(box.getText());pos="right";}
					else {
						box.setLayoutX(15);
						box.setLayoutY(5+(textAdder.getPrefHeight()*2) + ((textAdder.getPrefHeight()-15)*(Record.numBoxes%stackable)));
						record.percentX = box.getLayoutX() / pane.getWidth();
						record.percentY = box.getLayoutY() / pane.getHeight();
					}
				}
				else {
					if (pos.equals("intersection")) {Record.removeFromIntersetion(text);}
					if (pos.equals("left")) {Record.removeFromLeft(text);}
					if (pos.equals("right")) {Record.removeFromRight(text);}
				}
				
				FileHandling.saveChanges(autoSaveFile, ("Box"+boxNum), ("Box"+boxNum+" "+box.getText().length()+" "+box.getText()+" "+pos+" "+box.getLayoutX()+" "+box.getLayoutY()));
				FileHandling.saveChanges(autoSaveFile, ("Record"+record.recordNum), ("Record"+record.recordNum+" "+record.percentX+" "+record.percentY+" "+record.inCircleR+" "+record.inCircleL));
			}
		});

		box.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (VennBase.debug) {box.setText((int)box.getLayoutX()+", "+(int)box.getLayoutY());}
			}
		});

		//resize detection x:
		pane.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldX, Number newX) {
				box.setLayoutX(pane.getWidth() * record.percentX);
			}
		});

		//resize detection y:
		pane.heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldY, Number newY) {
				box.setLayoutY(pane.getHeight() * record.percentY);						
			}
		});


		//selection detection y:
		selection.heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldHeight, Number newHeight) {
				if (!(selection.getHeight()==0.0)) {
					if (!ctrlSelection) {
						if (box.getLayoutY()<(newHeight.doubleValue()+selection.getLayoutY()) && box.getLayoutY()>selection.getLayoutY()) {record.inSelectionY = true;}
						else {record.inSelectionY = false;}
					}
				}
			}
		});

		//selection detection x:
		selection.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldWidth, Number newWidth) {
				if (!(selection.getWidth()==0.0)) {
					if (!ctrlSelection) {
						if (box.getLayoutX()<(newWidth.doubleValue()+selection.getLayoutX()) && box.getLayoutX()>selection.getLayoutX()) {record.inSelectionX = true;}
						else {record.inSelectionX = false;}
					}
				}
			}
		});

		addToList(pane);
	}

	//method to add this text box
	public void addToList(Pane pane) {
		pane.getChildren().add(this.box);
		Record.addTextBox(this);
	}

	//method to delete a text box
	public void removeFromList(Pane pane) {
		pane.getChildren().remove(this.box);
		Record.removeTextBox(this);
	}

	//method to get a text box's text
	public String toString() {return this.box.getText();}

	//methods to move selected text boxes
	public static void prepMoveSelection(double x, double y) {
		ArrayList<TextBox> iterate = new ArrayList<TextBox>(Record.tBoxes);
		for (TextBox b:iterate) {
			if (b.record.inSelectionX&&b.record.inSelectionY) {
				b.record.moveX = b.box.getLayoutX() - x;
				b.record.moveY = b.box.getLayoutY() - y;
			}
		}
	}
	public static void moveSelection(Pane pane, double x, double y, FileHandling autoSaveFile) {
		ArrayList<TextBox> iterate = new ArrayList<TextBox>(Record.tBoxes);
		for (TextBox b:iterate) {
			if (b.record.inSelectionX&&b.record.inSelectionY) {
				b.box.setLayoutX(x + b.record.moveX);
				b.box.setLayoutY(y + b.record.moveY);
				b.record.percentX = b.box.getLayoutX() / pane.getWidth();
				b.record.percentY = b.box.getLayoutY() / pane.getHeight();
				
				FileHandling.saveChanges(autoSaveFile, ("Box"+b.boxNum), ("Box"+b.boxNum+" "+b.box.getText().length()+" "+b.box.getText()+" "+b.pos+" "+b.box.getLayoutX()+" "+b.box.getLayoutY()));
				FileHandling.saveChanges(autoSaveFile, ("Record"+b.record.recordNum), ("Record"+b.record.recordNum+" "+b.record.percentX+" "+b.record.percentY+" "+b.record.inCircleR+" "+b.record.inCircleL));
			}
		}
	}
	public static void releaseSelection() {
		ArrayList<TextBox> iterate = new ArrayList<TextBox>(Record.tBoxes);
		for (TextBox b:iterate) {
			if (b.record.inSelectionX&&b.record.inSelectionY && ctrlSelection) {
				b.record.inSelectionX=false;b.record.inSelectionY=false;
				b.box.setStyle("-fx-background-color: #80b380; -fx-border-width: 0px; -fx-border-color: #0000ff50");
			}
		}
		ctrlSelection=false;VennBase.ctrl.setText("Control Selection Mode: OFF");
	}



	//event handler for box movement
	public EventHandler<MouseEvent> boxMovementHandler(Pane pane) {
		EventHandler<MouseEvent> moveBox = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (ctrlSelection) {
					TextBox.moveSelection(pane, mouseEvent.getSceneX(), mouseEvent.getSceneY(), VennBase.autoSaveFile);
				}
				else {
					box.setLayoutX(mouseEvent.getSceneX() + record.x);
					box.setLayoutY(mouseEvent.getSceneY() + record.y);
					record.percentX = box.getLayoutX() / pane.getWidth();
					record.percentY = box.getLayoutY() / pane.getHeight();
				}
			}
		};
		return moveBox;
	}

	//method to turn off box movement event handler
	public static void turnOffBoxMovement() {
		ArrayList<TextBox> iterate = new ArrayList<TextBox>(Record.tBoxes);
		for (TextBox b:iterate) {
			b.box.setOnMouseDragged(null);
		}
	}

	//method to turn on box movement event handler
	public static void turnOnBoxMovement(Pane pane) {
		ArrayList<TextBox> iterate = new ArrayList<TextBox>(Record.tBoxes);
		for (TextBox b:iterate) {
			b.box.setOnMouseDragged(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent mouseEvent) {
					b.box.setLayoutX(mouseEvent.getSceneX() + b.record.x);
					b.box.setLayoutY(mouseEvent.getSceneY() + b.record.y);
					b.record.percentX = b.box.getLayoutX() / pane.getWidth();
					b.record.percentY = b.box.getLayoutY() / pane.getHeight();
				}
			});
		}
	}

}
