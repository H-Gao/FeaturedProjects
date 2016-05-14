package ItsMyNotes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTextField;

import WindowDevelopmentClassesHenry.SimpleWindow;

public class InputPanel extends SimpleWindow implements ActionListener
{
	String input = "";
	
	JTextField in;
	JButton submit;
	
	public InputPanel() 
	{
		super("Input Panel", 400, 160, true);
	} 
	
	//Creates the components, and such.
	public void onInit()
	{
		//Creates the text field.
		in = new JTextField();
		in.setBounds(0, 0, 400, 70);
		add(in);
		
		//Creates a submit button.
		submit = new JButton("Submit");
		submit.addActionListener(this);
		submit.setBounds(100, 75, 300, 60);
		add(submit);
		
		//Updates the components to make sure everything shows.
		repaint();
	}
	
	public static void main(String[] args)
	{
		InputPanel inputPanel = new InputPanel();
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		//If the submit button is pressed, it will close the window and set the Input to the text of the text field.
		//This allows the manager to detect it and will countinue.
		if (e.getSource() == submit)
		{
			input = in.getText();
			
			//If the input is not a It'sMyNotes folder, it will not accept it.
			if (!input.contains("It'sMyNotes") || input.lastIndexOf("It'sMyNotes") != (input.length()-11))
			{
				System.out.println("You must select the main It'sMyNotes folder.");
			}
			else
			{
				this.setVisible(false);
			}
		}
	}
}
