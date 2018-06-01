package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.LinkedList;

/*Status: SUCCESS! It has been transfered to the regular class file.*/

public class BlockIP 
{
	public LinkedList<String> blockedList = new LinkedList<String>();
	public String redirectIP = "66.135.46.119";
	
	//Stores what is currently inside the hosts file.
	public BlockIP()
	{
		try
		{
			//The input steam.
			BufferedReader in = new BufferedReader(new FileReader("C:\\Windows\\System32\\drivers\\etc\\hosts"));
			
			String input; //Stores the input, to check if it is the last line AND to put it into the blocklist.
			while ((input = in.readLine()) == null) blockedList.add(input.split(" ")[1]); //Puts the existing entries back.
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String args[])
	{
		BlockIP b = new BlockIP();
	}
	
	//Adds a new website to be blocked.
	public void addEntry(String website)
	{
		//If the entry already exists, then it won't block.
		for (int i = 0;i < blockedList.size();++i) if (blockedList.get(i).equals(website)) return;
		
		try
		{
			//The output stream.
			FileWriter out = new FileWriter(new File("C:\\Windows\\System32\\drivers\\etc\\hosts"));
			
			out.write(redirectIP + " " + website); //Adds the new entry.
			out.close(); //Saves the changes, by closing the stream.
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
