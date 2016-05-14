package Classes;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import Classes.ControlPanel;

public class Spy extends JFrame implements ActionListener
{
	ControlPanel cp;
	Spy s;
	
	JButton exit;
	
	JLabel frame;
	
	boolean terminate = false;
	
	//This class displays the results.
	public Spy(ControlPanel c)
	{
		cp = c;
		s = this;
		
		//Creates an empty JFrame
		this.setTitle("Spy");
		this.setSize(1000, 680);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(null);
		this.setUndecorated(true);
		this.setVisible(true);
		
		init();
	}
	
	public void init()
	{
		exit = new JButton();
		exit.setIcon(new ImageIcon(cp.currentDirectory + "ExitButton.png"));
		exit.setSize(exit.getIcon().getIconWidth(), exit.getIcon().getIconHeight());
		exit.setBorder(null);
		exit.setLocation((int)(this.getWidth()-exit.getWidth()), 0);
		exit.addActionListener(this);
		this.add(exit);
		
		frame = new JLabel();
		frame.setSize(this.getWidth(), this.getHeight());
		this.add(frame);
		
		SpyWatcher s = new SpyWatcher(60);
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		terminate = true;
		this.dispose();
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
			
			while (!terminate)
			{
				try
				{
					BufferedImage screen = r.createScreenCapture(new Rectangle((int)tk.getWidth(), (int)tk.getHeight()));
					frame.setIcon(new ImageIcon(screen.getScaledInstance(s.getWidth(), s.getHeight(), Image.SCALE_SMOOTH)));
					
					this.sleep(1000/frameRate);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
