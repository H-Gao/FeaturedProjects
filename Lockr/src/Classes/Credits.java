package Classes;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Credits extends JFrame
{
	//A component which is used to display the background IMAGE to make this application look better.
	JLabel background;
	
	//Displays the names of the credits.
	JLabel credits;
	
	public Credits()
	{
		//Creates an empty JFrame
		this.setTitle("Lockr");
		this.setSize(500, 650);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(null);
		this.setUndecorated(true);
		this.setVisible(true);
	}
	
	//Initalizes the components, after the credits have loaded.
	public void init(ControlPanel cp)
	{
		//Displays the credits. It is added first, so it appears on top of the background.
		credits = new JLabel();
		credits.setIcon(new ImageIcon(cp.currentDirectory + "Credits.png"));
				
		credits.setSize(credits.getIcon().getIconWidth(), //Sets the size to be the same as the icon.
				credits.getIcon().getIconHeight());
				
		this.add(credits);
		
		
		//Initalizes the background, purely for asthetics (serves no purpose).
		background = new JLabel();
		background.setIcon(new ImageIcon(cp.currentDirectory + "LockrBackground_3.png"));
				
		background.setSize(background.getIcon().getIconWidth(), //Sets the size to be the same as the icon.
			background.getIcon().getIconHeight());
				
		this.add(background);
		
				
		this.repaint();
	}
}
