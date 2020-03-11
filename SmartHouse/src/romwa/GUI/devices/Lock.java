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
import romwa.system.control.LockControl;

public class Lock {

private CombinedButton button;
	
	private TimeTable timeTable;
	
	private LockControl control;
	
	//false = lock, true = unlock
	private boolean lockState;
	
	private int screen;
	
	public Lock(ArduinoHandler arduino) {
		control = new LockControl(arduino);
		
		lockState = control.getState();
		
		button = new CombinedButton(lockState);
		
		if(control.isReady()) lock();
		
//		lc = new LightControl();
		
		String s = FileIO.fileSeparator;
		timeTable = new TimeTable("Data" + s + "timeTables" + s + "LockTimeTable.txt", "Lock Time Table");
		
		screen = SystemVariables.MAIN_SCREEN;
	}
	
	public void checkAction(int day, int hour) {
		if(timeTable.timeTableHour(day, hour)) {
			unlock();
		} else {
			lock();
		}
	}
	
	public void draw(PApplet drawer) {
		switch(screen) {
		case SystemVariables.MAIN_SCREEN:
			button.draw(drawer);
			break;
		case SystemVariables.TT_LOCK_SCREEN:
			timeTable.draw(drawer);
			break;
		}
	}
	
	public void setScreen(int screen) {
		this.screen = screen;
	}
	
	public void setButtonSettings(double x, double y, double width, double height) {
		button.setStats(lockState, x, y, width, height);
		button.setText("Lock Control", x+18, y-20, 17, Color.BLACK);
	}
	
	public void setTimeTableSettings(double x, double y, double width, double height) {
		timeTable.setStats(x, y, width, height);
	}
	
	public int mousePress(double x, double y) {
		if(screen == SystemVariables.MAIN_SCREEN) {
			int buttonPressed = button.mousePress(x, y);
			switch (buttonPressed) {
			case SystemVariables.ON_BUTTON:
				unlock();
				return -1;
			case SystemVariables.OFF_BUTTON:
				lock();
				return -1;
			case SystemVariables.TIME_TABLE_BUTTON:
				return SystemVariables.TIME_TABLE_BUTTON;
			}
		} else if(screen == SystemVariables.TT_LOCK_SCREEN){
			if(timeTable.mousePress(x, y)==0) {
				return SystemVariables.MAIN_SCREEN;
			}
		}
		return -1;
	}
	
	public void lock() {
		SystemVariables.changeCurrentAction(SystemVariables.LOCK);
		control.start();
		lockState = false;
		button.setDeviceState(lockState);
	}
	
	public void unlock() {
		SystemVariables.changeCurrentAction(SystemVariables.UNLOCK);
		control.stop();
		lockState = true;
		button.setDeviceState(lockState);
	}
}
