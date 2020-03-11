package romwa.system.control;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Scanner;

import javax.swing.JOptionPane;

import com.fazecast.jSerialComm.SerialPort;

import romwa.system.SystemVariables;

public class ArduinoHandler {
	
	private SerialPort arduino;
	private Scanner reader;
	private OutputStreamWriter writer;

	public static final char 
			LIGHT_ON=SystemVariables.LIGHT_ON, 
			LIGHT_OFF=SystemVariables.LIGHT_OFF, 
			LOCK=SystemVariables.LOCK, 
			UNLOCK=SystemVariables.UNLOCK, 
			CONFIRM=SystemVariables.CONFIRM, 
			IDLE=SystemVariables.IDLE;

	
	public ArduinoHandler() {
		initialize();
	}
	
	private void initialize() {
		SerialPort[] ports = SerialPort.getCommPorts();
		for(SerialPort p : ports) {
			if(p.getSystemPortName().equals("COM3")) { //this is the name that Arduino Uno always gives me
				arduino = p;
			}
		}
			if(arduino != null && arduino.openPort()) {
				System.out.println("Port " + arduino.getSystemPortName() + " opened successfully");
				arduino.setComPortParameters(9600, 8, 1, SerialPort.NO_PARITY);
				arduino.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 0, 0);
				reader = new Scanner(arduino.getInputStream());
				writer = new OutputStreamWriter(arduino.getOutputStream());
				System.out.println("done initialize");
			} else {
				System.out.println("Unable to open port");
				JOptionPane.showConfirmDialog(null, "Unable to open port", "ERROR", JOptionPane.DEFAULT_OPTION);
			}
		
	}
	
	public void reopenPort() {
		initialize();
	}
	
	public String portStatusString() {
		if(arduino == null) return "null";
		else if(arduino.isOpen()) return "open";
		else return "closed";
	}
	
	public boolean isPortOpen() {
		if(arduino != null)
			return arduino.isOpen();
		return false;
	}

	public boolean write(char command) {
		try {
			writer.append(command);
			writer.flush();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public char read() {
		if(reader.hasNextLine()) {
			return reader.nextLine().charAt(0);
		}
		return IDLE;
	}
}
