package RAT2;

import java.awt.Button;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.Queue;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class MouseController extends Thread implements Runnable
{
	public static final int port = 4238;
	
	public static ServerSocket server;
	public static Socket client;
	
	public static OutputStream out;
	
	public static Viewer viewer;
	
	public MouseController(Viewer v)
	{
		viewer = v;
		
		this.start();
	}
	
	public void run()
	{
		init();
	}
	
	public void init()
	{
		try
		{
			if (server != null) server.close();
			if (client != null) client.close();
			
			server = null;
			client = null;
			
			System.out.println("Started mouse server.");
			
			server = new ServerSocket(port);
			client = server.accept();
			
			System.out.println("Client obtained.");
			
			out = client.getOutputStream();
			
			while (true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void MouseAction(int id) throws IOException
	{
		if (client != null)
		out.write(id);
	}
	
	public void TypeKey(char key) throws IOException
	{
		if (key > 6 && key <= 127 && client != null)
		{
			out.write(key);
		}
	}
	
	public void moveMouse(int x, int y) throws IOException
	{
		int MAX_VALUE = 127;
		
		int i = 0;
		
		out.write(0);
		
		for (int a = 0;a < 2;++a)
		{
			if (a == 0) i = x;
			else if (a == 1) i = y;
			
			for (int b = 0;b < 14;++b)
			{
				if (i >= MAX_VALUE)
				{
					out.write(MAX_VALUE);
					i -= MAX_VALUE;
				}
				else if (i <= 0)
				{
					out.write(0);
				}
				else
				{
					out.write(i);
					i = 0;
				}
			}
		}
	}
}
