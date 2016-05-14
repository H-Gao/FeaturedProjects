package ItsMyNotes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTextField;

import WindowDevelopmentClassesHenry.Done;
import WindowDevelopmentClassesHenry.SimpleWindow;

public class SizeAdjuster extends SimpleWindow implements ActionListener
{
	ItsMyNotes note;
	
	//The text field that displays the height of the It'sMyNote file.
	JTextField height;
	
	//The text field that increases or decreases the height.
	JButton[] adjustHeight;
	
	Done done;
	
	public SizeAdjuster(ItsMyNotes note) 
	{
		super("Size Adjuster", 200, 100, false, true);
		
		this.note = note;
		
		System.out.println(this.note);
		
		done = new Done(this, this.note, 2, 68, 118, 28);
		
		this.setVisible(true);
	}

	public void onInit()
	{
		this.setUndecorated(true);
		
		height = new JTextField();
		height.setText(""+330);
		height.setBounds(2, 2, 118, 68);
		height.setOpaque(false);
		add(height);
		
		adjustHeight = new JButton[2];
				
		for (int i = 0;i != 2;++i)
		{
			//Sets the adjustion icon to either ^ or v, representing up or down.
			adjustHeight[i] = new JButton(""+(char)(94+(24*i)));
			adjustHeight[i].addActionListener(this);
			adjustHeight[i].setBounds(120, 2+(48*i), 78, 48);
			add(adjustHeight[i]);
		}
	}

	public void actionPerformed(ActionEvent e) 
	{
		try
		{
			//The value of the height field.
			int heightValue = Integer.parseInt(height.getText());
			
			//If the button is the ^ button.
			if (e.getSource() == adjustHeight[0])
			{		
				height.setText(""+(heightValue+1));
			}
			
			//If the button is the v button.
			else if (e.getSource() == adjustHeight[1])
			{
				//If the heightValue is smaller than 0, then it will display the error message, and set the heightValue to 0.
				if (heightValue <= 0)
				{
					heightValue = 0;
					throw new NumberFormatException();
				}
				
				height.setText(""+(heightValue-1));
			}
		}
		catch (NumberFormatException exception)
		{
			System.out.println("The height must be a integer between or equal to 0 and 400.");
		}
	}
	
	public void onDone()
	{
		try
		{
			//The value of the height field.
			int heightValue = Integer.parseInt(height.getText());
			
			//If the heightValue is smaller than 0, then it will display the error message, and set the heightValue to 0.
			if (heightValue < 0)
			{
				heightValue = 0;
				throw new NumberFormatException();
			}
		
			//Sets the height of the note to the height of the height value.
			this.note.setSize(this.note.getWidth(), heightValue + 120);
			
			this.dispose();
		}
		catch (NumberFormatException exception)
		{
			System.out.println("The height must be a integer between or equal to 0 and 400.");
		}
	}
}
