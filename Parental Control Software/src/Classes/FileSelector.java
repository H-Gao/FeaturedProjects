package Classes;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class FileSelector extends JFrame
{
	//A component which is used to display the background IMAGE to make this application look better.
	JLabel background;
	
	public FileSelector()
	{
		//Creates an empty JFrame
		this.setTitle("Lockr");
		this.setSize(600, 480);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(null);
		this.setUndecorated(true);
		this.setVisible(true);
	}
	
	//Initalizes the components.
	public void init()
	{
		//Initalizes the background, purely for asthetics (serves no purpose).
		background = new JLabel();
		background.setIcon(new ImageIcon("LockrBackground.png"));
		
		background.setSize(background.getIcon().getIconWidth(), //Sets the size to be the same as the icon.
			background.getIcon().getIconHeight());
		
		this.add(background);
		
		this.repaint();
	}
	
	public static void main(String[] args)
	{
		FileSelector f = new FileSelector();
	}
}
