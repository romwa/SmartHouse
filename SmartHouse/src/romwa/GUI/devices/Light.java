package romwa.GUI.devices;

import java.awt.Color;

import processing.core.PApplet;
import romwa.FileIO.FileIO;
import romwa.GUI.HouseControl;
import romwa.GUI.TimeTable;
import romwa.GUI.buttons.CombinedButton;
import romwa.GUI.buttons.OnOffButton;
import romwa.GUI.buttons.TimeTableButton;
import romwa.system.SystemVariables;
import romwa.system.control.ArduinoHandler;
import romwa.system.control.LightControl;

public class Light {

	private CombinedButton button;
	
	private TimeTable timeTable;
	
	private LightControl control;
	
//	private LightControl lc;
	
	private boolean lightState;
	
	private int screen;
	
	public Light(ArduinoHandler arduino) {
		control = new LightControl(arduino);
		
		lightState = control.getState();
		
		button = new CombinedButton(lightState);
		
//		lc = new LightControl();
		String s = FileIO.fileSeparator;
		timeTable = new TimeTable("Data" + s + "timeTables" + s + "LightTimeTable.txt", "Light Time Table");
		
		screen = SystemVariables.MAIN_SCREEN;
	}
	
	public void checkAction(int day, int hour) {
		if(timeTable.timeTableHour(day, hour)) {
			turnLightOn();
		} else {
			turnLightOff();
		}
	}
	
	public void draw(PApplet drawer) {
		switch(screen) {
		case SystemVariables.MAIN_SCREEN:
			button.draw(drawer);
			break;
		case SystemVariables.TT_LIGHT_SCREEN:
			timeTable.draw(drawer);
			break;
		}
	}
	
	public void setScreen(int screen) {
		this.screen = screen;
	}
	
	public void setButtonSettings(double x, double y, double width, double height) {
		button.setStats(lightState, x, y, width, height);
		button.setText("Light Control", x+18, y-20, 17, Color.BLACK);
	}
	
	public void setTimeTableSettings(double x, double y, double width, double height) {
		timeTable.setStats(x, y, width, height);
	}
	
	public int mousePress(double x, double y) {
		if(screen == SystemVariables.MAIN_SCREEN) {
			int buttonPressed = button.mousePress(x, y);
			switch (buttonPressed) {
			case SystemVariables.ON_BUTTON:
				turnLightOn();
				return -1;
			case SystemVariables.OFF_BUTTON:
				turnLightOff();
				return -1;
			case SystemVariables.TIME_TABLE_BUTTON:
				return SystemVariables.TIME_TABLE_BUTTON;
			}
		} else if(screen == SystemVariables.TT_LIGHT_SCREEN){
			if(timeTable.mousePress(x, y)==0) {
				return SystemVariables.MAIN_SCREEN;
			}
		}
		return -1;
	}
	
	
	
	public void turnLightOn() {
		SystemVariables.changeCurrentAction(SystemVariables.LIGHT_ON);
		control.start();
		lightState = true;
		button.setDeviceState(lightState);
	}
	
	public void turnLightOff() {
		SystemVariables.changeCurrentAction(SystemVariables.LIGHT_OFF);
		control.stop();
		lightState = false;
		button.setDeviceState(lightState);
	}
}
