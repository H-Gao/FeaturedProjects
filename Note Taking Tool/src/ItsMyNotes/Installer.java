package ItsMyNotes;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import WindowDevelopmentClassesHenry.SimpleWindow;

public class Installer extends SimpleWindow implements ActionListener
{
	List<File> out = new ArrayList<File>();
	
	int progress = 0;
	
	JTextField statusBar;
	
	JFileChooser chooser;
	
	JTextArea output;
	
	JButton next;
	JButton back;
	
	public Installer() 
	{
		super("Installer", 450, 585, true);
	}
	
	public void onInit()
	{
		chooser = new JFileChooser();
		statusBar = new JTextField();
		
		output = new JTextArea();
		output.setEditable(false);
		output.setBounds(0, 0, 450, 350);
		add(output);
		
		back = new JButton("Back");
		back.addActionListener(this);
		back.setBounds(0, 400, 225, 100);
		add(back);
		
		next = new JButton("Next");
		next.addActionListener(this);
		next.setBounds(225, 400, 225, 100);
		add(next);
		
		welcomeStage();
		
		repaint();
	}
	
	public static void main(String[] args)
	{
		Installer installer = new Installer();
	}
	
	public void welcomeStage()
	{
		output.setText("Welcome to the installer to countinue, click next.");
		
		back.setVisible(false);
	}
	
	public void introStage()
	{
		output.setText("Click next to countinue\n\n"
				+ "Introduction:\n"
				+ "It's My Notes is software that creates note on your computer\n"
				+ "The notes have many other features...\n"
				+ "^Create encrypted note files that cannot be opened and read by anyone.\n"
				+ "^Create reminders which opens after a specific amount of time.\n"
				+ "^Change each note's colour to match the subject of the note.\n"
				+ "^Automatically open each note on startup.\n"
				+ "^Works on all Operating Systems.\n");
		
		back.setVisible(false);
	}
	
	public void agreementStage()
	{
		try
		{
			//Creates a scanner object that can read the End User Agreement
			FileReader reader = new FileReader("End User Agreement.txt");
			Scanner scanner = new Scanner(reader);
			
			//Sets the text of the output.
			output.setText("Click next to countinue\n\n"
				+ "Before you are able to install the software, you must agree to\n"
				+ "the terms and conditions.\n");
			
			//Puts the contents of the end user agreement into the output area.
			if (scanner.hasNextLine())
			{
				output.setText(output.getText() + "\n" + scanner.nextLine());
				
				while (scanner.hasNextLine())
					output.setText(output.getText() + "\n" + scanner.nextLine());
			}
		
			back.setVisible(true);
			output.setVisible(true);
			next.setVisible(true);
			
			statusBar.setVisible(false);
			chooser.setVisible(false);
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
	}
	
	public void selectLocationStage()
	{
		statusBar.setBounds(0, 505, 450, 40);
		add(statusBar);
		
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.addActionListener(this);
		chooser.setBounds(0, 0, 435, 350);
		add(chooser);
		
		repaint();
		
		statusBar.setVisible(true);
		chooser.setVisible(true);
		output.setVisible(false);
		next.setVisible(false);
	}
	
	public void finishStage()
	{
		output.setText("You are finished the installation process.\n"
				+ "You may click X to exit out of the installation process.");
		output.setVisible(true);
		chooser.setVisible(false);
	}

	public void actionPerformed(ActionEvent e) 
	{
		//If the source is the next or back button, it will increase or decrease the progress.
		//It will then determine which stage to run depending on the progress.
		if (e.getSource() == next || e.getSource() == back)
		{
			if (e.getSource() == next && progress != 3)
				++progress;
			else
				--progress;
			
			if (progress == 1)
			{
				introStage();
			}
			
			if (progress == 2)
			{
				agreementStage();
			}
			
			if (progress == 3)
			{
				selectLocationStage();
			}
			
			if (progress == 4)
			{
				finishStage();
			}
		}
		//If it is the File Chooser...
		else
		{
			try 
			{
				chooser.getSelectedFile().setWritable(true);
				
				toUser("Chosen Directory: " + chooser.getSelectedFile().toPath());
				
				transfer();
				
				toUser("Successfully copied all files.");
					
				++progress;
				finishStage();
			} 
			catch (Exception exception) 
			{
				exception.printStackTrace();
				System.out.println("We could not complete the installation process.");
			}
		}
	}
	
	public void findFiles(File file)
	{
		//If the file is a directory, it will look for more files inside itself.
		if (file.isDirectory())
		{
			//Lists the files that is within itself.
			File[] files = file.listFiles();
		
			if (files.length > 0)
			{
				out.add(files[0]);
				
				//Converts the File[] into ArrayList<File>.
				for (int i = 0;i != files.length;++i)
					out.add(files[i]);
			}
			
			//It will find the files inside itself.
			for (int i = 0;i != files.length;++i)
				if (files[i].isDirectory())
					findFiles(files[i]);
		}
	}
	
	public String[] returnFoundFiles()
	{
		//The length of the data.
		int length = out.size();
		
		//The output.
		String[] output = new String[length+1];
		
		output[0] = "It'sMyNotes";
		System.out.println(output[0]);
		
		//Converts the ArrayList into String[].
		for (int i = 1;i != length;++i)
		{
			String path = out.get(i).getPath();
			
			output[i] = path.substring(path.indexOf("It'sMyNotes"), path.lastIndexOf(out.get(i).getName())) + " " + out.get(i).getName();
			System.out.println(output[i]);
		}
		
		return output;
	}
	
	public void transfer() throws Exception
	{
		String targetDirectory = chooser.getSelectedFile().toString();
			
		String directory = System.getProperty("java.class.path") + "\\It'sMyNotes";
		findFiles(new File(directory));
		
		String[] filesToCopy = returnFoundFiles();
			
		//If it does not end with a \, then the program will add a \.
		if (chooser.getSelectedFile().toString().charAt(chooser.getSelectedFile().toString().length()-1) != '\\')
		{
			targetDirectory += "\\";
		}
		
		
		//Creates the main directory.
		File mainDirectory = new File(targetDirectory + "\\It'sMyNotes");
		mainDirectory.mkdir();
			
		for (int i = 1;i != filesToCopy.length-1;++i)
		{
			//The file's name.
			String fileName = filesToCopy[i];
			//The location of the directory within the directory chosen by the file chooser.
			String directoryLocation = "";
				
			//If the file contains " ", it means that it has a directory as well. In that case, 
			//it will seperate the directory and the file name.
			if (filesToCopy[i].contains(" "))
			{
				//The directory is found in the first part.
				   directoryLocation = filesToCopy[i].split(" ")[0];
				    
				//The name of the file is found in the second part.
				fileName = filesToCopy[i].split(" ")[1];
			}
				
			//The amount of files copied.
			double percentFinished = 100*(double)i/(filesToCopy.length-1);
				
			//Tells the user that we are attempting to copy the file.
			toUser("Attempting to copy " + fileName + " " + toPercentage(percentFinished, 2));
			System.out.println(percentFinished);
			System.out.println(Runtime.getRuntime().freeMemory()/(8*1024) + "KB left");
				
			//If it is a file.
			if (filesToCopy[i].contains(".") && filesToCopy[i].lastIndexOf(".") != filesToCopy[i].length())
			{
				//Copies the file in the selected directory.
				Files.copy(Paths.get(System.getProperty("java.class.path") + "\\" + directoryLocation.substring(directoryLocation.lastIndexOf("It'sMyNotes"), directoryLocation.length()) + fileName), Paths.get(targetDirectory + directoryLocation.substring(directoryLocation.lastIndexOf("It'sMyNotes"), directoryLocation.length()) + fileName), StandardCopyOption.REPLACE_EXISTING);
			}
			//If it is a directory
			else
			{
				//Creates a file object.
				File file = new File(targetDirectory + directoryLocation.substring(directoryLocation.lastIndexOf("It'sMyNotes"), directoryLocation.length()) + fileName);
				System.out.println(file.getAbsolutePath());
				
				//Creates the directory.
				file.mkdir();
			}
				
			//Tells the user that the copying process had worked.
			toUser("Successfully copied " + fileName);
		}
			
		//Increases the progress here, so it only moves on if it works successfully.
		++progress;
	}
	
	public void toUser(String str)
	{
		statusBar.setText(str);
		System.out.println(str);
	}
	
	public String toPercentage(double in, int decimalPlace)
	{
		//Creates the string version of in.
		String out = ""+in;
		
		int decimalPlaces = (out.length() - out.lastIndexOf(".")-1);
		
		//If it has a decimal place longer than 1, then it will be cut to the certainNumber of decimalPlaces.
		//Ex. 28.28484838943 (decimalPlace == 2) == 28.28.
		if (decimalPlaces > 1)
			out = out.substring(0, out.lastIndexOf(".")+(decimalPlace+1));
		//If it has only one decimal place and the last decimal place not zero, it will cut out the decimal places.
		//Ex. 28.0 == 28.
		else if (decimalPlaces == 1 && out.charAt(out.lastIndexOf(".")+1) == '0')
			out = out.substring(0, out.lastIndexOf("."));
		
		return out + "%";
	}
	
	//Returns the name of the files that will be copied and their directories.
	public String[] returnCopiedFiles()
	{
		String[] copiedFiles = 
		{
				//Creates the main directories.
				"It'sMyNotes", "It'sMyNotes\\ Images", "It'sMyNotes\\ Changelog", "It'sMyNotes\\ Classes",
				 "It'sMyNotes\\ Notes", "It'sMyNotes\\ Reminders", "It'sMyNotes\\ Settings", 
				//Creates the secondary directories.
				 "It'sMyNotes\\Classes\\ It'sMyNotes", "It'sMyNotes\\Classes\\ WindowsDevelopmentClassesHenry",
				 "It'sMyNotes\\Images\\ Background", "It'sMyNotes\\Images\\ Colour", "It'sMyNotes\\Images\\ Commands",
				 "It'sMyNotes\\Images\\ StartScreen", "It'sMyNotes\\Images\\ TopBar", "It'sMyNotes\\Images\\ Tutorial",
				 
				 //Creates the files.
				 "It'sMyNotes\\ hello.txt", "It'sMyNotes\\Images\\Background\\ Background.png"
		};
		
		return copiedFiles;
	}
	
	//Replaced by the transfer method.
	public void copy(File file)
	{
		System.out.println("Attempting to copy " + file.toString());
		System.out.println(new File(chooser.getSelectedFile() + "\\" + file.toString()).toString());
		
		try
		{
			if (file.isDirectory())
			{
				if (chooser.getSelectedFile().toString().charAt(chooser.getSelectedFile().toString().length()-1) == '\\')
				{
					Runtime.getRuntime().exec("xcopy " + file.toString() + " " + chooser.getSelectedFile() + " /E");
				}
				else
				{
					File fileLocation = new File("tempFile");
					
					System.out.println(fileLocation.getAbsolutePath().substring(0, fileLocation.getAbsolutePath().lastIndexOf("tempFile")));
					Runtime.getRuntime().exec("cd " + fileLocation.getAbsolutePath().substring(0, fileLocation.getAbsolutePath().lastIndexOf("tempFile")-1));
					Runtime.getRuntime().exec("xcopy " + file.toString() + " " + chooser.getSelectedFile() + "\\" + " /E");
				}
			}
			else
			{
				FileReader reader = new FileReader(file);
				Scanner scanner = new Scanner(reader);
				
				PrintWriter writer;
			
				if (chooser.getSelectedFile().toString().charAt(chooser.getSelectedFile().toString().length()-1) == '\\')
				{
					Runtime.getRuntime().exec("copy ");
					writer = new PrintWriter(chooser.getSelectedFile() + file.toString());
				}
				else
				{
					writer = new PrintWriter(chooser.getSelectedFile() + "\\" + file.toString());
				}
			
				if (scanner.hasNextLine())
				{
					writer.println(scanner.nextLine());
				
					while(scanner.hasNextLine())
						writer.println(scanner.nextLine());
				}
			
				writer.close();
				scanner.close();
			}
			
			System.out.println("Successfully copied " + file.toString());
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
			System.out.println("We could not copy " + file.toString());
		}
	}
}
