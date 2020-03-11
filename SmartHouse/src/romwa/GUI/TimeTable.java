package romwa.GUI;

import java.awt.Color;
import java.util.ArrayList;

import processing.core.PApplet;
import romwa.FileIO.FileIO;
import romwa.GUI.buttons.Button;
import romwa.shapes.Rectangle;

public class TimeTable {
	
	private String filename;
	
	private boolean timeTable[][];
	private Button table[][];
	private Button done;
	
	private double x, y, width, height;
	
	public static final int DONE = 0;
	
	private String title;
	
	public TimeTable(String filename, double x, double y, double width, double height, String title) {
		this.filename = filename;
		timeTable = new boolean[7][24];
		table = new Button[7][24];
		this.title = title;
		load();
		setStats(x, y, width, height);
	}
	
	public TimeTable(String filename, String title) {
		this(filename, 0, 0, 100, 100, title);
	}
	
	private void load() {
		ArrayList<String> temp = FileIO.readFile(filename);
		String[] day = new String[24];
		for(int i = 0; i < temp.size(); i++) {
			day = temp.get(i).split(",");
			for(int j = 0; j < 24; j++) {
				timeTable[i][j] = iTb(Integer.parseInt(day[j]));
			}
		}
	}
	
	public void burn() {
		ArrayList<String> temp = new ArrayList<String>();
		String line = "";
		for(int i = 0; i < 7; i++) {
			for (int j = 0; j < 23; j++) {
//				line += timeTable[i][j] + ",";
				line += bTi(timeTable[i][j]) + ",";
			}
			line += bTi(timeTable[i][23]);
			temp.add(line);
			line = "";
		}
		FileIO.writeFile(filename, temp);
	}
	
	private int bTi(boolean b) {
		return (b) ? 1 : 0;
	}
	
	private boolean iTb(int i) {
		return (i==1) ? true : false;
	}
	
	private void setRects() {
//		double width = (this.width)/24;
//		double height = (this.height-100)/7;
//		for(int i = 0; i < 7; i++) {
//			for(int j = 0; j < 24; j++) {
////				drawRect(drawer, timeTable[i][j], x+j*width, y+i*height, width, height);
//				table[i][j] = setRect(timeTable[i][j], x+j*width, y+50+i*height, width, height);
//			}
//		}
		
		done = new Button(x+width/2-50, height-60, 100, 50, Color.BLACK, new Color(180, 50, 150), 1);
		done.setText("Done", x+width/2-25, height-27, 20, Color.BLACK);
		
		double width = (this.width-200)/12;
		double height = (this.height-200)/14;
		double seperator = 10;
		for(int i = 0; i < 7; i++) {
			for(int j = 0; j < 12; j++) {
//				drawRect(drawer, timeTable[i][j], x+j*width, y+i*height, width, height);
				table[i][j] = setRect(timeTable[i][j], x+200+j*width, y+50+seperator*i+2*i*height, width, height, j);
				
			}
			for(int j = 0; j < 12; j++) {
//				drawRect(drawer, timeTable[i][j], x+j*width, y+i*height, width, height);
				table[i][j+12] = setRect(timeTable[i][j+12], x+200+j*width, y+50+seperator*i+height+2*i*height, width, height, j+12);
			}
		}
	}
	
//	private Rectangle setRect(boolean state, double x, double y, double width, double height) {
//		Rectangle rect = new Rectangle(x, y, width, height, Color.BLACK, new Color(0,0,0,0), 1);
//		rect.setFillColor((state) ? Color.GREEN : Color.RED);
//		return rect;
//	}

	private Button setRect(boolean state, double x, double y, double width, double height, int hour) {
		Button b = new Button(x, y, width, height, Color.BLACK, new Color(0,0,0,0), 1);
		b.setFillColor((state) ? Color.GREEN : Color.RED);
		b.setText((hour<10) ? "0"+hour : hour+"", x+10, y+height-4, 20, Color.BLACK);
		return b;
	}
	
	public void setStats(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		setRects();
	}
	
	public void draw(PApplet drawer) {
		double height = (this.height-200)/14;
		double seperator = 10;
		drawer.fill(0, 0, 0);
		drawer.textSize(30);
		drawer.text(title, (float)(x+(width/2)-170), (float)y+30);
		drawer.textSize(30);
		drawer.text("Sunday:", (float)x, (float)(y+50+seperator + .8*height));
		drawer.text("Monday:", (float)x, (float)(y+50+seperator*2 + 2.8*height));
		drawer.text("Tuesday:", (float)x, (float)(y+50+seperator*3 + 4.8*height));
		drawer.text("Wednesday:", (float)x, (float)(y+50+seperator*4 + 6.8*height));
		drawer.text("Thursday:", (float)x, (float)(y+50+seperator*5 + 8.8*height));
		drawer.text("Friday:", (float)x, (float)(y+50+seperator*6 + 10.8*height));
		drawer.text("Saturday:", (float)x, (float)(y+50+seperator*7 + 12.9*height));
		for(int i = 0; i < 7; i++) {
			for(int j = 0; j < 24; j++) {
				table[i][j].draw(drawer);
			}
		}
		
		done.draw(drawer);
	}
	
	public int mousePress(double x, double y) {
		if(done.checkPoint(x, y)) {
			burn();
			return DONE;
		} else 
		for(int i = 0; i < 7; i++) {
			for(int j = 0; j < 24; j++) {
				if(table[i][j].checkPoint(x, y)) {
					toggleHour(i,j);
				}
			}
		}
		return -1;
	}
	
	public void toggleHour(int i, int j) {
		timeTable[i][j] = !timeTable[i][j];
		setRects();
	}
	
	/**
	 * 
	 * @param day
	 * @param hour
	 * @return 
	 */
	public boolean timeTableHour(int day, int hour) {
		return timeTable[day][hour];
	}
	
}
