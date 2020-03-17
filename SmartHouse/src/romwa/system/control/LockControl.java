package romwa.system.control;


import java.util.Date;

import romwa.FileIO.FileIO;
import romwa.system.SystemVariables;

public class LockControl extends Control{

	ArduinoHandler arduino;

	public LockControl(ArduinoHandler arduino) {
		super("Data" + FileIO.fileSeparator + "log" + FileIO.fileSeparator + "LockLog.txt", "Data" + FileIO.fileSeparator + "states" + FileIO.fileSeparator + "LockState.txt");
		this.arduino = arduino;
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		if(arduino.isPortOpen()) {
			arduino.write(ArduinoHandler.LOCK);
			logStart();
			setState(true);
			Date d = new Date();
			System.out.println(d);
			SystemVariables.changeCurrentAction(SystemVariables.AWAITING_CONFIRMATION);
		}
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		if(arduino.isPortOpen()) {
			arduino.write(ArduinoHandler.UNLOCK);
			logStop();
			setState(false);
			SystemVariables.changeCurrentAction(SystemVariables.AWAITING_CONFIRMATION);
		}
	}

	public boolean isReady() {
		return (arduino!=null&&arduino.isPortOpen());
	}

}
