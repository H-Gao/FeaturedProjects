package Classes;

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

/*
 * NOTE: ONGOING TESTS IN ORDER TO INCREASE THE RATE AT WHICH THE IMAGES REACHES THE CLIENT.
 * - THE MOVER HAS BEEN DISABLED.
 * - FEATURES BEING REWRITTEN.
 */

public class SpyClient extends JFrame implements ActionListener, MouseListener, MouseMotionListener
{
	int mouseX = 0, mouseY = 0;
	
	SpyClient s;
	
	JButton exit;
	JLabel frame;
	
	Socket kkSocket;
	BufferedReader in;
	PrintWriter out;
	
	boolean terminate = false;
	
	//This class displays the results.
	public SpyClient()
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
	        kkSocket = new Socket(InetAddress.getByName("localhost"), 4368);
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
		
		SpyWatcher s = new SpyWatcher(60);
		//SpyMover m = new SpyMover();
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
	
	//This class moves the mouse and gets the monitor images to be displayed.
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
			System.out.println("Started");
				
			int width = 100;
			int height = 100;
			
			while (!terminate)
			{
				try
				{
					//Creates an empty canvas.
					BufferedImage temp = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
						
					//Gets the RGB data (raster), that can be manipulated.
					WritableRaster pixArr = temp.getRaster();
					
					for (int y = 0;y < height;++y)
					{
						for (int x = 0;x < width;++x)
						{
							String[] dimensions = in.readLine().split(" ");
							width = Integer.parseInt(dimensions[0]);
							height = Integer.parseInt(dimensions[1]);
							
							String[] pixel = in.readLine().split(" ");
							int[] pixelData = new int[3];
							
							for (int i = 0;i < 3;++i)
							{
								pixelData[i] = Integer.parseInt(pixel[i]);
							}
							
							System.out.println(x + " " + y);
							pixArr.setPixel(x, y, pixelData);
						}
					}
						
					//Turns the RGB data (raster) into an image.
					BufferedImage im = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
						
					im.setData(pixArr);
						
					//Displays the image on the JLabel.
					frame.setIcon(new ImageIcon(new ImageIcon(im).getImage().getScaledInstance(frame.getWidth(), 
							frame.getHeight(), Image.SCALE_SMOOTH)));
					
					s.repaint();
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
		SpyClient s = new SpyClient();
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
