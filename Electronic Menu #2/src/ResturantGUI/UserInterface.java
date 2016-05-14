package ResturantGUI;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JTextField;

import WindowDevelopmentClassesHenry.SimpleWindow;

public class UserInterface extends SimpleWindow implements ActionListener
{
	String mainDirectory = System.getProperty("java.class.path").substring(0, System.getProperty("java.class.path").indexOf("bin"));
	
	private double funds;
	
	String username;
	String password;
	
	int width = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	int height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	
	JTextField cost;
	JTextField fund;
	
	JButton exit;
	
	JButton placeOrder;
	JButton checkOut;
	
	public UserInterface(String n) 
	{
		super("User Interface",(int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight(), false);
		this.setUndecorated(true);
		this.setVisible(true);
		
		try
		{
			username = n;
			
			Scanner in = new Scanner(new FileReader(mainDirectory + "Accounts\\" + username + ".acf"));
			password = in.nextLine();
			funds = Double.parseDouble(in.nextLine());
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
			System.out.println("We could not read the file.");
			System.exit(0);
		}
		
		doInit(width, height);
	}
	
	public void doInit(int width, int height)
	{
		System.out.println(width + " " + height);
		
		exit = new JButton("Exit");
		exit.setBounds(width-(width/14), 0, width/14, height/15);
		exit.addActionListener(this);
		add(exit);
		
		cost = new JTextField("$0");
		cost.setBounds(width/200, (int)((double)height/200), width/2, height/15);
		add(cost);
		
		fund = new JTextField("$"+ funds);
		fund.setBounds(width/200, (int)((double)height/11.8), width/2, height/15);
		add(fund);
		
		placeOrder = new JButton("Place Order");
		placeOrder.addActionListener(this);
		placeOrder.setBounds(width/200, (int)((double)height/1.38), width-(2*(width/200)), height/15);
		add(placeOrder);
		
		checkOut = new JButton("Check Out");
		checkOut.addActionListener(this);
		checkOut.setBounds(width/200, (int)((double)height/1.248), width-(2*(width/200)), height/15);
		add(checkOut);
		
		repaint();
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == exit)
		{
			if (cost.getText().equals("$0"))
				this.dispose();
			else
				sendMessage(280, 110, "Click check out, before closing.");
		}
		else if (e.getSource() == placeOrder)
		{
			Menu menu = new Menu(this);
		}
		else if (e.getSource() == checkOut)
		{
			CheckOut checkOut = new CheckOut(this);
		}
	}
}
