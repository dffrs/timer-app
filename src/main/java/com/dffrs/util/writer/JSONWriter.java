package com.dffrs.util.writer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

/**
 * 
 * Class responsible for creating a JSON file, saving the name of the project as well as the time spent working on it. To do this, this class, first, creates
 * an empty folder, where it will keep every saved .json file. Then, it creates the an instance of JSONObject, and writes it to a file.
 * 
 * @author dffrs-iscteiulpt.
 *
 */
public final class JSONWriter {
	
	/**
	 * String representing the path to the Folder that will be created.
	 */
	private static final String PATH = "Directory/";
	
	/**
	 * Method responsible for creating an empty folder, where it will be saved every .json file.
	 * @throws IOException Every time it fails to create a folder. Possible, it already exists.
	 */
	private static final void makeDir() throws IOException {
		Files.createDirectory(new File(PATH).toPath());
	}
	
	/**
	 * Method responsible to create an instance of JSONObject.
	 * 
	 * @param mapOfElements Map containing data that needs to be ported to a .json file.
	 * @return Instance of JSONObject, that will be used to create the file itself. If it fails, for some reason, it returns a NULL Reference.
	 */
	private static final JSONObject saveToJSONFormat(Map< String, List<String> > mapOfElements) {
		if (mapOfElements == null) {
			throw new NullPointerException("Error: Saving elements to a JSON format. Argument is a NULL Reference.\n");
		}
		
		if (!mapOfElements.isEmpty()) {
			JSONObject writer = new JSONObject();
			
			mapOfElements.forEach( (k, v) -> writer.put(k, v) );
			return writer;
		}
		return null;
	}
	
	/**
	 * Method responsible for creating an empty folder, that will be used to keep the .json file generated, as well as the file itself.
	 * It uses {@link #makeDir()} method to create the Directory, and {@link #saveToJSONFormat(Map)} method to generate a .json file.
	 * For more information, check {@link #makeDir()} and {@link #saveToJSONFormat(Map)} documentation.
	 * 
	 * @param FileName String representing the name of the .json file.
	 * @param mapOfElement Map used to generate the file itself, based on it's content.
	 */
	public static final void createJSONFile(String FileName, Map< String, List<String> > mapOfElement) {
		try {
			System.out.println("Creating a Directory.\n");
			makeDir();
			System.out.println("Directory created.\n");
		} catch (IOException e) {
			System.err.println("Warning: Something went wrong trying to create a storying folder. It already exists.\n");
		}
		
		JSONObject json = saveToJSONFormat(mapOfElement);
		
		if (json == null) {
			throw new NullPointerException("Error: Creating an instance of JSONObject. The argument was a NULL Reference.\n");
		}else {
			try(FileWriter f = new FileWriter(PATH + FileName + ".json")) {
				
				f.write(json.toString());
				System.out.println("JSON created.\n");
				
			} catch (IOException e) {
				System.err.println("Error: Creating a FileWrite instance to write to a file.\n");
			}
		}
	}
}