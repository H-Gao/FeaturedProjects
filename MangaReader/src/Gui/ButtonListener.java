package Gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class ButtonListener implements ActionListener
{
	String action;
	
	public ButtonListener(String action)
	{
		this.action = action;
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		System.out.println("Here...");
		
		if (action.equals("DownloadManga"))
		{
			MangaDownloader md = new MangaDownloader();
		}
		else if (action.equals("Search2"))
		{
			MangaDownloader.getDownloader().doSearch();
		}
		else if (action.equals("GetManga"))
		{
			System.out.println("Here..." + " " + action + " " + e.getSource());
			
			((JButton)e.getSource()).setEnabled(false);
			MangaDownloader.getDownloader().downloadManga(((JButton)e.getSource()).getName());
		}
		else if (action.equals("ScrollLeft"))
		{
			MainFrame mf = MainFrame.getMainFrame();
			
			mf.pointer = (mf.pointer - 1 + mf.files.length)%mf.files.length;
			
			mf.updateCovers();
		}
		else if (action.equals("ScrollRight"))
		{
			MainFrame mf = MainFrame.getMainFrame();
			
			mf.pointer = (mf.pointer + 1 + mf.files.length)%mf.files.length;
			
			mf.updateCovers();
		}
		else if (action.equals("ReadChapter"))
		{
			ChapterList.getList().remove();
			
			String name = ((JButton)e.getSource()).getName();
			
			int chapter = Integer.parseInt(name.substring(name.lastIndexOf("Ch.")+3));
			String path = name.substring(0, name.lastIndexOf('^'));
			
			Viewer viewer = new Viewer(path, chapter);
		}
	}
}
