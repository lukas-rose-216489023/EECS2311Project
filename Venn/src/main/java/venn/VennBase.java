//LUKAS ROSE'S BRANCH
package venn;

//imports
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import javax.imageio.ImageIO;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

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


		//Anchor option button
		Button anchorOption = new Button("Anchoring off");
		anchorOption.prefWidthProperty().bind(pane.widthProperty().multiply(20.0/100.0));
		anchorOption.prefHeightProperty().bind(pane.heightProperty().multiply(5.0/100.0));
		anchorOption.layoutXProperty().bind(pane.widthProperty().multiply(80.0/100.0));
		anchorOption.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (VennBase.anchor) {VennBase.anchor = false;anchorOption.setText("Anchoring off");}
				else {VennBase.anchor = true;anchorOption.setText("Anchoring on");}
			}
		});


		//selection ----------------------------------------------------------------------------------------------------------------------------
		Rectangle selection = new Rectangle();
		selection.setFill(blue.desaturate().desaturate());
		
		//Event handlers
		EventHandler<MouseEvent> initSelection = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				selection.setLayoutX(mouseEvent.getSceneX());
				selection.setLayoutY(mouseEvent.getSceneY());
				pane.getChildren().add(selection);
				TextBox.turnOffBoxMovement();
			}
		};
		
		EventHandler<MouseEvent> updateSelection = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				selection.setWidth(mouseEvent.getSceneX()-selection.getLayoutX());
				selection.setHeight(mouseEvent.getSceneY()-selection.getLayoutY());
				if (selection.getWidth()<0) {selection.setLayoutX(mouseEvent.getSceneX());selection.setWidth(0);}
				if (selection.getHeight()<0) {selection.setLayoutY(mouseEvent.getSceneY());selection.setHeight(0);}
			}
		};
		
		EventHandler<MouseEvent> implementSelection = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
//				pane.getChildren().remove(selection);
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Selection Actions");
				alert.setHeaderText("What would you like to do with the selected text boxes?");

				ButtonType moveButton = new ButtonType("Move");
				ButtonType deleteButton = new ButtonType("Delete");
				ButtonType cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

				alert.getButtonTypes().setAll(moveButton, deleteButton, cancel);

				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == moveButton){
					// ... user chose "move"
					Button selectionMove = new Button();
					selectionMove.setStyle("-fx-background-color: #0000ff10");
					selectionMove.setLayoutX(selection.getLayoutX());
					selectionMove.setLayoutY(selection.getLayoutY());
					selectionMove.setPrefWidth(selection.getWidth());
					selectionMove.setPrefHeight(selection.getHeight());
					pane.getChildren().add(selectionMove);
					
					//changes cursor when moving selection and records distance moved
					selectionMove.setOnMousePressed(new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent mouseEvent) {
							selectionMove.setCursor(Cursor.MOVE);
							Record.selectX = selectionMove.getLayoutX() - mouseEvent.getSceneX();
							Record.selectY = selectionMove.getLayoutY() - mouseEvent.getSceneY();
							TextBox.prepMoveSelection(mouseEvent.getSceneX(), mouseEvent.getSceneY());
						}
					});

					//Moves selection when dragged 
					selectionMove.setOnMouseDragged(new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent mouseEvent) {
							selectionMove.setLayoutX(mouseEvent.getSceneX() + Record.selectX);
							selectionMove.setLayoutY(mouseEvent.getSceneY() + Record.selectY);
							TextBox.moveSelection(pane, selection, mouseEvent.getSceneX(), mouseEvent.getSceneY());
						}
					});
					
					//Moves all text boxes according to selection
					selectionMove.setOnMouseReleased(new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent mouseEvent) {
							pane.getChildren().remove(selectionMove);
							selectionMove.setPrefSize(0, 0);
						}
					});

				}
				else if (result.get() == deleteButton) {
					// ... user chose "delete"
					Record.deleteSelection(pane);
				}
				else if (result.get() == cancel) {
					// ... user chose "cancel"
				}
				
				selection.setWidth(0);selection.setHeight(0);
				
				TextBox.turnOnBoxMovement(pane);

				pane.getChildren().remove(selection);
			}
		};
		
		//initiate selection
		pane.setOnMousePressed(initSelection);

		//Updates selection
		pane.setOnMouseDragged(updateSelection);

		//implement selection
		pane.setOnMouseReleased(implementSelection);


		//text box adder (useless invisible button, only need it for TextBox class)------------------------------------------------------------------------------------------------------------
		Button textAdder = new Button("Add New Text Box");		
		textAdder.prefWidthProperty().bind(pane.widthProperty().multiply(15.0/100.0));
		textAdder.prefHeightProperty().bind(pane.heightProperty().multiply(5.0/100.0));
		textAdder.layoutXProperty().bind(pane.widthProperty().multiply(0));
		textAdder.layoutYProperty().bind(pane.heightProperty().multiply(5.0/100.0));
		
//		textAdder.setOnMouseClicked(new EventHandler<MouseEvent>() {
//			@Override
//			public void handle(MouseEvent event) {
//				if (event.getButton() == MouseButton.PRIMARY) {
//					TextBox b = new TextBox(pane, textAdder, "New Text Box", circleL, circleR, intersection, leftCircle, rightCircle, p, selection);
//				}
//			}
//		});

		//multiple text box adder ------------------------------------------------------------------------------------------------------------
//		Button multAdder = new Button("Add Mulitple New Text Boxes");		
//		multAdder.prefWidthProperty().bind(pane.widthProperty().multiply(15.0/100.0));
//		multAdder.prefHeightProperty().bind(pane.heightProperty().multiply(5.0/100.0));
//		multAdder.layoutXProperty().bind(pane.widthProperty().multiply(0));
//		multAdder.layoutYProperty().bind(pane.heightProperty().multiply(0));
//		multAdder.setOnMouseClicked(new EventHandler<MouseEvent>() {
//			@Override
//			public void handle(MouseEvent event) {
//				final Stage dialog = new Stage();
//				dialog.initModality(Modality.APPLICATION_MODAL);
//				dialog.initOwner(stage);
//				VBox layout = new VBox(10);

//				listView = new ListView<>(FXCollections.observableArrayList());
//				listView.setEditable(true);
//				listView.prefWidthProperty().bind(pane.widthProperty().multiply(15.0/100.0));
//				listView.prefHeightProperty().bind(pane.widthProperty().multiply(15.0/100.0));
//				listView.setCellFactory(TextFieldListCell.forListView());		
//
//				listView.setOnEditCommit(new EventHandler<ListView.EditEvent<String>>() {
//					@Override
//					public void handle(ListView.EditEvent<String> t) {
//						listView.getItems().set(t.getIndex(), t.getNewValue());
//					}
//
//				});
//
//				listView.setOnEditCancel(new EventHandler<ListView.EditEvent<String>>() {
//					@Override
//					public void handle(ListView.EditEvent<String> t) {
//						//Do nothing
//					}
//				});
//				listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
//
//				//add cell to list button
//				Button addText = new Button("Add to List");
//				addText.setLayoutX(screenBounds.getMaxX());
//				addText.setLayoutY(screenBounds.getMaxY());
//				addText.setPrefWidth(listView.getPrefWidth());
//				addText.setOnMouseClicked(new EventHandler<MouseEvent>() {
//					@Override
//					public void handle(MouseEvent event) {
//						String c = new String("Enter Text");
//						listView.getItems().add(listView.getItems().size(), c);
//						listView.scrollTo(c);
//						listView.edit(listView.getItems().size() - 1);
//					}
//
//				});
//
//				//delete cell from list button
//				Button deleteText = new Button("Delete");
//				deleteText.setPrefWidth(listView.getPrefWidth());
//				deleteText.setOnMouseClicked(new EventHandler<MouseEvent>() {
//					@Override
//					public void handle(MouseEvent event) {
//						final int selectedIdx = listView.getSelectionModel().getSelectedIndex();
//						listView.getItems().remove(selectedIdx);
//					}
//				});
//				
//				//clear list button
//				Button clear = new Button("Clear");
//				clear.setPrefWidth(listView.getPrefWidth());
//				clear.setOnMouseClicked(new EventHandler<MouseEvent>() {
//					@Override
//					public void handle(MouseEvent event) {
//						listView.getItems().clear();
//					}
//				});
//
//				//make all cells text boxes button
//				Button finish = new Button("Convert All Into Text Boxes");
//				finish.setPrefWidth(listView.getPrefWidth());
//				finish.setOnMouseClicked(new EventHandler<MouseEvent>() {
//					@Override
//					public void handle(MouseEvent event) {
//						ObservableList<String> topics;
//						String list= "";
//						topics = listView.getItems();
//
//						for (int i = 0; i < topics.size(); i++) {
//							TextBox b = new TextBox(pane, textAdder, topics.get(i), circleL, circleR, intersection, leftCircle, rightCircle, p, selection);
//						}
//						listView.getItems().clear();
//					}
//
//				});
//				
//
//				layout.setPadding(new Insets(20,20,20,20));
//				layout.getChildren().addAll(listView, addText, deleteText, clear, finish);
//				Scene dialogScene = new Scene(layout, 300, 500);

//				dialog.setScene(dialogScene);
//				dialog.show();
//			}
//		});


		//New MultAdder ------------------------------------------------------------------------------------------------------------
		
		AnchorPane multAdd = new AnchorPane();
		multAdd.layoutXProperty().bind(pane.widthProperty().multiply(1.0/100.0));
		multAdd.layoutYProperty().bind(pane.heightProperty().multiply(2.0/100.0));
		multAdd.prefWidthProperty().bind(pane.widthProperty().multiply(10.0/100.0));
		multAdd.setStyle("-fx-background-color: linear-gradient(to right, #BBD2C5, #536976);" + 
						 "-fx-background-radius: 5;" );
		
		
		
			//Color Pickers
			ColorPicker boxcp = new ColorPicker(Color.GREY); 
			ColorPicker fontcp = new ColorPicker(Color.BLACK);
			boxcp.prefWidthProperty().bind(multAdd.prefWidthProperty());
			fontcp.prefWidthProperty().bind(multAdd.prefWidthProperty());
			
			Text txtbox = new Text("Box Color ");		
			txtbox.setFont(new Font(12));
			txtbox.setStroke(Color.BLACK);
			txtbox.setTextAlignment(TextAlignment.CENTER);
			
			Text txtfont = new Text("Font Color");
			txtfont.setFont(new Font(12));
			txtfont.setStroke(Color.BLACK);
			txtfont.setTextAlignment(TextAlignment.CENTER);
		
			//text field functions
			TextField text = new TextField();
			text.prefWidthProperty().bind(multAdd.prefWidthProperty());
			
			text.setOnKeyPressed(new EventHandler<KeyEvent>()
		    {
		        @Override
		        public void handle(KeyEvent ke)
		        {
		            if (ke.getCode().equals(KeyCode.ENTER))
		            {
		            	TextBox b = new TextBox(pane, textAdder, text.getText(), circleL, circleR, intersection, leftCircle, rightCircle, p, selection, boxcp.getValue(), fontcp.getValue());
		            	text.clear();
		            }
		        }
		    });
			
		HBox boxhb = new HBox(txtbox, boxcp);
			boxhb.setSpacing(5);
		HBox fonthb = new HBox(txtfont,fontcp);
			fonthb.setSpacing(5);
		
		VBox vb = new VBox(text,boxhb,fonthb);	
		vb.setPadding(new Insets(10));
		
		multAdd.getChildren().addAll(vb);
	
		
		
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


		//Screen-shot implementation -----------------------------------------------------------------------------------------------------------
		Button capture = new Button("Take Screenshot of Venn Diagram!");
		capture.layoutXProperty().bind(pane.widthProperty().multiply(80.0/100.0));
		capture.layoutYProperty().bind(pane.heightProperty().multiply(8.0/100.0));
		capture.prefWidthProperty().bind(pane.widthProperty().multiply(20.0/100.0));
		capture.prefHeightProperty().bind(pane.heightProperty().multiply(5.0/100.0));
		capture.setOnAction(event -> createScreenshot(pane));

		//Adds items to the window -----------------------------------------------------------------------------------------------
		pane.getChildren().add(circleR);
		pane.getChildren().add(circleL);
		pane.getChildren().addAll(cp1, cp2);
//		pane.getChildren().add(textAdder);
		pane.getChildren().add(anchorOption);
		pane.getChildren().add(capture);
//		pane.getChildren().add(layout);
		pane.getChildren().add(multAdd);

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
	
	
	//Code from https://code.makery.ch/blog/javafx-2-snapshot-as-png-image/
	public void createScreenshot(Pane pane) {
		WritableImage image = pane.snapshot(new SnapshotParameters(), null);
		File file = new File("Venn Diagram.png");

		try {
			ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}
