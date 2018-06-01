package snake;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Block 
{
	JLabel lblBlock;
	String state;
	
	public Block(GUI gui, int x, int y)
	{
		lblBlock = new JLabel();
		
		setOff();
		
		lblBlock.setSize(40, 40);
		lblBlock.setLocation(x, y);
		gui.add(lblBlock);
	}
	
	private void updateImage()
	{
		lblBlock.setIcon(new ImageIcon("Block_" + state + ".png"));
	}
	
	public void setOn()
	{
		state = "On";
		updateImage();
	}
	
	public void setOff()
	{
		state = "Off";
		updateImage();
	}
	
	public void setCoin()
	{
		state = "Coin";
		updateImage();
	}
}
