package venn;

//imports
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import javax.imageio.ImageIO;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import javafx.scene.control.MenuBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
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
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
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
import javafx.util.Duration;

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
		autoSaveFile.CreateFile("DoNotOpen.txt");

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
		
		//Right Side Panel
//		Pane rightSide = new Pane();

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
		final ColorPicker cp4 = new ColorPicker(Color.BLACK);
//		cp1.layoutYProperty().bind(pane.heightProperty().subtract(25));
//		cp2.layoutYProperty().bind(pane.heightProperty().subtract(51));
//		cp3.layoutYProperty().bind(pane.heightProperty().subtract(77));
//		cp4.layoutYProperty().bind(pane.heightProperty().subtract(103));
//		cp1.layoutXProperty().bind(pane.widthProperty().multiply(90.0/100.0));
//		cp2.layoutXProperty().bind(pane.widthProperty().multiply(90.0/100.0));
//		cp3.layoutXProperty().bind(pane.widthProperty().multiply(90.0/100.0));
//		cp4.layoutXProperty().bind(pane.widthProperty().multiply(90.0/100.0));
		cp1.prefWidthProperty().bind(pane.widthProperty().multiply(10.0/100.0));
		cp2.prefWidthProperty().bind(pane.widthProperty().multiply(10.0/100.0));
		cp3.prefWidthProperty().bind(pane.widthProperty().multiply(10.0/100.0));
		cp4.prefWidthProperty().bind(pane.widthProperty().multiply(10.0/100.0));
		cp1.setStyle("-fx-background-color: #999999");
		cp2.setStyle("-fx-background-color: #999999");
		cp3.setStyle("-fx-background-color: #999999");
		cp4.setStyle("-fx-background-color: #999999");
		
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
		

		autoSaveFile.WriteToFile("BuColor 999999");
		
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
		autoSaveFile.WriteToFile("Anchoring off");
		Button anchorOption = new Button("Anchoring off");
		anchorOption.prefWidthProperty().bind(pane.widthProperty().multiply(20.0/100.0));
		anchorOption.prefHeightProperty().bind(pane.heightProperty().multiply(5.0/100.0));
//		anchorOption.layoutXProperty().bind(pane.widthProperty().multiply(80.0/100.0));
		anchorOption.setStyle("-fx-background-color: #999999");
		anchorOption.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (VennBase.anchor) {VennBase.anchor = false;anchorOption.setText("Anchoring off");autoSaveFile.overwriteLineInFile("Anchoring ", "Anchoring "+"off");Record.printAll();}
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
				if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
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
					selection.setWidth(mouseEvent.getSceneX()-selection.getLayoutX());
					selection.setHeight(mouseEvent.getSceneY()-selection.getLayoutY());
					if (selection.getWidth()<0) {selection.setLayoutX(mouseEvent.getSceneX());selection.setWidth(0);}
					if (selection.getHeight()<0) {selection.setLayoutY(mouseEvent.getSceneY());selection.setHeight(0);}
			}
		};

		EventHandler<MouseEvent> implementSelection = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Selection Actions");
					alert.setHeaderText("What would you like to do with the selected text boxes?");

					ButtonType moveButton = new ButtonType("Move");
					ButtonType deleteButton = new ButtonType("Delete");
					ButtonType cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

					alert.getButtonTypes().setAll(moveButton, deleteButton, cancel);

					Optional<ButtonType> result = alert.showAndWait();
					if (result.get().equals(moveButton)){
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
								selectionMove.setPrefSize(0, 0);
								pane.getChildren().remove(selectionMove);
								TextBox.releaseMultiSelection();
								TextBox.releaseCheckAll(pane, circleL, circleR, intersection, leftCircle, rightCircle);
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
		
		
		//text box adder (useless invisible button, only need it for TextBox class)------------------------------------------------------------------------------------------------------------
		Button textAdder = new Button("Add New Text Box");		
		textAdder.prefWidthProperty().bind(pane.widthProperty().multiply(20.0/100.0));
		textAdder.prefHeightProperty().bind(pane.heightProperty().multiply(9.5/100.0));
		textAdder.layoutXProperty().bind(pane.widthProperty().multiply(0));
		textAdder.layoutYProperty().bind(pane.heightProperty().multiply(0));
		textAdder.setStyle("-fx-background-color: #999999");

		textAdder.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getButton() == MouseButton.PRIMARY) {
//					TextBox b = new TextBox(pane, textAdder, "New Text Box", circleL, circleR, intersection, leftCircle, rightCircle, p, selection, grey, Color.BLACK);
				}
			}
		});

		//New MultAdder ------------------------------------------------------------------------------------------------------------

		AnchorPane multAdd = new AnchorPane();
		VBox vb = new VBox();	
		multAdd.layoutXProperty().bind(pane.widthProperty().multiply(1.0/100.0));
		multAdd.layoutYProperty().bind(pane.heightProperty().multiply(2.0/100.0));
		multAdd.prefWidthProperty().bind(pane.widthProperty().multiply(10.0/100.0));
		multAdd.prefHeightProperty().bind(pane.heightProperty().multiply(15.0/100.0));
		multAdd.setStyle("-fx-background-color: white;" + 
				"-fx-background-radius: 5;" );

		//Color Pickers
		ColorPicker boxcp = new ColorPicker(Color.YELLOWGREEN);
		ColorPicker fontcp = new ColorPicker(Color.BLACK);
		boxcp.prefWidthProperty().bind(multAdd.prefWidthProperty());
		fontcp.prefWidthProperty().bind(multAdd.prefWidthProperty());

		//Texts
		Text txtbox = new Text("Box Color ");		
		txtbox.setFont(new Font(12));
		txtbox.setStroke(Color.BLACK);
		txtbox.setTextAlignment(TextAlignment.CENTER);

		Text txtfont = new Text("Font Color");
		txtfont.setFont(new Font(12));
		txtfont.setStroke(Color.BLACK);
		txtfont.setTextAlignment(TextAlignment.CENTER);
		
		//Add Button
		Button addbtn = new Button("+"); addbtn .prefWidthProperty().bind(multAdd.prefWidthProperty().multiply(19.0/100.0));
		addbtn.setStyle("-fx-background-radius: 30; -fx-background-color: #" + colorToHex(boxcp.getValue()) + ";");
		
		//Extra Section 
		Button plusbtn = new Button("More"); plusbtn.prefWidthProperty().bind(multAdd.prefWidthProperty().multiply(40.0/100.0));
		plusbtn.setStyle("-fx-background-radius: 30;"); //fix font color
		Button minusbtn = new Button("Less"); minusbtn.prefWidthProperty().bind(plusbtn.prefWidthProperty());
		minusbtn.setStyle("-fx-background-radius: 30;");
		
		TextArea xtraInfo = new TextArea();
		xtraInfo.prefWidthProperty().bind(multAdd.prefWidthProperty());
		xtraInfo.prefHeightProperty().bind(multAdd.prefHeightProperty());
		
		
		VBox xtravb = new VBox(minusbtn, xtraInfo);
		xtravb.setSpacing(5);
		
		plusbtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getButton() == MouseButton.PRIMARY) {
					vb.getChildren().add(xtravb);
					vb.getChildren().remove(plusbtn);
				}
			}
		});
		
		minusbtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getButton() == MouseButton.PRIMARY) {
					xtraInfo.clear();
					vb.getChildren().remove(xtravb);
					vb.getChildren().add(plusbtn);
				}
			}
		});
		
		//first text field functions
		TextField text = new TextField();
		text.prefWidthProperty().bind(multAdd.prefWidthProperty().add(30));
		
		text.setOnKeyPressed(new EventHandler<KeyEvent>()
		{
			@Override
			public void handle(KeyEvent ke)
			{
				if (ke.getCode().equals(KeyCode.ENTER))
				{
					TextBox b = new TextBox(pane, textAdder, text.getText(), circleL, circleR, intersection, leftCircle, rightCircle, p, selection, colorToHex(boxcp.getValue()), colorToHex(fontcp.getValue()), xtraInfo.getText());
					text.clear();
					xtraInfo.clear();
				}
			}
		});
		
		//+ button function
		addbtn.setOnMouseClicked(new EventHandler<MouseEvent>() 
		{
			@Override
			public void handle(MouseEvent event) {
				if (event.getButton() == MouseButton.PRIMARY) 
				{
					TextBox b = new TextBox(pane, textAdder, text.getText(), circleL, circleR, intersection, leftCircle, rightCircle, p, selection, colorToHex(boxcp.getValue()), colorToHex(fontcp.getValue()), xtraInfo.getText());
					text.clear();
					xtraInfo.clear();
				}
			}
		});

		
		//Putting multAdd together
		HBox tophb = new HBox(text, addbtn);
//		tophb.setAlignment(Pos.BASELINE_RIGHT);
		tophb.setSpacing(5);
		HBox boxhb = new HBox(txtbox, boxcp);
		boxhb.setSpacing(5);
		HBox fonthb = new HBox(txtfont,fontcp);
		fonthb.setSpacing(5);


		vb.getChildren().addAll(tophb,boxhb,fonthb, plusbtn);	
		vb.setPadding(new Insets(10));
		vb.setSpacing(5);

		multAdd.getChildren().addAll(vb);
	
		
		
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
		Text cpR = new Text("  Right circle color");
		cpR.setFont(new Font(12));
		cpR.setStroke(Color.BLACK);
//		cpR.layoutXProperty().bind(cp1.layoutXProperty().subtract(110));
//		cpR.layoutYProperty().bind(cp1.layoutYProperty().add(15));

		//Right circle color picker label
		Text cpL = new Text("  Left circle color");
		cpL.setFont(new Font(12));
		cpL.setStroke(Color.BLACK);
		cpL.setTextAlignment(TextAlignment.CENTER);
//		cpL.layoutXProperty().bind(cp2.layoutXProperty().subtract(110));
//		cpL.layoutYProperty().bind(cp2.layoutYProperty().add(15));

		//Background color picker label
		Text cpB = new Text("  Background color");
		cpB.setFont(new Font(12));
		cpB.setStroke(Color.BLACK);
		cpB.setTextAlignment(TextAlignment.CENTER);
//		cpB.layoutXProperty().bind(cp3.layoutXProperty().subtract(110));
//		cpB.layoutYProperty().bind(cp3.layoutYProperty().add(15));

		//Button color picker label
		Text cpBu = new Text("  Button color");
		cpBu.setFont(new Font(12));
		cpBu.setStroke(Color.BLACK);
		cpBu.setTextAlignment(TextAlignment.CENTER);
//		cpBu.layoutXProperty().bind(cp4.layoutXProperty().subtract(110));
//		cpBu.layoutYProperty().bind(cp4.layoutYProperty().add(15));

		//control selection mode label
		ctrl.setFont(new Font(12));
		ctrl.setStroke(Color.BLACK);
		ctrl.setTextAlignment(TextAlignment.CENTER);
		ctrl.layoutXProperty().bind(pane.widthProperty().multiply(0.0/100.0));
		ctrl.layoutYProperty().bind(pane.heightProperty().multiply(98.0/100.0));


		//Reset ------------------------------------------------------------------------------------------------------------
		Button reset = new Button("Reset");
//		reset.layoutXProperty().bind(pane.widthProperty().multiply(80.0/100.0));
//		reset.layoutYProperty().bind(pane.heightProperty().multiply(16.0/100.0));
		reset.prefWidthProperty().bind(pane.widthProperty().multiply(20.0/100.0));
		reset.prefHeightProperty().bind(pane.heightProperty().multiply(5.0/100.0));
		reset.setStyle("-fx-background-color: #999999");
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
					BackgroundFill backgroundColor = new BackgroundFill(black, null, null);
					Background background = new Background(backgroundColor);
					pane.setBackground(background);
					Record.numBoxes=0;
					Record.deleteAll(pane, autoSaveFile);
				}
			}
		});


		//Import ------------------------------------------------------------------------------------------------------------
		Button exportB = new Button("Export");
		Button capture = new Button("Screenshot");
		Button importB = new Button("Import");
//		importB.layoutXProperty().bind(pane.widthProperty().multiply(80.0/100.0));
//		importB.layoutYProperty().bind(pane.heightProperty().multiply(24.0/100.0));
		importB.prefWidthProperty().bind(pane.widthProperty().multiply(20.0/100.0));
		importB.prefHeightProperty().bind(pane.heightProperty().multiply(5.0/100.0));
		importB.setStyle("-fx-background-color: #999999");
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
						FileHandling.loadImport(file, pane, circleR, circleL, anchorOption, title, right, left, textAdder, intersection, leftCircle, rightCircle, p, selection, cp1, cp2, cp3, cp4, multAdd, reset, importB, exportB, capture);
					}
					catch(Exception e){System.out.println(e);}
				}
			}
		});


		//Export ------------------------------------------------------------------------------------------------------------
//		exportB.layoutXProperty().bind(pane.widthProperty().multiply(80.0/100.0));
//		exportB.layoutYProperty().bind(pane.heightProperty().multiply(32.0/100.0));
		exportB.prefWidthProperty().bind(pane.widthProperty().multiply(20.0/100.0));
		exportB.prefHeightProperty().bind(pane.heightProperty().multiply(5.0/100.0));
		exportB.setStyle("-fx-background-color: #999999;");
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
//		capture.layoutXProperty().bind(pane.widthProperty().multiply(80.0/100.0));
//		capture.layoutYProperty().bind(pane.heightProperty().multiply(8.0/100.0));
		capture.prefWidthProperty().bind(pane.widthProperty().multiply(20.0/100.0));
		capture.prefHeightProperty().bind(pane.heightProperty().multiply(5.0/100.0));
		capture.setStyle("-fx-background-color: #999999");
		capture.setOnAction(event -> createScreenshot(pane, cp1, cp2, cp3, cp4, textAdder, anchorOption, capture, multAdd, reset, importB, exportB, cpR, cpL, cpB, cpBu, ctrl));
		
		//Button color picker event
		cp4.setOnAction(new EventHandler() {
			@Override
			public void handle(javafx.event.Event event) {
				Color col4 = new Color(cp4.getValue().getRed(), cp4.getValue().getGreen(), cp4.getValue().getBlue(), 0.5);
				String hex = colorToHex(col4);
				changeButtonColor(hex, cp1, cp2, cp3, cp4, anchorOption, textAdder, reset, importB, exportB, capture);
			}
		});
		

		//Sliding menu -------------------------------------------------------------------------------------------------------------------------
		VBox menu = new VBox();
	    menu.prefHeightProperty().bind(pane.heightProperty());
	    menu.prefWidthProperty().bind(pane.widthProperty().multiply(20.0/100.0));
	    menu.setLayoutX(pane.getWidth()*.8);
//	    menu.layoutXProperty().bind(root.widthProperty().multiply(80.0/100.0)); //gets error message
	    menu.setSpacing(5);
		menu.setPadding(new Insets(10));
	    menu.setStyle("-fx-background-color: #b3b3b3; "
	    		+ "-fx-spacing: 20; "
	    		+ "-fx-background-radius: 5;");
	    
	    HBox rc = new HBox();
	    rc.getChildren().addAll(cp1, cpR);
	    HBox lc = new HBox();
	    lc.getChildren().addAll(cp2, cpL);
	    HBox ba = new HBox();
	    ba.getChildren().addAll(cp3, cpB);
	    HBox bu = new HBox();
	    bu.getChildren().addAll(cp4, cpBu);
	    
	    
	    menu.getChildren().addAll(anchorOption, capture, reset, importB, exportB, rc, lc, ba, bu);
	    
	    menu.setTranslateX(pane.getWidth()*.18);
	    TranslateTransition menuTranslation = new TranslateTransition(Duration.millis(500), menu);
	    
	    menuTranslation.setFromX(pane.getWidth()*.18);
	    menuTranslation.setToX(0);

	    menu.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				menuTranslation.setRate(1);
		        menuTranslation.play();
			}
		});
	    menu.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				menuTranslation.setRate(-1);
		        menuTranslation.play();
			}
		});
	    
	    anchorOption.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Record.prevStyle=anchorOption.getStyle();
				anchorOption.setStyle("-fx-background-color: #ffffff");
			}
		});
	    anchorOption.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				anchorOption.setStyle(Record.prevStyle);
			}
		});
	    capture.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Record.prevStyle=capture.getStyle();
				capture.setStyle("-fx-background-color: #ffffff");
			}
		});
	    capture.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				capture.setStyle(Record.prevStyle);
			}
		});
	    reset.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Record.prevStyle=reset.getStyle();
				reset.setStyle("-fx-background-color: #ffffff");
			}
		});
	    reset.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				reset.setStyle(Record.prevStyle);
			}
		});
	    importB.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Record.prevStyle=importB.getStyle();
				importB.setStyle("-fx-background-color: #ffffff");
			}
		});
	    importB.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				importB.setStyle(Record.prevStyle);
			}
		});
	    exportB.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Record.prevStyle=exportB.getStyle();
				exportB.setStyle("-fx-background-color: #ffffff");
			}
		});
	    exportB.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				exportB.setStyle(Record.prevStyle);
			}
		});
	    cp1.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Record.prevStyle=cp1.getStyle();
				cp1.setStyle("-fx-background-color: #ffffff");
			}
		});
	    cp1.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				cp1.setStyle(Record.prevStyle);
			}
		});
	    cp2.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Record.prevStyle=cp2.getStyle();
				cp2.setStyle("-fx-background-color: #ffffff");
			}
		});
	    cp2.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				cp2.setStyle(Record.prevStyle);
			}
		});
	    cp3.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Record.prevStyle=cp3.getStyle();
				cp3.setStyle("-fx-background-color: #ffffff");
			}
		});
	    cp3.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				cp3.setStyle(Record.prevStyle);
			}
		});
	    cp4.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Record.prevStyle=cp4.getStyle();
				cp4.setStyle("-fx-background-color: #ffffff");
			}
		});
	    cp4.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				cp4.setStyle(Record.prevStyle);
			}
		});


		//Adds items to the window -----------------------------------------------------------------------------------------------
		pane.getChildren().add(circleR);
		pane.getChildren().add(circleL);
//		pane.getChildren().addAll(cp1, cp2, cp3, cp4);
//		pane.getChildren().add(textAdder);
//		pane.getChildren().add(anchorOption);
//		pane.getChildren().add(capture);
//		pane.getChildren().add(layout);
		pane.getChildren().add(multAdd);
//		pane.getChildren().add(reset);;
//		pane.getChildren().add(importB);
//		pane.getChildren().add(exportB);
		pane.getChildren().add(menu);

		//Adds titles to window
		pane.getChildren().add(title);
		pane.getChildren().add(left);
		pane.getChildren().add(right);
//		pane.getChildren().addAll(cpR, cpL, cpB, cpBu);
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
				//control selection
				if (event.getCode() == KeyCode.CONTROL) {
					if (TextBox.ctrlSelection) {
						TextBox.releaseCtrlSelection();
						pane.setOnMouseMoved(maxScreen(stage));
						pane.setOnMousePressed(initSelection);
						pane.setOnMouseDragged(updateSelection);
						pane.setOnMouseReleased(implementSelection);
					}
					else {
						TextBox.ctrlSelection=true;
						ctrl.setText("Control Selection Mode: ON");
						pane.setOnMouseMoved(null);
						pane.setOnMousePressed(null);
						pane.setOnMouseDragged(null);
						pane.setOnMouseReleased(null);
					}
				}
				//debug
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

		pane.setOnMouseMoved(maxScreen(stage));
		
		
		
	}//Main end
	
	@Override
	public void stop(){
	    FileHandling autoSave = new FileHandling();
	    autoSave.CreateFile("autoSave.txt");
	    FileHandling.copyFiles(autoSaveFile, autoSave);
	}
	
	
	public void createScreenshot(Pane pane, ColorPicker cp1, ColorPicker cp2, ColorPicker cp3, ColorPicker cp4, Button textAdder, Button anchorOption, Button capture, AnchorPane multAdd, Button reset, Button importB, Button exportB, Text cpR, Text cpL, Text cpB, Text cpBu, Text ctrl) {
		System.out.println("Preparing screenshot..");
		pane.getChildren().removeAll(cpR, cpL, cpB, cpBu);
		pane.getChildren().removeAll(cp1, cp2, cp3, cp4);
		pane.getChildren().remove(textAdder);
		pane.getChildren().remove(anchorOption);
		pane.getChildren().remove(capture);
		pane.getChildren().remove(multAdd);
		pane.getChildren().remove(reset);;
		pane.getChildren().remove(importB);
		pane.getChildren().remove(exportB);
		pane.getChildren().remove(ctrl);
		
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
		
		pane.getChildren().addAll(cp1, cp2, cp3, cp4);
		pane.getChildren().add(textAdder);
		pane.getChildren().add(anchorOption);
		pane.getChildren().add(capture);
		pane.getChildren().add(multAdd);
		pane.getChildren().add(reset);;
		pane.getChildren().add(importB);
		pane.getChildren().add(exportB);
		pane.getChildren().addAll(cpR, cpL, cpB, cpBu);
		pane.getChildren().add(ctrl);
	}
	
	//code from https://stackoverflow.com/questions/17925318/how-to-get-hex-web-string-from-javafx-colorpicker-color/35814669
	public static String colorToHex(Color color) {
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
	
	public EventHandler<MouseEvent> maxScreen(Stage st){
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {		
				if (VennBase.anchor) {st.setMaximized(true);}
			}
		};
	}
	
	public static void changeButtonColor(String hex, ColorPicker cp1, ColorPicker cp2, ColorPicker cp3, ColorPicker cp4, Button anchorOption, Button textAdder, Button reset, Button importB, Button exportB, Button capture) {
		cp1.setStyle("-fx-background-color: #"+hex);
		cp2.setStyle("-fx-background-color: #"+hex);
		cp3.setStyle("-fx-background-color: #"+hex);
		cp4.setStyle("-fx-background-color: #"+hex);
		anchorOption.setStyle("-fx-background-color: #"+hex);
		textAdder.setStyle("-fx-background-color: #"+hex);
		reset.setStyle("-fx-background-color: #"+hex);
		importB.setStyle("-fx-background-color: #"+hex);
		exportB.setStyle("-fx-background-color: #"+hex);
		capture.setStyle("-fx-background-color: #"+hex);
		autoSaveFile.overwriteLineInFile("BuColor ", "BuColor "+hex);
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}
