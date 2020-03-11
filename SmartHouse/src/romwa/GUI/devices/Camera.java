package romwa.GUI.devices;

import java.awt.Color;
import java.util.ArrayList;

import processing.core.PApplet;
import romwa.FileIO.FileIO;
import romwa.GUI.HouseControl;
import romwa.GUI.TimeTable;
import romwa.GUI.buttons.CombinedButton;
import romwa.GUI.buttons.OnOffButton;
import romwa.shapes.Rectangle;
import romwa.system.SystemVariables;
import romwa.system.control.CameraControl;

/**
 * This class represents a "drawable" camera
 * Contains GUI to control the camera
 * @author romwa
 * @version 2/3/2020
 */
public class Camera {

	//how to decide when the camera should be on.



	private CombinedButton button;

	//things for setting time table
	private TimeTable timeTable;

	private CameraControl cc;

	private boolean lockSequence;

	private boolean cameraState;

	private int screen;

	public Camera() {		
		button = new CombinedButton(cameraState);

		cc = new CameraControl();
		
		cameraState = cc.getState();
		
		String s = FileIO.fileSeparator;
		timeTable = new TimeTable("Data" + s + "timeTables" + s + "CameraTimeTable.txt", "Camera Time Table");
//		timeTable = new TimeTable("Data" + FileIO.fileSeparator + "CameraTimeTable.txt", "Camera Time Table");
		//		timeTable = new boolean[7][24];
		//		timeTableFile = "Data" + FileIO.fileSeparator + "CameraTimeTable.txt";

		lockSequence = false;

		screen = SystemVariables.MAIN_SCREEN;
	}
	
	public void checkAction(int day, int hour) {
		if(timeTable.timeTableHour(day, hour) && !cc.getState()) {
			startCamera();
		} else if(!timeTable.timeTableHour(day, hour) && cc.getState()){
			stopCamera();
		}
	}

//	public void drawButton(PApplet drawer) {
//		button.draw(drawer);
//	}
//
//	public void drawTimeTable(PApplet drawer) {
//		timeTable.draw(drawer);
//	}

	public void draw(PApplet drawer) {
		switch(screen) {
		case SystemVariables.MAIN_SCREEN:
			button.draw(drawer);
			break;
		case SystemVariables.TT_CAMERA_SCREEN:
			timeTable.draw(drawer);
			break;
		}
	}

	public void setScreen(int screen) {
		this.screen = screen;
	}



	//	public static void main(String[] args) {
	//		Camera c = new Camera();
	//		c.burnTimeTable();
	//	}

	//	private void setCameraState() {
	//		if(lockSequence) cameraState = true;
	//		else if(onOff.status()) cameraState = true;
	//		else if(timeTable[0/*day*/][0/*hour*/] == 0 && onOff.status()) cameraState = true;
	//		else if(timer != 0 && onOff.status()) cameraState = true;
	//		else cameraState = false;
	//	}



	//	public void update() {
	//		if(timeTable(0/*day*/, 0/*time*/))
	//		setCameraState();
	//	}

	public void record(int seconds) {
		cc.record(seconds);
	}

	//	public CombinedButton onOffButton() {
	//		return button;
	//	}

	public void setButtonSettings(double x, double y, double width, double height) {
		button.setStats(cameraState, x, y, width, height);
		button.setText("Camera Control", x+8, y-20, 17, Color.BLACK);
	}

	public void setTimeTableSettings(double x, double y, double width, double height) {
		timeTable.setStats(x, y, width, height);
	}

	public int mousePress(double x, double y) {
		if(screen == SystemVariables.MAIN_SCREEN) {
			int buttonPressed = button.mousePress(x, y);
			switch (buttonPressed) {
			case SystemVariables.ON_BUTTON:
				startCamera();
				return -1;
			case SystemVariables.OFF_BUTTON:
				stopCamera();
				return -1;
			case SystemVariables.TIME_TABLE_BUTTON:
				return SystemVariables.TIME_TABLE_BUTTON;
			}
		} else if(screen == SystemVariables.TT_CAMERA_SCREEN){
			if(timeTable.mousePress(x, y)==0) {
				return SystemVariables.MAIN_SCREEN;
			}
		}
		return -1;
	}

	private void startCamera() {
		cc.start();
		cameraState = true;
		button.setDeviceState(true);
	}

	private void stopCamera() {
		cc.stop();
		cameraState = false;
		button.setDeviceState(false);
	}

	//	public TimeTableButton timeTableButton() {
	//		return timeTable;
	//	}
}