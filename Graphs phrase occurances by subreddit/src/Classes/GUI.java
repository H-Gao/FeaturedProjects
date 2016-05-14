package Classes;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.xml.crypto.Data;

public class GUI extends JFrame implements ActionListener, MouseListener, MouseMotionListener
{
	final int WIDTH = 8;
	
	int shift = 0;
	int startPosition = 0;
	
	ArrayList<Subreddit> subreddits = new ArrayList<Subreddit>();
	BufferedImage statistics;
	
	JLabel data;
	
	JTextField name;
	JTextField currentSubreddit;
	
	JButton exit;
	JButton left;
	JButton right;
	
	public GUI() throws Exception
	{
		this.setTitle("GUI");
		this.setSize(1200, 700);
		this.getContentPane().setBackground(Color.black);
		this.addMouseMotionListener(this);
		this.setUndecorated(true);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		exit = new JButton();
		exit.setSize(30, 30);
		exit.setLocation(1170, 0);
		exit.addActionListener(this);
		this.add(exit);
		
		name = new JTextField();
		name.setOpaque(false);
		name.setBorder(null);
		name.setForeground(Color.white);
		name.setSize(400, 20);
		name.setLocation(400, 20);
		this.add(name);
		
		currentSubreddit = new JTextField();
		currentSubreddit.addMouseListener(this);
		currentSubreddit.addMouseMotionListener(this);
		currentSubreddit.setBackground(new Color(10, 10, 180, 110));
		currentSubreddit.setBorder(null);
		currentSubreddit.setSize(WIDTH, 1200);
		currentSubreddit.setLocation(0, 0);
		this.add(currentSubreddit);
		
		left = new JButton();
		left.addActionListener(this);
		left.setSize(20, 20);
		left.setLocation(0, 680);
		this.add(left);
		
		right = new JButton();
		right.addActionListener(this);
		right.setSize(20, 20);
		right.setLocation(1180, 680);
		this.add(right);
		
		loadFile();
		
		if (subreddits.size() < 1200/WIDTH)
		{
			statistics = new BufferedImage(1200, 700, BufferedImage.TYPE_INT_RGB);
		}
		else
		{
			statistics = new BufferedImage(subreddits.size() * WIDTH, 700, BufferedImage.TYPE_INT_RGB);
		}
		
		double valPerPixel = 700/subreddits.get(0).val;
		
		for (int i = 0;i < subreddits.size();++i)
		{
			Subreddit subreddit = subreddits.get(i);
			
			int height = (int)(valPerPixel * subreddit.val);
			
			if (height < 1)
			{
				height = 1;
			}
			
			for (int y = 0;y < height;++y)
			{
				for (int x = 0;x < WIDTH;++x)
				{
					int colour;
					
					if (i < 200)
					{
						colour = new Color(240, i, i).getRGB();
					}
					else
					{
						colour = new Color(240, 200, 200).getRGB();
					}
					
					statistics.setRGB(x + WIDTH * i, 699 - y, colour);
				}
			}
		}
		
		data = new JLabel();
		data.setIcon(new ImageIcon(statistics));
		data.setSize(data.getIcon().getIconWidth(), data.getIcon().getIconHeight());
		this.add(data);
		
		this.repaint();
	}
	
	public void loadFile() throws Exception
	{
		BufferedReader in = new BufferedReader(new FileReader("outputFile.txt"));
		
		String input;
		while ((input = in.readLine()) != null)
		{
			String[] data = input.split(" ");
			
			subreddits.add(new Subreddit(data[0], Integer.parseInt(data[1])));
		}
		
		Collections.sort(subreddits);
	}
	
	public class Subreddit implements Comparable<Subreddit>
	{
		String name;
		int val;
		
		public Subreddit(String n, int v)
		{
			name = n;
			val = v;
		}

		public int compareTo(Subreddit o) 
		{
			if (this.val < o.val)
			{
				return 1;
			}
			else if (this.val > o.val)
			{
				return -1;
			}
			else
			{
				return 0;
			}
		}
	}
	
	public static void main(String args[]) throws Exception
	{
		GUI gui = new GUI();
	}

	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == exit)
		{
			this.dispose();
		}
		else if (e.getSource() == left)
		{
			if (shift < 0)
			{
				++shift;
				data.setLocation(data.getX() + 10 * WIDTH, data.getY());
			}
		}
		else if (e.getSource() == right)
		{
			--shift;
			data.setLocation(data.getX() - 10 * WIDTH, data.getY());
		}
	}

	public void mouseClicked(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mouseDragged(MouseEvent e) {}

	public void mouseMoved(MouseEvent e) 
	{
		int x = e.getX() - shift * 10 * WIDTH;
		
		if (e.getSource() == this && x/WIDTH < subreddits.size())
		{
			Subreddit subreddit = subreddits.get(x/WIDTH);
			
			name.setText(subreddit.name + "    Count: " + subreddit.val);
			currentSubreddit.setLocation((int)((x + shift * 10 * WIDTH)/WIDTH) * WIDTH, 0);
			
			this.repaint();
		}
	}
}
