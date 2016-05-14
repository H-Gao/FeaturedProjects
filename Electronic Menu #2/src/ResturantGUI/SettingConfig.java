package ResturantGUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import WindowDevelopmentClassesHenry.Done;
import WindowDevelopmentClassesHenry.SimpleWindow;

public class SettingConfig extends SimpleWindow implements MouseWheelListener, ActionListener
{
	static ArrayList<String> description = new ArrayList<String>();
	
	static Done done;
	
	static JButton reset;
	
	static int BOOLEAN = 0;
	static int INT = 1;
	static int OTHER = 2;
	
	static int screenWidth = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	static int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	
	static ArrayList<JTextField> optionChooser = new ArrayList<JTextField>();
	static ArrayList<JTextArea> userNote = new ArrayList<JTextArea>();
	
	public SettingConfig() 
	{
		super("Settings", screenWidth/2, screenHeight*3/4, true);
	}
	
	public void onInit()
	{
		load();
		
		this.addMouseWheelListener(this);
	}
	
	public static void main(String[] args)
	{
		SettingConfig settingConfig = new SettingConfig();
	}
	
	public void load()
	{
		try
		{
			File directory = new File(fileLocation + "Settings.txt");
			Scanner in = new Scanner(new FileReader(directory));
			
			done = new Done(this, this.getWidth()/6, this.getHeight()-100, this.getWidth()-(this.getWidth()/6), 60);
			
			reset = new JButton("Reset");
			reset.addActionListener(this);
			reset.setBounds(0, this.getHeight()-100, this.getWidth()/6, 60);
			add(reset);
			
			if (in.hasNextLine())
			{		
				for (int i = 0;in.hasNextLine();++i)
				{
					String[] information = in.nextLine().split(": ");
					
					String title = information[0];
					
					String[] optionComments = information[1].split(" //");
					String options = optionComments[0];
					String comments = optionComments[1];
					 
					description.add(title + ":" + comments);
					
					int objectType = determineObject(options);
					
					System.out.println(title + ": | //" + comments + options + " [" + objectType + "]");
					
					userNote.add(new JTextArea());
					
					JTextArea note = userNote.get(i);
					
					note.setBorder(BorderFactory.createLineBorder(Color.decode("#B3B2B2"), 1));
					
					note.setText("<" + title +  ">\n" + comments + "\n");
					
					if (objectType == BOOLEAN)
					{
						note.setText(note.getText() + "true/false");
					}
					else if (objectType == INT)
					{
						note.setText(note.getText() + "A whole number from 1 - 100");
					}
					else if (objectType == OTHER)
					{
						note.setText(note.getText() + "A String of characters as specified.");
					}
					
					note.setBounds(this.getWidth()/2, i * 60, this.getWidth()/2, 60);
					add(note);
					
					optionChooser.add(new JTextField(options));
					
					optionChooser.get(i).setBounds(0, i*60, this.getWidth()/2, 60);
					add(optionChooser.get(i));
				}
			}
			
			in.close();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void onDone()
	{
		try 
		{	
			boolean hasNotBroke = true;
			
			ArrayList<String> outHolder = new ArrayList<String>();
			
			for (int i = 0;i < description.size();++i)
			{	
				if (userNote.get(i).getText().contains("A whole number from 1 - 100"))
				{
					try
					{
						Integer.parseInt(optionChooser.get(i).getText());
					}
					catch (NumberFormatException exception)
					{
						sendMessage(280, 80, "You must choose a whole number from 1 - 100.");
						hasNotBroke = false;
						
						break;
					}
				}
				
				if (userNote.get(i).getText().contains("true/false") && !optionChooser.get(i).getText().equals("true") && !optionChooser.get(i).getText().equals("false"))
				{
					sendMessage(280, 80, "You must choose true or false.");
					hasNotBroke = false;
					
					break;
				}
				else 
				{
					String[] titleComment = description.get(i).split(":");
				
					String title = titleComment[0];
					String comment = titleComment[1];
				
					outHolder.add(title + ": " + optionChooser.get(i).getText() + " //" + comment);
				}
			}
		
			
			if (hasNotBroke)
			{
				PrintWriter out = new PrintWriter(fileLocation + "Settings.txt");
				
				for (int i = 0;i < outHolder.size();++i)
				{
					out.println(outHolder.get(i));
					System.out.println("****" + outHolder.get(i));
				}
			
				out.close();
				this.dispose();
			}
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
	}
	
	public int determineObject(String in)
	{
		char[] inChar = in.toCharArray();
		Arrays.sort(inChar);
		
		if (in.equals("true") || in.equals("false"))
			return 0;
		else if (inChar[0] >= '0' && inChar[inChar.length-1] <= '9')
			return 1;
		else
			return 2;
	}

	//A listener which will listen to the movement of the mouse wheel.
	//This will allow the options to move up or down, so all can be visible.
	public void mouseWheelMoved(MouseWheelEvent e) 
	{
		//The amount that it will move.
		int moveAmount = 5;
		
		//If it is a the mouse is scrolled up, the options will move down.
		if (e.getWheelRotation() > 0)
			moveAmount *= -1;
		
		//Checks to see if it is empty and if the height is bigger than the total height of all the option boxes.
		//If the total height is smaller than the height of the config, then there is no point of scrolling, as all boxes can already be seen.
		//If there are no option, there is no point of wasting resources doing nothing.
		if (!optionChooser.isEmpty() && (this.getHeight() < (optionChooser.size()*60)))
		{
			//If it is moving down, then the first option box must be visible.
			//If it is moving up, then the last option box must be visible.
			if ((moveAmount > 0 && optionChooser.get(0).getY() < (this.getHeight()-160))
			|| (moveAmount < 0 && optionChooser.get(optionChooser.size()-1).getY() > 0))
			{
			//Searches through the option box's, optionChooser.size() == userNote.size().
			//Therefore I is the same in both cases.
			for (int i = 0;i != optionChooser.size();++i)
				{
					//Created a currentOption variable for a shorter name.
					JTextField currentOption = optionChooser.get(i);
					//Moves the option up or down, depending on whether the mouse was scrolled up or down.
					//If it was scrolled up, the boxes will go down, and vice versa.
					currentOption.setLocation(currentOption.getX(), currentOption.getY()+moveAmount);
			
					//Created a currentDescription varible for a shorter name.
					JTextArea currentDescription = userNote.get(i);
					//Moves the option up or down, depending on whether the mouse was scrolled up or down.
					//If it was scrolled up, the boxes will go down, and vice versa.
					currentDescription.setLocation(currentDescription.getX(), currentDescription.getY()+moveAmount);
				}
			}
		}
	}

	public void actionPerformed(ActionEvent e) 
	{
		try
		{
			Scanner in = new Scanner(new FileReader(fileLocation + "Default Settings.txt"));
			
			for (int i = 0;i < optionChooser.size();++i)
			{
				this.remove(optionChooser.get(i));
				this.remove(userNote.get(i));
			}
			
			description.clear();
			optionChooser.clear();
			userNote.clear();
			
			PrintWriter out = new PrintWriter(fileLocation + "Settings.txt");
			
			while (in.hasNextLine())
			{
				String nextLine = in.nextLine();
				out.println(nextLine);
			}
			
			in.close();
			out.close();
			
			load();
		}
		catch (FileNotFoundException exception)
		{
			exception.printStackTrace();
			System.out.println("The file was not found.");
		} 
		catch (IOException exception)
		{
			exception.printStackTrace();
			System.out.println("We could not remove the existing configuartion.");
		} 
	}
}
