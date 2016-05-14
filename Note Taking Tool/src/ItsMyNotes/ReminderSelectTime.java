package ItsMyNotes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JTextField;

import WindowDevelopmentClassesHenry.SimpleWindow;

public class ReminderSelectTime extends SimpleWindow implements ActionListener
{
	Date currentDate;
	
	String file_Name;
	
	JTextField timeInformation;
	JTextField time[];
	
	JButton submit;
	JButton cancel;
	
	public ReminderSelectTime(String fileName) 
	{
		super("Select Time", 450, 200, true);
		
		file_Name = fileName;
	}
	
	public void createComponents()
	{
		currentDate = new Date();
		
		time = new JTextField[5];
				
		for (int n = 0;n != 5;++n)
			createJTextField(time, n);
		
		timeInformation = new JTextField();
		timeInformation.setText("        Year                    Month                    "
				+ "Day                    Hour                    Minute");
		timeInformation.setEditable(false);
		timeInformation.setBounds(1, 40, 432, 30);
		add(timeInformation);
		
		submit = new JButton("Submit");
		submit.addActionListener(this);
		submit.setBounds(1, 80, 340, 30);
		add(submit);
		
		cancel = new JButton("Cancel");
		cancel.addActionListener(this);
		cancel.setBounds(341, 80, 92, 30);
		add(cancel);
		
		repaint();
	}
	
	public void createJTextField(JTextField[] time, int n)
	{
		if (n == 0)
			time[n] = new JTextField("20"+(""+currentDate.getYear()).substring(1, 3));
		else if (n == 1)
			time[n] = new JTextField(""+currentDate.getMonth());
		else if (n == 2)
			time[n] = new JTextField(""+currentDate.getDay());
		else if (n == 3)
		{
			if (currentDate.getHours() <= 12)
				time[n] = new JTextField(currentDate.getHours() + "AM");
			else
				time[n] = new JTextField((currentDate.getHours()-12) + "PM");
		}
		else if (n == 4)
			time[n] = new JTextField(""+currentDate.getMinutes());
		
		time[n].setBounds(n*88+1, 10, 80, 30);
		add(time[n]);
	}
	
	public String returnDate(String year, String month, String day, String hour, String minute)
	{
		int twentyFourHourClock;
		
		if (hour.substring(hour.length()-2, hour.length()).equalsIgnoreCase("AM"))
			twentyFourHourClock = Integer.parseInt(hour.substring(0, hour.length()-2));
		else
			twentyFourHourClock = Integer.parseInt(hour.substring(0, hour.length()-2))+12;
		
		return (";1"+year.substring(2, 4) + ";" + month + ";" + day + ";" + twentyFourHourClock + ";" + minute);
	}
	
	public void save()
	{
		try
		{
			PrintWriter reminderSaver = new PrintWriter("C:\\It'sMyNotes\\Reminders\\" + file_Name + ".imr");
			reminderSaver.print(file_Name + returnDate(time[0].getText(), time[1].getText(), time[2].getText(), time[3].getText(), time[4].getText()));
			reminderSaver.flush();
		}
		catch (FileNotFoundException exception)
		{
			
		}
	}
	
	public static void main(String[] args)
	{
		ReminderSelectTime reminderSelectTime = new ReminderSelectTime("Reminder");
	}

	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == submit) save();
		this.dispose();
	}
}