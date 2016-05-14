package Client;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JTextField;

public class EMenu extends Menu
{
	JTextField title = new JTextField("Title");
	JMenu section = new JMenu();
	
	JButton information = new JButton("Information");
	JButton checkout = new JButton("Checkout");
	
	public EMenu()
	{
		initComponent((JComponent)title, width/10, 0, width*8/11, height/10, true);
		initComponent((JComponent)section, width*8/11 + width/10, 0, width*2/11, height/10, true);
		
		initComponent((JComponent)information, width/10, height*9/10, width/4, height/10, true);
		initComponent((JComponent)checkout, width/10, height*9/10, width*3/4, height/10, true);
		
		repaint();
		
	}
	
	public void initComponent(JComponent jcompon, int x, int y, int width, int height, boolean isVisible)
	{
		jcompon.setVisible(isVisible);
		jcompon.setBounds(x, y, width, height);
		add(jcompon);
	}
	
	public static void main(String[] args)
	{
		EMenu menu = new EMenu();
	}
}
