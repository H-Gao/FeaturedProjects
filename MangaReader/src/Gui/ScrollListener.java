package Gui;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.LinkedList;

public class ScrollListener implements MouseWheelListener
{
	MangaDownloader md;
	
	public ScrollListener(MangaDownloader md)
	{
		this.md = md;
	}
	
	public void mouseWheelMoved(MouseWheelEvent arg0) 
	{
		if (md == null)
		{
			ChapterList.getList().scroll += 14 * arg0.getWheelRotation();
			
			ChapterList.getList().updateResultPositions();
		}
		else
		{
			md.scroll += 2 * arg0.getWheelRotation();
			
			md.updateResultPositions();
		}
	}
}
