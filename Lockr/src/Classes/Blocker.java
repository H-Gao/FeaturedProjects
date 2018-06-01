package Classes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

public class Blocker extends Thread implements Runnable
{
	/*Stores the blockedApps, if it is TRUE it is blocked.
	Otherwise, it is not. (Applications can be temperarily blocked).*/
	HashMap<String, Boolean> blockedApps = new HashMap<String, Boolean>();
	
	//Returns the path of the blocked application, used to find it's icon.
	HashMap<String, String> path = new HashMap<String, String>();
	
	//Starts the actual applications. It does not require any specail inputs.
	public Blocker()
	{
		//Starts running the application, put here to be concise.
		this.start();
	}
	
	//The portion of the thread which will detect whether blocked applications are trying to be run.
	public void run()
	{
		while (true)
		{
			try
			{
				/*Creates a reader, which outputs what processes are active.
				These processes can then be blocked, if they are not allowed.*/
				BufferedReader in = new BufferedReader(new InputStreamReader
						(Runtime.getRuntime().exec("tasklist.exe").getInputStream()));
				
				//Reads the first three lines, because they do not contain useful information.
				for (int i = 0;i < 3;++i) in.readLine();
				
				//This portion of the thread repeatedly detects the running processes.
				String input;
				while ((input = in.readLine()) != null)
				{
					/*Removes all of the unnecessary information, such as the amount of memory used.
					It only keeps the name of the process.*/
					input = input.substring(0, input.indexOf(' '));
					
					//If the application is blocked, it is shut down.
					if (blockedApps.get(input) != null && blockedApps.get(input)) Runtime.getRuntime().exec("taskkill /F /IM " + input);
					
					//Sleeps for a while to preserve CPU/Processing Time.
					Thread.sleep(10);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
