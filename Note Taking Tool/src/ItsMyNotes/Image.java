package ItsMyNotes;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import WindowDevelopmentClassesHenry.SimpleWindow;

public class Image extends SimpleWindow
{
	public Image(String name) 
	{
		super(name, 10, 10, true);
		
		ImageIcon image = new ImageIcon(name);
		
		JLabel images = new JLabel(new ImageIcon(name));
		images.setBounds(0, 0, image.getIconWidth(), image.getIconHeight());
		add(images);
		
		this.setSize(image.getIconWidth(), image.getIconHeight());
	}
}
