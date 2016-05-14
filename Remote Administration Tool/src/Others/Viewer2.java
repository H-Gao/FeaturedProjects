package Others;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
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
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Viewer2 extends JFrame implements MouseListener, MouseMotionListener
{
	public static final int port = 4324;
	public static Dimension s = Toolkit.getDefaultToolkit().getScreenSize();
	
	public static JLabel l;
	
	public static ServerSocket server;
	public static Socket client;
	
	public static ServerSocket server2;
	public static Socket client2;
	public static OutputStream out2;
	
	public static Viewer2 v;
	
	public static Robot r;
	
	public Viewer2()
	{
		this.setTitle("Viewer");
		this.setSize((int)s.getWidth(), (int)s.getHeight());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setUndecorated(false);
		this.setVisible(true);
		
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		
	    l = new JLabel();
	    l.setSize((int)s.getWidth(), (int)s.getHeight());
	    this.add(l);
	}
	
	public static void init() throws Exception
	{
		System.out.println("Restarting...");
		
		if (server != null) server.close();
		if (client != null) client.close();
		
		server = null;
		client = null;
		
		server = new ServerSocket(port);
		client = server.accept();
		
	    v = new Viewer2();
	    
	    int numDisconnect = 0;
	    
		while (true)
		{
		    ImageInputStream input = ImageIO.createImageInputStream(client.getInputStream());
		    BufferedImage img = ImageIO.read(input);
		    
		    if (img == null)
		    {
		    	++numDisconnect;
		    }
		    else
		    {
			    l.setIcon(new ImageIcon(img));
			    v.repaint();
		    }
		    
		    if (numDisconnect >= 500)
		    {
				server.close();
				client.close();
				
				server = null;
				client = null;
				
				v.dispose();
				init();
		    }
		}
	}
	
	public static void main(String args[])
	{
		try 
		{
			r = new Robot();
			
			server2 = new ServerSocket(port+10);
			client2 = server2.accept();
			out2 = client2.getOutputStream();
			
			init();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	public void mouseClicked(MouseEvent e) 
	{
		
	}
	
	public void mouseMoved(MouseEvent e) 
	{
		System.out.println(e.getX() + " " + e.getY());
		
		try
		{
			out2.write(e.getX());
			out2.write(e.getY());
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
	}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mouseDragged(MouseEvent e) {}
}
