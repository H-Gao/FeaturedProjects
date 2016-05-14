package Classes;

import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.Queue;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;

public class TimerList extends JFrame implements MouseListener, MouseMotionListener
{
	//Links to the control panel, so it can access it's features. Ex. The currentDirectory.
	ControlPanel cp;
	
	//A component which is used to display the background IMAGE to make this application look better.
	JLabel background;
		
	//Used to select tools, such as home, edit and settings.
	JLabel toolbar;
	
	//Used to open the file menu, to select an application to block.
	JLabel addBlock;
	
	//Stores the information of the blocked apps.
	LinkedList<BlockedInfo> info = new LinkedList<BlockedInfo>();
		
	//Initalizes the JFrame.
	public TimerList(ControlPanel c)
	{
		//Stores the location of the control panel for further use.
		cp = c;
		
		//Creates an empty JFrame
		this.setTitle("Lockr");
		this.setSize(500, 650);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(null);
		this.setUndecorated(true);
		this.setVisible(true);
		
		//Initalizes the components, so they can be used.
		init();
		
		//Loads the applications that will be blocked.
		loadBlockedApplications();
	}
	
	public void init()
	{	
		//Initalizes the toolbar, which can select different options.
		addBlock = new JLabel();
		addBlock.setIcon(new ImageIcon(cp.currentDirectory + "AddNew.png"));
		addBlock.addMouseListener(this);
				
		addBlock.setSize(addBlock.getIcon().getIconWidth(), //Sets the size to be the same as the icon.
				addBlock.getIcon().getIconHeight());
		
		addBlock.setLocation(0, 650 - addBlock.getHeight());
		this.add(addBlock);
		
		
		//Initalizes the toolbar, which can select different options.
		toolbar = new JLabel();
		toolbar.setIcon(new ImageIcon(cp.currentDirectory + "BlockingToolbar_0.png"));
		toolbar.addMouseListener(this);
		toolbar.addMouseMotionListener(this);
				
		toolbar.setSize(toolbar.getIcon().getIconWidth(), //Sets the size to be the same as the icon.
				toolbar.getIcon().getIconHeight());
				
		this.add(toolbar);
				
				
		//Initalizes the background, purely for asthetics (serves no purpose).
		background = new JLabel();
		background.setIcon(new ImageIcon(cp.currentDirectory + "LockrBackground_2.png"));
				
		background.setSize(background.getIcon().getIconWidth(), //Sets the size to be the same as the icon.
			background.getIcon().getIconHeight());
				
		this.add(background);
		
		this.repaint();
	}
	
	public void createBlockedApplication(String path)
	{
		BlockedInfo bl = new BlockedInfo(this, path);
		info.add(bl);
	}
	
	//Creates the images for the blocked applications, so the user can interact with them.
	public void loadBlockedApplications()
	{
		//Removes the applications that are already existing.
		for (int i = 0;i < info.size();++i)
		{
			//The blocked app that is being checked.
			BlockedInfo target = info.get(i);
			
			this.remove(target.back);
			this.remove(target.icon);
			this.remove(target.name);
		}
		
		info = new LinkedList<BlockedInfo>();
		
		//Gets an array of blocked apps.
		Object[] blockedApps = cp.t.time.toArray();
		
		//Returns the path of the blocked application and creates the images.
		for (int i = 0;i < blockedApps.length;++i)
		{
			System.out.println("****" + blockedApps[i]);
			createBlockedApplication(""+blockedApps[i]);
		}
		
		this.repaint();
	}
	
	/*Checks to see which section the mouse hits on the toolbar.
	The corresponding section will light up.*/
	public void checkToolbarSection(MouseEvent e)
	{
		if (e.getSource() == toolbar)
		{
			if (e.getX() > 370)
				toolbar.setIcon(new ImageIcon(cp.currentDirectory + "BlockingToolbar_1.png"));
			else if (e.getX() > 288)
				toolbar.setIcon(new ImageIcon(cp.currentDirectory + "BlockingToolbar_2.png"));
			else if (e.getX() > 197)
				toolbar.setIcon(new ImageIcon(cp.currentDirectory + "BlockingToolbar_3.png"));
			else
				toolbar.setIcon(new ImageIcon(cp.currentDirectory + "BlockingToolbar_4.png"));
			
			if (e.getX() > 485 && e.getY() < 15)
				toolbar.setIcon(new ImageIcon(cp.currentDirectory + "BlockingToolbar_0.png"));
		}
	}
	
	public class BlockedInfo
	{
		//The layer that decorates the icon and name.
		public JLabel back;
		public JLabel icon;
		public JTextArea name;
		
		public BlockedInfo(TimerList t, String path)
		{
			icon = new JLabel();
			icon.setIcon(new ImageIcon(cp.currentDirectory + "Timer.png"));
			icon.setSize(icon.getIcon().getIconWidth(), icon.getIcon().getIconHeight());
			icon.setLocation(5, info.size()*64+5+64);
			
			t.add(icon);
			
			
			name = new JTextArea();
			name.setFont(new Font("Corsiva Hebrew", Font.BOLD + Font.ITALIC, 14));
			name.setText(extractName(path));
			name.setEditable(false);
			name.setOpaque(false);
			name.setBorder(null);
			name.setSize(340, icon.getHeight());
			name.setLocation(icon.getWidth()+28, icon.getY());
			t.add(name);
			
			
			back = new JLabel();
			
			back.setIcon(new ImageIcon(cp.currentDirectory + "BlockedBack.png"));
			back.setName("BlockedApplication_" + path.substring(path.lastIndexOf("\\")+1, path.length()));
			back.addMouseListener(t);
			back.setSize(back.getIcon().getIconWidth(), back.getIcon().getIconHeight());
			back.setLocation(0, icon.getY()-5);
			
			t.add(back);
			t.add(background);
			
			t.repaint();
		}
	}
	
	//Extracts the data from the string (which contains raw data in the form [timeInfo][ID].
	public String extractName(String path)
	{
		String dateRaw = path.substring(0, 14); //Gets the RAW date information, to be parse to a readable date.
		String actionRaw = cp.t.connector.get(Integer.parseInt(path.substring(14, 18))); //Gets the ID, and reads the action.
		
		//This formats the raw action into a refined string.
		String actionRefined = "";
		
		//Gets the seperate arguments, from the raw data.
		String[] command = actionRaw.split(""+cp.t.seperator);
		
		//Determines the command, to figure out how to format the data.
		switch (command[0])
		{
			case "Notice": //Formating, if it is a notice.
			{
				String type = ""; //What kind of notice is it (Ex. Notice, or a warning).
				
				//Turns the integer, into the type which is a string.
				if (command[2].equals("0")) type = "NOTICE";
				else if (command[2].equals("1")) type = "WARNING";
					
				//Formats the data into a recognizable form.
				actionRefined = "A " + type + " will display " + '"' + command[3] + '"';
				break; //Exits, so it doesn't move onto the next command.
			}
			
			case "Block": //Formating, if it is a blocking command.
			{
				//Formats the data.
				actionRefined = "The following application will be blocked " + command[1];
				break; //Exits, so it doesn't move onto the next command.
			}
		}
		
		int year = Integer.parseInt(dateRaw.substring(0, 4))+1900; //Gets the year.
		
		int monthInt = Integer.parseInt(dateRaw.substring(4, 6)); //Gets the month in numbers (with 0 being january and 11 being december).
		String monthStr = null; //The month in a String form (Ex. 0 = January).
		
		//Gets the month as a String, from the given integer.
		switch (monthInt)
		{
			case 0: { monthStr = "January"; break; }
			case 1: { monthStr = "Febuary"; break; }
			case 2: { monthStr = "March"; break; }
			case 3: { monthStr = "April"; break; }
			case 4: { monthStr = "May" ;break; }
			case 5: { monthStr = "June"; break; }
			case 6: { monthStr = "July"; break; }
			case 7: { monthStr = "August"; break; }
			case 8: { monthStr = "September"; break; }
			case 9: { monthStr = "October"; break; }
			case 10: { monthStr = "November"; break; }
			case 11: { monthStr = "December"; break; }
		}
		
		int day = Integer.parseInt(dateRaw.substring(6, 8))+1; //Gets the current day.
		
		
		int hour = Integer.parseInt(dateRaw.substring(8, 10))+1; //Gets the hour in the form of a 24 hour clock.
		String timeOfDay = null; //The time of the day; whether it is am or pm.
		
		//Converts the 24 hour time to 12 hour time.
		if (hour == 12) { hour = 12; timeOfDay = "pm";} //12 and 24 are exceptions, as they don't follow the algorithmn below.
		else if (hour == 24){ hour = 12; timeOfDay = "am"; }
		else if (hour > 12){ hour = hour%12; timeOfDay = "pm"; }
		else if (hour < 12){ timeOfDay = "am"; }
		
		
		int minute = Integer.parseInt(dateRaw.substring(10, 12))+1; //Gets the minutes.
		int second = Integer.parseInt(dateRaw.substring(12, 14))+1; //Gets the seconds.
		
		//Assembles the dates, into a refined format.
		String dateRefined = day + " " + monthStr + " " + year + " at " + hour + ":" + minute + " " + timeOfDay + " and " + second + " seconds.";
		
		//Puts the action and the date together, each on their own line.
		return actionRefined + "\n" + dateRefined;
	}
	
	public void mouseEntered(MouseEvent e) 
	{
		Component source = (Component)e.getSource();
		
		checkToolbarSection(e);
		
		if (source.getName() != null && source.getName().startsWith("BlockedApplication_") && e.getX() > 420)
		{
			String name = source.getName();
			
			if (e.getY() < 31)
			{
				for (int i = 0;i < info.size();++i)
				{
					//The blocked app that is being checked.
					BlockedInfo target = info.get(i);
					
					System.out.println();
					
					//If the name matches with the components, it get deleted.
					if (target.back.getName().equals("BlockedApplication_" + name))
					{
						target.back.setIcon(new ImageIcon(cp.currentDirectory + "BlockedBack_0.png"));
						
						this.repaint();
						break;
					}
				}
			}
			else
				for (int i = 0;i < info.size();++i)
				{
					//The blocked app that is being checked.
					BlockedInfo target = info.get(i);
					
					//If the name matches with the components, it get deleted.
					if (target.back.getName().equals("BlockedApplication_" + name))
					{
						target.back.setIcon(new ImageIcon(cp.currentDirectory + "BlockedBack_1.png"));
						
						this.repaint();
						break;
					}
				}
		}
	}
	
	public void mouseExited(MouseEvent e) 
	{
		Component source = (Component)e.getSource();
		
		if (e.getSource() == toolbar)
		{
			toolbar.setIcon(new ImageIcon(cp.currentDirectory + "BlockingToolbar_0.png"));
		}
		
		if (source.getName() != null && source.getName().startsWith("BlockedApplication_") && e.getX() > 420)
		{
			String name = source.getName();
			
			
			for (int i = 0;i < info.size();++i)
			{
				//The blocked app that is being checked.
				BlockedInfo target = info.get(i);
					
				//If the name matches with the components, it get deleted.
				if (target.back.getName().equals("BlockedApplication_" + name))
				{
					target.back.setIcon(new ImageIcon(cp.currentDirectory + "BlockedBack.png"));
						
					this.repaint();
					break;
				}
			}
		}
	}
	
	//Unblocks an application, given it's name.
	public void removeBlocked(String name)
	{
		for (int i = 0;i < info.size();++i)
		{
			//The blocked app that is being checked.
			BlockedInfo target = info.get(i);
			
			//If the name matches with the components, it get deleted.
			if (target.back.getName().equals("BlockedApplication_" + name))
			{
				this.remove(target.back);
				this.remove(target.icon);
				this.remove(target.name);
				info.remove(i);
				
				this.repaint();
				break;
			}
		}
		
		cp.b.blockedApps.remove(name); //Notifies the system that the app is blocked.
		cp.b.path.remove(name); //Stores the location of the app.
		
		//Reloads the blocked applications, the space that the deleted application left behind, is used up.
		loadBlockedApplications();
	}

	public void mouseClicked(MouseEvent e) 
	{
		Component source = (Component)e.getSource();
		
		//If the mouse clicks on the close button, the application closes.
		if (e.getSource() == toolbar && e.getX() > 485 && e.getY() < 15) this.dispose();
		
		if (source.getName() != null && source.getName().startsWith("BlockedApplication_") && e.getX() > 420)
		{
			String name = source.getName();
			
			if (e.getY() < 31)
				this.removeBlocked(name.substring(name.indexOf('_')+1, name.length()));
			else
				System.out.println("remove.");
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
