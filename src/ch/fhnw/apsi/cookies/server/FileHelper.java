package ch.fhnw.apsi.cookies.server;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.annotation.Nonnull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * Helper class to read files from folder.
 * 
 * @author Matthias Brun
 */
public final class FileHelper {
	
	private static final Logger logger = LogManager.getLogger(FileHelper.class.getName());
	
	private FileHelper() {}
	
	public static String fileToString(@Nonnull File file) throws FileNotFoundException {
		if (!file.canRead()){
			logger.error("Could not read file. " + file);
			throw new FileNotFoundException("Could not read file. " + file);
		}
			
		try (BufferedReader in = new BufferedReader(new FileReader(file))) {
			StringBuilder builder = new StringBuilder();
			for (String line = in.readLine(); line != null; line = in.readLine()) {
				builder.append(line + "\n");
			}
			return builder.toString();
		} catch (IOException ex) {
			logger.error("Error reading file: " + ex.getMessage());
			throw new FileNotFoundException("Error reading file: " + ex.getMessage());
		}
	}
}
