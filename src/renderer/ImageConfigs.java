package renderer;

import java.io.Serializable;

public class ImageConfigs implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String fileName;
    private int COLS, ROWS;
    private int antialiasingSamples;
    private Codification codificationType; //binary or ascii, p6 or p3 respectively
    public enum Codification { BINARY, ASCII}
    
	public ImageConfigs(String fileName, int COLS, int ROWS, int antialiasingSamples, Codification codification) {
		super();
		this.fileName = fileName;
		this.COLS = COLS;
		this.ROWS = ROWS;
		this.antialiasingSamples = antialiasingSamples;
		this.codificationType = codification;
	}
	
	private String getCodificationType(Codification codification) {
		String type = "";
		switch (codification) {
		case BINARY:
			type = "P6";
			break;
		case ASCII:
			type = "P3";
			break;
		default:
			System.out.println("ERROR, INVALID CODIFICATION TYPE");
			break;
		}
		
		return type;
	}
	
	public String getConfig() {
		String config = getCodificationType(codificationType) + "\r\n";
		config += COLS + " " + ROWS + "\r\n";
		config += "255\n";
		
		return config;
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public int getCOLS() {
		return COLS;
	}
	public void setCOLS(int cOLS) {
		COLS = cOLS;
	}
	public int getROWS() {
		return ROWS;
	}
	public void setROWS(int rOWS) {
		ROWS = rOWS;
	}
	public int getAntialiasingSamples() {
		return antialiasingSamples;
	}
	public void setAntialiasingSamples(int antialiasingSamples) {
		this.antialiasingSamples = antialiasingSamples;
	}
    
}
