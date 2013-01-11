package ch.fhnw.apsi.cookies.server;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Helper class to read files from folder.
 * 
 * @author Matthias Brun
 */
public class FileHelper {

	public static String fileToString(File file) throws FileNotFoundException {
		if (!file.canRead())
			throw new FileNotFoundException("Could not read file. " + file);

		try (BufferedReader in = new BufferedReader(new FileReader(file))) {
			StringBuilder builder = new StringBuilder();
			for (String line = in.readLine(); line != null; line = in.readLine()) {
				builder.append(line + "\n");
			}
			return builder.toString();
		} catch (IOException ex) {
			throw new FileNotFoundException("Error reading file: " + ex.getMessage());
		}
	}
}
