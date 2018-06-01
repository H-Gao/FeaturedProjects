package Gui;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class MangaCard implements MouseListener
{
	JLabel page;
	
	public MangaCard(String img, int x, int y, double scale)
	{
		page = new JLabel();
		page.addMouseListener(this);
		page.setIcon(new ImageIcon(new ImageIcon(img).getImage().getScaledInstance((int)(200 * scale), (int)(310 * scale), BufferedImage.SCALE_SMOOTH)));
		page.setLocation(x+1, y+1);
		page.setSize((int)(200 * scale), (int)(310 * scale));
		page.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		MainFrame.getMainFrame().add(page);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) 
	{
		ChapterList cl = new ChapterList(MainFrame.getBundle().info.dir);
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
}

	@Override
	public void mouseExited(MouseEvent arg0) {
}

	@Override
	public void mousePressed(MouseEvent arg0) {
}

	@Override
	public void mouseReleased(MouseEvent arg0) {
}
}
