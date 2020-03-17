package romwa.system;

public abstract class SystemVariables {

	//variables for HouseControl
	//screens
	public static final int MAIN_SCREEN = 0;
	public static final int TT_CAMERA_SCREEN = 1;
	public static final int TT_LOCK_SCREEN = 2;
	public static final int TT_LIGHT_SCREEN = 3;
	public static final int MANUAL_SCREEN = 4;
	public static final int LOADING_SCREEN = 5;
	
	//variables for ArduinoHandler
	//commands
	public static final char LIGHT_ON='n' ;
	public static final char LIGHT_OFF='f' ;
	public static final char LOCK='l' ;
	public static final char UNLOCK='u' ;
	public static final char CONFIRM='c' ;
	public static final char AWAITING_CONFIRMATION = 'a';
	public static final char IDLE='e' ;
	private static char currentAction = IDLE;
	private static char lastAction = IDLE;
	public static final char START_CAMERA = 'c';
	public static final char STOP_CAMERA = 's';
	private static boolean tryingToRead = false;
	//com
	public static final int COM_NUN=12;
	
	private static void setTryingToRead(boolean read) {
		tryingToRead = read;
	}
	
	public static void stopReading() {
		setTryingToRead(false);
	}
	
	public static boolean isIdle() {
		return currentAction == IDLE;
	}
	
	public static void changeCurrentAction(char action) {
		if(action == AWAITING_CONFIRMATION) setTryingToRead(true);
		if(action == IDLE && lastAction == AWAITING_CONFIRMATION) setTryingToRead(false);
		lastAction = currentAction;
		currentAction = action;
	}
	
	public static char currentAction() {
		return currentAction;
	}
	
	public static char lastAction() {
		return lastAction;
	}
	
	public static String actionToString(char action) {
		switch(action) {
		case LIGHT_ON: return "turning light on";
		case LIGHT_OFF: return "turning light off";
		case LOCK: return "locking";
		case UNLOCK: return "unlocking";
		case AWAITING_CONFIRMATION: return "awaiting confirmation";
		case IDLE: return "idle";
		}
		return "no such action";
	}
	
	//variables for CombinedButton
	//button kinds
	public static final int ON_BUTTON = 100;
	public static final int OFF_BUTTON = 101;
	public static final int TIME_TABLE_BUTTON = 102;
	public static final int LOG_BUTTON = 103;
	

	public static boolean tryingToRead() {
		// TODO Auto-generated method stub
		return tryingToRead;
	}
}
