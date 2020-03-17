
package romwa.runners;
import java.awt.Dimension;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import processing.awt.PSurfaceAWT;
import processing.core.PApplet;
import romwa.GUI.HouseControl;


public class Main {
	
	public static void main(String args[]) {
		HouseControl drawing = new HouseControl();
		PApplet.runSketch(new String[]{""}, drawing);
		PSurfaceAWT surf = (PSurfaceAWT) drawing.getSurface();
		PSurfaceAWT.SmoothCanvas canvas = (PSurfaceAWT.SmoothCanvas) surf.getNative();
		JFrame window = (JFrame)canvas.getFrame();

		window.move(50, 50);
		window.setSize(1000, 900);
		window.setMinimumSize(new Dimension(100,100));
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(true);
		window.setVisible(true);
	}
	
}

