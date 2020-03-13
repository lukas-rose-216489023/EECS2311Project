package venn;

import java.io.*;
import java.util.ArrayList;

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
	
}





	
	