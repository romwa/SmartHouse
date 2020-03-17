package romwa.FileIO;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class represents a file handler
 * @author romwa
 * @version 3/12/2020
 */
public class FileIO {

	/**
	 * The os-specific file seperator
	 */
	public static final String fileSeparator = System.getProperty("file.separator");
	
	/**
	 * The os-specific line seperator
	 */
	public static final String lineSeparator = System.getProperty("line.separator");

	/**
	 * Reads the file and puts each line in a String object in the ArrayList
	 * @param filename The file to be read
	 * @return The ArrayList containing all of the lines
	 */
	public static ArrayList<String> readFile(String filename) {
		Scanner scan = null;
		try {
			ArrayList<String> output = new ArrayList<String>();
			FileReader reader = new FileReader(filename);
			scan = new Scanner(reader);
			while(scan.hasNextLine()) {
				String line = scan.nextLine();
				output.add(line);
			}

			return output;
		} catch (FileNotFoundException e) {
			System.out.println("The file " + filename + " was not found");
			e.printStackTrace();
		} finally {
			if(scan != null)
				scan.close();
		}
		return null;
	}
	
	
	/**
	 * Reads the first line of a file
	 * @param filename The file to be read
	 * @return The string of the first line of the file
	 */
	public static String readLine(String filename) {
		Scanner scan = null;
		try {
			String output = "";
			FileReader reader = new FileReader(filename);
			scan = new Scanner(reader);
			while(scan.hasNextLine()) {
				String line = scan.nextLine();
				output = line;
				break;
			}

			return output;
		} catch (FileNotFoundException e) {
			System.out.println("The file " + filename + " was not found");
			e.printStackTrace();
		} finally {
			if(scan != null)
				scan.close();
		}
		return null;
	}

	/**
	 * Writes the ArrayList fillData to the file. Completely erasing everything that was written there before
	 * @param filename The file to write to
	 * @param fileData The data to put in the file
	 * @post The file will only contain the lines from the fillData ArrayList
	 */
	public static void writeFile(String filename, ArrayList<String> fileData) {
		FileWriter writer = null;
		try {
			writer = new FileWriter(filename);

			for(String line : fileData) {
				writer.write(line += lineSeparator);
			}

		} catch (IOException e) {
			//				System.out.println("The file " + filename + " was not found");
			e.printStackTrace();
		} finally {
			try {
				if(writer != null)
					writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Writes a single line to a file, replacing everything that was written there before
	 * @param filename The file to write to
	 * @param line The line to be written in the file
	 * @post The file will contain only the line
	 */
	public static void writeLine(String filename, String line) {
		FileWriter writer = null;
		try {
			writer = new FileWriter(filename);

				writer.write(line += lineSeparator);

		} catch (IOException e) {
			//				System.out.println("The file " + filename + " was not found");
			e.printStackTrace();
		} finally {
			try {
				if(writer != null)
					writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Converts ArrayList<String> to a String object
	 * @param arr The ArrayList to be converted
	 * @return The String containing all of the lines from the ArrayList
	 */
	public static String arrayListToString(ArrayList<String> arr) {
		String result = "";
		for(String str : arr) {
			result += str + "\n";
		}
		return result;
	}

}
