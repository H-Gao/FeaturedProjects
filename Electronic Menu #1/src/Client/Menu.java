package Client;

import java.awt.Dimension;
import java.awt.Toolkit;

import WindowDevelopmentClassesHenry.SimpleWindow;

public class Menu extends SimpleWindow
{
	public static Dimension toolkit = Toolkit.getDefaultToolkit().getScreenSize();
	
	public static int width = (int)toolkit.width;
	public static int height = (int)toolkit.height;
	
	public Menu() 
	{
		super("E-Menu", width, height, false);
		
		this.setUndecorated(true);
		this.setVisible(true);
	}
}
