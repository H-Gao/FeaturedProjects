package ServerAPI;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class BasicClient 
{
	public Socket client;
	
	public BufferedReader in;
	public PrintWriter out;
	
	public BasicClient(int port)
	{
		try 
		{
			client = new Socket("localhost", port);
			
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(client.getOutputStream(), true);
			
			ServerWriter output = new ServerWriter(client);
		}
		catch (IOException ioexception) 
		{
			ioexception.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		BasicClient client = new BasicClient(2834);
	}
}
