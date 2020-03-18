package venn;

import java.io.*;
import java.util.ArrayList;

import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

//Saving systems
public class FileHandling {
	boolean reset=true;
	File file;
	boolean first;

	public void CreateFile(String fileName) {
		first=true;
		try {
			file = new File(fileName);
			if (file.createNewFile()) {System.out.println("File created!");}
			else {System.out.println("File already exists!");}
		}
		catch(Exception e) {System.out.println("An error occurred when creaing the file"+fileName+"!");}
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
			System.out.println("Successfully updated -<"+oldData+">- with -<"+newData+">-!");
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
			FileWriter writer = new FileWriter("VennApplicationAutoSave.txt");
			writer.write(line+"\n");
			writer.close();
			first=false;
			}
			else {
				FileWriter writer = new FileWriter("VennApplicationAutoSave.txt", true);
				writer.write(line+"\n");
				writer.close();
			}
			System.out.println("Successfully wrote -<"+line+">- to file!");
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
			System.out.println("Successfully copied -<"+copyFrom.file.getName()+">- and pasted to -<"+pasteTo.file.getName()+">-!");
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
	
	
	public static void loadImport(File file, Pane pane, Circle circleR, Circle circleL, Button anchorOption, Text title, Text right, Text left, Button textAdder, Anchor intersection, Anchor leftCircle, Anchor rightCircle, Points p, Rectangle selection) {
		//Read save data
		System.out.println("Reading save data...");
		ArrayList<String> fileContents = new ArrayList<String>();
		BufferedReader reader = null;

		try
		{
			reader = new BufferedReader(new FileReader(file));

			//Reading all the lines of input text file into fileContents
			System.out.println("Reading save data lines...");
			String nline = reader.readLine();
			while (nline != null) 
			{
				fileContents.add(nline);
				nline = reader.readLine();
			}
			
			//Load save data into nodes
			System.out.println("Loading save data...");
			ArrayList<String> prevIndivContent = new ArrayList<String>();
			for (String line:fileContents) {
				ArrayList<String> indivContent = new ArrayList<String>();
				String[] split= line.split(" ");
				for (String indiv:split) {indivContent.add(indiv);}
				
				if (indivContent.get(0).equals("BColor")) {
					System.out.println("Loading background color...");
					Color col = new Color(Double.parseDouble(indivContent.get(1)), Double.parseDouble(indivContent.get(2)), Double.parseDouble(indivContent.get(3)), 0.5);
					BackgroundFill backgroundColor = new BackgroundFill(col, null, null);
					Background background = new Background(backgroundColor);
					pane.setBackground(background);
					VennBase.autoSaveFile.overwriteLineInFile("BColor ", "BColor "+col.getRed()+" "+col.getGreen()+" "+col.getBlue());
				}
				
				else if (indivContent.get(0).equals("RColor")) {
					System.out.println("Loading right circle color...");
					Color col = new Color(Double.parseDouble(indivContent.get(1)), Double.parseDouble(indivContent.get(2)), Double.parseDouble(indivContent.get(3)), 0.5);
					circleR.setFill(col);
					VennBase.autoSaveFile.overwriteLineInFile("RColor ", "RColor "+col.getRed()+" "+col.getGreen()+" "+col.getBlue());
					col.saturate();
					col.saturate();
					col.saturate();
					circleR.setStroke(col);
				}
				
				else if (indivContent.get(0).equals("LColor")) {
					System.out.println("Loading left circle color...");
					Color col = new Color(Double.parseDouble(indivContent.get(1)), Double.parseDouble(indivContent.get(2)), Double.parseDouble(indivContent.get(3)), 0.5);
					circleL.setFill(col);
					VennBase.autoSaveFile.overwriteLineInFile("LColor ", "LColor "+col.getRed()+" "+col.getGreen()+" "+col.getBlue());
					col.saturate();
					col.saturate();
					col.saturate();
					circleL.setStroke(col);
				}
				
				else if (indivContent.get(0).equals("Anchoring")) {
					System.out.println("Loading anchor state...");
					if (indivContent.get(1).equals("off")) {VennBase.anchor = false;anchorOption.setText("Anchoring off");VennBase.autoSaveFile.overwriteLineInFile("Anchoring ", "Anchoring "+"off");}
					else if (indivContent.get(1).equals("on")){VennBase.anchor = true;anchorOption.setText("Anchoring on");VennBase.autoSaveFile.overwriteLineInFile("Anchoring ", "Anchoring "+"on");}
				}
				
				else if (indivContent.get(0).equals("Title")) {
					System.out.println("Loading title text...");
					String text="";
					for (int i=1;i<indivContent.size();i++) {text+=indivContent.get(i)+" ";}
					title.setText(text);
					title.layoutXProperty().bind(pane.widthProperty().divide(2).subtract(title.getText().length()*4));
					VennBase.autoSaveFile.overwriteLineInFile("Title ", "Title "+title.getText());
				}
				
				else if (indivContent.get(0).equals("Right")) {
					System.out.println("Loading right text...");
					String text="";
					for (int i=1;i<indivContent.size();i++) {text+=indivContent.get(i)+" ";}
					right.setText(text);
					right.layoutXProperty().bind(pane.widthProperty().divide(5.0/3.0).subtract(right.getText().length()*5/2));
					VennBase.autoSaveFile.overwriteLineInFile("Right ", "Right "+right.getText());
				}
				
				else if (indivContent.get(0).equals("Left")) {
					System.out.println("Loading left text...");
					String text="";
					for (int i=1;i<indivContent.size();i++) {text+=indivContent.get(i)+" ";}
					left.setText(text);
					left.layoutXProperty().bind(pane.widthProperty().divide(5.0/2.0).subtract(left.getText().length()*5/2));
					VennBase.autoSaveFile.overwriteLineInFile("Left ", "Left "+left.getText());
				}
				
				else {
					if (indivContent.get(0).contains("Box")) {
						System.out.println("Loading "+indivContent.get(0)+"...");
						String text="";
						int letterCount=0;
						int i = 2;
						while (letterCount<Integer.parseInt(indivContent.get(1))){text+=indivContent.get(i)+" "; letterCount+=indivContent.get(i).length()+1; i++;}
						
						TextBox b = new TextBox(pane, textAdder, text, circleL, circleR, intersection, leftCircle, rightCircle, p, selection, VennBase.autoSaveFile);
						b.pos = indivContent.get(i);
						b.box.setLayoutX(Double.parseDouble(indivContent.get(i+1)));
						b.box.setLayoutY(Double.parseDouble(indivContent.get(i+2)));
						
						b.record.percentX = Double.parseDouble(prevIndivContent.get(1));
						b.record.percentY = Double.parseDouble(prevIndivContent.get(2));
						b.record.inCircleR = Boolean.parseBoolean(prevIndivContent.get(3));
						b.record.inCircleL = Boolean.parseBoolean(prevIndivContent.get(4));
						
					}
				}
				
				prevIndivContent = indivContent;
			}
			
			System.out.println("Successfully loaded form file -<"+file.getName()+">-!");
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
	
}





	
	