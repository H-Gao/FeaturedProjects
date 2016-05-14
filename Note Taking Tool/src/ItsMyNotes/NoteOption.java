package ItsMyNotes;

import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import WindowDevelopmentClassesHenry.SimpleWindow;

public class NoteOption extends SimpleWindow implements MouseListener
{
	Manager manager;
	
	String name;
	
	int width;
	int height;
	
	int mouseX;
	int mouseY;
	
	JButton action;
	
	public NoteOption(Manager manager, String name, int width, int height, boolean isVisible) 
	{
		super(name, width, height, isVisible);
		
		this.manager = manager;
		
		this.name = name;
		
		this.width = width;
		this.height = height;
		
		this.setUndecorated(true);
		
		//Calls onInit again, because width and height was not initalized.
		onInit();
		
		this.setVisible(true);
	}
	
	public void onInit()
	{
		String exactLocation = System.getProperty("java.class.path");
		String fileLocation = exactLocation.substring(0, exactLocation.indexOf("bin"));
		
		//Creates the button that will do the action.
		action = new JButton(name);
		action.setIcon(new ImageIcon(fileLocation + "\\Images\\NoteOptions\\" + name + ".png"));
		action.addMouseListener(this);
		action.setBounds(0, 0, width, height);
		add(action);
		
		repaint();
	}
	
	public void mousePressed(MouseEvent e) 
	{
		//The mouse as of the time it was pressed.
		mouseX = e.getXOnScreen() - this.getX();
		mouseY = e.getYOnScreen() - this.getY();
	}

	public void mouseReleased(MouseEvent e) 
	{
		//The current mouse location.
		int currentX = e.getXOnScreen() - this.getX();
		int currentY = e.getYOnScreen() - this.getY();
		
		if ((mouseX - currentX) != 0 || (mouseY - currentY) != 0)
		{
			this.setLocation(currentX - mouseX, currentY - mouseY);
		}
		else
		{
			if (name.equals("Create New Note"))
			{
				//Creates a new note.
				manager.notes.add(new ItsMyNotes());
			}
			if (name.equals("Hide Notes"))
			{
				//Either hides all the notes, or makes them all visible.
				for (ItsMyNotes note : manager.notes)
					note.requestHideFromScreen();
			}
			if (name.equals("Remove Notes"))
			{
				//Removes all the notes.
				for (ItsMyNotes note : manager.notes)
					note.requestExit();
			}
			if (name.equals("Open Notes"))
			{
				//Removes all the notes.
				for (ItsMyNotes note : manager.notes)
					note.requestExit();
				
				//Opens all notes.
					manager.openNotes();
			}
		}
	}
	
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
}
