package Classes;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Notice extends JFrame implements ActionListener
{
	//Links to the control panel.
	ControlPanel cp;
	
	//Closes the notice.
	JButton exit;
	
	//The message that is displayed.
	JTextArea content;
	
	//The background, which displays the type of message, and the icon.
	JLabel background;
	
	public Notice(ControlPanel c, double r, String content, int id)
	{
		cp = c;
		
		//Creates an empty JFrame
		this.setTitle("Lockr");
		this.setSize(450, 300);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(null);
		this.setUndecorated(true);
		this.setVisible(true);
		
		init(r, content, id);
	}
	
	public void init(double r, String c, int i)
	{
		exit = new JButton();
		exit.setIcon(new ImageIcon(cp.currentDirectory + "ExitButton.png"));
		exit.setSize(exit.getIcon().getIconWidth(), exit.getIcon().getIconHeight());
		exit.setBorder(null);
		exit.setLocation((int)(450*r-exit.getWidth()), 0);
		exit.addActionListener(this);
		this.add(exit);
		
		content = new JTextArea(c);
		content.setSize((int)(450*r/1.5), (int)(300*r/1.23));
		content.setBorder(null);
		content.setLocation((int)(450*r/2.9), (int)(300*r/5));
		content.setFont(new Font("TimesNewRoman", Font.ITALIC, 14));
		content.setText(adjustString(content.getText(), content.getFont(), content.getWidth(), 5));
		content.setEditable(false);
		this.add(content);
		
		background = new JLabel();
		background.setIcon(new ImageIcon(cp.currentDirectory + "Notice.png"));
		background.setSize(background.getIcon().getIconWidth(), background.getIcon().getIconHeight());
		this.add(background);
		
		this.repaint();
	}
	
	public String adjustString(String input, Font f, int maxWidth, int padding)
	{
		//The output.
		String output = "";
		
		//The class that determines the character's length based on the font that is used.
		FontMetrics fm = this.getGraphics().getFontMetrics(f);
		
		int currentLength = 0; //The current length.
		char[] in = input.toCharArray(); //Turns it into a char array, for faster returning/ease of use.
		
		//Does this for the entire string.
		for (int i = 0;i < in.length;++i)
		{
			//Updates the currentLength to see if it crosses the maximum width.
			currentLength += fm.stringWidth(""+in[i]);
			
			//If it crosses the maxWidth, then it jumps to a new line.
			if (currentLength > maxWidth-padding)
			{
				output += "\n";
				currentLength = 0;
			}
			
			output += in[i]; //Adds the new character.
		}
		
		return output;
	}

	public void actionPerformed(ActionEvent e) 
	{
		this.dispose();
	}
	
}
