package WindowDevelopmentClassesHenry;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import ItsMyNotes.ItsMyNotes;

public class Done extends JButton implements ActionListener
{
	ItsMyNotes noteLink;
	
	SimpleWindow window;
	SimpleWindow link;
	
	public Done(SimpleWindow window, ItsMyNotes link, int x, int y, int width, int height)
	{
		this.window = window;
		this.link = link;
		
		this.setText("Done");
		
		this.addActionListener(this);
		this.setBounds(x, y, width, height);
		window.add(this);
	}
	
	public Done(SimpleWindow window, ItsMyNotes link, int x, int y, int width, int height, String commands)
	{
		this.window = window;
		this.link = link;
		
		this.setText("Done");
		
		String[] commandList = commands.split(" ");
		
		for (int i = 0;i != commandList.length;++i)
		{
			if (commandList[i].equalsIgnoreCase("setOpaque"))
			{
				boolean isTrue;
				
				++i;
				
				if (commandList[i].equalsIgnoreCase("True"))
					isTrue = true;
				else
					isTrue = false;
				
				this.setOpaque(isTrue);
			}
		}
		
		this.addActionListener(this);
		this.setBounds(x, y, width, height);
		window.add(this);
	}
	
	public Done(SimpleWindow window, SimpleWindow link, int x, int y, int width, int height)
	{
		this.window = window;
		this.link = link;
		
		this.setText("Done");
		
		this.addActionListener(this);
		this.setBounds(x, y, width, height);
		window.add(this);
	}
	
	public Done(SimpleWindow window, SimpleWindow link, int x, int y, int width, int height, String commands)
	{
		this.window = window;
		this.link = link;
		
		this.setText("Done");
		
		String[] commandList = commands.split(" ");
		
		for (int i = 0;i != commandList.length;++i)
		{
			if (commandList[i].equalsIgnoreCase("setOpaque"))
			{
				boolean isTrue;
				
				++i;
				
				if (commandList[i].equalsIgnoreCase("True"))
					isTrue = true;
				else
					isTrue = false;
				
				this.setOpaque(isTrue);
			}
		}
		
		this.addActionListener(this);
		this.setBounds(x, y, width, height);
		window.add(this);
	}

	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == this)
		{
			window.onDone();
		}
	}
}
