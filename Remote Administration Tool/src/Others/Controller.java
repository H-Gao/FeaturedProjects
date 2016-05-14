package Others;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

public class Controller 
{
	public static final int port = 4324;
	
	static int MAX_WIDTH;
	static int MAX_HEIGHT;
	
	static Robot r;
	
	static OutputStream out;
	static Socket client;
	
	public static void main(String args[])
	{
		Toolkit tk = Toolkit.getDefaultToolkit();
		
		MAX_WIDTH = (int)tk.getScreenSize().getWidth();
		MAX_HEIGHT = (int)tk.getScreenSize().getHeight();
		
		try 
		{
			r = new Robot();
			
			client = new Socket("HenrysLaptop", port);
			out = client.getOutputStream();
			
			while (true)
			{
				BufferedImage img = r.createScreenCapture(new Rectangle(MAX_WIDTH, MAX_HEIGHT));
				ImageIO.write(img, "png", out);
			}
		} 
		catch (SocketException e)
		{
			System.out.println("Connection terminated.");
			System.exit(0);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}
