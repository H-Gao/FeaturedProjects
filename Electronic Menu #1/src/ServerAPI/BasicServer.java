package ServerAPI;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import javax.management.timer.Timer;

//This BasicServer class is a basic server with the essential tools and processes to run a server.
public class BasicServer
{
	public HashMap<String, Boolean> searchOnlineUsers = new HashMap<String, Boolean>();
	public ArrayList<String> onlineUsers = new ArrayList<String>();
	
	public BufferedReader in;
	public PrintWriter out;
	
	public BasicProtocol bprot;
	
	public BasicServer(int port)
	{	
		try
		{
			//Establishes the server.
			ServerSocket server = new ServerSocket(port);
			
			System.out.println("Attempting to establish a connection.");
			
			//Establishes the client.
			Socket client = server.accept();
			
			System.out.println("Connection established.");
			
			//The input and output, which can communicate with the client.
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(client.getOutputStream(), true);
			
			//The protocol.
			bprot = new BasicProtocol(this, out, server, client);
			
			System.out.println("Listening for requests.");
			
			while (true)
			{
				onListen();
			}
		}
		catch (IOException ioexception)
		{
			ioexception.printStackTrace();
		}
	}
	
	public void onListen() throws IOException
	{
		readFromClient();
	}
	
	public void readFromClient() throws IOException
	{
		String input = in.readLine();
		
		if (input != null)
			System.out.println(bprot.parseCommands(input));
	}
	
	public static void main(String[] args)
	{
		BasicServer server = new BasicServer(2834);
	}
}
