package Classes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class ObtainPing extends Thread implements Runnable
{
	String website = null;
	
	int numTimes = 0;
	
	boolean isRunning = true;
	boolean isScanning = false;
	
	boolean hasLoaded = false;
	
	Queue<Integer> speeds = new LinkedList<Integer>();
	ArrayList<String> websites = new ArrayList<String>();
	
	public ObtainPing(int times)
	{
		this.numTimes = times;
		
		this.Start();
		this.Load();
		this.start();
	}
	
	public static void main(String args[])
	{
		ObtainPing o = new ObtainPing(10);
	}
	
	public void run()
	{
		try 
		{
			while (isRunning)
			{
				for (int i = 0;i < websites.size();++i)
				{
					website = websites.get(i);
					
					for (int a = 0;a < 400;++a)
					if (isScanning && hasLoaded)
					{
						InputStream in = Runtime.getRuntime().exec("ping " + websites.get(i) + " -n " + numTimes).getInputStream();
						
						String inputStr = "";
						
						int input;
						while ((input = in.read()) != -1)
						{
							inputStr += (char)input;
						}
							
						int startLoc = inputStr.indexOf("time=");
						int endLoc = inputStr.indexOf("ms");
							
						while (startLoc != -1)
						{
							int pingSpeed = Integer.parseInt(inputStr.substring(startLoc+5, endLoc));
							speeds.add(pingSpeed);
							
							inputStr = inputStr.substring(0, startLoc) + inputStr.substring(endLoc+2);
								
							startLoc = inputStr.indexOf("time=");
							endLoc = inputStr.indexOf("ms");
						}
						
						in.close();
						
						while (speeds.size() > 100)
						{
							speeds.poll();
						}
					}
				}
			} 
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void Start()
	{
		isScanning = true;
	}
	
	public void Pause()
	{
		isScanning = false;
	}
	
	public void Stop()
	{
		isRunning = false;
	}
	
	public void Load()
	{
		try 
		{
			BufferedReader in = new BufferedReader(new FileReader("Websites.txt"));
			
			String input;
			while ((input = in.readLine()) != null)
			{
				websites.add(input);
			}
			
			hasLoaded = true;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}
