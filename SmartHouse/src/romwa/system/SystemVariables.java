package romwa.system;

public abstract class SystemVariables {

	//variables for HouseControl
	//screens
	public static final int MAIN_SCREEN = 0;
	public static final int TT_CAMERA_SCREEN = 1;
	public static final int TT_LOCK_SCREEN = 2;
	public static final int TT_LIGHT_SCREEN = 3;
	
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
	
	public static void changeCurrentAction(char action) {
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
}
