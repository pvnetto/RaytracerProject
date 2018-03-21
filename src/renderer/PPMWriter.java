package renderer;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class PPMWriter {
	private String fileName;
	
	public PPMWriter(String fileName) {
		this.fileName = fileName;
	}
	
	public void Write(String ppmString) {
		try {
			PrintWriter printWriter = new PrintWriter(fileName + ".ppm", "UTF-8");
			printWriter.println(ppmString);
			printWriter.close();
		} catch (FileNotFoundException | UnsupportedEncodingException ex) {
			System.out.println("> Error!");
			System.out.println("> " + ex.getMessage());
		}
	}
	
}
