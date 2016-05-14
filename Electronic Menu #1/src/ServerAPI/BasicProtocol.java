package ServerAPI;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import ServerAPI.Encryption;

public class BasicProtocol 
{
	Encryption encryption = new Encryption(218, 8);
	
	long currentTime;
	
	String decryptionKey = "basicServer";
	
	BasicServer server;
	
	Closeable current;
	Closeable target;
	
	//The valid commands.
	String[] commandList = { "ping", "receivePing", "makeUser", "requestLogin", "print", "kick", "commandList", "shutdown" };
	
	public BasicProtocol(BasicServer server, PrintWriter out, Closeable tempServer, Closeable tempClient)
	{
		this.server = server;
		
		current = tempServer;
		target = tempClient;
	}
	
	public BasicProtocol(PrintWriter out, Closeable tempServer, Closeable tempClient)
	{
		current = tempServer;
		target = tempClient;
	}
	
	public String parseCommands(String in) throws IOException
	{
		if (in.length() > 2)
		{
			in = encryption.decrypt(decryptionKey, in);
			
			if (in.charAt(0) == '<')
			{
				//The commands will come in the format of [CLIENT/SERVER] <COMMAND> INPUT INPUT INPUT.
				int startOfIndex = in.indexOf('<');
				int endOfIndex = in.indexOf('>')+1;
			
				String command = in.substring(startOfIndex, endOfIndex);
				String data = null;
				
				//If there is data.
				if (in.length() > endOfIndex)
					data = in.substring(endOfIndex+1, in.length());
			
				return completeCommand(command, data);
			}
			else
			{
				char[] inChar = in.toCharArray();
				
				System.out.println(in);
				
				for (int i = 0;i < in.length();++i)
					System.out.print((int)inChar[i] + " ");
				
				System.out.println();
				
				return "Invalid format.";
			}
		}
		else
		{
			return "The length cannot be " + in.length();
		}
	}
	
	public String completeCommand(String command, String data) throws IOException
	{
		switch (command)
		{
			case "<ping>":
			{
				currentTime = System.nanoTime();
				
				server.out.println("<print> has this message been received?");
				server.out.flush();
				
				return "message sent.";
			}
			case "<receivePing>":
			{
				return (System.nanoTime()-currentTime)/1000000 + " ms";
			}
			case "<requestUser>": case "<requestLogin>":
			{
				Encryption decryptor = new Encryption(false, 218, 8000);
				
				String username = data.substring(0, data.indexOf(' '));
				String password = data.substring(data.indexOf(' ')+1, data.length());
				
				BufferedReader in = new BufferedReader(new FileReader(username + ".txt"));
				String out = "";
				
				String input;
				
				while ((input = in.readLine()) != null)
					out += input;
				
				if (server.searchOnlineUsers.get(username) == null)
				{
					if (decryptor.decrypt(username, out).equals(password))
					{
						server.onlineUsers.add(username);
						server.searchOnlineUsers.put(username, true);
					
						return "The user " + username + " is now able to access the servers.";
					}
					else
					{
						return "Invalid username or password.";
					}
				}
				else
				{
					return "The user has already logged in.\n"
							+ "We recommend you log out or wait until the user times out (in 10 minutes).";
				}
			}
			case "<makeUser>":
			{
				Encryption encryptor = new Encryption(false, 218, 8000);
				
				String username = data.substring(0, data.indexOf(' '));
				String password = data.substring(data.indexOf(' ')+1, data.length());
				
				PrintWriter out = new PrintWriter(username + ".txt");
				out.println(encryptor.encrypt(username, password));
				out.close();
				
				return "The user " + username + " has been activated with the password " + password;
			}
			case "<print>":
			{
				return data;
			}
				
			case "<kick>":
			{
				try 
				{
					target.close();
					return "The client has been kicked from the server.";
				} 
				catch (IOException e) 
				{
					System.err.println("An IO exception occured.");
					e.printStackTrace();
				}
			}
			case "<commandList>":
			{
				String out = commandList[0];
					
				for (int i = 1;i < commandList.length;++i)
					out += "\n" + commandList[i];
					
				return out;
			}
			case "<shutdown>":
			{
				try 
				{
					current.close();
					System.out.println("The server has been closed.");
					System.exit(0);
						
					return "The server has been closed.";
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
		
		return null;
	}
}
