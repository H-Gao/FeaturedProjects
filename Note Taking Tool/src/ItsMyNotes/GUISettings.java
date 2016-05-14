package ItsMyNotes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import WindowDevelopmentClassesHenry.SimpleWindow;

public class GUISettings extends SimpleWindow implements ActionListener
{
	JButton[] tabs;
	
	public GUISettings() 
	{
		super("Settings", 600, 400, true);
	}
	
	public void onInit()
	{
		String[] tabNames = 
			{
				"File", "Colour", "...", "...", "...",
				"...", "...", "...", "..."
			};
		
		tabs = new JButton[8];
		
		for (int n = 0;n != tabs.length;++n)
		{
			int tabWidth = 68;
			int tabHeight = 20;
			
			tabs[n] = new JButton();
			tabs[n].setText(tabNames[n]);
			tabs[n].setBounds((n%8)* tabWidth, (n/8) * tabHeight, tabWidth, tabHeight);
			add(tabs[n]);
		}
	}
	
	public static void main(String[] args)
	{
		GUISettings guiSettings = new GUISettings();
	}

	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == tabs[0]) {}
		
		else if (e.getSource() == tabs[1]) {}
	}
}
