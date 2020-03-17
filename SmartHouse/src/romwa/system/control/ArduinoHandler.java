package romwa.system.control;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Scanner;

import javax.swing.JOptionPane;

import com.fazecast.jSerialComm.SerialPort;

import romwa.system.SystemVariables;

public class ArduinoHandler {

	private SerialPort arduino;
//	private InputStreamReader reader;
	private InputStream reader;
	//	private BufferedWriter writer;
	private OutputStreamWriter writer;

	public static final char 
	LIGHT_ON=SystemVariables.LIGHT_ON, 
	LIGHT_OFF=SystemVariables.LIGHT_OFF, 
	LOCK=SystemVariables.LOCK, 
	UNLOCK=SystemVariables.UNLOCK, 
	CONFIRM=SystemVariables.CONFIRM, 
	IDLE=SystemVariables.IDLE;
	
//	public static final int COM_NUM=SystemVariables.COM_NUM;


	public ArduinoHandler() {
		initialize();
	}

	private void initialize() {
		SerialPort[] ports = SerialPort.getCommPorts();
		for(SerialPort p : ports) {
			if(p.getSystemPortName().equals("COM" + 12)) { //this is the name that Arduino Uno always gives me
				arduino = p;
			}
		}
		if(arduino != null && arduino.openPort()) {
			System.out.println("Port " + arduino.getSystemPortName() + " opened successfully");
			arduino.setComPortParameters(9600, 8, 1, SerialPort.NO_PARITY);
			arduino.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 0, 0);
			//				reader = new Scanner(arduino.getInputStream());
			writer = new OutputStreamWriter(arduino.getOutputStream());
//			reader = new InputStreamReader(arduino.getInputStream());
			reader = arduino.getInputStream();
			System.out.println("Getting reader ready...");

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
		char read = IDLE;
		try {
//			System.out.println("trying to read: ");
			//			System.out.println("reader init: " + reader != null);
			//			System.out.println("reader state: " + reader.ready());
			int numToRead = 1;
			byte[] message = new byte[numToRead];
//			if(reader.read(message)) {
				reader.read(message, 0, numToRead);
//				System.out.println(read);
//				for(int i = 0; i < numToRead; i++) {
//					System.out.println("byte" + i + ": " + message[i]);
//				}
				int readI = message[0];
//				System.out.println(readI);
				read = (char)readI;
//				System.out.println(reader.available());
//				System.out.println("read successfully");
//			} else System.out.println("Reader not ready");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return read;
		//		if(reader.hasNextLine()) {
		//			return reader.nextLine().charAt(0);
		//		}

	}

	//*********************************************************************************

	public static void main(String[] args) {
		ArduinoHandler ah = new ArduinoHandler();
		Scanner s = new Scanner(System.in);
		while(true) {
			System.out.println("what to send");
			char c = s.nextLine().charAt(0);
			if(c == 'e') break;
			ah.write(c);
			System.out.println("sent");
			char message = ah.read();
			//			System.out.println("read");
			//			while(message == IDLE) {
			//				System.out.println("in while " + message);
//			try {
//				Thread.sleep(100);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			message = ah.read();
			System.out.println("read: " + message);
			//			}
//			System.out.println("done reading");
//			System.out.println("found "+message);
		}
	}
}
