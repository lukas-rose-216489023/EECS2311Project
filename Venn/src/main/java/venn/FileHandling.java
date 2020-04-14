package venn;

import java.io.*;
import java.util.ArrayList;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

//Saving systems
public class FileHandling {
	boolean reset=true;
	File file;
	boolean first;
	
	public void CreateFile(String fileName) {
		first=true;
		try {
			file = new File(fileName);
			if (file.createNewFile()) {System.out.println("File -<"+fileName+">- created!");}
		}
		catch(Exception e) {System.out.println("An error occurred when creaing the file"+fileName+"!");}
	}
	
	public void CreateFile(String fileName, Stage stage) {
		FileChooser fc = new FileChooser();
		fc.setTitle("Where would you like to save the compare results?");
		fc.setInitialFileName("Compare Results");
		fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Venn diagram save file", "*.txt"));
		try {
			File f = new File(fileName);
			f = fc.showSaveDialog(stage);
			fc.setInitialDirectory(f.getParentFile());
			this.file = f;
		}
		catch(Exception e){System.out.println(e);}
	}

	public void overwriteLineInFile(String oldData, String newData) {
		ArrayList<String> fileContents = new ArrayList<String>();
		BufferedReader reader = null;
		FileWriter writer = null;

		try
		{
			reader = new BufferedReader(new FileReader(file));

			//Reading all the lines of input text file into oldContent
			String line = reader.readLine();
			while (line != null) 
			{
				fileContents.add(line);
				line = reader.readLine();
			}

			//Replacing oldData with newData in the fileContents
			String updatedFileContents = "";
			for (String l:fileContents) {
				if (l.contains(oldData)) {l=newData;}
				updatedFileContents+=l+System.lineSeparator();
			}

			//Rewriting the input text file with newContent
			writer = new FileWriter(file);

			writer.write(updatedFileContents);
//			System.out.println("Successfully updated -<"+oldData+">- with -<"+newData+">-!");
		}
		catch (IOException e){e.printStackTrace();}

		finally {
			try {
				//Closing the resources
				reader.close();
				writer.close();
			} 
			catch (IOException e) {e.printStackTrace();}
		}
	}

	public void WriteToFile(String line) {
		try {
			if (first) {
			FileWriter writer = new FileWriter(file);
			writer.write(line+"\n");
			writer.close();
			first=false;
			}
			else {
				FileWriter writer = new FileWriter(file, true);
				writer.write(line+"\n");
				writer.close();
			}
//			System.out.println("Successfully wrote -<"+line+">- to file!");
		} 
		catch (IOException e) {System.out.println("An error occured!");e.printStackTrace();}
	}
	
	protected static void saveChanges(FileHandling file, String replace, String with) {
		file.overwriteLineInFile(replace, with);		
	}
	
	
	public static void copyFiles(FileHandling copyFrom, FileHandling pasteTo) {
		ArrayList<String> fileContents = new ArrayList<String>();
		BufferedReader reader = null;
		FileWriter writer = null;

		try
		{
			reader = new BufferedReader(new FileReader(copyFrom.file));

			//Reading all the lines of input text file into oldContent
			String line = reader.readLine();
			while (line != null) 
			{
				fileContents.add(line);
				line = reader.readLine();
			}

			//Copying the input text file with contents
			String contents = "";
			for (String l:fileContents) {
				contents+=l+System.lineSeparator();
			}
			writer = new FileWriter(pasteTo.file);
			
			writer.write(contents);
//			System.out.println("Successfully copied -<"+copyFrom.file.getName()+">- and pasted to -<"+pasteTo.file.getName()+">-!");
		}
		catch (IOException e){e.printStackTrace();}

		finally {
			try {
				//Closing the resources
				reader.close();
				writer.close();
			} 
			catch (IOException e) {e.printStackTrace();}
		}
	}
	
	
	public static void loadImport(File file, Pane pane, Circle circleR, Circle circleL, Button anchorOption, Text title, Text right, Text left, Anchor intersection, Anchor leftCircle, Anchor rightCircle, Points p, Rectangle selection, ColorPicker cp1, ColorPicker cp2, ColorPicker cp3, ColorPicker cp4, ColorPicker cp5, AnchorPane multAdd, Button reset, Button importB, Button exportB, Button capture, Button undo, Button redo, Button compare) {
		//Read save data
//		System.out.println("Reading save data...");
		ArrayList<String> fileContents = new ArrayList<String>();
		BufferedReader reader = null;

		Record.numBoxes=0;
		Record.deleteAll(pane, VennBase.autoSaveFile);
		
		try
		{
			reader = new BufferedReader(new FileReader(file));

			//Reading all the lines of input text file into fileContents
//			System.out.println("Reading save data lines...");
			String nline = reader.readLine();
			while (nline != null) 
			{
				fileContents.add(nline);
				nline = reader.readLine();
			}
			
			//Load save data into nodes
//			System.out.println("Loading save data...");
			ArrayList<String> prevIndivContent = new ArrayList<String>();
			int iter=0;
			for (iter=0;iter<fileContents.size();iter++) {
				String line = fileContents.get(iter);
				ArrayList<String> indivContent = new ArrayList<String>();
				String[] split= line.split(" ");
				for (String indiv:split) {indivContent.add(indiv);}
				
				if (iter==0) {
					System.out.println("Loading background color...");
					Color col = new Color(Double.parseDouble(indivContent.get(1)), Double.parseDouble(indivContent.get(2)), Double.parseDouble(indivContent.get(3)), 0.5);
					BackgroundFill backgroundColor = new BackgroundFill(col, null, null);
					Background background = new Background(backgroundColor);
					pane.setBackground(background);
					VennBase.autoSaveFile.overwriteLineInFile("BColor ", "BColor "+col.getRed()+" "+col.getGreen()+" "+col.getBlue());
				}
				
				else if (iter==1) {
					System.out.println("Loading right circle color...");
					Color col = new Color(Double.parseDouble(indivContent.get(1)), Double.parseDouble(indivContent.get(2)), Double.parseDouble(indivContent.get(3)), 0.5);
					circleR.setFill(col);
					VennBase.autoSaveFile.overwriteLineInFile("RColor ", "RColor "+col.getRed()+" "+col.getGreen()+" "+col.getBlue());
					col.saturate();
					col.saturate();
					col.saturate();
					circleR.setStroke(col);
				}
				
				else if (iter==2) {
					System.out.println("Loading left circle color...");
					Color col = new Color(Double.parseDouble(indivContent.get(1)), Double.parseDouble(indivContent.get(2)), Double.parseDouble(indivContent.get(3)), 0.5);
					circleL.setFill(col);
					VennBase.autoSaveFile.overwriteLineInFile("LColor ", "LColor "+col.getRed()+" "+col.getGreen()+" "+col.getBlue());
					col.saturate();
					col.saturate();
					col.saturate();
					circleL.setStroke(col);
				}
				
				else if (iter==3) {
					System.out.println("Loading button color...");
					VennBase.changeButtonColor(indivContent.get(1), cp1, cp2, cp3, cp4, cp5, anchorOption, reset, importB, exportB, capture, undo, redo, compare);
				}
				
				else if (iter==4) {
					System.out.println("Loading title text...");
					String text="";
					for (int i=1;i<indivContent.size();i++) {text+=indivContent.get(i)+" ";}
					title.setText(text);
					title.layoutXProperty().bind(pane.widthProperty().divide(2).subtract(title.getText().length()*4));
					VennBase.autoSaveFile.overwriteLineInFile("Title ", "Title "+title.getText());
				}
				
				else if (iter==5) {
					System.out.println("Loading right text...");
					String text="";
					for (int i=1;i<indivContent.size();i++) {text+=indivContent.get(i)+" ";}
					right.setText(text);
					right.layoutXProperty().bind(pane.widthProperty().divide(5.0/3.0).subtract(right.getText().length()*5/2));
					VennBase.autoSaveFile.overwriteLineInFile("Right ", "Right "+right.getText());
				}
				
				else if (iter==6) {
					System.out.println("Loading left text...");
					String text="";
					for (int i=1;i<indivContent.size();i++) {text+=indivContent.get(i)+" ";}
					left.setText(text);
					left.layoutXProperty().bind(pane.widthProperty().divide(5.0/2.0).subtract(left.getText().length()*5/2));
					VennBase.autoSaveFile.overwriteLineInFile("Left ", "Left "+left.getText());
				}
				
				else {
					if (indivContent.get(0).contains("Record")) {
						prevIndivContent = indivContent;
					}
					else if (indivContent.get(0).contains("Box")) {
						System.out.println("Loading "+indivContent.get(0)+"...");
						String text="";
						int letterCount=0;
						int i = 3;
						if (!(indivContent.get(1).equals("0"))) {
							i--;
							while (letterCount<Integer.parseInt(indivContent.get(1))){text+=indivContent.get(i)+" "; letterCount+=indivContent.get(i).length()+1; i++;}
						}
						System.out.println(indivContent.get(i+3)+" "+indivContent.get(i+4));
						TextBox b = new TextBox(pane, text, circleL, circleR, intersection, leftCircle, rightCircle, p, selection, indivContent.get(i+3), indivContent.get(i+4), "");
						System.out.println("Text Box created..");
						b.pos = indivContent.get(i);
						b.box.setLayoutX(Double.parseDouble(indivContent.get(i+1)));
						b.box.setLayoutY(Double.parseDouble(indivContent.get(i+2)));
						
						b.record.percentX = Double.parseDouble(prevIndivContent.get(1));
						b.record.percentY = Double.parseDouble(prevIndivContent.get(2));
						b.record.inCircleR = Boolean.parseBoolean(prevIndivContent.get(3));
						b.record.inCircleL = Boolean.parseBoolean(prevIndivContent.get(4));
						
						String xtraText="";
						int xtraLen=0;
						int xtraCount=0;
						
						
						if (Integer.parseInt((indivContent.get(i+5)))!=0) {
							
							xtraLen=Integer.parseInt((indivContent.get(i+5)));
							xtraCount=0;
							xtraText="";
							while (xtraCount<xtraLen){
								
								iter++;
								line = fileContents.get(iter);
								indivContent = new ArrayList<String>();
								split= line.split(" ");
								for (String indiv:split) {indivContent.add(indiv);}
								
								xtraText+=line; 
								xtraCount+=line.length(); 

								if (xtraCount+1<xtraLen) {xtraCount++; xtraText+="\n";}
							}
							
							b.xtraBox.setText(xtraText);
						}

						FileHandling.saveChanges(VennBase.autoSaveFile, "Record"+b.record.recordNum, "Record"+b.record.recordNum+" "+b.record.percentX+" "+b.record.percentY+" "+b.record.inCircleR+" "+b.record.inCircleL);
						FileHandling.saveChanges(VennBase.autoSaveFile, "Box"+b.boxNum, "Box"+b.boxNum+" "+b.box.getText().length()+" "+b.box.getText()+" "+b.pos+" "+b.box.getLayoutX()+" "+b.box.getLayoutY()+" "+indivContent.get(i+3)+" "+indivContent.get(i+4));
					}
				}
			}
			
//			System.out.println("Successfully loaded form file -<"+file.getName()+">-!");
		}
		catch (IOException e){e.printStackTrace();System.out.println(e.getMessage());}

		finally {
			try {
				//Closing the resources
				reader.close();
			} 
			catch (IOException e) {e.printStackTrace();}
		}
		
	}
	
	public static void compareFiles(File thisFile, File fileToCompare, Stage stage) {
		//Read save data
		System.out.println("Reading save data...");
		ArrayList<String> thisFileContents = new ArrayList<String>();
		ArrayList<String> otherFileContents = new ArrayList<String>();
		BufferedReader reader = null;
		BufferedReader reader2 = null;

		try
		{
			reader = new BufferedReader(new FileReader(thisFile));

			//Reading all the lines of input text file
			System.out.println("Reading currunt file content...");
			String nline = reader.readLine();
			while (nline != null) {
				thisFileContents.add(nline);
				nline = reader.readLine();
			}

			reader2 = new BufferedReader(new FileReader(fileToCompare));

			//Reading all the lines of input text file
			System.out.println("Reading compare file content...");
			String nline2 = reader2.readLine();
			while (nline2 != null) {
				otherFileContents.add(nline2);
				nline2 = reader2.readLine();
			}

			//compare data
			System.out.println("Comparing data and putting in a new file...");

			FileHandling compareData = new FileHandling();
			compareData.CreateFile("compareData", stage);
			compareData.WriteToFile("Compare data between current file and the file "+fileToCompare+" : ");

			if(thisFileContents.size()>=otherFileContents.size()) {
				for (int iter=0;iter<thisFileContents.size();iter++) {
					ArrayList<String> indivContentT = new ArrayList<String>();
					ArrayList<String> indivContentO = new ArrayList<String>();
					String[] split= thisFileContents.get(iter).split(" ");
					for (String indiv:split) {indivContentT.add(indiv);}
					split = otherFileContents.get(iter).split(" ");
					for (String indiv:split) {indivContentO.add(indiv);}

					if (indivContentT.get(0).equals("Title")) {
						System.out.println("Comparing title text...");
						String textT="";
						for (int i=1;i<indivContentT.size();i++) {textT+=indivContentT.get(i)+" ";}
						String textO="";
						for (int i=1;i<indivContentO.size();i++) {textO+=indivContentO.get(i)+" ";}
						compareData.WriteToFile("Current title: "+textT+"\t\tCompare title: "+textO);
					}

					else if (indivContentT.get(0).equals("Right")) {
						System.out.println("Comparing right text...");
						String textT="";
						for (int i=1;i<indivContentT.size();i++) {textT+=indivContentT.get(i)+" ";}
						String textO="";
						for (int i=1;i<indivContentO.size();i++) {textO+=indivContentO.get(i)+" ";}
						compareData.WriteToFile("Current right circle title: "+textT+"\t\tCompare right cicrcle title: "+textO);
					}

					else if (indivContentT.get(0).equals("Left")) {
						System.out.println("Comparing left text...");
						String textT="";
						for (int i=1;i<indivContentT.size();i++) {textT+=indivContentT.get(i)+" ";}
						String textO="";
						for (int i=1;i<indivContentO.size();i++) {textO+=indivContentO.get(i)+" ";}
						compareData.WriteToFile("Current left circle title: "+textT+"\t\tCompare left circle title: "+textO);
					}

					else {
						if (indivContentT.get(0).contains("Box")) {
							System.out.println("Comparing "+indivContentT.get(0)+"...");
							String textT="";
							int letterCount=0;
							int i = 2;
							while (letterCount<Integer.parseInt(indivContentT.get(1))){textT+=indivContentT.get(i)+" "; letterCount+=indivContentT.get(i).length()+1; i++;}

							String textO="";
							letterCount=0;
							int j = 2;
							while (letterCount<Integer.parseInt(indivContentO.get(1))){textO+=indivContentO.get(j)+" "; letterCount+=indivContentO.get(j).length()+1; j++;}

							compareData.WriteToFile("Current "+indivContentT.get(0)+" text: "+textT+"\t\tCompare "+indivContentO.get(0)+"text: "+textO);
							compareData.WriteToFile("Current "+indivContentT.get(0)+" position: "+indivContentT.get(i)+"\t\tCompare "+indivContentO.get(0)+" position: "+indivContentO.get(j));
						}
					}
				}
			}
			else {
				for (int iter=0;iter<otherFileContents.size();iter++) {
					ArrayList<String> indivContentT = new ArrayList<String>();
					ArrayList<String> indivContentO = new ArrayList<String>();
					String[] split= thisFileContents.get(iter).split(" ");
					for (String indiv:split) {indivContentT.add(indiv);}
					split = otherFileContents.get(iter).split(" ");
					for (String indiv:split) {indivContentO.add(indiv);}

					if (indivContentT.get(0).equals("Title")) {
						System.out.println("Comparing title text...");
						String textT="";
						for (int i=1;i<indivContentT.size();i++) {textT+=indivContentT.get(i)+" ";}
						String textO="";
						for (int i=1;i<indivContentO.size();i++) {textO+=indivContentO.get(i)+" ";}
						compareData.WriteToFile("Current title: "+textT+"\t\tCompare title: "+textO);
					}

					else if (indivContentT.get(0).equals("Right")) {
						System.out.println("Comparing right text...");
						String textT="";
						for (int i=1;i<indivContentT.size();i++) {textT+=indivContentT.get(i)+" ";}
						String textO="";
						for (int i=1;i<indivContentO.size();i++) {textO+=indivContentO.get(i)+" ";}
						compareData.WriteToFile("Current right circle title: "+textT+"\t\tCompare right cicrcle title: "+textO);
					}

					else if (indivContentT.get(0).equals("Left")) {
						System.out.println("Comparing left text...");
						String textT="";
						for (int i=1;i<indivContentT.size();i++) {textT+=indivContentT.get(i)+" ";}
						String textO="";
						for (int i=1;i<indivContentO.size();i++) {textO+=indivContentO.get(i)+" ";}
						compareData.WriteToFile("Current left circle title: "+textT+"\t\tCompare left circle title: "+textO);
					}

					else {
						if (indivContentT.get(0).contains("Box")) {
							System.out.println("Comparing "+indivContentT.get(0)+"...");
							String textT="";
							int letterCount=0;
							int i = 2;
							while (letterCount<Integer.parseInt(indivContentT.get(1))){textT+=indivContentT.get(i)+" "; letterCount+=indivContentT.get(i).length()+1; i++;}

							String textO="";
							letterCount=0;
							int j = 2;
							while (letterCount<Integer.parseInt(indivContentO.get(1))){textO+=indivContentO.get(j)+" "; letterCount+=indivContentO.get(j).length()+1; j++;}

							compareData.WriteToFile("Current "+indivContentT.get(0)+" text: "+textT+"\t\tCompare "+indivContentO.get(0)+"text: "+textO);
							compareData.WriteToFile("Current "+indivContentT.get(0)+" position: "+indivContentT.get(i)+"\t\tCompare "+indivContentO.get(0)+" position: "+indivContentO.get(j));
						}
					}
				}
			}
		}
		catch (IOException e){e.printStackTrace();System.out.println(e.getMessage());}

		finally {
			try {
				//Closing the resources
				reader.close();
				reader2.close();
			} 
			catch (IOException e) {e.printStackTrace();}
		}
	}
	
}