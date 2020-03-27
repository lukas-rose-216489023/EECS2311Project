package venn;

import java.util.ArrayList;
import java.util.Optional;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class TextBox {
	static boolean ctrlSelection = false;
	
	int stackable;
	Button box;
	Record record;
	String pos;		//can be: "universal", "intersection", "left", "right"
	int boxNum;
	Color boxcol;
	Color fontcol;


	//Text box constructor
	public TextBox(Pane pane, Button textAdder, String text, Circle circleL, Circle circleR, Anchor intersection, Anchor leftCircle, Anchor rightCircle, Points p, Rectangle selection, Color boxcol, Color fontcol){
		this.stackable = (int) ((pane.getHeight()-pane.getHeight()*.2) / (pane.getHeight()*.05+10));

		//Text box properties
		box = new Button(text);
		box.prefWidthProperty().bind(circleL.radiusProperty().subtract(50));
		box.prefHeightProperty().bind(pane.heightProperty().multiply(5.0/100.0));
		box.setLayoutX(15);
		box.setLayoutY((pane.getHeight()*.2)+(pane.getHeight()*.05+10)*(Record.numBoxes%stackable));
		boxNum=Record.numBoxes;
		pos="universal";

		//variables for use in resize detection and position detection
		record = new Record(VennBase.autoSaveFile);
		record.percentX = box.getLayoutX() / pane.getWidth();
		record.percentY = box.getLayoutY() / pane.getHeight();
		record.inCircleL = false;
		record.inCircleR = false;
		Record.addToUniversal(getThis());

		Record.numBoxes++;


		
		//styling box
        BackgroundFill background_fill = new BackgroundFill(boxcol, CornerRadii.EMPTY, Insets.EMPTY); 
        Background background = new Background(background_fill);
        box.setBackground(background); 

//		box.setBackground(new Background(new BackgroundFill(Color.rgb((int)boxcol.getRed()*255,(int)boxcol.getGreen()*255,(int)boxcol.getBlue()*255), CornerRadii.EMPTY, Insets.EMPTY)));
//		box.setStyle("-fx-background-radius: 5;"
//					+ "-fx-background-color: rgb(" + (int)boxcol.getRed()*255 + "," + (int)boxcol.getGreen()*255 + "," + (int)boxcol.getBlue()*255 + ");"); 
		box.textFillProperty().set(fontcol);
		
		VennBase.autoSaveFile.WriteToFile("Box"+boxNum+" "+box.getText().length()+" "+box.getText()+" "+pos+" "+box.getLayoutX()+" "+box.getLayoutY()+" "+box.getStyle());

		//Text box action options
		box.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (ctrlSelection) {
					if (record.ctrlSelected) {
						record.ctrlSelected=false;
						box.setStyle("-fx-background-color: #80b380; -fx-border-width: 0px; -fx-border-color: #0000ff50");
					}
					else {
						record.ctrlSelected=true;
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
						FileHandling.saveChanges(VennBase.autoSaveFile, ("Box"+boxNum), ("Box"+boxNum+" "+box.getText().length()+" "+box.getText()+" "+pos+" "+box.getLayoutX()+" "+box.getLayoutY()+" "+box.getStyle()));
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

		//Anchoring
		box.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (ctrlSelection) {releaseCheckAll(pane, circleL, circleR, intersection, leftCircle, rightCircle);}
				releaseCheck(pane, circleL, circleR, intersection, leftCircle, rightCircle);

				FileHandling.saveChanges(VennBase.autoSaveFile, ("Box"+boxNum), ("Box"+boxNum+" "+box.getText().length()+" "+box.getText()+" "+pos+" "+box.getLayoutX()+" "+box.getLayoutY()+" "+box.getStyle()));
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
//
//				p.l1 = new Point(circleL.getLayoutX()-(circleL.getRadius()*0.51172), circleL.getLayoutY()-(circleL.getRadius()*0.8359));
//				p.l2 = new Point(circleL.getLayoutX()-(circleL.getRadius()*0.65625), p.l1.yValue+pane.getHeight()*0.0608);
//				p.l3 = new Point(circleL.getLayoutX()-(circleL.getRadius()*0.78125), p.l2.yValue+pane.getHeight()*0.0608);
//				p.l4 = new Point(circleL.getLayoutX()-(circleL.getRadius()*0.87890), p.l3.yValue+pane.getHeight()*0.0608);
//				p.l5 = new Point(circleL.getLayoutX()-(circleL.getRadius()*0.92969), p.l4.yValue+pane.getHeight()*0.0608);
//				p.l6 = new Point(circleL.getLayoutX()-(circleL.getRadius()*0.96875), p.l5.yValue+pane.getHeight()*0.0608);
//				p.l7 = new Point(circleL.getLayoutX()-(circleL.getRadius()*0.92969), p.l6.yValue+pane.getHeight()*0.0608);
//				p.l8 = new Point(circleL.getLayoutX()-(circleL.getRadius()*0.87890), p.l7.yValue+pane.getHeight()*0.0608);
//				p.l9 = new Point(circleL.getLayoutX()-(circleL.getRadius()*0.78125), p.l8.yValue+pane.getHeight()*0.0608);
//				p.l10 = new Point(circleL.getLayoutX()-(circleL.getRadius()*0.65625), p.l9.yValue+pane.getHeight()*0.0608);
//				p.l11 = new Point(circleL.getLayoutX()-(circleL.getRadius()*0.51172), p.l10.yValue+pane.getHeight()*0.0608);
//
//				p.i1 = new Point((pane.getWidth()*0.41875), (pane.getHeight()*0.33739));
//				p.i2 = new Point(pane.getWidth()*0.41875, p.i1.yValue+pane.getHeight()*0.05471);
//				p.i3 = new Point(pane.getWidth()*0.41875, p.i2.yValue+pane.getHeight()*0.05471);
//				p.i4 = new Point(pane.getWidth()*0.41875, p.i3.yValue+pane.getHeight()*0.05471);
//				p.i5 = new Point(pane.getWidth()*0.41875, p.i4.yValue+pane.getHeight()*0.05471);
//				p.i6 = new Point(pane.getWidth()*0.41875, p.i5.yValue+pane.getHeight()*0.05471);
//
//				p.r1 = new Point(p.l1.xValue+pane.getWidth()*0.23984, p.l1.yValue);
//				p.r2 = new Point(p.l2.xValue+pane.getWidth()*0.29609, p.l2.yValue);
//				p.r3 = new Point(p.l3.xValue+pane.getWidth()*0.34375, p.l3.yValue);
//				p.r4 = new Point(p.l4.xValue+pane.getWidth()*0.378125, p.l4.yValue);
//				p.r5 = new Point(p.l5.xValue+pane.getWidth()*0.40156, p.l5.yValue);
//				p.r6 = new Point(p.l6.xValue+pane.getWidth()*0.42109, p.l6.yValue);
//				p.r7 = new Point(p.l7.xValue+pane.getWidth()*0.40156, p.l7.yValue);
//				p.r8 = new Point(p.l8.xValue+pane.getWidth()*0.378125, p.l8.yValue);
//				p.r9 = new Point(p.l9.xValue+pane.getWidth()*0.34375, p.l9.yValue);
//				p.r10 = new Point(p.l10.xValue+pane.getWidth()*0.29609, p.l10.yValue);
//				p.r11 = new Point(p.l11.xValue+pane.getWidth()*0.23984, p.l11.yValue);

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
		Record.addTextBox(this);
	}

	//method to delete a text box
	public void removeFromList(Pane pane) {
		pane.getChildren().remove(this.box);
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
				
				FileHandling.saveChanges(autoSaveFile, ("Box"+b.boxNum), ("Box"+b.boxNum+" "+b.box.getText().length()+" "+b.box.getText()+" "+b.pos+" "+b.box.getLayoutX()+" "+b.box.getLayoutY()));
				FileHandling.saveChanges(autoSaveFile, ("Record"+b.record.recordNum), ("Record"+b.record.recordNum+" "+b.record.percentX+" "+b.record.percentY+" "+b.record.inCircleR+" "+b.record.inCircleL));
			}
		}
	}
	public static void releaseCtrlSelection() {
		ArrayList<TextBox> iterate = new ArrayList<TextBox>(Record.tBoxes);
		for (TextBox b:iterate) {
			if (b.record.ctrlSelected) {
				b.record.ctrlSelected=false;
				b.box.setStyle("-fx-background-color: #80b380; -fx-border-width: 0px; -fx-border-color: #0000ff50");
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
	}
	
	public static void releaseCheckAll(Pane pane, Circle circleL, Circle circleR, Anchor intersection, Anchor leftCircle, Anchor rightCircle) {
		ArrayList<TextBox> iterate = new ArrayList<TextBox>(Record.tBoxes);
		for (TextBox b:iterate) {
			b.releaseCheck(pane, circleL, circleR, intersection, leftCircle, rightCircle);
		}
	}

}
