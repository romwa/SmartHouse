package romwa.GUI.buttons;

import java.awt.Color;

import processing.core.PApplet;

public class OnOffButton extends Button{

	private Color onColor, onDarkColor, offColor, offDarkColor;
	public static final int ON = 0, OFF = 1;
	private final int type;

	//false if pressed
	private boolean status;

	public OnOffButton(int type, double x, double y, double width, double height) {
		super(x, y, width, height);
		onColor = Color.GREEN;
		offColor = Color.RED;
		onDarkColor = new Color(0, 150, 0, 150);
		offDarkColor = new Color(150, 0, 0, 150);
		status = false;
		this.type = type;
		this.setStrokeColor(new Color(0,0,0,0));
	}

	public void setStatus(boolean status) {
		this.status = status;
		if(status) {
			if(type==0) {
				this.setFillColor(onColor);
//				this.setStrokeColor(onColor);
			} else {
				this.setFillColor(offColor);
//				this.setStrokeColor(offColor);
			}
		} else {
			if(type==0) {
				this.setFillColor(onDarkColor);
//				this.setStrokeColor(onDarkColor);
			} else {
				this.setFillColor(offDarkColor);
//				this.setStrokeColor(offDarkColor);
			}
		}
	}

	public void changeStatus() {
		setStatus(!status);
	}

	public boolean status() {
		return status;
	}

}
