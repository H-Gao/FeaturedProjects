package snake;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class GUI extends JFrame implements KeyListener, MouseListener
{
	GUI g;
	
	JLabel finishedScreen;
	JLabel startScreen;
	
	JButton restart;
	JButton exit;
	
	move m;
	
	int direction = 3;
	boolean hasStarted = false;
	
	Point head = new Point();
	Point coin = new Point();
	
	int newSnakes = 0;
	
	public GUI()
	{
		g = this;
		
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		
		startScreen = new JLabel();
		startScreen.setIcon(new ImageIcon("start.png"));
		startScreen.setSize(1100, 620);
		startScreen.setVisible(true);
		this.add(startScreen);
		
		this.setTitle("Snake");
		this.setSize(1100, 640);
		this.setLocation((int)(size.getWidth()-this.getWidth())/2, (int)(size.getHeight()-this.getHeight())/2 - 20);
		this.setUndecorated(true);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.addKeyListener(this);
		
		m = new move();
	}
	
	public class move extends Thread implements Runnable
	{
		LinkedList<Point> line = new LinkedList<Point>();
		Block[][] blocks = new Block[55][32];
		
		public move()
		{	
			for (int y = 0;y < blocks[0].length;++y)
				for (int x = 0;x < blocks.length;++x)
					blocks[x][y] = new Block(g, 20*x, 20*y);
			
			newCoin(blocks, 8);
			head.setLocation(18, 15);
			
			this.start();
		}
		
		public void run()
		{
			try
			{
				int a = 0;
				
				while (true)
				{	
					if (!hasStarted)
					{
						if (a >= 0 && a < 20)
							direction = 3;
						else if (a >= 20 && a < 31)
							direction = 2;
						else if (a >= 31 && a < 51)
							direction = 1;
						else if (a >= 51 && a < 61)
							direction = 0;
						
						if (a == 61) a = 0;
						else ++a;
					}
					
					if (direction == 0)
						head.setLocation((int)head.getX(), (int)head.getY()-1);
					
					else if (direction == 1)
						head.setLocation((int)head.getX()-1, (int)head.getY());
					
					else if (direction == 2)
						head.setLocation((int)head.getX(), (int)head.getY()+1);
					
					else if (direction == 3)
						head.setLocation((int)head.getX()+1, (int)head.getY());
					
					if (head.getX() < 0 || head.getX() >= blocks.length || head.getY() < 0 || head.getY() >= blocks[0].length)
					{
						finishGame(blocks);						
						return;
					}
					else if (blocks[(int)head.getX()][(int)head.getY()].state.equals("On"))
					{
						finishGame(blocks);
						return;
					}
					
					line.add(new Point(head));
					blocks[(int)head.getX()][(int)head.getY()].setOn();
					
					if (newSnakes <= 0)
					{
						Point endingPoint = line.poll();
						blocks[(int)endingPoint.getX()][(int)endingPoint.getY()].setOff();
					}
					else --newSnakes;
					
					if (head.getX() == coin.getY() && head.getY() == coin.getX())
						newCoin(blocks, (int)Math.pow(line.size()/10+3, 1.2));
					
					int decreaseVal = line.size()/16;
							
					if (decreaseVal > 50) decreaseVal = 50;
					
					g.repaint();
					this.sleep(70-decreaseVal);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				System.exit(0);
			}
		}
		
		public void readLine()
		{
			System.out.println("Reading...");
			
			for (int i = 0;i < line.size();++i)
				System.out.println((int)line.get(i).getX() + " " + (int)line.get(i).getX());
		}
	}
	
	public void finishGame(Block[][] blocks)
	{
		finishedScreen = new JLabel();
		finishedScreen.setIcon(new ImageIcon("finished.png"));
		finishedScreen.setSize(1100, 620);
		this.add(finishedScreen);
		
		restart = new JButton();
		restart.setIcon(new ImageIcon("restart.png"));
		restart.addMouseListener(this);
		restart.setBorder(null);
		restart.setSize(450, 70);
		restart.setLocation(75, 550);
		this.add(restart);
		
		exit = new JButton();
		exit.setIcon(new ImageIcon("exit.png"));
		exit.addMouseListener(this);
		exit.setBorder(null);
		exit.setSize(450, 70);
		exit.setLocation(575, 550);
		this.add(exit);
		
		for (int y = 0;y < blocks[0].length;++y)
		{
			for (int x = 0;x < blocks.length;++x)
			{
				if (blocks[x][y].state.equals("Off"))
				{
					this.remove(blocks[x][y].lblBlock);
					blocks[x][y] = new Block(g, 20*x, 20*y);
				}
			}
		}
		
		finishedScreen.setVisible(true);
		restart.setVisible(true);
		
		this.repaint();
	}
	
	public void newCoin(Block[][] blocks, int numSnakes)
	{
		newSnakes = numSnakes;
		
		Random r = new Random();
		coin.setLocation(r.nextInt(32), r.nextInt(55));
		blocks[(int)coin.getY()][(int)coin.getX()].setCoin();
	}
	
	public static void main(String args[])
	{
		GUI gui = new GUI();
	}

	public void keyTyped(KeyEvent e) {}

	public void keyPressed(KeyEvent e) 
	{
		startScreen.setVisible(false);
		hasStarted = true;
		
		switch (e.getKeyChar())
		{
			case 'w':
			{
				direction = 0;
				break;
			}
			case 'a':
			{
				direction = 1;
				break;
			}
			case 's':
			{
				direction = 2;
				break;
			}
			case 'd':
			{
				direction = 3;
				break;
			}
		}
	}
	
	public void mouseClicked(MouseEvent e)
	{
		if (e.getSource() == restart)
		{
			GUI gui = new GUI();
			this.dispose();
		}
		else
		{
			System.exit(0);
		}
	}
	
	public void mouseEntered(MouseEvent e) 
	{
		if (e.getSource() == restart)
			restart.setIcon(new ImageIcon("restart_Click.png"));
		else
			exit.setIcon(new ImageIcon("Exit_Click.png"));
	}

	public void mouseExited(MouseEvent e) 
	{
		if (e.getSource() == restart)
			restart.setIcon(new ImageIcon("restart.png"));
		else
			exit.setIcon(new ImageIcon("Exit.png"));
	}

	public void keyReleased(KeyEvent e) {}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}
}
