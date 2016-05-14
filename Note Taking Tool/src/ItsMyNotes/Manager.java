package ItsMyNotes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JButton;

import WindowDevelopmentClassesHenry.SimpleWindow;

public class Manager extends SimpleWindow implements ActionListener
{
	NoteOption createNote;
	NoteOption requestHideFromScreen;
	NoteOption requestExit;
	NoteOption openNotes;
	
	String targetDirectory = "";
	List<ItsMyNotes> notes = new ArrayList<ItsMyNotes>();
	
	int EncryptKey = 1;
	int EncryptKey2 = 3;
	
	JButton addNote;
	JButton help;
	JButton support;
	JButton tutorial;
	
	public Manager() 
	{
		super("It's My Notes.", 400, 500, true);
		
		createNote = new NoteOption(this, "Create New Note", 214, 50, false);
		requestHideFromScreen = new NoteOption(this, "Hide Notes", 214, 50, false);
		requestExit = new NoteOption(this, "Remove Notes", 214, 50, false);
		openNotes = new NoteOption(this, "Open Notes", 214, 50, false);
		
		createNote.setLocation(1160, 5);
		requestHideFromScreen.setLocation(1160, 60);
		requestExit.setLocation(1160, 115);
		openNotes.setLocation(1160, 170);
		
		openNotes();
		
		System.out.println((Runtime.getRuntime().freeMemory()/(1024*1024)) + "MB available memory/" + (Runtime.getRuntime().maxMemory()/(1024*1024))  + "MB total memory.");
}
	
	public void createComponents()
	{
		addNote = new JButton("+ Add Note.");
		addNote.addActionListener(this);
		addNote.setBounds(0, 220, 400, 40);
		add(addNote);
		
		help = new JButton("Help");
		help.addActionListener(this);
		help.setBounds(0, 340, 400, 40);
		add(help);
		
		support = new JButton("Support");
		support.addActionListener(this);
		support.setBounds(0, -100, 400, 40);
		add(support);
		
		tutorial = new JButton("Tutorial");
		tutorial.addActionListener(this);
		tutorial.setBounds(0, -100, 400, 40);
		add(tutorial);
	}
	
	public static void main(String[] args)
	{
		Manager manager = new Manager();
	}
	
	public String searchInput()
	{
		InputPanel inputPanel = new InputPanel();
		
		//Waits until the user inputs a proper input.
		while (inputPanel.isVisible())
		{
			//Updates the while loop, making sure that it will exit.
			repaint();
		}
		
		inputPanel.dispose();
		
		return inputPanel.input;
	}
	
	public void openNotes()
	{
		try
		{
			String exactLocation = System.getProperty("java.class.path");
			String fileLocation = exactLocation.substring(0, exactLocation.indexOf("bin"));
			
			System.out.println("Looking for files in the directory " + fileLocation);
			
			File targetDirectoryData = new File("targetDirectory.txt");
			
			System.out.println("The program is waiting...");
		
			if (!targetDirectoryData.exists())
			{
				PrintWriter writeTargetDirectory = new PrintWriter(targetDirectoryData);
				
				writeTargetDirectory.println(searchInput());
				writeTargetDirectory.flush();
			}
			
			
			System.out.println("The program is done waiting...");
		
			//Searches for the notes.
			File directory = new File(fileLocation + "Notes\\");
		
			System.out.println(new File(fileLocation + "Notes\\Note_2.imn").exists());
		
			for (int i = 1;new File(fileLocation + "Notes\\Note_"+(((i+1)/2))+".imn").exists();i+=2)
			{
					FileReader reader = new FileReader(directory.listFiles()[i]);
					Scanner scanner = new Scanner(reader);
			
					String file_data = scanner.nextLine();
			
					String name = file_data.split(";")[0];
					int x  = Integer.parseInt(file_data.split(";")[1]);
					int y  = Integer.parseInt(file_data.split(";")[2]);
					double versionID  = Double.parseDouble(file_data.split(";")[3]);
					
					int red_colour = Integer.parseInt(file_data.split(";")[4]);
					int green_colour = Integer.parseInt(file_data.split(";")[5]);
					int blue_colour = Integer.parseInt(file_data.split(";")[6]);
					
					int height = Integer.parseInt(file_data.split(";")[7]);
				
					reader = new FileReader(directory.listFiles()[i-1]);
					scanner = new Scanner(reader);
			
					String[] file_text = new String[100];
						
					for (int n = 0;scanner.hasNextLine();++n)
					{
						String lineFromFile = scanner.nextLine();
						file_text[n] = lineFromFile;
					}
				
					ItsMyNotes note = new ItsMyNotes(versionID, name, removeExtension(directory.listFiles()[i-1].toString()), x, y, file_text, red_colour, green_colour, blue_colour);
					note.setSize(350, height);
					notes.add(note);
				
					System.out.println("Opened: " + directory.listFiles()[i-1] + ", " + directory.listFiles()[i]);
			}
			
			System.out.println("Searched for and successfully opened all notes in " + fileLocation);
		}
		catch (FileNotFoundException exception)
		{
			System.out.println("We could not open a file.");
			exception.printStackTrace();
		}
	}
	
	public String removeExtension(String str)
	{
		return str.substring(0, str.lastIndexOf("."));
	}

	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == addNote)
		{
			ItsMyNotes note = new ItsMyNotes();
		}
		else if (e.getSource() == help)
		{
			if (help.getText().equals("Cancel."))
			{
				help.setText("Help");
				support.setLocation(0, -100);
				tutorial.setLocation(0, -100);
				repaint();
			}
			else
			{
				help.setText("Cancel.");
				support.setLocation(0, 380);
				tutorial.setLocation(0, 420);
				repaint();
			}
		}
	}
}
