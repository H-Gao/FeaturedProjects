package WindowDevelopmentClassesHenry;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;

public class SimpleWindow extends JFrame
{
	int transitionFinish = 0;
	
	Timer timer = new Timer(1, new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
	        transition(transitionFinish);
	    }
	});
	
	JLabel background;
	
	public SimpleWindow(String name, int width, int height, boolean isVisible)
	{
		this.setTitle(name);
		this.setSize(width, height);
		this.setLayout(null);
		this.setVisible(isVisible);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		onInit();
		createComponents();
		
		repaint();
	}
	
	public SimpleWindow(String name, int width, int height, boolean isVisible, boolean willDisposeOnClose)
	{
		this.setTitle(name);
		this.setSize(width, height);
		this.setLayout(null);
		this.setVisible(isVisible);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		onInit();
		createComponents();
		
		repaint();
	}
	
	public SimpleWindow(String name, int x, int y, int width, int height, boolean isVisible)
	{
		this.setTitle(name);
		this.setBounds(x, y, width, height);
		this.setLayout(null);
		this.setVisible(isVisible);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		onInit();
		createComponents();
		
		repaint();
	}
	
	public SimpleWindow(String name, int x, int y, int width, int height, boolean isVisible, boolean willDisposeOnClose)
	{
		this.setTitle(name);
		this.setBounds(x, y, width, height);
		this.setLayout(null);
		this.setVisible(isVisible);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		onInit();
		createComponents();
		
		repaint();
	}
	
	public void say(String say)
	{
		System.out.println(say);
	}
	
	public void updateWindows()
	{
		repaint();
		System.out.println("Windows updated");
	}
	
	public void beginTransition(int finish)
	{
		this.setSize(0, this.getHeight());
		
		transitionFinish = finish;
		timer.start();
	}
	
	public void transition(int finish)
	{
		if (this.getWidth() < finish)
		{
			this.setSize(this.getWidth() + 10, this.getHeight());
		}
		else
		{
			timer.stop();
		}
	}
	
	public void setBackground(String path)
	{
		background = new JLabel();
		background.setIcon(new ImageIcon(path));
		background.setBounds(0, 0, this.getWidth(), this.getHeight());
		add(background);
	}
	
	public void forceUpdate()
	{
		this.setVisible(false);
		this.setVisible(true);
	}

	public void createComponents() {}
	
	public void onInit() {}
	
	public void onDone() {}
}

/*
 * Created by: Henry Gao
 * Date of completion: February 15th, 2014.
 * Purpose: To avoid repeating rewriting the same lines of code AGAIN and AGAIN after time.
 * Logs: February 15th, 2014. Started and finished writing.
 * Logs: February 15th, 2014 used in the RemindingProgram.
 */
