package Classes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Effects2 extends JFrame
{
	Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	
	public Effects2() throws InterruptedException
	{
		this.setTitle("Effects");
		this.setSize((int)screen.getWidth(), (int)screen.getHeight());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setUndecorated(true);
		this.setVisible(true);
		setBackground(new Color(0, 255, 0, 0));
		this.setAlwaysOnTop(true);
		
		Particle p = new Particle();
		FindSpeed f = new FindSpeed();
	}
	
	Effects2 e = this;
	
	int MAX_SIZE = 20;
	double MIN_SPEED = 0;//4.0;
	
	JLabel[] p = new JLabel[MAX_SIZE];
	
	public class Particle extends Thread implements Runnable
	{
		Random r = new Random();
		int[] sequence = new int[1000000];
		int distance = 10;
		
		public Particle()
		{
			generateSequence();
			
			for (int i = 0;i < MAX_SIZE;++i)
			{
				p[i] = new JLabel();
				
				if (sequence[(int)i%1000000] > 3)
				{
					p[i].setIcon(new ImageIcon("Particle2.png"));
				}
				else
				{
					p[i].setIcon(new ImageIcon("Particle" + ((i%6)+1) + ".png"));
				}
				
				p[i].setSize(4, 4);
				e.add(p[i]);
			}
			
			this.start();
		}
		
		public void generateSequence()
		{
			for (int i = 0;i < sequence.length;++i)
			{
				sequence[i] = r.nextInt(distance);
			}
		}
		
		public void run()
		{
			for (long i = 0;true;++i)
			{
				Point mouse = MouseInfo.getPointerInfo().getLocation();
				
				int index = (int)(i%MAX_SIZE);
				
				p[index].setLocation(mouse.x - distance/2 + sequence[(int)i%sequence.length], mouse.y - distance/2 + sequence[(int)i%sequence.length]);
				
				for (int a = 0;a < MAX_SIZE;++a)
				{
					if (p[a] != null)
					{
						boolean xInBounds = mouse.getX() <= p[a].getX() + p[a].getWidth() && mouse.getX() >= p[a].getX();
						boolean yInBounds = mouse.getY() <= p[a].getY() + p[a].getHeight() && mouse.getY() >= p[a].getY();
						
						p[a].setVisible(true);
						
						if (xInBounds && yInBounds)
						{
							p[a].setVisible(false);
						}
					}
				}
				
				try 
				{
					System.out.println(speed);
					
					if (speed < 25) this.sleep(100 - (int)(4 * speed));
					else this.sleep(1);
				} 
				catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	double speed = 0;
	
	public class FindSpeed extends Thread implements Runnable
	{
		public FindSpeed()
		{
			this.start();
		}
		
		Point lastPoint = new Point();
		
		public void run()
		{
			for (long i = 0;true;++i)
			{
				Point mouse = MouseInfo.getPointerInfo().getLocation();
				
				speed = Math.sqrt(Math.pow(lastPoint.getX() - mouse.getX(), 2) + Math.pow(lastPoint.getY() - mouse.getY(), 2));
				
				try 
				{
					this.sleep(20);
				} 
				catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
				
				lastPoint = mouse;
			}
		}
	}
	
	public static void main(String args[]) throws InterruptedException
	{
		Effects2 e = new Effects2();
	}
}

