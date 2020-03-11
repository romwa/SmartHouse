package romwa.system.control;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import romwa.FileIO.FileIO;

public class CameraControl extends Control{

	private Robot r;

	private Runtime runtime;
	

	public CameraControl() {
		super("Data" + FileIO.fileSeparator + "log" + FileIO.fileSeparator + "CameraLog.txt", "Data" + FileIO.fileSeparator + "states" + FileIO.fileSeparator + "CameraState.txt");
		try {
			r = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		runtime = Runtime.getRuntime();
	}

	public boolean openApp() {
		try { 
			runtime.exec("C:\\Program Files\\Logitech\\Logitech WebCam Software\\LWS.exe");
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Records for the amount of time
	 * @param time The time in seconds
	 */
	public void record(long time) {
		if(!getState())
		try {
			start();
			TimeUnit.SECONDS.sleep(time);
			if(getState())
				stop();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void start() {
		if(setState(true)) {
			try {
				openApp();
				TimeUnit.MILLISECONDS.sleep(200);

				//click tool bar
				r.mouseMove(50, 100);
				r.mousePress(InputEvent.BUTTON1_DOWN_MASK);
				r.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
				TimeUnit.MILLISECONDS.sleep(2800);

				//click start recording
				r.mouseMove(830, 550);
				r.mousePress(InputEvent.BUTTON1_DOWN_MASK);
				r.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
				
				logStart();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	

	public void stop() {
		//click stop recording
		if(setState(false)) {
			r.mouseMove(830, 550);
			r.mousePress(InputEvent.BUTTON1_DOWN_MASK);
			r.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

			//closes recording window
			r.mouseMove(910, 260);
			r.mousePress(InputEvent.BUTTON1_DOWN_MASK);
			r.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

			//closing tool bar
			r.mouseMove(70, 10);
			r.mousePress(InputEvent.BUTTON1_DOWN_MASK);
			r.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
			logStop();
		}
	}
	


	//******************************************************************************************
//	public static void main(String[] args) {
//		CameraControl cc = new CameraControl();
//		cc.record(5);
//	}
}
