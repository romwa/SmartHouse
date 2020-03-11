package romwa.GUI;

import java.awt.Color;
import java.util.Date;

import processing.core.PApplet;
import romwa.GUI.buttons.Button;
import romwa.GUI.buttons.CombinedButton;
import romwa.GUI.devices.Camera;
import romwa.GUI.devices.Light;
import romwa.GUI.devices.Lock;
import romwa.system.SystemVariables;
import romwa.system.control.ArduinoHandler;

public class HouseControl extends PApplet{

//	Camera camera;
	private Camera camera;
	private Light light;
	private Lock lock;
	private ArduinoHandler arduino;
	
	private Button reopenPortButton;
	
	private int screen;
	
	private boolean start;
	
	
	public static final float DRAWING_WIDTH = 788, DRAWING_HEIGHT = 565;
	
	
	public HouseControl() {
		arduino = new ArduinoHandler();
		camera = new Camera();
		lock = new Lock(arduino);
		light = new Light(arduino);
		setThings();
		screen = 0;
		start = true;
	}
	
//	public void settings() {
//		this.setSize(800, 500);
//		this.
//	}
//	
	
	public void setThings() {
		
		reopenPortButton = new Button((float)DRAWING_WIDTH-200, (float)40, 100, 20, Color.BLACK, new Color(0,0,0,0), 1);
		reopenPortButton.setText("Reopen Port", (float)DRAWING_WIDTH-190, (float)55, 12, Color.BLACK);
		
		camera.setButtonSettings(50, DRAWING_HEIGHT/2-75, 150, 150);
		light.setButtonSettings(DRAWING_WIDTH/2-75, DRAWING_HEIGHT/2-75, 150, 150);
		lock.setButtonSettings(DRAWING_WIDTH-200, DRAWING_HEIGHT/2-75, 150, 150);
		
		camera.setTimeTableSettings(10, 10, DRAWING_WIDTH-20, DRAWING_HEIGHT-20);
		light.setTimeTableSettings(10, 10, DRAWING_WIDTH-20, DRAWING_HEIGHT-20);
		lock.setTimeTableSettings(10, 10, DRAWING_WIDTH-20, DRAWING_HEIGHT-20);
	}
	
	//width 788 height 565
	public void draw() {
		scale((float)width/DRAWING_WIDTH, (float)height/DRAWING_HEIGHT);
		background(255);
		Date d = new Date();
		int seconds = d.getSeconds();
		int minute = d.getMinutes();
		int day = d.getDay();
		int hour = d.getHours();
		if(minute==0 && seconds == 0) {
			camera.checkAction(day, hour);
			light.checkAction(day, hour);
			lock.checkAction(day, hour);
		}
		if(start) {//checks the hour if the program was just started
			camera.checkAction(day, hour);
			start = false;
		}
		switch (screen) {
		case SystemVariables.MAIN_SCREEN:
			camera.draw(this);
			light.draw(this);
			lock.draw(this);
			break;
		case SystemVariables.TT_CAMERA_SCREEN:
			camera.draw(this);
			break;
		case SystemVariables.TT_LIGHT_SCREEN:
			light.draw(this);
			break;
		case SystemVariables.TT_LOCK_SCREEN:
			lock.draw(this);
			break;
		}
		if (SystemVariables.currentAction() == SystemVariables.AWAITING_CONFIRMATION) {
			SystemVariables.changeCurrentAction(SystemVariables.IDLE);
			//need to fix
//			char message = arduino.read();
//			if(message == SystemVariables.CONFIRM) {
//				SystemVariables.changeCurrentAction(SystemVariables.IDLE);
//			}
		}
//		camera.drawTimeTable(this);
		textSize(12);
		fill(0);
		text(d.toString(), 10f, (float)(DRAWING_HEIGHT-10));
		text("Arduino State: " + SystemVariables.actionToString(SystemVariables.currentAction()), (float)DRAWING_WIDTH-200, (float)20);
		text("Port Status: " + arduino.portStatusString(), (float)DRAWING_WIDTH-200, (float)36);
		reopenPortButton.draw(this);
	}
	
	public void mousePressed() {
		mouseX = (int) (mouseX/((float)width/DRAWING_WIDTH));
		mouseY = (int)(mouseY/((float)height/DRAWING_HEIGHT));
		if(screen == SystemVariables.MAIN_SCREEN) {
			if(reopenPortButton.checkPoint(mouseX, mouseY)) {
				arduino.reopenPort();
			}
			int action = camera.mousePress(mouseX, mouseY);
			if(action==SystemVariables.TIME_TABLE_BUTTON) {
				screen = SystemVariables.TT_CAMERA_SCREEN;
				camera.setScreen(SystemVariables.TT_CAMERA_SCREEN);
			}
			action = light.mousePress(mouseX, mouseY);
			if(action==SystemVariables.TIME_TABLE_BUTTON) {
				screen = SystemVariables.TT_LIGHT_SCREEN;
				light.setScreen(SystemVariables.TT_LIGHT_SCREEN);
			}
			action = lock.mousePress(mouseX, mouseY);
			if(action==SystemVariables.TIME_TABLE_BUTTON) {
				screen = SystemVariables.TT_LOCK_SCREEN;
				lock.setScreen(SystemVariables.TT_LOCK_SCREEN);
			}
		} else if(screen == SystemVariables.TT_CAMERA_SCREEN) {
			int action = camera.mousePress(mouseX, mouseY);
			if(action == SystemVariables.MAIN_SCREEN) {
				camera.setScreen(SystemVariables.MAIN_SCREEN);
				screen = SystemVariables.MAIN_SCREEN;
			}
		} 
		else if(screen == SystemVariables.TT_LIGHT_SCREEN) {
			int action = light.mousePress(mouseX, mouseY);
			if(action == SystemVariables.MAIN_SCREEN) {
				light.setScreen(SystemVariables.MAIN_SCREEN);
				screen = SystemVariables.MAIN_SCREEN;
			}
		} else if(screen == SystemVariables.TT_LOCK_SCREEN) {
			int action = lock.mousePress(mouseX, mouseY);
			if(action == SystemVariables.MAIN_SCREEN) {
				lock.setScreen(SystemVariables.MAIN_SCREEN);
				screen = SystemVariables.MAIN_SCREEN;
			}
		}
		
		
//		if(camera.onOffButton().checkPoint(mouseX, mouseY)) {
//			camera.onOffButton().changeStatus();
//			if(camera.onOffButton().status()) {
//				camera.startRecording();
//			} else {
//				camera.stopRecording();
//			}
//		}
	}
	
//	public void saveData() {
//		camera.burnTimeTable();
//	}
}
