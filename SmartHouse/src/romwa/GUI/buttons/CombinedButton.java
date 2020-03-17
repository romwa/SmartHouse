package romwa.GUI.buttons;

import java.awt.Color;
import java.awt.geom.Point2D;

import processing.core.PApplet;
import romwa.shapes.Rectangle;
import romwa.system.SystemVariables;

/**
 * This class represents a drawable "combined button" consisting of on, off, and time-table buttons.
 * The on/off buttons switch between them (only one is available at any given time)
 * @author romwa
 * @version 3/12/2020
 */
public class CombinedButton {

	private Rectangle stroke;
	private OnOffButton onButton;
	private OnOffButton offButton;
	private TimeTableButton ttButton;
	private LogButton logButton;

	private boolean deviceState;
	
	private String text;
	private Point2D textPlace;
	private double textSize;
	private Color textColor;
	
	private static final int 
			ON_BUTTON = SystemVariables.ON_BUTTON, 
			OFF_BUTTON = SystemVariables.OFF_BUTTON, 
			TIME_TABLE_BUTTON = SystemVariables.TIME_TABLE_BUTTON,
			LOG_BUTTON = SystemVariables.LOG_BUTTON;
	
	/**
	 * Creates an instance of the CombinedButton at (x,y) with width and height
	 * @param deviceState The state of the device that the button controls
	 * @param x The x coordinates of the button
	 * @param y The y coordinates of the button
	 * @param width The width of the button
	 * @param height The height of the button
	 */
	public CombinedButton(boolean deviceState, double x, double y, double width, double height) {
		setStats(deviceState, x, y, width, height);
	}

	/**
	 * Creates a default instance of the button at (0,0), width of 100 and height of 100
	 * @param cameraState The state of the device that the button controls
	 */
	public CombinedButton(boolean cameraState) {
		this(cameraState, 0, 0, 100, 100);
	}

	//	public void move(double x, double y) {
	//		stroke.move(x, y);
	//		onButton.move(x+5, y+5);
	//		offButton.move(x+5+smallWidth, y+5);
	//	}

	/**
	 * Sets the stats of the button (deviceState, x, y, width and height)
	 * @param deviceState The new state of the device
	 * @param x The new x coordinates of the button
	 * @param y The new y coordinates of the button
	 * @param width The new width of the button
	 * @param height The new height of the button
	 */
	public void setStats(boolean deviceState, double x, double y, double width, double height) {
		stroke = new Rectangle(x, y, width, height, Color.RED, new Color(0,0,0,0), 5);

		double smallWidth = (width-10)/2, smallHeight = (height-10)/2;
		onButton = new OnOffButton(OnOffButton.ON, x+5, y+5, smallWidth, smallHeight);
		offButton = new OnOffButton(OnOffButton.OFF, x+5+smallWidth, y+5, smallWidth, smallHeight);
		ttButton = new TimeTableButton(x+5, y+5+smallHeight, smallWidth, smallHeight);
		logButton = new LogButton(x+5+smallWidth, y+5+smallHeight, smallWidth, smallHeight);
		this.deviceState = deviceState;
		if(deviceState) {
			onButton.setStatus(false);
			offButton.setStatus(true);
		} else {
			onButton.setStatus(true);
			offButton.setStatus(false);
		}
	}
	
	/**
	 * Sets the text of the button at (x,y)
	 * @param text The text of the button
	 * @param x The x coordinates of the text
	 * @param y The y coordinates of the text
	 * @param size The size of the text
	 * @param color The color of the text
	 */
	public void setText(String text, double x, double y, double size, Color color) {
		this.text = text;
		textPlace = new Point2D.Double(x,y);
		textSize = size;
		textColor = color;
	}

	/**
	 * Switching between the on and off buttons
	 */
	private void switchOnOff() {
		if(onButton.status()) {
			onButton.setStatus(false);
			offButton.setStatus(true);
		} else {
			onButton.setStatus(true);
			offButton.setStatus(false);
		}
	}
	
	/**
	 * Handles a mouse press at coordinates (x,y):
	 * If on button is available, and pressed, change to off button and return SystemVariables.ON_BUTTON. 
	 * Same for off button.
	 * If time-table button was pressed, return SystemVariables.TIME_TABLE_BUTTON.
	 * Returns -1 if no button was pressed
	 * @param x The x coordinates of the mouse press
	 * @param y The y coordinates of the mouse press
	 * @return SystemVariables.ON_BUTTON if on button was pressed, SystemVariables.OFF_BUTTON if off button was pressed,
	 * SystemVariables.TIME_TABLE_BUTTON if time-table button was pressed and -1 if no button was pressed
	 */
	public int mousePress(double x, double y) {
		if(onButton.status() && onButton.checkPoint(x, y)) {
			onButton.changeStatus();
			offButton.changeStatus();
			return ON_BUTTON;
		} else if(offButton.status() && offButton.checkPoint(x, y)) {
			onButton.changeStatus();
			offButton.changeStatus();
			return OFF_BUTTON;
		} else if(ttButton.checkPoint(x, y)) {
			return TIME_TABLE_BUTTON;
		} else if(logButton.checkPoint(x, y)) {
			return LOG_BUTTON;
		}
		return -1;
	}
	
	/**
	 * Sets the state of the device
	 * @param state The new state of the device
	 */
	public void setDeviceState(boolean state) {
		deviceState = state;
		if(deviceState) {
			if(onButton.status()) {
				switchOnOff();
			}
			stroke.setStrokeColor(Color.GREEN);
		} else {
			if(offButton.status()) {
				switchOnOff();
			}
			stroke.setStrokeColor(Color.RED);
		}
	}

	/**
	 * Draws an instance of the CombinedButton with the specified attributes
	 * @param drawer PApplet
	 * @pre Marker attributes set beforehand except stroke, fill, and strokeWeight
	 * @post PApplet attributes stroke = strokeColor, fill = text color specified (time-table button fill if no text), 
	 * strokeWeight = strokeWeight, textSize = text size specified
	 */
	public void draw(PApplet drawer) {
		stroke.draw(drawer);
		onButton.draw(drawer);
		offButton.draw(drawer);
		ttButton.draw(drawer);
		logButton.draw(drawer);
		if(text!=null && textPlace != null && textColor!=null) {
			drawer.fill(textColor.getRGB());
			drawer.textSize((float)textSize);
			drawer.text(text, (float)textPlace.getX(), (float)textPlace.getY());
		}
	}
}
