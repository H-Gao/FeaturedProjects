package ItsMyNotes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Scanner;

import javax.swing.Timer;

import WindowDevelopmentClassesHenry.SimpleWindow;

public class ReminderChecker extends SimpleWindow implements ActionListener
{
	Date date = new Date();
	
	Timer timer = new Timer(500, this);
	
	int time = 0;
	
	String[] fileInfo;
	
	public ReminderChecker() 
	{
		super("Reminder", 100, 100, false);
		
		timer.start();
	}
	
	public void checkReminders()
	{
		File directory = new File("C:\\It'sMyNotes\\Reminders");
		fileInfo = new String[directory.listFiles().length];
		
		for (int n = 0;n != directory.listFiles().length;++n)
		{
			try
			{
				FileReader reader = new FileReader(directory.listFiles()[n]);
				Scanner scanner = new Scanner(reader);
				
				fileInfo[n] = "";
				
				fileInfo[n] += scanner.nextLine();
				
				while (scanner.hasNextLine())
					fileInfo[n] += scanner.nextLine();
			}
			catch (FileNotFoundException exception)
			{
				
			}
		}
	}
	
	public static void main(String[] args)
	{
		ReminderChecker reminderChecker = new ReminderChecker();
	}

	public void actionPerformed(ActionEvent e) 
	{
		++time;
		
		if (time%10==0)
		{
			date = new Date();
			
			if (time%60==0)
				System.out.println("Checking date: " + (date.getYear()+1900) + " " + date.getMonth() + " " + date.getDay() + " " + (date.getHours()%12) + ":" + date.getMinutes());
			
			checkReminders();
			
			for (int n = 0;n != fileInfo.length;++n)
			{
				if (!fileInfo[n].equalsIgnoreCase("Reminder Completed"))
				{
				int year = Integer.parseInt(fileInfo[n].split(";")[1]);
				int month = Integer.parseInt(fileInfo[n].split(";")[2]);
				int day = Integer.parseInt(fileInfo[n].split(";")[3]);
				int hour = Integer.parseInt(fileInfo[n].split(";")[4]);
				int minute = Integer.parseInt(fileInfo[n].split(";")[5]);
				
				if (year >= date.getYear() && month >= date.getMonth() && day >= date.getDay()
						&& hour >= date.getHours() && minute >= date.getMinutes()) {
					try
					{
						String file_Name = "C:\\It'sMyNotes\\Notes\\" + fileInfo[n].split(";")[0] + ".imn";
						String file_Info = "C:\\It'sMyNotes\\Notes\\" + fileInfo[n].split(";")[0] + ".imnd";
						
						FileReader reader = new FileReader(file_Info);
						Scanner scanner = new Scanner(reader);
					
						String file_data = scanner.nextLine();
					
						String name = file_data.split(";")[0];
						int x  = Integer.parseInt(file_data.split(";")[1]);
						int y  = Integer.parseInt(file_data.split(";")[2]);
						double versionID  = Double.parseDouble(file_data.split(";")[3]);
						
						int red_colour = Integer.parseInt(file_data.split(";")[4]);
						int green_colour = Integer.parseInt(file_data.split(";")[5]);
						int blue_colour = Integer.parseInt(file_data.split(";")[6]);
						
						reader = new FileReader(file_Name);
						scanner = new Scanner(reader);
					
						String[] file_text = new String[100];
								
						for (int i = 0;scanner.hasNextLine();++i)
						{
							String lineFromFile = scanner.nextLine();
							file_text[n] = lineFromFile;
						}
						
						ItsMyNotes note = new ItsMyNotes(versionID, name, file_Name.substring(file_Name.lastIndexOf("")), x, y, file_text, red_colour, green_colour, blue_colour);
						
						PrintWriter fileWiper = new PrintWriter("C:\\It'sMyNotes\\Reminders\\" + fileInfo[n].split(";")[0] + ".imr");
						fileWiper.print("Reminder Completed");
						fileWiper.flush();
					
						System.out.println("Opened: " + file_Name + ", " + file_Info);
					}
					catch (FileNotFoundException exception)
					{
						System.out.println("We could not open a file.");
						exception.printStackTrace();
					}
				}
			}
			}
		}
	}
}
