package Test;

import java.awt.Dimension;
import java.awt.Image;
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
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import Classes.ControlPanel;

public class SpyMoveClient extends JFrame implements ActionListener, MouseListener, MouseMotionListener
{
	int mouseX = 0, mouseY = 0;
	
	SpyMoveClient s;
	
	JButton exit;
	JLabel frame;
	
	Socket kkSocket;
	BufferedReader in;
	PrintWriter out;
	
	boolean terminate = false;
	
	//This class displays the results.
	public SpyMoveClient()
	{
		s = this;
		
		//Creates an empty JFrame
		this.setTitle("Spy");
		this.setSize(1366, 780);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(null);
		this.setUndecorated(true);
		this.setVisible(true);
		
		connect();
		
		init();
	}
	
	//Establishes a connection to the server.
	public void connect()
	{
		try
		{
	        kkSocket = new Socket(InetAddress.getByName("Family-Pc"), 4369);
	        in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));
	        out = new PrintWriter(kkSocket.getOutputStream(), true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void init()
	{
		exit = new JButton();
		exit.setIcon(new ImageIcon("C:\\Judo Workspace\\AlgorithmIdeas\\LockrSoftware\\" + "ExitButton.png"));
		exit.setSize(exit.getIcon().getIconWidth(), exit.getIcon().getIconHeight());
		exit.setBorder(null);
		exit.setLocation((int)(this.getWidth()-exit.getWidth()), 0);
		exit.addActionListener(this);
		this.add(exit);
		
		frame = new JLabel();
		frame.addMouseListener(this);
		frame.addMouseMotionListener(this);
		frame.setSize(this.getWidth(), this.getHeight());
		this.add(frame);
		
		SpyMover m = new SpyMover();
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		try
		{
			in.close();
			out.close();
			kkSocket.close();
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
		terminate = true;
		this.dispose();
	}
	
	//Moves the mouse of the server's computer.
	public class SpyMover extends Thread implements Runnable
	{
		Robot r;
			
		public SpyMover()
		{
			this.start();
		}
			
		public void run()
		{
			while (!terminate)
			{
				try
				{
					out.println(mouseX);
					out.println(mouseY);
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
		SpyMoveClient s = new SpyMoveClient();
	}

	public void mouseClicked(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mouseDragged(MouseEvent e) {}

	public void mouseMoved(MouseEvent e) 
	{
		mouseX = e.getX();
		mouseY = e.getY();
		
		System.out.println(mouseX + " " + mouseY);
	}
}
