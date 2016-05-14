package ResturantGUI;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Settings
{
	String mainDirectory = System.getProperty("java.class.path").substring(0, System.getProperty("java.class.path").indexOf("bin"));
	
	List<String> settingOptions = new ArrayList<String>();
	
	public Settings()
	{
		loadSettings();
	}
	
	public static void main(String[] args)
	{
		Settings settings = new Settings();
		settings.showSettings();
	}
	
	public void loadSettings()
	{
		try
		{
			File directory = new File(mainDirectory + "Settings.txt");
			
			Scanner in = new Scanner(new FileReader(directory));
			
			if (in.hasNextLine())
			{
				settingOptions.add(in.nextLine().split(": ")[1].split(" //")[0]);
				
				while (in.hasNextLine())
				{
					settingOptions.add(in.nextLine().split(": ")[1].split(" //")[0]);
				}
			}
		}
		catch (Exception exception)
		{
			System.out.println("A error occured.");
		}
	}
	
	public void showSettings()
	{
		for (String i : settingOptions)
		{
			System.out.println(i);
		}
	}
	
	public String checkSetting(int index)
	{
		return settingOptions.get(index);
	}
}
