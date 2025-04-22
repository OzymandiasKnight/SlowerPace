package fr.ozymandiasknight.slowerpace;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

public class FileManager {
	//Update File
	private static void update_file(String file_path, HashMap<String, HashMap<String, Integer>> data) {
		try {
			
			FileWriter write_file = new FileWriter(file_path);
			//Convert Hashmap into String
			Gson gson = new Gson();
			String string_data = gson.toJson(data);
			//Write data
			write_file.write(string_data);
			
			//End File Writing
			write_file.close();
			System.out.println("Slower Pace: Data Updated");
			
		//Error Handling
		} catch (IOException e) {
			System.out.println("Slower Pace: Error Updating Data");
			e.printStackTrace();
		}
	}
	//Save File, or Update it if already exist
	public static void save_file(String file_path, HashMap<String, HashMap<String, Integer>> data) {
		try {
			File data_file = new File(file_path);
			if (data_file.createNewFile()) {
				System.out.println("Slower Pace: Saving Data");
			}
			else {
				System.out.println("Slower Pace: File Exist");
			}
			update_file(file_path, data);
		//Error Handling
		} catch (IOException e) {
			System.out.println("Slower Pace: Error Saving Data");
			e.printStackTrace();
		}
		
		
	}
	//Read File
	public static HashMap<String, HashMap<String, Integer>> load_file(String file_path) {
		try {
			File data_file = new File(file_path);
			Scanner reader_file = new Scanner(data_file);
			String content = new String(Files.readAllBytes(Paths.get(file_path)));
			//Close and return file content
			reader_file.close();
			//Convert to Hashmap
			Gson gson = new Gson();
			HashMap<String, HashMap<String, Integer>> map = null;
			try {
				Type type = new TypeToken<HashMap<String, HashMap<String, Integer>>>() {
					private static final long serialVersionUID = 1L;
				}.getType();
				map = gson.fromJson(content, type);
			}
			catch (Exception e) {
				return null;
			}
			return map;
			
		//Error Handling
		} catch (IOException e) {
			System.out.println("Slower Pace: Error Reading Data");
			e.printStackTrace();
		}
		return null;
	}
}
