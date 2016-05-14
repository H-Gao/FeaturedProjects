package Classes;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class DisplayPing extends JFrame implements ActionListener, MouseMotionListener, MouseListener
{
	DisplayPing displayPing;
	ObtainPing o;
	UpdateFrame uf;
	
	int numTimes = 400;
	
	JButton btnExit;
	
	JLabel lblData;
	JLabel lblProgress;
	JLabel lblAverage;
	JLabel lblAverageNum;
	JLabel lblWebsite;
	
	public DisplayPing()
	{
		displayPing = this;
		
		this.setSize(400, 400);
		this.setLayout(null);
		this.setUndecorated(true);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
		
		btnExit = new JButton();
		btnExit.setIcon(new ImageIcon("Exit.png"));
		btnExit.addActionListener(this);
		btnExit.setLocation(400 - 20, 0);
		btnExit.setSize(20, 20);
		this.add(btnExit);
		
		lblWebsite = new JLabel("Pinging...");
		lblWebsite.setSize(100, 100);
		lblWebsite.setLocation(120, -35);
		lblWebsite.setForeground(Color.WHITE);
		this.add(lblWebsite);
		
		lblAverageNum = new JLabel("Average: ----ms");
		lblAverageNum.setSize(100, 100);
		lblAverageNum.setLocation(5, -35);
		lblAverageNum.setForeground(Color.WHITE);
		this.add(lblAverageNum);
		
		lblAverage = new JLabel();
		lblAverage.setLocation(0, 0);
		lblAverage.setSize(400, 1);
		lblAverage.setOpaque(true);
		lblAverage.setBackground(Color.YELLOW);
		this.add(lblAverage);
		
		lblProgress = new JLabel();
		lblProgress.setLocation(0, 0);
		lblProgress.setSize(1, 400);
		lblProgress.setOpaque(true);
		lblProgress.setBackground(Color.BLUE);
		this.add(lblProgress);
		
		lblData = new JLabel();
		lblData.setLocation(0, 0);
		lblData.setSize(400, 400);
		this.add(lblData);
		
		o = new ObtainPing(1);
		uf = new UpdateFrame();
	}
	
	public class UpdateFrame extends Thread implements Runnable
	{
		public UpdateFrame()
		{
			this.start();
		}
		
		public void run()
		{
			int xVal = 0;
			int AverageVal = 0;
			int totalNum = 0;
			
			BufferedImage img = new BufferedImage(400, 400, BufferedImage.TYPE_INT_RGB);
			
			while (true)
			{
				for (int x = 0;x < o.speeds.size();++x)
				{
					if (o.speeds.isEmpty()) break;
					
					int speed = o.speeds.poll();
					
					if (x + xVal >= 400)
					{
						xVal = 0;
						AverageVal = 0;
						totalNum = 0;
					}
					
					AverageVal += speed;
					++totalNum;
					
					int average = AverageVal/totalNum;
					
					for (int y = 0;y < 400;++y) 
						img.setRGB(x + xVal, y, new Color(0, 0, 0).getRGB());
					
					for (int y = 0;y < speed;++y)
						if (y < 400) img.setRGB(x + xVal, 399 - y, getColour(y));
					
					lblAverageNum.setText("Average: " + average + "ms");
					lblWebsite.setText(o.website);
					
					lblAverage.setLocation(0, 400 - average);
					lblProgress.setLocation(xVal, 0);
					
					lblData.setIcon(new ImageIcon(img));
					displayPing.repaint();
					
					++xVal;
				}
			}
		}
	}
	
	public int getColour(int in)
	{
		if (in < 50)
			return new Color(0, 255, 0).getRGB();
		else if (in < 100)
			return new Color(100, 255, 80).getRGB();
		else if (in < 150)
			return new Color(255, 255, 0).getRGB();
		else if (in < 200)
			return new Color(255, 128, 0).getRGB();
		else
			return new Color(255, 0, 0).getRGB();
	}
	
	public static void main(String args[])
	{
		DisplayPing dp = new DisplayPing();
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		System.exit(0);
	}
	
	int mouseX = 0;
	int mouseY = 0;
	
	public void mouseDragged(MouseEvent e)
	{
		this.setLocation(e.getXOnScreen() - mouseX, e.getYOnScreen() - mouseY);
	}
	
	public void mousePressed(MouseEvent e) 
	{
		mouseX = e.getX();
		mouseY = e.getY();
	}

	public void mouseMoved(MouseEvent e) {}

	public void mouseClicked(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}
}
