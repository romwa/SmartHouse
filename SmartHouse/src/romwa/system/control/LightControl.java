package romwa.system.control;

import romwa.FileIO.FileIO;
import romwa.system.SystemVariables;

public class LightControl extends Control{

	ArduinoHandler arduino;

	public LightControl(ArduinoHandler arduino) {
		super("Data" + FileIO.fileSeparator + "log" + FileIO.fileSeparator + "LightLog.txt", "Data" + FileIO.fileSeparator + "states" + FileIO.fileSeparator + "LightState.txt");
		this.arduino = arduino;
	}


	@Override
	public void start() {
		// TODO Auto-generated method stub
		if(arduino.isPortOpen()) {
			arduino.write(ArduinoHandler.LIGHT_ON);
			logStart();
			setState(true);
			SystemVariables.changeCurrentAction(SystemVariables.AWAITING_CONFIRMATION);
		}
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		if(arduino.isPortOpen()) {
			arduino.write(ArduinoHandler.LIGHT_OFF);
			logStop();
			setState(false);
			SystemVariables.changeCurrentAction(SystemVariables.AWAITING_CONFIRMATION);
		}
	}

	public boolean isReady() {
		return (arduino!=null&&arduino.isPortOpen());
	}

}
