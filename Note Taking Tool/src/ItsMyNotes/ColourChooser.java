package ItsMyNotes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;

import WindowDevelopmentClassesHenry.SimpleWindow;

public class ColourChooser extends SimpleWindow implements MouseMotionListener, ActionListener
{
	//Constants for Red, Green and Blue.
	int RED = 0;
	int GREEN = 1;
	int BLUE = 2;
	
	ItsMyNotes currentNote;
	
	int red_Value;
	int blue_Value;
	int green_Value;
	
	JButton red;
	JButton blue;
	JButton green;
	
	JButton done;
	
	public ColourChooser(ItsMyNotes note) 
	{
		super("Colour Chooser", 400, 200, false);
		
		this.setLocation(note.getX() + 350, note.getY());
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		currentNote = note;
		
		red_Value = note.red_colour;
		blue_Value = note.blue_colour;
		green_Value = note.green_colour;
		
		red.setBounds(36, (20-red_Value)*10, 60, 10);
		add(red);
		
		green.setBounds(169, (20-green_Value)*10, 60, 10);
		add(green);
		
		blue.setBounds(301, (18-blue_Value)*10, 60, 10);
		add(blue);
		
		this.setVisible(true);
		
		this.setBackground("C:\\It'sMyNotes\\Images\\Background\\RGB_Background.png");
	}
	
	public void onInit()
	{
		this.setUndecorated(true);
		
		red = new JButton("Red");
		red.addMouseMotionListener(this);
		
		green = new JButton("Green");
		green.addMouseMotionListener(this);
		
		blue = new JButton("Blue");
		blue.addMouseMotionListener(this);
		
		done = new JButton("Done");
		done.addActionListener(this);
		done.setBounds(320, 180, 80, 20);
		add(done);
		
		repaint();
	}

	public void mouseDragged(MouseEvent e) 
	{
		//The value that the buttons must be shifted down in order for the mouse to be precise.
		int mouseY = (e.getYOnScreen()-this.getY()) - 5;
		
		System.out.println(mouseY);
		
		//If it is the red button.
		if (e.getSource() == red)
		{
			//Sets the location of the button to the mouse location.
			if (red.getY() > 0 && red.getY() < 195)
				red.setLocation(36, mouseY);
			
			//If it is at 0, prevents it from getting stuck.
			if (red.getY() <= 0)
				red.setLocation(36, 1);
			
			//If it is at 195, prevents it from getting stuck.
			if (red.getY() >= 195)
				red.setLocation(36, 194);
			
			System.out.println(red.getY());
			
			setColourToNote(RED);
		}
		
		//If it is the green button.
		else if (e.getSource() == green)
		{
			//Sets the location of the button to the mouse location.
			if (green.getY() > 0 && green.getY() < 195)
				green.setLocation(169, mouseY);
			
			//If it is at 0, prevents it from getting stuck.
			if (green.getY() <= 0)
				green.setLocation(169, 1);
			
			//If it is at 195, prevents it from getting stuck.
			if (green.getY() >= 195)
				green.setLocation(169, 194);
			
			setColourToNote(GREEN);
		}
		
		//If it is the blue button.
		else if (e.getSource() == blue)
		{
			//Sets the location of the button to the mouse location.
			if (blue.getY() > 0 && blue.getY() < 195)
				blue.setLocation(301, mouseY);
			
			//If it is at 0, prevents it from getting stuck.
			if (blue.getY() <= 0)
				blue.setLocation(301, 1);
			
			//If it is at 195, prevents it from getting stuck.
			if (blue.getY() >= 195)
				blue.setLocation(301, 194);
			
			setColourToNote(BLUE);
		}
	}
	
	//Sets the colour of the note to the current RGB colours.
	public void setColourToNote(int colour)
	{
		if (colour == RED)
		{
			red_Value = 20 - (red.getY()/10);
		
			currentNote.red_colour = red_Value;
			currentNote.changeColour(currentNote.red, currentNote.red_colour);
		}
		
		else if (colour == GREEN)
		{
			green_Value = 20 - (green.getY()/10);
		
			currentNote.green_colour = green_Value;
			currentNote.changeColour(currentNote.green, currentNote.green_colour);
		}
		
		else if (colour == BLUE)
		{
			blue_Value = 20 - (blue.getY()/10);
		
			currentNote.blue_colour = blue_Value;
			currentNote.changeColour(currentNote.blue, currentNote.blue_colour);
		}
	}

	public void mouseMoved(MouseEvent e) {}

	public void actionPerformed(ActionEvent e) 
	{
		this.dispose();
	}
}
