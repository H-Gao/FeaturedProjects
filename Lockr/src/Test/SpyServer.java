package Test;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import Classes.ControlPanel;

public class SpyServer
{
	SpyServer s;
	
	int compression = 5;
	
	ServerSocket serverSocket;
	Socket clientSocket;
	BufferedReader in;
	PrintWriter out;
	
	boolean terminate = false;
	
	//This class displays the results.
	public SpyServer()
	{
		s = this;
		
		System.out.println("Enter compression value.");
		Scanner in = new Scanner(System.in);
		compression = in.nextInt();
		
		System.out.println("Starting server.");
		System.out.println("Attempting to obtain connection...");
		
		connect();
		
		System.out.println("Server successfully started.");
		
		init();
	}
	
	public void close()
	{
		try
		{
			in.close();
			out.close();
			clientSocket.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		System.exit(0);
	}
	
	//Creates the server that the client can connect to.
	public void connect()
	{
		try
		{
	        serverSocket = new ServerSocket(4368);
	        clientSocket = serverSocket.accept();
	        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	        out = new PrintWriter(clientSocket.getOutputStream(), true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void init()
	{
		SpyWatcher s = new SpyWatcher(60);
		SpyMover m = new SpyMover();
	}
	
	//Moves the mouse of the server's computer.
	public class SpyMover extends Thread implements Runnable
	{
		Robot r;
		
		public SpyMover()
		{
			try
			{
				r = new Robot();
				this.start();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		public void run()
		{
			while (!terminate)
			{
				try
				{
					int x = Integer.parseInt(in.readLine());
					int y = Integer.parseInt(in.readLine());
					
					r.mouseMove(x, y);
					
					if (!clientSocket.isConnected()) s.close();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	//This class gets the monitor images to be displayed, on the client.
	public class SpyWatcher extends Thread implements Runnable
	{
		int frameRate;
		
		Robot r;
		
		public SpyWatcher(int fr)
		{
			try
			{
				frameRate = fr;
				
				r = new Robot();
				this.start();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		public void run()
		{
			Dimension tk = Toolkit.getDefaultToolkit().getScreenSize();
			
			while (!terminate)
			{
				try
				{
					int width = (int)tk.getWidth();
					int height = (int)tk.getHeight();
					
					BufferedImage screen = r.createScreenCapture(new Rectangle(width, height));
					WritableRaster raster = screen.getRaster();
					
					String output = "";
					
					for (int y = 0;y < height/compression;++y)
					{
						for (int x = 0;x < width/compression;++x)
						{
							for (int i = 0;i < 3;++i)
							{
								output += raster.getPixel(x*compression, y*compression, new int[3])[i];
								
								if (i < 2) output += " ";
							}
							
							out.println(width/compression + " " + height/compression);
							out.println(output);
							output = "";
						}
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String args[])
	{
		SpyServer s = new SpyServer();
	}
}
