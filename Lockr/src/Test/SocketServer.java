package Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer 
{
	public static void main(String args[])
	{
		try
		{
	        ServerSocket serverSocket = new ServerSocket(4368);
	        Socket clientSocket = serverSocket.accept();
	        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
	        
	       while (true) 
	       {
	           out.println("here");
	       }
	     } 
		 catch (Exception e) 
		 {
			 e.printStackTrace();
	     }
	}
}
