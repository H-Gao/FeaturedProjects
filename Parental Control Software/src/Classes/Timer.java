package Classes;

import java.util.Date;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Timer extends Thread implements Runnable
{
	//A reference to the control panel, so it can relate to other resources.
	ControlPanel cp;
	
	//The seperator, which is used to separate the command from it's arguments.
	public final static char seperator = (char)(177);
	
	//Format:[Year][Month][Day][Hour][Minute][Second][ID = 0000 to 9999]
	PriorityQueue<String> time = new PriorityQueue<String>();
	
	//Connects the ID to the name of the blocked application.
	HashMap<Integer, String> connector = new HashMap<Integer, String>();
	
	public Timer(ControlPanel c)
	{
		cp = c; //Gains access to the control panel.
		
		//Starts running the thread, so this function does not need to be called later.
		this.start();
	}
	
	//Waits for timers, while checking the most recent timer.
	public void run()
	{
		//This thread waits for new timers, until the entire program is shut down. Hence, the infinite loop.
		while (true)
		{
			//Updates the while loop, so it doesn't freeze up.
			System.out.print("");
			
			//Makes sure there are timers left, so it doesn't get a null exception and so it saves resources.
			if (!time.isEmpty())
			{
				//Catches the errors, so that it doesn't cause the entire thread to shut down.
				try 
				{		
					//Gets the current date to compare.
					Date d = new Date();
					
					//Gets the date that the timer is set to.
					String setD = time.peek();
					
					/*Stores the current date in an array, in the order; years, months, days, hours, minutes and seconds.
					  Note: The years actually displays the number of years AFTER 1990, Ex. 2015 = (2015 - 1900) = 115.
					  Also, the monthes starts at 0, so January = 0 and December = 11.*/
					int[] currentDate = { d.getYear(), d.getMonth(), d.getDate(), d.getHours(), 
														d.getMinutes(), d.getSeconds() };
					
					//Stores the set date in an array, in the same order as the current dates.
					int[] setDate = { toInt(setD.substring(0, 4)), toInt(setD.substring(4, 6)), 
							toInt(setD.substring(6, 8)), toInt(setD.substring(8, 10)), toInt(setD.substring(10, 12)),
									toInt(setD.substring(12, 14))};
					
					//If the set date is bigger, compared to the current date, it runs the timer's command.
					if (compare(currentDate, setDate))
					{
						runCommand(time.poll().substring(14, 18)); //Runs the instructions here.
					}
					
					//Waits a bit and restarts.
					this.sleep(500);
				} 
				catch (Exception e) //Prints the name of the exception, for debugging.
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	//Compares the current date (a) to the set date (b).
	public boolean compare(int[] a, int[] b)
	{
		//Checks the order (total 6) = { years, monthes, days, hours, minutes and seconds }.
		for (int i = 0;i < a.length;++i)
		{
			//If it is checking the seconds, that means that the current date is exactly EQUAL to the set date.
			//Therefore, the timer's date has passed.
			if (i == 5)
			{
				if (a[i] >= b[i]) return true;  //If the current date is larger or equal to the set date.
				else return false;  //If the current date is larger than the set date.
			}
			/*Otherwise, if any portion (year, month, day, hour, etc.) is LARGER than the set date, and
			  everything larger than the CURRENT period of time (Ex. Year > Month > Day, etc.), then
			  it is certain that the timer's date has passed. If it is LOWER than the set date, it is CERTAIN
			  that the timer's date has not passed. If it is EQUAL, then we need to keep checking until ALL
			  of the periods of time are accounted for.*/
			else
			{
				if (a[i] > b[i]) return true; //If the current date is larger than the set date.
				else if (a[i] < b[i]) return false;  //If the current date is lower than the set date.
			}
		}
		
		/*It should never get to this piece of code, but this is still necessary to include this as a default,
		  in order to prevent java from sending an error.*/
		return false;
	}
	
	//Sortened form of Integer.parseInt(String in), turns a String into an integer.
	public int toInt(String in)
	{
		return Integer.parseInt(in); //Actually redirects it to this.
	}
	
	//Runs the command, after the timer has passed.
	public void runCommand(String id)
	{
		//Extracts the actual command from the other arguments.
		String[] command = connector.get(Integer.parseInt(id)).split(""+seperator);
		
		//Detects the command, which is always the first part of the String.
		switch (command[0])
		{
			//Sends a notice with the format [COMMAND] [WIDTH] [HEIGHT] [CONTENT] [TYPE (Warning, Message)].
			case "Notice":
			{
				System.out.println(cp + " " + command[0] + " " + command[1] + " " + command[2] + " " + command[3] + " ");
				Notice notice = new Notice(cp, Double.parseDouble(command[1]), command[3], Integer.parseInt(command[2]));
				System.out.println("Notice send " + command[3]);
			}
			
			//Blocks an application with the format [COMMAND] [APPLICATION]
			case "Block":
			{
				System.out.println("Blocking " + command[1]);
			}
		}
		
		System.out.println("Command started " + command[0] + " " + id);
	}
	
	//Adds a timer, which contains a command, which is executed after the timer has passed.
	public void addTimer(String name, int y, int m, int d, int h, int min, int s)
	{
		//The unique ID used to determine the string, since Strings and Integers cannot be sorted together.
		int id = 0;
		
		//Generates an unused ID.
		for (int i = 0;i < 10000;++i)
		{
			//If the id is unused, it can be used.
			if (connector.get(i) == null)
			{
				id = i;
				break;
			}
		}
		
		//Links the id (so it can be stored as a number), and the name 
		//(the command that will be run upon the timer), so it can be retrievedd later.
		connector.put(id, name);
		
		//Stores the SET date, and the ID.
		time.add(f(""+y, 4) + f(""+m, 2) +  f(""+d, 2)
				+ f(""+h, 2) + f(""+min, 2) + f(""+s, 2) + f(""+id, 4));
	}
	
	//Properly formats the string, to add the proper amount of zeroes after it.
	public String f(String in, int max)
	{
		//Calculates how many zeroes need to go in front of the current number.
		int num = max-in.length();
		
		//Adds the necessary amount of zeroes.
		for (int i = 0;i < num;++i) in = "0"+in;
		
		//Returns the formated String (the zeroes are not stored in an integer).
		return in;
	}
	
	//Used to test this thread.
	public static void main(String args[])
	{
		Timer t = new Timer(null);
		
		t.addTimer("Notice" + seperator + "1" + seperator + "0" + seperator + "Hello!", 115, 7, 15, 14, 48, 48);

		t.addTimer("Block" + seperator + "Audacity.exe", 115, 7, 15, 14, 49, 42);
	}
}
