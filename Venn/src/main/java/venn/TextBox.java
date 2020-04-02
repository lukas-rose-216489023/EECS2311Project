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
import javafx.scene.control.TextArea;
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
	String pos;		//can be: "universal", "intersection", "left", "right"
	int boxNum;
	TextArea xtraBox;
	String boxCol;
	String fontCol;
	
	static boolean lock;


	//Text box constructor
	public TextBox(Pane pane, String text, Circle circleL, Circle circleR, Anchor intersection, Anchor leftCircle, Anchor rightCircle, Points p, Rectangle selection, String boxcol, String fontcol, String xtraInfo){
		this.stackable = (int) ((pane.getHeight()-pane.getHeight()*.4) / (pane.getHeight()*.05+10));

		//Text box properties
		box = new Button(text);
		box.prefWidthProperty().bind(circleL.radiusProperty().subtract(50));
		box.prefHeightProperty().bind(pane.heightProperty().multiply(5.0/100.0));
		box.setLayoutX(15);
		box.setLayoutY((pane.getHeight()*.4)+(pane.getHeight()*.05+10)*(Record.numBoxes%stackable));
		boxNum=Record.numBoxes;
		pos="universal";
		
		//xtraInfo box properties
		xtraBox = new TextArea(xtraInfo);
		xtraBox.prefWidthProperty().bind(box.prefWidthProperty());
		xtraBox.prefHeightProperty().bind(box.prefHeightProperty().multiply(2.0));
		xtraBox.layoutXProperty().bind(box.layoutXProperty());
		xtraBox.layoutYProperty().bind(box.layoutYProperty().add(box.getPrefHeight()));
		xtraBox.setVisible(false);


		//variables for use in resize detection and position detection
		record = new Record(VennBase.autoSaveFile);
		record.percentX = box.getLayoutX() / pane.getWidth();
		record.percentY = box.getLayoutY() / pane.getHeight();
		record.inCircleL = false;
		record.inCircleR = false;
		Record.addToUniversal(getThis());

		Record.numBoxes++;


		
		//styling box
		
		box.setStyle("-fx-background-color: #"+boxcol+"; -fx-text-fill: #"+fontcol);
		xtraBox.setStyle("-fx-background-color: #"+boxcol+"; -fx-text-fill: #"+fontcol);
		
		boxCol = boxcol;
		fontCol = fontcol;
		
		VennBase.autoSaveFile.WriteToFile("Box"+boxNum+" "+box.getText().length()+" "+box.getText()+" "+pos+" "+box.getLayoutX()+" "+box.getLayoutY()+" "+boxCol+" "+fontCol);

		//Text box action options
		box.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (ctrlSelection) {
					if (record.ctrlSelected) {
						record.ctrlSelected=false;
						box.setStyle("-fx-border-width: 0px; -fx-border-color: #0000ff50; -fx-background-color: #"+boxCol+"; -fx-text-fill: #"+fontCol);
					}
					else {
						record.ctrlSelected=true;
						box.setStyle("-fx-border-width: 5px; -fx-border-color: #0000ff50; -fx-background-color: #"+boxCol+"; -fx-text-fill: #"+fontCol);
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
						VennBase.undoList.add(new Double(0.0));
						VennBase.undoBox = box;
						VennBase.undoBoxName.add(++VennBase.undoBoxNameCursor, result);
						FileHandling.saveChanges(VennBase.autoSaveFile, "Box"+boxNum, "Box"+boxNum+" "+box.getText().length()+" "+box.getText()+" "+pos+" "+box.getLayoutX()+" "+box.getLayoutY()+" "+boxCol+" "+fontCol);
						FileHandling.saveChanges(VennBase.autoSaveFile, ("Record"+record.recordNum), ("Record"+record.recordNum+" "+record.percentX+" "+record.percentY+" "+record.inCircleR+" "+record.inCircleL));
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
						VennBase.undoList.add(new Boolean(true));
						VennBase.undoBox = box;
						FileHandling.saveChanges(VennBase.autoSaveFile, ("Box"+boxNum), "");
						FileHandling.saveChanges(VennBase.autoSaveFile, ("Record"+record.recordNum), "");
					}
					else if (result.get() == cancel) {
						// ... user chose "cancel"
					}
					
					
					selection.setWidth(0);selection.setHeight(0);
				}
			}
		});
		

		//changes cursor when moving text box  and  records distance moved
		box.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (ctrlSelection) {TextBox.prepMoveSelection(mouseEvent.getSceneX(), mouseEvent.getSceneY());}
				else {
					box.setCursor(Cursor.MOVE);
					record.x = box.getLayoutX() - mouseEvent.getSceneX();
					record.y = box.getLayoutY() - mouseEvent.getSceneY();
					if (pos.equals("intersection")) {Record.removeFromIntersetion(getThis());}
					if (pos.equals("left")) {Record.removeFromLeft(getThis());}
					if (pos.equals("right")) {Record.removeFromRight(getThis());}
					if (pos.equals("universal")) {Record.removeFromUniversal(getThis());}
				}
			}
		});

		//Moves text box when dragged 
		box.setOnMouseDragged(new EventHandler<MouseEvent>() {
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
		});
		
		//xtraInfo box shows when hovered over text box
		box.hoverProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue && !lock) {
				xtraBox.toFront();
				xtraBox.setVisible(true);

			} else {
				xtraBox.setVisible(false);
			}
		});

		xtraBox.hoverProperty().addListener((observabl, oldValue, newValue) -> {
			if (newValue && !lock) {
				xtraBox.toFront();
				xtraBox.setVisible(true);
			} else {
				xtraBox.setVisible(false);
			}
		});

		//Anchoring
		box.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (ctrlSelection) {releaseCheckAll(pane, circleL, circleR, intersection, leftCircle, rightCircle);}
				releaseCheck(pane, circleL, circleR, intersection, leftCircle, rightCircle);

				FileHandling.saveChanges(VennBase.autoSaveFile, "Box"+boxNum, "Box"+boxNum+" "+box.getText().length()+" "+box.getText()+" "+pos+" "+box.getLayoutX()+" "+box.getLayoutY()+" "+boxCol+" "+fontCol);
				FileHandling.saveChanges(VennBase.autoSaveFile, ("Record"+record.recordNum), ("Record"+record.recordNum+" "+record.percentX+" "+record.percentY+" "+record.inCircleR+" "+record.inCircleL));
			}
		});

		box.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (VennBase.debug) {box.setText(pos);}
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
				if (selection.getHeight()!=0.0 && !ctrlSelection) {
					if (box.getLayoutY()<(newHeight.doubleValue()+selection.getLayoutY()) && box.getLayoutY()>selection.getLayoutY()) {record.inSelectionY = true;}
					else {record.inSelectionY = false;}
				}
			}
		});

		//selection detection x:
		selection.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldWidth, Number newWidth) {
				if (selection.getWidth()!=0.0 && !ctrlSelection) {
					if (box.getLayoutX()<(newWidth.doubleValue()+selection.getLayoutX()) && box.getLayoutX()>selection.getLayoutX()) {record.inSelectionX = true;}
					else {record.inSelectionX = false;}
				}
			}
		});
		
		addToList(pane);
	}

	//method to add this text box
	public void addToList(Pane pane) {
		pane.getChildren().add(this.box);
		pane.getChildren().add(this.xtraBox);
		Record.addTextBox(this);
	}

	//method to delete a text box
	public void removeFromList(Pane pane) {
		pane.getChildren().remove(this.box);
		pane.getChildren().remove(this.xtraBox);
		Record.removeTextBox(this);
		Record.removeFromIntersetion(this);
		Record.removeFromLeft(this);
		Record.removeFromRight(this);
		Record.removeFromUniversal(this);
	}

	//method to get a text box's text
	public String toString() {return this.box.getText();}

	//methods to move selected text boxes
	public static void prepMoveSelection(double x, double y) {
		ArrayList<TextBox> iterate = new ArrayList<TextBox>(Record.tBoxes);
		for (TextBox b:iterate) {
			if ((b.record.inSelectionX&&b.record.inSelectionY)||b.record.ctrlSelected) {
				b.record.moveX = b.box.getLayoutX() - x;
				b.record.moveY = b.box.getLayoutY() - y;
				if (b.pos.equals("intersection")) {Record.removeFromIntersetion(b);}
				if (b.pos.equals("left")) {Record.removeFromLeft(b);}
				if (b.pos.equals("right")) {Record.removeFromRight(b);}
				if (b.pos.equals("universal")) {Record.removeFromUniversal(b);}
			}
		}
	}
	public static void moveSelection(Pane pane, double x, double y, FileHandling autoSaveFile) {
		ArrayList<TextBox> iterate = new ArrayList<TextBox>(Record.tBoxes);
		for (TextBox b:iterate) {
			if ((b.record.inSelectionX&&b.record.inSelectionY)||b.record.ctrlSelected) {
				b.box.setLayoutX(x + b.record.moveX);
				b.box.setLayoutY(y + b.record.moveY);
				b.record.percentX = b.box.getLayoutX() / pane.getWidth();
				b.record.percentY = b.box.getLayoutY() / pane.getHeight();
				
				FileHandling.saveChanges(VennBase.autoSaveFile, "Box"+b.boxNum, "Box"+b.boxNum+" "+b.box.getText().length()+" "+b.box.getText()+" "+b.pos+" "+b.box.getLayoutX()+" "+b.box.getLayoutY()+" "+b.boxCol+" "+b.fontCol);
				FileHandling.saveChanges(autoSaveFile, ("Record"+b.record.recordNum), ("Record"+b.record.recordNum+" "+b.record.percentX+" "+b.record.percentY+" "+b.record.inCircleR+" "+b.record.inCircleL));
			}
		}
	}
	public static void releaseCtrlSelection() {
		ArrayList<TextBox> iterate = new ArrayList<TextBox>(Record.tBoxes);
		for (TextBox b:iterate) {
			if (b.record.ctrlSelected) {
				b.record.ctrlSelected=false;
				b.box.setStyle("-fx-border-width: 0px; -fx-border-color: #0000ff50; -fx-background-color: #"+b.boxCol+"; -fx-text-fill: #"+b.fontCol);
			}
		}
		ctrlSelection=false;
		VennBase.ctrl.setText("Control Selection Mode: OFF");
	}
	public static void releaseMultiSelection() {
		ArrayList<TextBox> iterate = new ArrayList<TextBox>(Record.tBoxes);
		for (TextBox b:iterate) {
			if (b.record.inSelectionX&&b.record.inSelectionY) {
				b.record.inSelectionX=false;
				b.record.inSelectionY=false;
			}
		}
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
					if (ctrlSelection) {
						TextBox.moveSelection(pane, mouseEvent.getSceneX(), mouseEvent.getSceneY(), VennBase.autoSaveFile);
					}
					else {
						b.box.setLayoutX(mouseEvent.getSceneX() + b.record.x);
						b.box.setLayoutY(mouseEvent.getSceneY() + b.record.y);
						b.record.percentX = b.box.getLayoutX() / pane.getWidth();
						b.record.percentY = b.box.getLayoutY() / pane.getHeight();
					}
				}
			});
		}
	}
	
	public TextBox getThis() {return this;}
	
	public void releaseCheck(Pane pane, Circle circleL, Circle circleR, Anchor intersection, Anchor leftCircle, Anchor rightCircle) {
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
			Record.addToIntersection(getThis());
		}
		else if (record.inCircleL) {
			//box x and y are closest anchor points in the left circle
			if (VennBase.debug) {box.setText("Currently in left circle");}
			if (VennBase.anchor) {box.setLayoutX(leftCircle.closest(box.getLayoutY()).xValue);}
			if (VennBase.anchor) {box.setLayoutY(leftCircle.closest(box.getLayoutY()).yValue);}
			record.percentX = box.getLayoutX() / pane.getWidth();
			record.percentY = box.getLayoutY() / pane.getHeight();
			Record.addToLeft(getThis());

		}
		else if (record.inCircleR) {
			//box x and y are closest anchor points in the right circle
			if (VennBase.debug) {box.setText("Currently in right circle");}
			if (VennBase.anchor) {box.setLayoutX(rightCircle.closest(box.getLayoutY()).xValue);}
			if (VennBase.anchor) {box.setLayoutY(rightCircle.closest(box.getLayoutY()).yValue);}
			record.percentX = box.getLayoutX() / pane.getWidth();
			record.percentY = box.getLayoutY() / pane.getHeight();
			Record.addToRight(getThis());
		}
		else {
			Record.addToUniversal(getThis());
		}
		FileHandling.saveChanges(VennBase.autoSaveFile, "Box"+boxNum, "Box"+boxNum+" "+box.getText().length()+" "+box.getText()+" "+pos+" "+box.getLayoutX()+" "+box.getLayoutY()+" "+boxCol+" "+fontCol);
		FileHandling.saveChanges(VennBase.autoSaveFile, ("Record"+record.recordNum), ("Record"+record.recordNum+" "+record.percentX+" "+record.percentY+" "+record.inCircleR+" "+record.inCircleL));
	}
	
	public static void releaseCheckAll(Pane pane, Circle circleL, Circle circleR, Anchor intersection, Anchor leftCircle, Anchor rightCircle) {
		ArrayList<TextBox> iterate = new ArrayList<TextBox>(Record.tBoxes);
		for (TextBox b:iterate) {
			b.releaseCheck(pane, circleL, circleR, intersection, leftCircle, rightCircle);
		}
	}

}