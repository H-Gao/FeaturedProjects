package Classes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.LinkedList;

public class BlockIP 
{
	//A reference to the control panel.
	ControlPanel cp;
	
	public LinkedList<String> blockedList = new LinkedList<String>();
	public String redirectIP = "66.135.46.119";
	
	//Stores what is currently inside the hosts file.
	public BlockIP(ControlPanel c)
	{
		cp = c; //Adds the reference.
		
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
	
	//Adds a new website to be blocked.
	public void addEntry(String website)
	{
		//If the entry already exists, then it won't block.
		for (int i = 0;i < blockedList.size();++i)
		{
			if (blockedList.get(i).equals(website)) //If it exists, then inform the user and exit.
			{
				Notice notice = new Notice(cp, 1, "You have already blocked this website.", 0); //Informs the user, that the website is already blocked.
				return;
			}
		}
		
		blockedList.add(website); //Adds the entry to memory.
		cp.blockIPList.update();
		dump(); //Puts the memory into the file.
	}
	
	//Adds a new website to be blocked.
	public void deleteEntry(String website)
	{
		//If the entry already exists, then it won't block.
		for (int i = 0;i < blockedList.size();++i)
		{
			if (blockedList.get(i).equals(website)) //If it exists, then inform the user and exit.
			{
				blockedList.remove(i); //Removes the entry.
				cp.blockIPList.update();
				dump(); //Updates the file.
				
				return; //Exits.
			}
		}
	}
	
	//Dumps what is stored in memory, into the file.
	public void dump()
	{
		try
		{
			//The output stream.
			BufferedWriter out = new BufferedWriter(new FileWriter(new File("C:\\Windows\\System32\\drivers\\etc\\hosts")));
			
			//Adds the new entries.
			for (int i = 0;i < blockedList.size();++i)
			{
				out.write(redirectIP + " " + blockedList.get(i));
				out.newLine();
			}
			
			out.close(); //Saves the changes, by closing the stream.
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String args[])
	{
		BlockIP b = new BlockIP(null);
	}
}

