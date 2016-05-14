package ResturantGUI;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;

import StaffOrder.OrderingScreen;
import WindowDevelopmentClassesHenry.SimpleWindow;

public class Menu extends SimpleWindow implements ActionListener, MouseListener
{
	boolean isMainMenu = true;
	
	//OrderingScreen orderingScreen;
	
	int price = 0;
	
	JTextField output;
	JButton accept;
	JButton decline;
	
	String mainDirectory = System.getProperty("java.class.path").substring(0, System.getProperty("java.class.path").indexOf("bin"));
	
	JTextArea information;
	
	List<JButton> menuItemName = new ArrayList<JButton>();
	List<String> menuItemData = new ArrayList<String>();
	
	UserInterface ui;
	
	JButton back;
	JButton main;
	JButton side;
	JButton drink;
	
	int width = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	int height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	
	public Menu(UserInterface ui) 
	{
		super("Menu", (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight(), false);
		
		this.setUndecorated(true);
		this.setVisible(true);
		
		System.out.println("Linking with the User...");
		this.ui = ui;
		System.out.println("A Link has been successfully established.");
		
		System.out.println("Loading menu items...");
		readMenuItems("Main");
		System.out.println("Menu items loaded.");
		
		onInit(true);
		
		beginTransition(width);
	}
	
	public void onInit(Object afterStart)
	{
		back = new JButton("Exit");
		back.addActionListener(this);
		back.setBounds(width-(width/10), 0, width/10, height/24);
		add(back);
		
		main = new JButton("Main");
		main.addActionListener(this);
		main.setBounds(0, (int)((double)height/10), width/10, height/24);
		add(main);
		
		side = new JButton("Side");
		side.addActionListener(this);
		side.setBounds(0, (int)((double)height/7), width/10, height/24);
		add(side);
		
		drink = new JButton("Drink");
		drink.addActionListener(this);
		drink.setBounds(0, (int)((double)height/4), width/10, height/24);
		add(drink);
		
		output = new JTextField();
		output.setBounds(width-(width/4 + 5), (int)((double)height/1.28), width/4, (int)((double)height/16));
		add(output);
		
		information = new JTextArea();
		information.setBounds(width-(width/4 + 5), height/16, width/4, (int)((double)height/1.4));
		add(information);
		
		accept = new JButton("Yes");
		accept.setVisible(false);
		accept.addActionListener(this);
		accept.setBounds(width-(width/4 + 5), (int)((double)height/1.18), width/8, (int)((double)height/16));
		add(accept);
		
		decline = new JButton("No");
		decline.setVisible(false);
		decline.addActionListener(this);
		decline.setBounds(width-(width/4 + 5)/2, (int)((double)height/1.18), width/8, (int)((double)height/16));
		add(decline);
	}
	
	public void readMenuItems(String menuType)
	{
		try
		{
			File mainMenuDirectory = new File(mainDirectory + "Menu\\" + menuType + "\\");
			
			for (int i = 0;i != menuItemName.size();++i)
			{
				menuItemName.get(i).setVisible(false);
			}
			
			menuItemName.clear();
			menuItemData.clear();
			
			for (int i = 0;i != mainMenuDirectory.listFiles().length;++i)
			{
				String fileName = mainMenuDirectory.listFiles()[i].getName();
				
				String name = fileName.substring(0, fileName.lastIndexOf("."));
				Scanner in = new Scanner(new FileReader(mainMenuDirectory.listFiles()[i]));
				
				menuItemName.add(new JButton(name));
				menuItemName.get(i).addMouseListener(this);
				menuItemName.get(i).setBounds(width/6, height/10 + ((height/24)*i), (name.length()+1)*18, height/24);
				add(menuItemName.get(i));
				
				menuItemData.add(name);
				
				System.out.println();
				
				int numberOfLines = Integer.parseInt(in.nextLine());
				menuItemData.add(""+(numberOfLines+1));
				
				for (int n = 0;n != numberOfLines;++n)
				{
					menuItemData.add(in.nextLine());
				}
				
				menuItemData.add("Price: $" + in.nextLine());
				
				repaint();
				in.close();
			}
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
			System.out.println("A error occured.");
		}
	}

	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == back)
		{
			if (!isMainMenu)
			{
				onInit(true);
			}
			else
			{
				this.dispose();
			}
		}
		else if (e.getSource() == accept || e.getSource() == decline)
		{
			output.setText("");
			
			accept.setVisible(false);
			decline.setVisible(false);
			
			if (e.getSource() == accept)
			{
				//The text of the cost field, including the $.
				String costStatement = ui.cost.getText();
				//The text of the cost field without the $.
				int previousCost = Integer.parseInt(costStatement.substring(1, costStatement.length()));
				
				ui.cost.setText("$"+(price+previousCost));
			}
			
			price = 0;
		}
		else if (e.getSource() == main || e.getSource() == drink || e.getSource() == side)
		{
			if (e.getSource() == main)
				readMenuItems("Main");
			
			if (e.getSource() == drink)
				readMenuItems("Drinks");
			
			if (e.getSource() == side)
				readMenuItems("Sides");
		}
	}

	public void mouseEntered(MouseEvent e) 
	{
		for (JButton i : menuItemName)
		{
			if (e.getSource() == i)
			{
				String name = i.getText();
				
				for (int index = 0;index != menuItemData.size();++index)
				{
					String data = menuItemData.get(index);
					
					if (name.equals(data))
					{
						int numberOfLines = Integer.parseInt(menuItemData.get(index + 1)) + (index + 2);
						
						for (int n = index;n != numberOfLines;++n)
						{
							if (n != index+1)
							{
								information.setText(information.getText() + menuItemData.get(n) + "\n");
							
								if (menuItemData.get(n).contains("Price: $"))
								{
									//orderingScreen.addToList(menuItemData.get(index), orderingScreen.orders);
									price = Integer.parseInt(menuItemData.get(n).substring(menuItemData.get(n).lastIndexOf("Price: $")+8, menuItemData.get(n).length()));
								}
							}
						}
					}
				}
			}
		}
	}

	public void mouseExited(MouseEvent e) 
	{
		information.setText("");
	}
	
	public void mouseClicked(MouseEvent e) 
	{
		for (JButton i : menuItemName)
		{
			if (e.getXOnScreen() >= i.getX() && e.getXOnScreen() <= (i.getX()+i.getWidth()) && e.getYOnScreen() >= i.getY() && e.getYOnScreen() <= (i.getY()+i.getHeight()))
			{
				output.setText("Are you sure, you want to order " + (i.getText() + "?"));
				accept.setVisible(true);
				decline.setVisible(true);
			}
		}
	}
	
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
}
