package romwa.GUI.buttons;

import java.awt.Color;
import java.awt.geom.Point2D;

import processing.core.PApplet;
import romwa.shapes.Rectangle;

/**
 * This class represents a drawable Button
 * @author romwa
 * @version 3/8/2020
 */
public class Button {

	private Rectangle button;
	
	private String text;
	private Point2D textPlace;
	private double textSize;
	private Color textColor;
			
	/**
	 * Creates an instance of the Button at coordinates (x, y) with width and height
	 * @param x The x coordinates
	 * @param y The y coordinates
	 * @param width The width of the button
	 * @param height The height of the button
	 */
	public Button(double x, double y, double width, double height) {
		button = new Rectangle(x,y,width,height);
	}
	
	/**
	 * Creates an instance of the Button at coordinates (x, y) with width, height, Stroke Color (color of perimeter),
	 * Fill Color (color of area) and Stroke weight (how thick the perimeter is).
	 * @param x The x coordinates
	 * @param y The y coordinates
	 * @param width The width of the button
	 * @param height The height of the button
	 * @param strokeColor The color of the stroke (perimeter)
	 * @param fillColor The color of the area (the inside of the button)
	 * @param strokeWeight The thickness of the stroke (perimeter)
	 */
	public Button(double x, double y, double width, double height, Color strokeColor, Color fillColor, int strokeWeight) {
		button = new Rectangle(x, y, width, height, strokeColor, fillColor, strokeWeight);
	}
	
	/**
	 * Checks if a point is inside the button
	 * (mostly used for mouse presses)
	 * @param x The x coordinates of the point
	 * @param y The y coordinates of the point
	 * @return True if the point is inside, false if not
	 */
	public boolean checkPoint(double x, double y) {
		return button.isPointInside(x, y);
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
	 * Sets the text of the button. Will only appear if given coordinates, size and color
	 * @param text The new text of the button
	 */
	public void setText(String text) {
		this.text = text;
	}
	
	/**
	 * Sets the fill color of the button
	 * @param c The new color of the button
	 * @post PApplet attributes fill = c
	 */
	public void setFillColor(Color c) {
		button.setFillColor(c);
	}
	
	/**
	 * Sets the stroke color of the button
	 * @param c The new color of the stroke
	 * @post PApplet attributes stroke = c
	 */
	public void setStrokeColor(Color c) {
		button.setStrokeColor(c);
	}
	
	/**
	 * Move the button to coordinate (x,y). Does NOT move the text of the button
	 * @param x The x coordinates
	 * @param y The y coordinates
	 */
	public void move(double x, double y) {
		button.move(x, y);
	}
	
	/**
	 * Sets the size of the button
	 * @param width The new width of the button
	 * @param height The new height of the button
	 */
	public void setSize(double width, double height) {
		button.setSize((float)width, (float)height);
	}
	
	/**
	 * Draws the instance of the button at the coordinates specified and specified size and attributes
	 * @param drawer PApplet
	 * @pre Marker attributes set beforehand except stroke, fill, and strokeWeight
	 * @post PApplet attributes stroke = strokeColor, fill = text color specified (button fill if no text), 
	 * strokeWeight = strokeWeight, textSize = text size specified
	 */
	public void draw(PApplet drawer) {
		button.draw(drawer);
		if(text!=null && textPlace != null && textColor!=null) {
			drawer.fill(textColor.getRGB());
			drawer.textSize((float)textSize);
			drawer.text(text, (float)textPlace.getX(), (float)textPlace.getY());
		}
	}
}
