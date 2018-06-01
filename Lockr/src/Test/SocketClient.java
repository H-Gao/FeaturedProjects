package Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class SocketClient 
{
	public static void main(String args[])
	{
		try
		{
	        Socket kkSocket = new Socket(InetAddress.getByName("Family-Pc"), 4368);
	        BufferedReader in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));
	        PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
	        
	        System.out.println(InetAddress.getLocalHost().getHostAddress());

	        String input;
	        while (!(input = in.readLine()).equals("exit")) 
	        {		
	        	System.out.println(input);
	        }
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
