package ItsMyNotes;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;

import WindowDevelopmentClassesHenry.SimpleWindow;

public class ItsMyNotes extends SimpleWindow implements ActionListener, MouseListener
{
	//The exact location of the file.
	String exactLocation;
	//The main location of the It'sMyNotes folder.
	String fileLocation;
	
	//The name of the file.
	//If it is a newly made note, it will be saved as "Not Saved."
	//If it is saved note, it will be saved as file_n. Where n is the same n in the name of the file, of the note.
	String file_Name;
	
	//RGB values that chooses the colours of the "Top Bar".
	int red_colour;
	int green_colour;
	int blue_colour;
	
	//The version of the software.
	double versionID;
	
	//The transparent layers of colour that intensifies with more layers.
	List<JLabel> red;
	List<JLabel> blue;
	List<JLabel> green;
	
	//The encryption key which can be changed at any time in order to change the way
	//that the text is encrypted.
	private int EncryptKey = 1;
	private int EncryptKey2 = 3;
	
	//The X and Y locations of the location of where a mouse is clicked.
	int pressedX;
	int pressedY;
	
	//The main panel, it only displays the icons.
	JLabel mainPanel;
	
	//A red button with a X, click to close the note and save it.
	JButton exit;
	
	//The hitboxes for the Top Bars, this is only used to move the note.
	JLabel topBar_hitbox1;
	JLabel topBar_hitbox2;
	JLabel topBar_hitbox3;
	
	//The area that holds the title of the note.
	JTextField title;
	
	//The area that holds the text of the note.
	JTextArea text;
	
	
	//Constructor that runs if it is a newly made note.
	public ItsMyNotes() 
	{
		//Creates a SimpleWindow with the name "It's My Notes, with the wength of 350 pixels and a height of 450 pixels.
		super("It's My Notes", 350, 450, false);
		
		//Indicates that the file is Not Saved.
		file_Name = "Not Saved";
		
		this.setDefaultColour();
		
		//Removes the default TopBar, exit button and minimize button.
		this.setUndecorated(true);
		//Allows the note to be seen.
		this.setVisible(true);
		
		System.out.println("A new note was successfully created, it is " + file_Name);
	}
	
	
	//Constructor that runs if it a previously saved file, it is opened by the Manager class.
	public ItsMyNotes(double versionID, String name, String fileName, int x, int y, String[] file_text, int red, int green, int blue) 
	{
		//Creates a SimpleWindow with the name "It's My Notes, with the wength of 350 pixels and a height of 450 pixels.
		super("It's My Notes", 350, 450, false);
		
		this.versionID = versionID;
		this.file_Name = fileName;
		this.setLocation(x, y);
		
		this.red_colour = red;
		this.green_colour = green;
		this.blue_colour = blue;
		
		this.setDefaultColour(red, green, blue);
		
		title.setText(name);
		
		for (int n = 0;file_text[n] != null;++n)
		{
			if (n == 0)
				text.setText(decrypt(file_text[n], file_text[n].length()-1));
			else
				text.setText(text.getText() + "\n" + decrypt(file_text[n], file_text[n].length()-1));
		}
		
		this.setUndecorated(true);
		this.setVisible(true);
		
		System.out.println("The file " + file_Name + "was opened.");
	}
	
	public void createComponents()
	{
		//Inits both the exact and general location of the file.
		exactLocation = System.getProperty("java.class.path");
		fileLocation = exactLocation.substring(0, exactLocation.indexOf("bin"));
		
		System.out.println("The main It'sMyNotes folder is " + fileLocation);
		
		mainPanel = new JLabel(new ImageIcon(fileLocation + "Images\\Colours\\MainPanel.png"));
		mainPanel.setBounds(0, 0, 350, 450);
		add(mainPanel);
				
		topBar_hitbox1 = new JLabel();
	    topBar_hitbox1.addMouseListener(this);
		topBar_hitbox1.setBounds(0, 0, 100, 80);
		add(topBar_hitbox1);
		
		topBar_hitbox2 = new JLabel();
		topBar_hitbox2.addMouseListener(this);
		topBar_hitbox2.setBounds(200, 0, 100, 80);
		add(topBar_hitbox2);
		
		topBar_hitbox3 = new JLabel();
		topBar_hitbox3.addMouseListener(this);
		topBar_hitbox3.setBounds(250, 25, 100, 40);
		add(topBar_hitbox3);
		
		title = new JTextField("My Note.");
		title.setOpaque(false);
		title.setBorder(null);
		title.setFont(new Font("TimeNewRoman", Font.PLAIN, 18));
		title.setHorizontalAlignment(JTextField.CENTER);
		title.setBounds(0, 0, 280, 30);
		add(title);
		
		exit = new JButton();
		exit.addActionListener(this);
		exit.addMouseListener(this);
		exit.setIcon(new ImageIcon(fileLocation + "Images\\TopBar\\Exit.png"));
		exit.setBorder(null);
		exit.setBounds(310, 0, 42, 25);
		add(exit);
		
		text = new JTextArea();
		text.setLineWrap(true);
		text.setOpaque(false);
		text.setBorder(null);
		text.setFont(new Font("TimeNewRoman", Font.PLAIN, 12));
		text.setBounds(0, 50, 350, 400);
		add(text);
		
		red = new ArrayList<JLabel>();
		green = new ArrayList<JLabel>();
		blue = new ArrayList<JLabel>();
		
		createColour(red, "Red");
		createColour(green, "Green");
		createColour(blue, "Blue");
		
		setDefaultColour();
		
		this.setBackground(fileLocation + "Images\\Background\\Background.png");
	}
	
	public static void main(String[] args)
	{
		ItsMyNotes itsMyNotes = new ItsMyNotes();
	}
	
	public void setDefaultColour()
	{
		red_colour = 8;
		green_colour = 3;
		blue_colour = 0;
		
		changeColour(red, red_colour);
		changeColour(green, green_colour);
		changeColour(blue, blue_colour);
	}
	
	public void setDefaultColour(int redValue, int greenValue, int blueValue)
	{
		red_colour = 8;
		green_colour = 3;
		blue_colour = 0;
		
		changeColour(red, redValue);
		changeColour(green, greenValue);
		changeColour(blue, blueValue);
	}
	
	public void createColour(List<JLabel> colour, String colourName)
	{
		for (int n = 0;n != 20;++n)
			colour.add(new JLabel(new ImageIcon(fileLocation + "Images\\Colours\\" + colourName + ".png")));
			
		for (JLabel c : colour)
		{
			c.setVisible(false);
			c.setBounds(0, 0, 350, 450);
			add(c);
		}
	}
	
	public void changeColour(List<JLabel> colour, int num)
	{
		for (int n = 0;n != colour.size();++n)
		{
			//Sets numbers from 0 to n as visible, creating the illusion of colour change.
			if (n < num)
				colour.get(n).setVisible(true);
			else
				colour.get(n).setVisible(false);
		}
	}
	
	public void requestHideFromScreen()
	{
		if (!this.isVisible())
			this.setVisible(true);
		else
			this.setVisible(false);
	}
	
	public void requestExit()
	{
		this.save();
		this.dispose();
	}

	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == exit)
		{
			this.requestExit();
		}
		else
		{
			String[] lines = text.getText().split("\\n");
		}
	}
	
	public void save()
	{
		try
		{
			if (file_Name.equals("Not Saved"))
			{
				file_Name = fileLocation + "Notes\\Note_1";
		
				for (int i = 1;new File(file_Name + ".imn").exists();++i)
					file_Name = fileLocation + "Notes\\Note_"+i;
				
				//Sets the version into the current version.
				versionID = 0;
			}
				
			PrintWriter fileSaver = new PrintWriter(new File(file_Name + ".imn"));
			PrintWriter dataSaver = new PrintWriter(new File(file_Name + ".imnd"));
			
			String[] lines = text.getText().split("\\n");
		
			for (int i = 0;i != lines.length;++i)
			{
				if (!text.getText().equals(""))
					fileSaver.println(encrypt(lines[i], lines[i].length()-1));
			}
			
			fileSaver.flush();
			
			dataSaver.print(title.getText() + ";" + this.getX() + ";" + this.getY() + ";" + this.versionID + ";" + this.red_colour + ";" + this.green_colour + ";" + this.blue_colour + ";" + this.getHeight());
			dataSaver.flush();
		} 
		
		catch (FileNotFoundException exception)
		{
			System.out.println("Saving aborted. An error occured.");
		}
	}
	
	public void createReminder()
	{
		if (!file_Name.equals("Not Saved"))
		{
			ReminderSelectTime reminderSelectTime = new ReminderSelectTime(file_Name.substring(file_Name.lastIndexOf("\\"), file_Name.length()));
		}
		else
		{
			System.out.println("This file is not saved.");
		}
	}
	
	
	public void openColourGUI()
	{
		ColourChooser colourChooser = new ColourChooser(this);
	}
	
	public void openSizeAdjuster()
	{
		SizeAdjuster sizeAdjuster = new SizeAdjuster(this);
	}
	
	public String encrypt(String str, int n)
	{
		int EncryptAmount = ((EncryptKey2-EncryptKey)-(EncryptKey2*EncryptKey2));
		
		if (n == 0)
		{
			return String.valueOf((char)(str.charAt(0)+EncryptAmount));
		}
		else
		{
			String lastStr = encrypt(str, n-1);
			return lastStr + String.valueOf((char)(str.charAt(n)+EncryptAmount));
		}
	}
	
	public String decrypt(String str, int n)
	{
		int DecryptAmount = ((EncryptKey2-EncryptKey)-(EncryptKey2*EncryptKey2));
		
		if (n == 0)
		{
			return String.valueOf((char)(str.charAt(0)-DecryptAmount));
		}
		else
		{
			String lastStr = decrypt(str, n-1);
			return lastStr + String.valueOf((char)(str.charAt(n)-DecryptAmount));
		}
	}
	
	public void shiftLines(String[] lines, int n)
	{
	}

	public void mouseEntered(MouseEvent e) 
	{
		if (e.getSource() == exit)
		{
			exit.setIcon(new ImageIcon(fileLocation + "Images\\TopBar\\Exit_Touched.png"));
		}
	}

	public void mouseExited(MouseEvent e) 
	{
		if (e.getSource() == exit)
		{
			exit.setIcon(new ImageIcon(fileLocation + "Images\\TopBar\\Exit.png"));
		}
	}

	public void mouseClicked(MouseEvent e) {}

	public void mousePressed(MouseEvent e) 
	{
		//Once pressed, the X and Y locations of the mouse is saved, to be used when the mouse is released.
		pressedX = e.getXOnScreen();
		pressedY = e.getYOnScreen();
	
		//Gets the x on the screen, relative to the note.
		int mouseX = e.getXOnScreen() - this.getX();
		int mouseY = e.getYOnScreen() - this.getY();
		
		//If the main panel is visible.
		if (mainPanel.isVisible())
		{		
			System.out.println(mouseX + " " + mouseY);
		
			if (mouseX < 28 && mouseX >= 0 && mouseY < 24 && mouseY >= 0)
			{
				if (title.getText().equals("Henry: Random Animal Image"))
				{
					Random random = new Random();
				
					Image image = new Image(fileLocation + "Images\\Commands\\Animal_" + random.nextInt(3) + ".png");
					title.setText("ANIMALS! Welcome back Henry.");
				}
				else
				{
					createReminder();
				}
			}
		
			if (mouseX < 56 && mouseX >= 28 && mouseY < 48 && mouseY >= 0)
			{
				openColourGUI();
			}
			
			if (mouseX < 84 && mouseX >= 56 && mouseY < 62 && mouseY >= 0)
			{
				openSizeAdjuster();
			}
			
			if (mouseX < 112 && mouseX >= 84 && mouseY < 86 && mouseY >= 0)
			{
				mainPanel.setVisible(false);
			}
		}
		//If the main panel is not visible.
		else
		{
			if (mouseX < 28 && mouseX >= 0 && mouseY < 24 && mouseY >= 0)
			{
				mainPanel.setVisible(true);
			}
		}
	}

	public void mouseReleased(MouseEvent e) 
	{
		this.setLocation(this.getX() + (e.getXOnScreen() - pressedX), this.getY() + (e.getYOnScreen() - pressedY)); 
	}
}

/* User Guide: March 27th, 2014.
 * 
 * Last Code Cleanup Date: April 18th, 2014.
 * 
 * Note: Remember to finish your geography presentation.
 * 
 * It's My Notes.
 * ===================
 * Made by Henry Gao.
 * Started at: March 26th, 2014.
 * Finished at: ------.
 * ===================
 * Purpose to provide the user Sticky Notes.
 * The sticky notes will save the notes in
 * .imn and .imnd files.
 * They will be encrypted with a special changeable key.
 * The notes will also save line by line.
 * ===================
 * .imn files stand for .It's My Notes files.
 * They will store the text, but will not store
 * extra data such as the name and version.
 * They will be encrypted.
 * ===================
 * .imnd files stand for .It's My Notes Data files.
 * They store the extra data, but not the text.
 * They will be encrypted, as well.
 */
