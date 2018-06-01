package Gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ClickPositionListener implements MouseListener
{
	Viewer viewer;
	
	public ClickPositionListener(Viewer v)
	{
		viewer = v;
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) 
	{
		if (arg0.getSource() == viewer.cover)
		{
			viewer.loadPage(+1);
		}
		else
		{
			viewer.loadPage(-1);
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {}

}
