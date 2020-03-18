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
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;

@SuppressWarnings("unused")
public class VennBase extends Application	 {

	static boolean debug = false;
	static boolean anchor = false;
	ListView<String> listView;
	static FileHandling autoSaveFile;
	static Text ctrl = new Text("Control Selection Mode: OFF");

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override 
	public void start(Stage stage) {
		
		//Create file
		autoSaveFile = new FileHandling();
		autoSaveFile.CreateFile("VennApplicationAutoSave.txt");

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
		Color black = new Color(Color.BLACK.getRed(), Color.BLACK.getGreen(), Color.BLACK.getBlue(), 0.5);
		Color grey = new Color(179.0/255.0, 179.0/255.0, 179.0/255.0, 1);
		
		
		//background
		BackgroundFill backgroundColor = new BackgroundFill(black, null, null);
		Background background = new Background(backgroundColor);
		pane.setBackground(background);
		autoSaveFile.WriteToFile("BColor "+black.getRed()+" "+black.getGreen()+" "+black.getBlue());
		
		
		//Right venn circle
		Circle circleR = new Circle();
		circleR.centerXProperty().bind(pane.widthProperty().divide(5.0/3.0));
		circleR.centerYProperty().bind(pane.heightProperty().divide(2.0));
		circleR.radiusProperty().bind(pane.widthProperty().divide(5.0));
		circleR.setStroke(Color.BLUE);
		circleR.setFill(blue);
		autoSaveFile.WriteToFile("RColor "+blue.getRed()+" "+blue.getGreen()+" "+blue.getBlue());

		//Left venn circle
		Circle circleL= new Circle();
		circleL.centerXProperty().bind(pane.widthProperty().divide(5.0/2.0));
		circleL.centerYProperty().bind(pane.heightProperty().divide(2.0));
		circleL.radiusProperty().bind(pane.widthProperty().divide(5.0));
		circleL.setStroke(Color.RED);
		circleL.setFill(red);
		autoSaveFile.WriteToFile("LColor "+red.getRed()+" "+red.getGreen()+" "+red.getBlue());

		//color picker setup ------------------------------------------------------------------------------------------------------------
		final ColorPicker cp1 = new ColorPicker(Color.BLUE);
		final ColorPicker cp2 = new ColorPicker(Color.RED);
		final ColorPicker cp3 = new ColorPicker(Color.BLACK);
		cp1.layoutYProperty().bind(pane.heightProperty().subtract(25));
		cp2.layoutYProperty().bind(pane.heightProperty().subtract(51));
		cp3.layoutYProperty().bind(pane.heightProperty().subtract(77));
		cp1.prefWidthProperty().bind(pane.widthProperty().multiply(10.0/100.0));
		cp2.prefWidthProperty().bind(pane.widthProperty().multiply(10.0/100.0));
		cp3.prefWidthProperty().bind(pane.widthProperty().multiply(10.0/100.0));
		cp1.layoutXProperty().bind(pane.widthProperty().multiply(90.0/100.0));
		cp2.layoutXProperty().bind(pane.widthProperty().multiply(90.0/100.0));
		cp3.layoutXProperty().bind(pane.widthProperty().multiply(90.0/100.0));
		cp1.setStyle("-fx-background-color: #b3b3b3");
		cp2.setStyle("-fx-background-color: #b3b3b3");
		cp3.setStyle("-fx-background-color: #b3b3b3");
		
		cp1.setOnAction(new EventHandler() {
			@Override
			public void handle(javafx.event.Event event) {
				Color col1 = new Color(cp1.getValue().getRed(), cp1.getValue().getGreen(), cp1.getValue().getBlue(), 0.5);
				autoSaveFile.overwriteLineInFile("RColor ", "RColor "+col1.getRed()+" "+col1.getGreen()+" "+col1.getBlue());
				circleR.setFill(col1);
				col1.saturate();
				col1.saturate();
				col1.saturate();
				circleR.setStroke(col1);
				System.out.println(colorToHex(cp1.getValue()));
			}
		});
		
		cp2.setOnAction(new EventHandler() {
			@Override
			public void handle(javafx.event.Event event) {
				Color col2 = new Color(cp2.getValue().getRed(), cp2.getValue().getGreen(), cp2.getValue().getBlue(), 0.5);
				autoSaveFile.overwriteLineInFile("LColor ", "LColor "+col2.getRed()+" "+col2.getGreen()+" "+col2.getBlue());
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
				Color col3 = new Color(cp3.getValue().getRed(), cp3.getValue().getGreen(), cp3.getValue().getBlue(), 0.5);
				autoSaveFile.overwriteLineInFile("BColor ", "BColor "+col3.getRed()+" "+col3.getGreen()+" "+col3.getBlue());
				BackgroundFill backgroundColor = new BackgroundFill(col3, null, null);
				Background background = new Background(backgroundColor);
				pane.setBackground(background);
			}
		});

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

		p.l1 = new Point(paneWidth*0.29766, paneHeight*0.16717);
		p.l2 = new Point(paneWidth*0.26875, p.l1.yValue+paneHeight*0.0608);
		p.l3 = new Point(paneWidth*0.24375, p.l2.yValue+paneHeight*0.0608);
		p.l4 = new Point(paneWidth*0.22422, p.l3.yValue+paneHeight*0.0608);
		p.l5 = new Point(paneWidth*0.21406, p.l4.yValue+paneHeight*0.0608);
		p.l6 = new Point(paneWidth*0.20625, p.l5.yValue+paneHeight*0.0608);
		p.l7 = new Point(paneWidth*0.21406, p.l6.yValue+paneHeight*0.0608);
		p.l8 = new Point(paneWidth*0.22422, p.l7.yValue+paneHeight*0.0608);
		p.l9 = new Point(paneWidth*0.24375, p.l8.yValue+paneHeight*0.0608);
		p.l10 = new Point(paneWidth*0.26875, p.l9.yValue+paneHeight*0.0608);
		p.l11 = new Point(paneWidth*0.29766, p.l10.yValue+paneHeight*0.0608);

		p.i1 = new Point(paneWidth*0.41875, paneHeight*0.33739);
		p.i2 = new Point(paneWidth*0.41875, p.i1.yValue+paneHeight*0.05471);
		p.i3 = new Point(paneWidth*0.41875, p.i2.yValue+paneHeight*0.05471);
		p.i4 = new Point(paneWidth*0.41875, p.i3.yValue+paneHeight*0.05471);
		p.i5 = new Point(paneWidth*0.41875, p.i4.yValue+paneHeight*0.05471);
		p.i6 = new Point(paneWidth*0.41875, p.i5.yValue+paneHeight*0.05471);
		
		p.r1 = new Point(paneWidth*0.53750, paneHeight*0.16717);
		p.r2 = new Point(paneWidth*0.56484, p.r1.yValue+paneHeight*0.0608);
		p.r3 = new Point(paneWidth*0.58750, p.r2.yValue+paneHeight*0.0608);
		p.r4 = new Point(paneWidth*0.60234, p.r3.yValue+paneHeight*0.0608);
		p.r5 = new Point(paneWidth*0.61563, p.r4.yValue+paneHeight*0.0608);
		p.r6 = new Point(paneWidth*0.62734, p.r5.yValue+paneHeight*0.0608);
		p.r7 = new Point(paneWidth*0.61563, p.r6.yValue+paneHeight*0.0608);
		p.r8 = new Point(paneWidth*0.60234, p.r7.yValue+paneHeight*0.0608);
		p.r9 = new Point(paneWidth*0.58750, p.r8.yValue+paneHeight*0.0608);
		p.r10 = new Point(paneWidth*0.57484, p.r9.yValue+paneHeight*0.0608);
		p.r11 = new Point(paneWidth*0.54750, p.r10.yValue+paneHeight*0.0608);

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

		
		//Anchor option button  ----------------------------------------------------------------------------------------------------------------------------
		autoSaveFile.WriteToFile("Anchoring "+"off");
		Button anchorOption = new Button("Anchoring off");
		anchorOption.prefWidthProperty().bind(pane.widthProperty().multiply(20.0/100.0));
		anchorOption.prefHeightProperty().bind(pane.heightProperty().multiply(5.0/100.0));
		anchorOption.layoutXProperty().bind(pane.widthProperty().multiply(80.0/100.0));
		anchorOption.setStyle("-fx-background-color: #b3b3b3");
		anchorOption.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (VennBase.anchor) {VennBase.anchor = false;anchorOption.setText("Anchoring off");autoSaveFile.overwriteLineInFile("Anchoring ", "Anchoring "+"off");}
				else {VennBase.anchor = true;anchorOption.setText("Anchoring on");autoSaveFile.overwriteLineInFile("Anchoring ", "Anchoring "+"on");}
			}
		});


		//selection ----------------------------------------------------------------------------------------------------------------------------
		Rectangle selection = new Rectangle();
		selection.setFill(blue.desaturate().desaturate());
		
		//Event handlers
		EventHandler<MouseEvent> initSelection = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && !TextBox.ctrlSelection) {
					selection.setLayoutX(mouseEvent.getSceneX());
					selection.setLayoutY(mouseEvent.getSceneY());
					pane.getChildren().add(selection);
					TextBox.turnOffBoxMovement();
				}
			}
		};
		
		EventHandler<MouseEvent> updateSelection = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (!TextBox.ctrlSelection) {
					selection.setWidth(mouseEvent.getSceneX()-selection.getLayoutX());
					selection.setHeight(mouseEvent.getSceneY()-selection.getLayoutY());
					if (selection.getWidth()<0) {selection.setLayoutX(mouseEvent.getSceneX());selection.setWidth(0);}
					if (selection.getHeight()<0) {selection.setLayoutY(mouseEvent.getSceneY());selection.setHeight(0);}
				}
			}
		};

		EventHandler<MouseEvent> implementSelection = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && !TextBox.ctrlSelection) {
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Selection Actions");
					alert.setHeaderText("What would you like to do with the selected text boxes?");

					ButtonType moveButton = new ButtonType("Move");
					ButtonType deleteButton = new ButtonType("Delete");
					ButtonType cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

					alert.getButtonTypes().setAll(moveButton, deleteButton, cancel);

					Optional<ButtonType> result = alert.showAndWait();
					if (result.get() == moveButton){
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
								TextBox.moveSelection(pane, mouseEvent.getSceneX(), mouseEvent.getSceneY(), autoSaveFile);
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
						Record.deleteSelection(pane, autoSaveFile);
					}
					else if (result.get() == cancel) {
					}

					selection.setWidth(0);selection.setHeight(0);

					TextBox.turnOnBoxMovement(pane);

					pane.getChildren().remove(selection);
				}
			}
		};

		//initiate selection
		pane.setOnMousePressed(initSelection);

		//Updates selection
		pane.setOnMouseDragged(updateSelection);

		//implement selection
		pane.setOnMouseReleased(implementSelection);

		
		//Save button
		
		
		//text box adder ------------------------------------------------------------------------------------------------------------
		Button textAdder = new Button("Add New Text Box");		
		textAdder.prefWidthProperty().bind(pane.widthProperty().multiply(20.0/100.0));
		textAdder.prefHeightProperty().bind(pane.heightProperty().multiply(9.5/100.0));
		textAdder.layoutXProperty().bind(pane.widthProperty().multiply(0));
		textAdder.layoutYProperty().bind(pane.heightProperty().multiply(0));
		textAdder.setStyle("-fx-background-color: #b3b3b3");

		textAdder.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getButton() == MouseButton.PRIMARY) {
					TextBox b = new TextBox(pane, textAdder, "New Text Box", circleL, circleR, intersection, leftCircle, rightCircle, p, selection, autoSaveFile);
				}
			}
		});

		//multiple text box adder ------------------------------------------------------------------------------------------------------------
		Button multAdder = new Button("Add Mulitple New Text Boxes");		
		multAdder.prefWidthProperty().bind(pane.widthProperty().multiply(20.0/100.0));
		multAdder.prefHeightProperty().bind(pane.heightProperty().multiply(10.0/100.0));
		multAdder.layoutXProperty().bind(pane.widthProperty().multiply(0));
		multAdder.layoutYProperty().bind(pane.heightProperty().multiply(10.0/100.0));
		multAdder.setStyle("-fx-background-color: #b3b3b3");
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
					public void handle(ListView.EditEvent<String> t) {}
				});
				listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

				//add cell to list button
				Button addText = new Button("Add One Text to List");
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
				Button deleteText = new Button("Delete Selected Text from List");

				deleteText.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						final int selectedIdx = listView.getSelectionModel().getSelectedIndex();
						listView.getItems().remove(selectedIdx);


						//Still in process of integrating multiple selected deletes

						//ObservableList<Integer> selectedCells;
						//selectedCells = listView.getSelectionModel().getSelectedIndices();
						//System.out.println(selectedCells);

						//for (int i = 0; i <= selectedCells.size(); i++) {
						//	listView.getItems().remove(i);
						//}
					}
				});

				//make all cells text boxes button
				Button finish = new Button("Convert All Texts in List Into Text Boxes");
				finish.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						ObservableList<String> topics;
						String list= "";
						topics = listView.getItems();

						for (int i = 0; i < topics.size(); i++) {
							TextBox b = new TextBox(pane, textAdder, topics.get(i), circleL, circleR, intersection, leftCircle, rightCircle, p, selection, autoSaveFile);
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
		autoSaveFile.WriteToFile("Title "+"Title");
		Text title = new Text("Title");
		title.setFont(new Font(20));
		title.setStroke(Color.BLACK);
		//title.getText().length()*4 : text half length
		title.layoutXProperty().bind(pane.widthProperty().divide(2).subtract(title.getText().length()*4));
		title.setLayoutY(25);

		title.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getButton() == MouseButton.SECONDARY) {
					TextInputDialog dialog = new TextInputDialog("Enter Venn diagram title");
					dialog.setTitle("Change title");
					dialog.setHeaderText("Enter to change title");
					dialog.setContentText("Please enter the new title:");
					String result = dialog.showAndWait().get();
					title.setText(result);
					title.layoutXProperty().bind(pane.widthProperty().divide(2).subtract(title.getText().length()*4));
					autoSaveFile.overwriteLineInFile("Title ", "Title "+title.getText());
				}
			}
		});
		
		autoSaveFile.WriteToFile("Right "+"right");
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
					TextInputDialog dialog = new TextInputDialog("Enter right circle title");
					dialog.setTitle("Change text");
					dialog.setHeaderText("Enter to change text");
					dialog.setContentText("Please enter some text:");
					String result = dialog.showAndWait().get();
					right.setText(result);
					right.layoutXProperty().bind(pane.widthProperty().divide(5.0/3.0).subtract(right.getText().length()*5/2));
					autoSaveFile.overwriteLineInFile("Right ", "Right "+right.getText());
				}
			}
		});
		
		autoSaveFile.WriteToFile("Left "+"left");
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
					TextInputDialog dialog = new TextInputDialog("Enter left circle title");
					dialog.setTitle("Change text");
					dialog.setHeaderText("Enter to change text");
					dialog.setContentText("Please enter some text:");
					String result = dialog.showAndWait().get();
					left.setText(result);
					left.layoutXProperty().bind(pane.widthProperty().divide(5.0/2.0).subtract(left.getText().length()*5/2));
					autoSaveFile.overwriteLineInFile("Left ", "Left "+left.getText());
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

		//Background color picker label
		Text cpB = new Text("Background color : ");
		cpB.setFont(new Font(12));
		cpB.setStroke(Color.BLACK);
		cpB.setTextAlignment(TextAlignment.CENTER);
		cpB.layoutXProperty().bind(cp3.layoutXProperty().subtract(100));
		cpB.layoutYProperty().bind(cp3.layoutYProperty().add(15));

		//control selection mode label
		ctrl.setFont(new Font(12));
		ctrl.setStroke(Color.BLACK);
		ctrl.setTextAlignment(TextAlignment.CENTER);
		ctrl.layoutXProperty().bind(pane.widthProperty().multiply(0.0/100.0));
		ctrl.layoutYProperty().bind(pane.heightProperty().multiply(98.0/100.0));


		//Reset ------------------------------------------------------------------------------------------------------------
		Button reset = new Button("Reset application");
		reset.layoutXProperty().bind(pane.widthProperty().multiply(80.0/100.0));
		reset.layoutYProperty().bind(pane.heightProperty().multiply(16.0/100.0));
		reset.prefWidthProperty().bind(pane.widthProperty().multiply(20.0/100.0));
		reset.prefHeightProperty().bind(pane.heightProperty().multiply(5.0/100.0));
		reset.setStyle("-fx-background-color: #b3b3b3");
		reset.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getButton() == MouseButton.PRIMARY) {
					circleR.setFill(blue);
					circleL.setFill(red);
					if (VennBase.anchor) {VennBase.anchor = false;anchorOption.setText("Anchoring off");}
					title.setText("Title");
					right.setText("right");
					left.setText("left");
					Record.deleteAll(pane, autoSaveFile);
				}
			}
		});


		//Import ------------------------------------------------------------------------------------------------------------
		Button importB = new Button("Import");
		importB.layoutXProperty().bind(pane.widthProperty().multiply(80.0/100.0));
		importB.layoutYProperty().bind(pane.heightProperty().multiply(24.0/100.0));
		importB.prefWidthProperty().bind(pane.widthProperty().multiply(20.0/100.0));
		importB.prefHeightProperty().bind(pane.heightProperty().multiply(5.0/100.0));
		importB.setStyle("-fx-background-color: #b3b3b3");
		importB.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getButton() == MouseButton.PRIMARY) {
					FileChooser fc = new FileChooser();
					fc.setTitle("Import Venn Diagram");
					fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Venn diagram save file", "*.txt"));
					try {
						File file = fc.showOpenDialog(stage);
						fc.setInitialDirectory(file.getParentFile());
						//loadImport(File file, Pane pane, Circle circleR, Circle circleL, Button anchorOption, Text title, Text right, Text left, Button textAdder, Anchor intersection, Anchor leftCircle, Anchor rightCircle, Points p, Rectangle selection)
						FileHandling.loadImport(file, pane, circleR, circleL, anchorOption, title, right, left, textAdder, intersection, leftCircle, rightCircle, p, selection);
					}
					catch(Exception e){System.out.println(e);}
				}
			}
		});


		//Export ------------------------------------------------------------------------------------------------------------
		Button exportB = new Button("Export");
		exportB.layoutXProperty().bind(pane.widthProperty().multiply(80.0/100.0));
		exportB.layoutYProperty().bind(pane.heightProperty().multiply(32.0/100.0));
		exportB.prefWidthProperty().bind(pane.widthProperty().multiply(20.0/100.0));
		exportB.prefHeightProperty().bind(pane.heightProperty().multiply(5.0/100.0));
		exportB.setStyle("-fx-background-color: #b3b3b3");
		exportB.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getButton() == MouseButton.PRIMARY) {
					FileChooser fc = new FileChooser();
					fc.setTitle("Export Venn Diagram");
					fc.setInitialFileName("VennSave");
					fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Venn diagram save file", "*.txt"));
					try {
						FileHandling fh = new FileHandling();
						fh.file = fc.showSaveDialog(stage);
						fc.setInitialDirectory(fh.file.getParentFile());
						FileHandling.copyFiles(autoSaveFile, fh);
						System.out.println("Created save file"+fh.file.getName()+"!");
					}
					catch(Exception e){System.out.println(e);}
				}
			}
		});


		//Screen-shot implementation -----------------------------------------------------------------------------------------------------------
		Button capture = new Button("Take Screenshot of Venn Diagram");
		capture.layoutXProperty().bind(pane.widthProperty().multiply(80.0/100.0));
		capture.layoutYProperty().bind(pane.heightProperty().multiply(8.0/100.0));
		capture.prefWidthProperty().bind(pane.widthProperty().multiply(20.0/100.0));
		capture.prefHeightProperty().bind(pane.heightProperty().multiply(5.0/100.0));
		capture.setStyle("-fx-background-color: #b3b3b3");
		capture.setOnAction(event -> createScreenshot(pane, cp1, cp2, cp3, capture, multAdder, textAdder, anchorOption, exportB, importB, reset, cpR, cpL, cpB));


		//Adds items to the window -----------------------------------------------------------------------------------------------
		pane.getChildren().add(circleR);
		pane.getChildren().add(circleL);
		pane.getChildren().addAll(cp1, cp2, cp3);
		pane.getChildren().add(textAdder);
		pane.getChildren().add(anchorOption);
		pane.getChildren().add(capture);
		pane.getChildren().add(multAdder);
		pane.getChildren().add(reset);;
		pane.getChildren().add(importB);
		pane.getChildren().add(exportB);

		//Adds titles to window
		pane.getChildren().add(title);
		pane.getChildren().add(left);
		pane.getChildren().add(right);
		pane.getChildren().addAll(cpR, cpL, cpB);
		pane.getChildren().add(ctrl);


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
		Text resetButton = new Text();
		resetButton.setLayoutY(300);

		pane.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.CONTROL) {
					if (TextBox.ctrlSelection) {TextBox.releaseSelection();}
					else {TextBox.ctrlSelection=true;ctrl.setText("Control Selection Mode: ON");}
				}
				if (event.getCode() == KeyCode.F3) {
					if (VennBase.debug) {
						VennBase.debug=false;
						pane.getChildren().removeAll(screen_bounds, pane_bounds, cp1data, cp2data, textAdderData, titleData, rightData, leftData, cpLData, cpRData, captureButton, resetButton);
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
						resetButton.setText("reset: "+reset.getLayoutX()+", "+reset.getLayoutY());

						VennBase.debug=true;
						pane.getChildren().addAll(screen_bounds, pane_bounds, cp1data, cp2data, textAdderData, titleData, rightData, leftData, cpLData, cpRData, captureButton, resetButton);
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
		
		
		
	}//Main end
	
	@Override
	public void stop(){
	    FileHandling autoSave = new FileHandling();
	    autoSave.CreateFile("autoSave.txt");
	    FileHandling.copyFiles(autoSaveFile, autoSave);
	}
	
	
	public void createScreenshot(Pane pane, ColorPicker cp1, ColorPicker cp2, ColorPicker cp3, Button textAdder, Button anchorOption, Button capture, Button multAdder, Button reset, Button importB, Button exportB, Text cpR, Text cpL, Text cpB) {
		pane.getChildren().addAll(cpR, cpL, cpB);
		pane.getChildren().removeAll(cp1, cp2, cp3);
		pane.getChildren().remove(textAdder);
		pane.getChildren().remove(anchorOption);
		pane.getChildren().remove(capture);
		pane.getChildren().remove(multAdder);
		pane.getChildren().remove(reset);;
		pane.getChildren().remove(importB);
		pane.getChildren().remove(exportB);
		
		WritableImage image = pane.snapshot(new SnapshotParameters(), null);
		
		FileChooser fc = new FileChooser();
		Window stage = pane.getScene().getWindow();
		fc.setTitle("Save screenshot");
		fc.setInitialFileName("Venn Diagram");
		fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Venn diagram", "*.png"));
		
		try {
			File png = fc.showSaveDialog(stage);
			fc.setInitialDirectory(png.getParentFile());
			ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", png);
			System.out.println("Screenshot saved!");
		}
		catch(Exception e){System.out.println(e);}
		
		pane.getChildren().addAll(cp1, cp2, cp3);
		pane.getChildren().add(textAdder);
		pane.getChildren().add(anchorOption);
		pane.getChildren().add(capture);
		pane.getChildren().add(multAdder);
		pane.getChildren().add(reset);;
		pane.getChildren().add(importB);
		pane.getChildren().add(exportB);
		pane.getChildren().addAll(cpR, cpL, cpB);
	}
	
	//code from https://stackoverflow.com/questions/17925318/how-to-get-hex-web-string-from-javafx-colorpicker-color/35814669
	public String colorToHex(Color color) {
	    String hex1;
	    String hex2;

	    hex1 = Integer.toHexString(color.hashCode()).toUpperCase();

	    switch (hex1.length()) {
	    case 2:
	        hex2 = "000000";
	        break;
	    case 3:
	        hex2 = String.format("00000%s", hex1.substring(0,1));
	        break;
	    case 4:
	        hex2 = String.format("0000%s", hex1.substring(0,2));
	        break;
	    case 5:
	        hex2 = String.format("000%s", hex1.substring(0,3));
	        break;
	    case 6:
	        hex2 = String.format("00%s", hex1.substring(0,4));
	        break;
	    case 7:
	        hex2 = String.format("0%s", hex1.substring(0,5));
	        break;
	    default:
	        hex2 = hex1.substring(0, 6);
	    }
	    return hex2;
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}
