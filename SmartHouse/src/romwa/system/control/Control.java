package romwa.system.control;

import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;

import romwa.FileIO.FileIO;

public abstract class Control {
	
	protected Log log;
	protected String stateFile;
	
	
	public Control() {
		
	}
	
	public Control(String logFile, String stateFile) {
		this.log = new Log(logFile);
		this.stateFile = stateFile;
	}
	
	public abstract void start();
	
	public abstract void stop();
	
	protected void logStart() {
		Date d = new Date();
		log.write("START, " + d.toString());
	}
	
	protected void logStop() {
		Date d = new Date();
		log.write("STOP, " + d.toString());
	}
	
	/**
	 * Sets the status of the controlled device
	 * @return Whether or not set was successful
	 */
	protected boolean setState(boolean state) {
		if(FileIO.readLine(stateFile).equals(state+"")) {
//			String message = "";
//			if(state) message = "Already working";
//			else message = "Not working";
//			JOptionPane.showConfirmDialog(null, message, "ERROR", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
			return false;
		}
		else FileIO.writeLine(stateFile, state+"");
		return true;
	}
	
	public boolean getState() {
		return FileIO.readLine(stateFile).equals("true");
	}
	
	public String getLog() {
		return log.read();
	}
	
	public void openLog() {
		log.openLog();
	}
}
