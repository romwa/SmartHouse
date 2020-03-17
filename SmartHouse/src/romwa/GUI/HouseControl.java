package romwa.GUI;

import java.awt.Color;
import java.util.Date;

import javax.swing.JTextArea;

import processing.core.PApplet;
import romwa.GUI.buttons.Button;
import romwa.GUI.devices.Camera;
import romwa.GUI.devices.Light;
import romwa.GUI.devices.Lock;
import romwa.system.SystemVariables;
import romwa.system.control.ArduinoHandler;
import romwa.system.control.Log;
import romwa.system.dataBases.Stack;

public class HouseControl extends PApplet {

	//	Camera camera;
	private Camera camera;
	private Light light;
	private Lock lock;
	private ArduinoHandler arduino;

	private Button reopenPortButton;
	private Button manual;

	private int screen;

	private boolean start;

	private final int LOADING_SCREEN = SystemVariables.LOADING_SCREEN;
//	private int loadingTimer = 0;
//	private final int loadingDone = 240;

	//camera: -1 - no action, 0 - stop, 1 - start
	//lock: -1 - no action, 0 - lock, 1 - unlock
	//light: -1 - no action, 0 - turn off, 1 - turn on
	private Stack<Character> actionStack;


	public static final float DRAWING_WIDTH = 988, DRAWING_HEIGHT = 865;

	private char message;

	private boolean checkedThisHour;


	public HouseControl() {
		screen = SystemVariables.MAIN_SCREEN;
//		loadingTimer = 0;
		initialize();
	}

	public void initialize() {
//		new Thread() {
//			public void run() {
				actionStack = new Stack<Character>();
				arduino = new ArduinoHandler();
				camera = new Camera();
				lock = new Lock(arduino);
				light = new Light(arduino);
				setThings();
				start = true;
				checkedThisHour = false;
//			}
//		}.start();
	}

	//	public void settings() {
	//		this.setSize(800, 500);
	//		this.
	//	}
	//	

	public void setThings() {



		reopenPortButton = new Button(10, (float)52, 90, 16, Color.BLACK, new Color(0,0,0,0), 1);
		reopenPortButton.setText("Reopen Port", 20, (float)64, 12, Color.BLACK);

		manual = new Button(DRAWING_WIDTH-200, DRAWING_HEIGHT-100, 100, 50, Color.BLACK, Color.CYAN, 1);
		manual.setText("manual", DRAWING_WIDTH-185, DRAWING_HEIGHT-68, 20, Color.BLACK);

		camera.setButtonSettings(50, DRAWING_HEIGHT/2-75, 150, 150);
		light.setButtonSettings(DRAWING_WIDTH/2-75, DRAWING_HEIGHT/2-75, 150, 150);
		lock.setButtonSettings(DRAWING_WIDTH-200, DRAWING_HEIGHT/2-75, 150, 150);

		camera.setTimeTableSettings(10, 20, DRAWING_WIDTH-20, DRAWING_HEIGHT-20);
		light.setTimeTableSettings(10, 20, DRAWING_WIDTH-20, DRAWING_HEIGHT-20);
		lock.setTimeTableSettings(10, 20, DRAWING_WIDTH-20, DRAWING_HEIGHT-20);
	}

	//width 788 height 565
	public void draw() {
		//		System.out.println(width + " " + height);
		scale((float)width/DRAWING_WIDTH, (float)height/DRAWING_HEIGHT);
		background(255);
		Date d = new Date();
		int seconds = d.getSeconds();
		int minute = d.getMinutes();
		int day = d.getDay();
		int hour = d.getHours();
		if((minute==0 && seconds == 0)) checkedThisHour = false;
		if(start || ((minute==0 && seconds == 0) && !checkedThisHour)) {
			//			System.out.println("TT");
			addLockAction(lock.checkAction(day, hour));
			addCameraAction(camera.checkAction(day, hour));
			addLightAction(light.checkAction(day, hour));
			checkedThisHour = true;
			start = false;
		}

		switch (screen) {
		case SystemVariables.MAIN_SCREEN:
			camera.draw(this);
			light.draw(this);
			lock.draw(this);
			manual.draw(this);
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
		case SystemVariables.MANUAL_SCREEN:
			textSize(15);
			fill(0);
			text(manual(), 170, 15);
			manual.draw(this);
			break;
		case LOADING_SCREEN:
			textSize(30);
			fill(0);
			text("LOADING", 170, 100);
//			loadingTimer++;
//			if(loadingTimer == loadingDone) screen = SystemVariables.MAIN_SCREEN;
			break;
		}
		if (SystemVariables.currentAction() == SystemVariables.AWAITING_CONFIRMATION) {
			//			SystemVariables.changeCurrentAction(SystemVariables.IDLE);
			//			System.out.println(new Date().toString());
			//need to fix
			arduino.write(SystemVariables.AWAITING_CONFIRMATION);
			if(SystemVariables.tryingToRead()) readFromArduino();

			if(message == SystemVariables.CONFIRM) SystemVariables.changeCurrentAction(SystemVariables.IDLE);
			//			while(message != SystemVariables.CONFIRM) {
			//				message = arduino.read();
			//				System.out.println("read");
			//			}
			//			SystemVariables.changeCurrentAction(SystemVariables.IDLE);
			//			if(message == SystemVariables.CONFIRM) {
			//				SystemVariables.changeCurrentAction(SystemVariables.IDLE);
			//			}

		} else if(SystemVariables.currentAction() == SystemVariables.IDLE) {
			act();
		}
		//		camera.drawTimeTable(this);
		if(screen != LOADING_SCREEN) {
			fill(0,0,0,0);
			rect(5, 7, 140, 65);
			textSize(12);
			fill(0);
			text(d.toString(), 10f, (float)(DRAWING_HEIGHT-10));
			text("Arduino State:" , 10, (float)20);
			text(SystemVariables.actionToString(SystemVariables.currentAction()), 10, 34);
			text("Port Status: " + arduino.portStatusString(), 10, (float)47);
			reopenPortButton.draw(this);
		}
	}

	public void readFromArduino() {
		message = SystemVariables.currentAction();
		new Thread() {
			public void run() {
				SystemVariables.stopReading();
				//				System.out.println("trying to read");
				message = arduino.read();
				//				System.out.println("read: " + message);
				if(message == SystemVariables.CONFIRM)
					SystemVariables.changeCurrentAction(SystemVariables.IDLE);
			}
		}.start();
	}

	public void mousePressed() {
		mouseX = (int) (mouseX/((float)width/DRAWING_WIDTH));
		mouseY = (int)(mouseY/((float)height/DRAWING_HEIGHT));
		if(screen == SystemVariables.MAIN_SCREEN) {
			if(manual.checkPoint(mouseX, mouseY)) {
				manual.move(DRAWING_WIDTH/2-50, DRAWING_HEIGHT-55);
				manual.setText("back", DRAWING_WIDTH/2-23, DRAWING_HEIGHT-23, 20, Color.BLACK);
				screen = SystemVariables.MANUAL_SCREEN;
				//				this.setSize((int)DRAWING_WIDTH, (int)DRAWING_HEIGHT+300);
			}
			if(reopenPortButton.checkPoint(mouseX, mouseY)) {
				arduino.reopenPort();
			}
			int action = camera.mousePress(mouseX, mouseY);
			addCameraAction(action);
			switch(action) {
			case  SystemVariables.TIME_TABLE_BUTTON:
				screen = SystemVariables.TT_CAMERA_SCREEN;
				camera.setScreen(SystemVariables.TT_CAMERA_SCREEN);
				break;
			case SystemVariables.LOG_BUTTON:
				camera.openLog();
			}


			action = light.mousePress(mouseX, mouseY);
			addLightAction(action);
			switch(action) {
			case  SystemVariables.TIME_TABLE_BUTTON:
				screen = SystemVariables.TT_LIGHT_SCREEN;
				light.setScreen(SystemVariables.TT_LIGHT_SCREEN);
				break;
			case SystemVariables.LOG_BUTTON:
				light.openLog();
			}


			action = lock.mousePress(mouseX, mouseY);
			addLockAction(action);
			switch(action) {
			case  SystemVariables.TIME_TABLE_BUTTON:
				screen = SystemVariables.TT_LOCK_SCREEN;
				lock.setScreen(SystemVariables.TT_LOCK_SCREEN);
				break;
			case SystemVariables.LOG_BUTTON:
				lock.openLog();
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
		} else if(screen == SystemVariables.MANUAL_SCREEN ) {
			if(manual.checkPoint(mouseX, mouseY)) {
				manual.move(DRAWING_WIDTH-200, DRAWING_HEIGHT-100);
				manual.setText("manual", DRAWING_WIDTH-185, DRAWING_HEIGHT-68, 20, Color.BLACK);
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

	private void act() {
		//		if(actionStack != null && !actionStack.isEmpty())
		//			System.out.println(actionStack.toString());
		if(!actionStack.isEmpty() && SystemVariables.isIdle()) {
			char action = actionStack.pop();
			switch (action) {
			case SystemVariables.LOCK:
				lock.lock();
				break;
			case SystemVariables.UNLOCK:
				lock.unlock();
				break;
			case SystemVariables.LIGHT_ON:
				System.out.println("light on sequence");
				light.turnLightOn();
				break;
			case SystemVariables.LIGHT_OFF:
				light.turnLightOff();
				break;
			case SystemVariables.START_CAMERA:
				camera.startCamera();
				break;
			case SystemVariables.STOP_CAMERA:
				camera.stopCamera();
				break;
			}
		}
	}

	private void addAction(char c) {
		actionStack.push(c);
	}

	private void addCameraAction(int action) {
		switch(action) {
		case 1:
			addAction(SystemVariables.START_CAMERA);
			break;
		case 0: 
			addAction(SystemVariables.STOP_CAMERA);
			break;
		case -1:
			break;
		}
	}

	private void addLightAction(int action) {
		switch(action) {
		case 1:
			System.out.println("add light on action");
			addAction(SystemVariables.LIGHT_ON);
			break;
		case 0:
			addAction(SystemVariables.LIGHT_OFF);
			break;
		}
	}

	private void addLockAction(int action) {
		switch(action) {
		case 1:
			//			System.out.println("added lock");
			addAction(SystemVariables.UNLOCK);
			break;
		case 0:
			addAction(SystemVariables.LOCK);
			break;
		}
	}

	//	private void setActions() {
	//		switch(lockAction) {
	//		case 1:
	//			actionStack.push(SystemVariables.UNLOCK);
	//			break;
	//		case 0:
	//			actionStack.push(SystemVariables.LOCK);
	//			break;
	//		case -1:
	//			break;
	//		}
	//		lockAction = -1;
	//		
	//		switch(cameraAction) {
	//		case 1:
	//			actionStack.push(SystemVariables.START_CAMERA);
	//			break;
	//		case 0:
	//			actionStack.push(SystemVariables.STOP_CAMERA);
	//			break;
	//		case -1:
	//			break;
	//		}
	//		cameraAction = -1;
	//		
	//		switch(lightAction) {
	//		case 1:
	//			actionStack.push(SystemVariables.LIGHT_ON);
	//			break;
	//		case 0:
	//			actionStack.push(SystemVariables.LIGHT_OFF);
	//			break;
	//		case -1:
	//			break;
	//		}
	//		lightAction = -1;
	//	}

	//add how to know if work or not
	private String manual() {
		String manual = "";
		manual += "Control Buttons:\n\n";
		//				+ "General: The perimeter (outside rectangle) is the state of the device\n\n";

		manual += "Camera:\nGreen Button - Turns camera on\n"
				+ "Red Button - Turns camera off\n"
				+ "Purple Button - Opens the Time Table for the camera\n"
				+ "Additional Information: Moving of the mouse might prevent \nthe camera from working\n"
				+ "                        Perimeter: red - camera off, green - camera on\n\n";

		manual += "Light Control:\nGreen Button - Turns light on\n"
				+ "Red Button - Turns light off\n"
				+ "Purple Button - Opens the Time Table for the light\n"
				+ "Additional Information: Perimeter: red - light off, green - light on\n\n";

		manual += "Lock Control:\nGreen Button - Unlock (allows the door to open)\n"
				+ "Red Button - Lock (doesn't allow the door to open)\n"
				+ "Purple Button - Opens the Time Table for the lock\n"
				+ "Additional Information: Time Table unlocks the lock (allows the door to open)\n"
				+ "                        Perimeter: red - locked, green - unlocked\n\n";

		manual += "Time Table:\nPress on the time and day that you want the device to work at\n"
				+ "For example, if I choose Sunday at 14, the device will be on from 14:00 to 15:00/n/n";

		return manual;
	}

	//	public void saveData() {
	//		camera.burnTimeTable();
	//	}
}
