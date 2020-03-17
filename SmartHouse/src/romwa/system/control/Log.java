package romwa.system.control;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import romwa.FileIO.FileIO;

/**
 * This class represents log of a device, with a window to show the log.
 * The log keeps only the last 100 actions
 * @author romwa
 * @version 14/3/2020
 */
public class Log extends JFrame{

	private String logFile;

	private Container c;

	private JTextArea log;
	
	private JScrollPane scroll;

	public Log(String logFile) {

		this.logFile = logFile;

		setFeatures();
	}

	
	private void setFeatures() {
		c = new Container();

		log = new JTextArea();
		log.setBounds(10, 10, 270, 845);
		log.setEditable(false);
		log.setVisible(true);
		
		scroll = new JScrollPane();
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.getViewport().setBackground(Color.WHITE);
		scroll.getViewport().add(log);
		scroll.setBounds(10,10,270,845);

		c.add(scroll);
		
		this.move(50, 50);
		this.setSize(300, 900);
		this.setMinimumSize(new Dimension(100,100));
		//		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(true);
		
		this.add(c);
	}

	public String read() {
		ArrayList<String> file = FileIO.readFile(logFile);
		flip(file);
		String str = "";
		for (int i = 0; i < file.size(); i++) {
			str += i + " " + file.get(i) + "\n";
		}
		return str;
	}

	public void openLog() {
		log.setText(read());
		this.setVisible(true);
	}


	public void write(String newLine) {
		// TODO Auto-generated method stub
		ArrayList<String> newLog = FileIO.readFile(logFile);
		newLog.add(newLine);
		FileIO.writeFile(logFile, newLog);
	}
	
	private void flip(ArrayList<String> log) {
		int size = log.size();
		String[] newLog = new String[size];
		int i = 0;
		while (!log.isEmpty()) {
			newLog[i] = log.remove(log.size()-1);
			i++;
		}
		for (i = 0; i < newLog.length; i++) {
			log.add(newLog[i]);
		}
	}
	
	//*********************************************************************
//	public static void main(String[] args) {
//		Log l = new Log("Data\\log\\test");
//		l.write("this is the forth line");
//	}
}
