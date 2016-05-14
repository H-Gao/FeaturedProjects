package ResturantGUI;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.Timer;

import WindowDevelopmentClassesHenry.SimpleWindow;

public class StartUp extends SimpleWindow implements ActionListener, MouseListener, MouseMotionListener
{
	static Settings settings = new Settings();
	
	boolean stopRotate = false;
	
	static String titleName = "Start Up Screen.";
	
	int standbyTimer = 0;
	int delayTimer = 0;
	
	static int numFrames = Integer.parseInt(settings.settingOptions.get(1));
	static int holdImageTime = Integer.parseInt(settings.settingOptions.get(2)) * 10;
	
	int UP = -Integer.parseInt(settings.settingOptions.get(3));
	int DOWN = Integer.parseInt(settings.settingOptions.get(3));
	
	int slideAnimationDirection = UP;
	
	List<List> images;
	List<JLabel> imageHolder;
	
	Timer slideUpdater = new Timer(1, this);
	
	Timer delay = new Timer(40, this);
	
	static int screenWidth = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	static int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	
	JTextField title;
	JTextField message;
	
	public StartUp() 
	{
		super("Start Up", screenWidth, screenHeight, "undecorated");
		
		delay.start();
	}
	
	public void onInit()
	{
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		
		settings.loadSettings();
		
		images = new ArrayList<List>();
		
		title = new JTextField(titleName);
		title.setFont(new Font("TimesNewRoman", Font.PLAIN, 20));
		title.setBounds((int)(screenWidth/2.28), screenHeight/200, screenWidth/2, screenHeight/11);
		title.setEditable(false);
		title.setBorder(null);
		title.setOpaque(false);
		add(title);
		
		message = new JTextField("Click or swipe the screen to view the Login screen.");
		message.setBounds((int)(screenWidth/2.5), (int)((double)screenHeight/1.1), screenWidth/2, screenHeight/20);
		message.setEditable(false);
		message.setBorder(null);
		message.setOpaque(false);
		add(message);
		
		System.out.println("Searching for files in " + fileLocation + " " + screenWidth + " " + screenHeight);
		File file = new File(fileLocation + "StartUp\\");
		File[] allFilesInFolder = file.listFiles();
		
		//If there are files in the folder, so if there isn't it can show the user
		//that there isn't any images in hopes that they will add some.
		if (allFilesInFolder != null)
		{
			for (int i = 0;i != allFilesInFolder.length;++i)
			{
				//Displays the image found, for easy debugging.
				System.out.println("The image " + allFilesInFolder[i] + " has been found.");
				
				//Creates a new ImageHolder, so the program will not put the old ImageHolder and all
				//of it's images into the a ArrayList object that holds ImageHolders.
				imageHolder = new ArrayList<JLabel>();
				
				//Adds forty objects of the ImageFromFolder into the ImageHolder.
				//They are transparent images, which means they will become more and more opaque.
				//When more images are displayed.
				for (int n = 0;n != numFrames;++n)
				{					
					//Adds the image to the ImageHolder.
					imageHolder.add(new JLabel(new ImageIcon(allFilesInFolder[i].toString())));
					imageHolder.get(n).setVisible(false);
					imageHolder.get(n).setBounds(0, 0, screenWidth, screenHeight);
					add(imageHolder.get(n));
				}
			
				//adds all forty objects of the image into the images list, which holds many set of forty images.
				images.add(imageHolder);
			}
		}
		else
		{
			//If there is no images inside the selected directory, then this StartUp object will display a
			//warning message telling the user that there are no images in the Startup Directory.
			sendMessage(400, 180, "<<CAUTION>>: NO IMAGES FOUND IN THE STARTUP DIRECTORY.");
		}
	}
	
	public void makeVisible(List<JLabel> l)
	{

		if (!l.get(numFrames-1).isVisible())
		{
			for (int i = 0;i < numFrames;++i)
			{
				if (!l.get(i).isVisible())
				{
					l.get(i).setVisible(true);
					break;
				}
			}
		}
	}
	
	public void makeInvisible(List<JLabel> l)
	{
		if (l.get(numFrames-1).isVisible())
		{
			for (int i = 0;i < numFrames;++i)
			{
				if (l.get(i).isVisible())
				{
					l.get(i).setVisible(false);
					break;
				}
			}
		}
	}
	
	//Starts the timer, which creates the slide animation.
	public void commenceSlide(int direction)
	{
		slideAnimationDirection = direction;
		slideUpdater.start();
		
		stopRotate = true;
	}
	
	//Creates the animation of sliding the screen up or down, revealing the LoginScreen.
	//If direction is equal to -1, it will slide up, it if it equal to +1, it will move down.
	public void slide(int direction)
	{
		if (direction < 0 && this.getY() <= -screenWidth)
		{
			slideUpdater.stop();
		}
		else if (direction > 0 && this.getY() >= screenWidth)
		{
			slideUpdater.stop();
		}
		
		this.setLocation(this.getX(), this.getY()+direction);
	}
	
	//Listener for the timer object, it slides the screen up.
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == slideUpdater)
		{
			slide(slideAnimationDirection);
		}
		else if (e.getSource() == delay && !stopRotate)
		{
			if (delayTimer%(2*numFrames) == numFrames-1 && standbyTimer < holdImageTime)
			{
				++standbyTimer;
			}
			else
			{
				++delayTimer;
				
				standbyTimer = 0;
			}
			
			if (delayTimer%(2*numFrames) <= numFrames)
			{
				makeVisible(images.get(delayTimer/(2*numFrames)));
			}
			else
			{
				makeInvisible(images.get(delayTimer/(2*numFrames)));
			}
			
			if (delayTimer > (numFrames * images.size()))
			{
				delayTimer = 0;
			}
		}
	}

	//MouseClicked, MouseDragged, MouseReleased and ActionListener are all part of a system
	//to make the StartUp, slide up.
	public void mouseClicked(MouseEvent e) 
	{
		//If the mouse is clicked, it will slide up.
		commenceSlide(UP);
	}
	
	public void mouseDragged(MouseEvent e) 
	{
		//If the mouse is dragged, the screen will move to the mouse's location.
		//This is for astethics and so the MouseReleased object can work.
		this.setLocation(this.getX(), e.getYOnScreen()-screenHeight);
	}
	
	public void mouseReleased(MouseEvent e) 
	{
		//If the Y axis of the StartUp screen, is higher than a quarter of the screen, it
		//will start sliding up.
		if (this.getY() < -(screenHeight/4))
		{
			commenceSlide(UP);
		}
	}

	public void mousePressed(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mouseMoved(MouseEvent e) {}
}
