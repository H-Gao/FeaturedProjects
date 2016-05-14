package Classes;

import java.awt.Component;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ControlPanel extends JFrame implements MouseListener, MouseMotionListener
{
	//The directory that this .class file is located in.
	String currentDirectory;
	
	//The thread that keeps the blocked applications from running.
	Blocker b;
	
	//The thread that checks for scheduled timers, and executes then, when it's ready.
	Timer t;
	
	//The class that edits the hosts file, in order to block certain websites.
	BlockIP bIP;
	
	static Credits credits;
	
	//If it has been pressed, the sound should not play.
	boolean hasChanged = false;
	boolean isLocked = true;
	boolean hasEntered = false;
	
	//There can only be one blocklist, and it must be stored for later usage.
	BlockList blockList;
	
	//There can only be one blocked ip list, and it must be stored for later usage.
	BlockIPList blockIPList;
	
	//There can only be one timer list, and it must be stored for later usage.
	TimerList timerList;
	
	//A component which is used to display the background IMAGE to make this application look better.
	JLabel background;
	
	//Used to select tools, such as home, edit and settings.
	JLabel toolbar;
	
	//Informs the user of what icon they are selecting.
	JLabel infoBar;
	
	//The logo which starts or stops the blocking services.
	JLabel logo;
	
	//The options which can be used to access the blocking list, timing list, parental control or settings.
	JLabel[] options;
	
	public ControlPanel()
	{
		//Creates an empty JFrame
		this.setTitle("Lockr");
		this.setSize(500, 650);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(null);
		this.setUndecorated(true);
		this.setVisible(true);
		
		//Gets the current directory, so it can be used in the init() process by some components.
		//Ex. the background needs it to find the directory of the image.
		this.getDirectory();
		
		currentDirectory = "C:\\Judo Workspace\\AlgorithmIdeas\\LockrSoftware\\";
		
		credits.init(this);
		
		/*Initalizes the variables, in particular, the components.
		Anything that has to be done, before it starts running.*/
		this.init();
		
		/*Initalizes the blocker, which is a thread, which 
		stores which processes to block and actually block them.*/
		b = new Blocker();
		
		/*Initalizes the IP blocker, which is a class that edits the hosts file to
		  redirect certain websites, effectively blocking them.*/
		bIP = new BlockIP(this);
		
		/*Initalizes the blocker, which is a thread, which 
		stores which processes to block and actually block them.*/
		t = new Timer(this);
		t.addTimer("Notice" + t.seperator + "1" + t.seperator + "0" + t.seperator + "Hello!", 115, 8, 15, 14, 48, 48);
	}
	
	//Creates the options, which can be used to access the rest of the software (block list, settings, etc).
	public void createOption(int i, int width, int height)
	{
		//Initalizes the option.
		JLabel temp = new JLabel();
		temp.setName("Option_" + i);
		
		if (width == 0 && height == 0)
			//Creates a smaller version of itself, because the original is too big.
			temp.setIcon(new ImageIcon(currentDirectory + "Option_" + i + ".png"));
		else
			temp.setIcon(new ImageIcon(new ImageIcon(currentDirectory + "Option_" + i + ".png").getImage()
					.getScaledInstance(width, height, Image.SCALE_SMOOTH)));
			
		temp.addMouseListener(this);
		
		temp.setSize(temp.getIcon().getIconWidth(), //Sets the size to be the same as the icon.
				temp.getIcon().getIconHeight());
		
		//Used to keep the options from appearing in a circle if pressed, by using an equation, rather
		//than manually setting each position.
		int a = 1; if (i == 3) a = -1;
		int b = 1; if (i == 0) b = -1;
		
		//Lines up at the four sides of the circle.
		temp.setLocation(250 + 185*((i%2)*a) - temp.getWidth()/2, 320 + 195*(((i+1)%2)*b) - temp.getHeight()/2);
		
		this.add(temp);
		
		if (options[i] != null) this.remove(options[i]);
		options[i] = temp;
	}
	
	/*Initalizes the variables, in particular, the components.
	Anything that has to be done, before it starts running.*/
	public void init()
	{
		options = new JLabel[4];
		
		//Initalizes each individual option.
		for (int i = 0;i < 4;++i) createOption(i, 0, 0);
		
		
		//Initalizes the Lockr logo, which can be used to start or stop the blocking services.
		logo = new JLabel();
		
		//Creates a smaller version of itself, because the original is too big.
		logo.setIcon(new ImageIcon(new ImageIcon(currentDirectory + "LockrLogo.png").getImage()
				.getScaledInstance(275, 275, Image.SCALE_SMOOTH)));
		
		logo.addMouseListener(this);
		
		logo.setSize(logo.getIcon().getIconWidth(), //Sets the size to be the same as the icon.
				logo.getIcon().getIconHeight());
		
		/*Half of the JFrame - half of the Lockr Logo, so the middle of the Jframe
		lines up with the middle of the logo.*/
		logo.setLocation(250 - logo.getWidth()/2, 325 -  logo.getHeight()/2);
		
		this.add(logo);
		
		//Initalizes the Infobar, which informs the user of what they are selecting.
		infoBar = new JLabel();
		infoBar.setIcon(new ImageIcon(currentDirectory + "infoBar.png"));
		infoBar.setLocation(0, 579);
		
		infoBar.setSize(infoBar.getIcon().getIconWidth(), //Sets the size to be the same as the icon.
				infoBar.getIcon().getIconHeight());
		
		this.add(infoBar);
		
		
		//Initalizes the toolbar, which can select different options.
		toolbar = new JLabel();
		toolbar.setIcon(new ImageIcon(currentDirectory + "LockrToolbar_0.png"));
		toolbar.addMouseListener(this);
		toolbar.addMouseMotionListener(this);
		
		toolbar.setSize(toolbar.getIcon().getIconWidth(), //Sets the size to be the same as the icon.
				toolbar.getIcon().getIconHeight());
		
		this.add(toolbar);
		
		
		//Initalizes the background, purely for asthetics (serves no purpose).
		background = new JLabel();
		background.setIcon(new ImageIcon(currentDirectory + "LockrBackground.png"));
		
		background.setSize(background.getIcon().getIconWidth(), //Sets the size to be the same as the icon.
			background.getIcon().getIconHeight());
		
		this.add(background);
		
		this.repaint();
	}
	
	//Gets the directory that this file is located in, and stores it in the memory.
	public void getDirectory()
	{
		//Finds the path by finding a file with "." and taking it's path.
		String path = new File(".").getAbsolutePath();
		
		//Removes the dot at the end, and stores it for further use.
		currentDirectory = path.substring(0, path.length()-1);
	}
	
	public static void main(String args[])
	{
		credits = new Credits();
		ControlPanel cp = new ControlPanel();
	}
	
	//Plays a certain sound.
	public void playSound(String path)
	{
		//Required by Java, due to the possibility of an exception from the Clip.
		try
		{
			//Creates the clip object.
			Clip a = AudioSystem.getClip();
			a.open(AudioSystem.getAudioInputStream(new File(path).toURI().toURL())); //Loads the sound.
			a.start(); //Plays the sound.
			a.close(); //Closes the sound, as it is now useless.
		}
		//Outputs the exception.
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/*Checks to see which section the mouse hits on the toolbar.
	The corresponding section will light up.*/
	public void checkToolbarSection(MouseEvent e)
	{
		//If it is actually on ANY part of the toolbar.
		if (e.getSource() == toolbar)
		{
			//These determines where the mouse is, and if it is directly on a button, by changing the image
			//to one, where the cooresponding button that has been litten up.
			
			if (e.getX() > 370) toolbar.setIcon(new ImageIcon(currentDirectory + "LockrToolbar_1.png"));
			else if (e.getX() > 288) toolbar.setIcon(new ImageIcon(currentDirectory + "LockrToolbar_2.png"));
			else if (e.getX() > 197) toolbar.setIcon(new ImageIcon(currentDirectory + "LockrToolbar_3.png"));
			else toolbar.setIcon(new ImageIcon(currentDirectory + "LockrToolbar_4.png"));
			
			if (e.getX() > 485 && e.getY() < 15)
				toolbar.setIcon(new ImageIcon(currentDirectory + "LockrToolbar_0.png"));
		}
	}
	
	public void mouseEntered(MouseEvent e) 
	{
		//Checks to see if the mouse is on an option on the toolbar.
		checkToolbarSection(e);
		
		//If the lock logo has been locked, it will APPEAR unlocked when the mouse has entered it.
		//However, it will unlock IF the mouse didn't click on it and vice versa.
		if (e.getSource() == logo)
		{
			//If it is locked, it will appear to be unlocked.
			if (isLocked)
			{
				//Plays a sound of the logo unlocking.
				playSound("C:\\Judo Workspace\\AlgorithmIdeas\\LockrSoftware\\Unlock.wav");
				
				logo.setIcon(new ImageIcon(new ImageIcon(currentDirectory + "LockrLogo_Unlocked.png").getImage()
						.getScaledInstance(275, 275, Image.SCALE_SMOOTH)));
			}
			//If it is unlocked, it will appear to be locked.
			else
			{
				//Plays a sound of the logo locking.
				playSound("C:\\Judo Workspace\\AlgorithmIdeas\\LockrSoftware\\Lock.wav");
				
				logo.setIcon(new ImageIcon(new ImageIcon(currentDirectory + "LockrLogo.png").getImage()
						.getScaledInstance(275, 275, Image.SCALE_SMOOTH)));
			}
			
			//Updates the infoBar to inform the user that the logo enables/disables blocking.
			infoBar.setIcon(new ImageIcon(currentDirectory + "infoBar_4.png"));
			
			repaint();
		}
		
		//If the mouse has entered an option, it will enlarge and play a sound.
		if (!hasEntered && ((Component)e.getSource()).getName() != null && ((Component)e.getSource()).getName().startsWith("Option"))
		{
			String name = ((JLabel)e.getSource()).getName();
			int id = Integer.parseInt(name.substring(name.length()-1, name.length()));
			
			//Plays a sound.
			playSound("C:\\Judo Workspace\\AlgorithmIdeas\\LockrSoftware\\Notification_" + id + ".wav");
			
			//Updates the infoBar to display what the icon of the option means.
			infoBar.setIcon(new ImageIcon(currentDirectory + "infoBar_" + id + ".png"));
			
			//Remembers that it has entered the option, as it tries to enlarge it, multiple times.
			hasEntered = true;
			
			createOption(id, 90, 90); //Enlarges the option.
			
			this.add(background); //Adds the background again, because the option tends to go behind it.
		}
		
		this.repaint();
	}
	
	public void mouseExited(MouseEvent e) 
	{
		//If the mouse exits the toolbar, the picture resets so NONE of the options light up.
		if (e.getSource() == toolbar)
		{
			toolbar.setIcon(new ImageIcon(currentDirectory + "LockrToolbar_0.png"));
		}
		
		//If the mouse did not click the lock logo, it's state (locked or unlocked) will reset.
		//Otherwise, the state will stay as it appears.
		if (e.getSource() == logo)
		{
			if (isLocked)
			{
				//If the state of the lock has changed, it should not play the sound, since it did not change.
				if (!hasChanged)
				//Plays a sound of the logo locking.
				playSound("C:\\Judo Workspace\\AlgorithmIdeas\\LockrSoftware\\Lock.wav");
				
				logo.setIcon(new ImageIcon(new ImageIcon(currentDirectory + "LockrLogo.png").getImage()
						.getScaledInstance(275, 275, Image.SCALE_SMOOTH)));
			}
			else
			{
				if (!hasChanged)
				//Plays a sound of the logo unlocking.
				playSound("C:\\Judo Workspace\\AlgorithmIdeas\\LockrSoftware\\Unlock.wav");
				
				logo.setIcon(new ImageIcon(new ImageIcon(currentDirectory + "LockrLogo_Unlocked.png").getImage()
						.getScaledInstance(275, 275, Image.SCALE_SMOOTH)));
			}
			
			//Resets the infobar.
			infoBar.setIcon(new ImageIcon(currentDirectory + "infoBar.png"));
			
			//Stops the change, so the sound can play again.
			hasChanged = false;
		}
		
		//If it exits, the enlarged icon will return to it's normal size.
		if (((Component)e.getSource()).getName() != null && ((Component)e.getSource()).getName().startsWith("Option"))
		{
			String name = ((JLabel)e.getSource()).getName(); //Gets the name of the option.
			int id = Integer.parseInt(name.substring(name.length()-1, name.length())); //Extracts the ID of the option.
			
			//Resets the infoBar.
			infoBar.setIcon(new ImageIcon(currentDirectory + "infoBar.png"));
			
			hasEntered = false; //Makes sure it can enlarge later, if it enters again.
			createOption(id, 0, 0); //Returns it to it's normal size.
			
			this.add(background); //Resets the background, so the background stays at the back.
		}
		
		this.repaint();
	}

	public void mouseClicked(MouseEvent e) 
	{
		//If the mouse clicks on the close button, the application closes.
		if (e.getSource() == toolbar && e.getX() > 485 && e.getY() < 15) System.exit(0);
				
		if (e.getSource() == logo)
		{
			//This allows the lock to STAY locked up or unlocked.
			if (isLocked) isLocked = false;
			else isLocked = true;
			
			//The state of the lock has changed.
			hasChanged = true;
		}
		
		//If the mouse clicks on the option, it will react accordingly.
		if (((Component)e.getSource()).getName() != null && ((Component)e.getSource()).getName().startsWith("Option"))
		{
			//The name of the option.
			String name = ((Component)e.getSource()).getName();
			
			System.out.println(name);
			
			//Extracts the id from the name, and reacts accordingly.
			switch (name.substring(name.length()-1, name.length()))
			{
				//Opens up the block list, and closes any open block lists.
				case "0":
				{
					if (blockList != null) blockList.dispose(); //Closes any remaining block lists, if any.
					blockList = new BlockList(this); //Opens up a new block list.
					System.out.println("Block List Opened");
					break;
				}
				case "1":
				{
					if (timerList != null) timerList.dispose(); //Closes any remaining block lists, if any.
					timerList = new TimerList(this); //Opens up a new block list.
					System.out.println("Timer List Opened");
					break;
				}
				case "2":
				{
					if (blockIPList != null) blockIPList.dispose(); //Closes any remaining block lists, if any.
					blockIPList = new BlockIPList(this); //Opens up a new block list.
					System.out.println("Block IP List Opened");
					break;
				}
				case "3":
				{
					Spy s = new Spy(this);
				}
			}
		}
	}
	
	public void mousePressed(MouseEvent e) {}
	
	public void mouseReleased(MouseEvent e) 
	{
		checkToolbarSection(e);
	}

	public void mouseDragged(MouseEvent e) 
	{
		checkToolbarSection(e);
	}

	public void mouseMoved(MouseEvent e) 
	{
		checkToolbarSection(e);
	}
}
