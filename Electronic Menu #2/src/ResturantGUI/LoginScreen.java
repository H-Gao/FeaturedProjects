package ResturantGUI;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.Timer;

import WindowDevelopmentClassesHenry.SimpleWindow;

public class LoginScreen extends SimpleWindow implements ActionListener
{
	Timer decayTimer = new Timer(1000, this);
	
	static StartUp startScreen;
	
	int decayTime = 0;
	
	static Settings settings = new Settings();
	
	String mainDirectory = System.getProperty("java.class.path").substring(0, System.getProperty("java.class.path").indexOf("bin"));
	
	Encryption encryption = new Encryption();
	
	JButton select;
	
	JTextField userName;
	JPasswordField passWord;
	
	int width = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	int height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	
	public LoginScreen(StartUp startScreen) 
	{
		super("Login Screen", (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight(), false);
		
		this.startScreen =  startScreen;
		
		this.setUndecorated(true);
		
		this.setVisible(true);
		
		onInit(true);
	}
	
	public void onInit(Object afterStartup)
	{
		System.out.println("Setting the main directory... " + mainDirectory);
		
		StartUp startUp = new StartUp();
		
		select = new JButton("Enter");
		select.setBounds(0, (int)((double)height/1.2), (int)((double)width/2.6), height/10);
		select.addActionListener(this);
		add(select);
		
		userName = new JTextField();
		userName.setBounds(0, (int)((double)height/1.8), (int)((double)width/2.6), height/10);
		userName.addActionListener(this);
		add(userName);
		
		passWord = new JPasswordField();
		passWord.setBounds(0, (int)((double)height/1.48), (int)((double)width/2.6), height/10);
		passWord.addActionListener(this);
		add(passWord);
		
		repaint();
	}
	
	public static void main(String[] args)
	{
		if (settings.checkSetting(0).equals("true"))
		{
			LoginScreen loginScreen = new LoginScreen(startScreen);
		}
		else
		{
			UserInterface ui = new UserInterface("Guest");
		}
	}

	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == select)
		{
			try
			{
				boolean allowAccess = false;
			
				String username = userName.getText();
				String password = passWord.getText();
				
				if (username.equals("Exit"))
					this.dispose();
			
				File file = new File(mainDirectory + "Accounts\\" + username + ".acf");
			
				if (file.exists())
				{
					Scanner in = new Scanner(new FileReader(file));
					
					String realPassword = encryption.decrypt(in.nextLine());
					
					in.close();
					
				
					if (realPassword.equals(password))
					{
						allowAccess = true;
					}
					else
					{
						sendMessage(280, 110, "The passward is not correct, please try again.");
					}
				}
				else
				{
					sendMessage(280, 110, "The user does not exist, please try again.");
				}
			
				if (allowAccess)
				{
					System.out.println("User identified: " + username);
					System.out.println("Loading the User Interface...");
				
					UserInterface ui = new UserInterface(username);
				
					System.out.println("User interface successfully loaded.");
				}
			}
			catch (Exception exception)
			{
				System.out.println("A error occured.");
			}
		}
		else
		{
			++decayTime;
			
			if (decayTime == 40)
			{
				startScreen.commenceSlide(startScreen.DOWN);
			}
		}
	}
}
