package ScrappedFiles;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardCopyOption.*;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import javax.swing.Timer;

import WindowDevelopmentClassesHenry.SimpleWindow;

public class Installer extends SimpleWindow implements ActionListener
{
	//Installer only to be changed via code.
	String hostFolder;
	
	String[] installFiles;
	
	String name;
	int parts = 0;
	int totalParts = 5;
	
	JTextArea text;
	JButton next;
	JButton back;
	
	JFileChooser chooser;
	
	public Installer(String n) throws Exception 
	{
		super(n + " Installer", 450, 600, true);
		name = n;
		doParts(name, parts, totalParts);
	}
	
	public void createComponents()
	{
		text = new JTextArea();
		text.setBounds(10, 325, 415, 150);
		add(text);
		
		next = new JButton();
		next.addActionListener(this);
		next.setBounds(10, 480, 207, 50);
		add(next);
		
		back = new JButton();
		back.addActionListener(this);
		add(back);
		
		repaint();
	}
	
	public void doParts(String name, int parts, int totalParts) throws Exception
	{
		if (parts == 0)
		{
			text.setText("Hello, welcome to the " + name + "Installer. \n"
				+ "You are only " + totalParts + " away from installing " + name + ".");
			
			next.setText("Next");
			
			back.setText("Back");
			back.setLocation(-100, -100);
		}
		
		else if (parts == 1)
		{
			text.setText("Before you download this software, you must \n"
					+ "agree with the end user agreement. \n\n"
					+ "You cannot proceed without agreeing.");
			
			next.setText("Agree");
			
			back.setBounds(218, 480, 207, 50);
			back.setText("Decline");
		}
		else if (parts == 2)
		{
			chooser = new JFileChooser();
			chooser.addActionListener(this);
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setBounds(10, 25, 415, 300);
			add(chooser);
			
			text.setText("Select the folder in which you would \n"
					+ "like to install this file in.");
			
			next.setText("Next");
			next.setLocation(-100, -100);
			
			back.setText("Back");
		}
		else if (parts == 3)
		{
			int progress = 0;
			
			long installedSpace = 0;
			long totalSpace = 0;
			
			installFiles = new String[1];
			installFiles[0] = "Classes";
			
			System.out.println("Calculating total space...");
			
			for (int n = 0;n != installFiles.length;++n)
				totalSpace = new File(installFiles[n]).getTotalSpace();
			
			System.out.println("Total space: " + totalSpace);
				
			chooser.setLocation(-400, -400);
			next.setBounds(10, 480, 207, 50);
			
			for (int n = 0;n != installFiles.length;++n)
			{
				File source = new File(installFiles[n]);
				File destination = new File(hostFolder);
				
				Files.move(source.toPath(), destination.toPath(), REPLACE_EXISTING);
				
				String sourcePath = source.toString();
				String sourceFile = sourcePath.substring(sourcePath.lastIndexOf((char)92), sourcePath.length());
				
				if (new File(destination.toString() + sourceFile).exists())
				{
					text.setText("File could not be installed " + sourceFile);
				}
			}
		}
	}
	
	public static void main(String[] args) throws Exception
	{
		Installer installer = new Installer("It's My Notes");
	}

	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == next) 
		{
			if (parts == totalParts)
				this.dispose();
			else
				++parts;
		}
		else if (e.getSource() == back) 
		{
			--parts;
		}
		else if (e.getSource() == chooser)
		{
			hostFolder = chooser.getSelectedFile().toString();
			++parts;
		}
		
		try
		{
			doParts(name, parts, totalParts);
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
	}
}
