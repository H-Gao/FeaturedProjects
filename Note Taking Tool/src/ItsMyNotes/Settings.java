package ItsMyNotes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

public class Settings 
{
	String font;
	int fontSize;
	boolean checkReminderOnStartUp;
	
	public Settings() throws Exception
	{
		updateSettings();
	}
	
	public static void main(String[] args) throws Exception
	{
		Settings settings = new Settings();
		System.out.println(settings.fontSize);
		System.out.println(settings.font);
	}
	
	public void updateSettings() throws Exception
	{
		File settings = new File("C:\\It'sMyNotes\\Settings\\Setting.ims");
		
		if (!settings.exists())
		{
			PrintWriter writer = new PrintWriter(settings);
			writer.println("OpensReminderOnStartup:true");
			writer.println("FontSize:12");
			writer.println("Font:TimesNewRoman");
			writer.println("BrewsTea:false");
			writer.flush();
		}
		
		BufferedReader reader = new BufferedReader(new FileReader(settings));
		
		checkReminderOnStartUp = toBoolean(reader.readLine().split(":")[1]);
		fontSize = toInt(reader.readLine().split(":")[1]);
		font = reader.readLine().split(":")[1];
	}
	
	public boolean getOpenReminderOnStartup()
	{	
		return checkReminderOnStartUp;
	}
	
	public int getFontSize()
	{	
		return fontSize;
	}
	
	public String getFont()
	{	
		return font;
	}
	
	public boolean toBoolean(String str)
	{
		return Boolean.parseBoolean(str);
	}
	
	public int toInt(String str)
	{
		return Integer.parseInt(str);
	}
}
