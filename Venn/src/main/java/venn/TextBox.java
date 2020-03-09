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
	int stackable;
	Button box;
	Record record;
	
	//Text box constructor
	public TextBox(Pane pane, Button textAdder, String text, Circle circleL, Circle circleR, Anchor intersection, Anchor leftCircle, Anchor rightCircle, Points p, Rectangle selection){
		this.stackable = (int) (pane.getHeight() / (textAdder.getHeight()-10)) -2;		

		//Text box properties
		box = new Button(text);
		box.prefWidthProperty().bind(circleL.radiusProperty().subtract(50));
		box.prefHeightProperty().bind(pane.heightProperty().multiply(5.0/100.0));
		box.setLayoutX(15);
		box.setLayoutY(5+(textAdder.getPrefHeight()*2) + ((textAdder.getPrefHeight()-15)*(Record.numBoxes%stackable)));
		Record.numBoxes++;
		
		//variables for use in resize detection and position detection
		record = new Record();
		record.percentX = box.getLayoutX() / pane.getWidth();
		record.percentY = box.getLayoutY() / pane.getHeight();
		record.inCircleL = false;
		record.inCircleR = false;

		//box.setStyle("-fx-background-color: "+Record.textBox);
		box.setStyle("-fx-background-color: #80b380");

		//Change text
		box.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getButton().equals(MouseButton.PRIMARY)) {
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
						if (box.getText().equals("")) {removeFromList(pane);}
					}
				}
			}
		});
		
		//Text box action options
		box.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getButton().equals(MouseButton.SECONDARY)) {
					pane.getChildren().remove(selection);
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Text Box Actions");
					alert.setHeaderText("What would you like to do with this text box?");
					
					ButtonType deleteButton = new ButtonType("Delete");
					ButtonType cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

					alert.getButtonTypes().setAll(deleteButton, cancel);

					Optional<ButtonType> result = alert.showAndWait();
					if (result.get() == deleteButton) {
						// ... user chose "delete"
						removeFromList(pane);
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
					Record.removeFromLeft(box.getText());
					Record.removeFromRight(box.getText());
					record.addToIntersection(box.getText());
				}
				else if (record.inCircleL) {
					//box x and y are closest anchor points in the left circle
					if (VennBase.debug) {box.setText("Currently in left circle");}
					if (VennBase.anchor) {box.setLayoutX(leftCircle.closest(box.getLayoutY()).xValue);}
					if (VennBase.anchor) {box.setLayoutY(leftCircle.closest(box.getLayoutY()).yValue);}
					record.percentX = box.getLayoutX() / pane.getWidth();
					record.percentY = box.getLayoutY() / pane.getHeight();
					Record.removeFromIntersetion(box.getText());
					Record.removeFromRight(box.getText());
					record.addToLeft(box.getText());
				}
				else if (record.inCircleR) {
					//box x and y are closest anchor points in the right circle
					if (VennBase.debug) {box.setText("Currently in right circle");}
					if (VennBase.anchor) {box.setLayoutX(rightCircle.closest(box.getLayoutY()).xValue);}
					if (VennBase.anchor) {box.setLayoutY(rightCircle.closest(box.getLayoutY()).yValue);}
					record.percentX = box.getLayoutX() / pane.getWidth();
					record.percentY = box.getLayoutY() / pane.getHeight();
					Record.removeFromIntersetion(box.getText());
					Record.removeFromLeft(box.getText());
					record.addToRight(box.getText());
				}
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
				if (box.getLayoutY()<((double)newHeight+selection.getLayoutY()) && box.getLayoutY()>selection.getLayoutY()) {record.inSelectionY = true;}
				else {record.inSelectionY = false;}
			}
		});

		//selection detection x:
		selection.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldWidth, Number newWidth) {
				if (box.getLayoutX()<((double)newWidth+selection.getLayoutX()) && box.getLayoutX()>selection.getLayoutX()) {record.inSelectionX = true;}
				else {record.inSelectionX = false;}
			}
		});
		
		addToList(pane);
	}
	
	//method to add a text box
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
	
	//method to delete selected text boxes
	public static void moveSelection() {
		ArrayList<TextBox> iterate = new ArrayList<TextBox>(Record.tBoxes);
		for (TextBox b:iterate) {
			if (b.record.inSelectionX&&b.record.inSelectionY) {
				b.box.setLayoutX(b.box.getLayoutX()+Record.moveX);
				b.box.setLayoutY(b.box.getLayoutY()+Record.moveY);
				System.out.println("Box moved");
			}
		}
		
	}
	
	//method to move a text box
	public void moveBox(double moveX, double moveY) {
	}
	
}
