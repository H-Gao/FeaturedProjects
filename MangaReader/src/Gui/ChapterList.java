package Gui;

import java.awt.Container;
import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class ChapterList extends JFrame
{
	int scroll = 0;
	
	static ChapterList cl;
	
	LinkedList<ChapterBundle> chapterBundles = new LinkedList<ChapterBundle>();
	
	public ChapterList(String path)
	{
		cl = this;
		
		this.setTitle("Manga Zombie V.0.1 BETA");
		this.setName("Example");
		this.setSize(304, 408);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(null);
		this.setIconImage(new ImageIcon("C:\\Users\\henry\\OneDrive\\Pictures\\MangaRipper\\Icon.png").getImage());
		this.addMouseWheelListener(new ScrollListener(null));
		
		path = "C:\\Users\\henry\\workspace\\MangaDownloader\\Manga\\" + path;
		
		File[] file = new File(path).listFiles();
		
		Arrays.sort(file, new Comparator<File>() 
		{
            @Override
            public int compare(File o1, File o2) 
            {
            	try
            	{
	                int n1 = Integer.parseInt(o1.getName().substring(3));
	                int n2 = Integer.parseInt(o2.getName().substring(3));
	                return n1 - n2;
            	}
            	catch (Exception e)
            	{
            		e.printStackTrace();
            	}
            	
            	return 0;
            }
		});
		
		int index = 0;
		
		for (int i = 0;i < file.length;++i)
		{
			String chapter = file[i].getAbsolutePath().substring(file[i].getAbsolutePath().lastIndexOf("\\")+1);
			
			System.out.println(chapter);
			
			if (file[i].isDirectory() && chapter.startsWith("Ch."))
			{
				ChapterBundle cb = new ChapterBundle(path, chapter, index, this);
				chapterBundles.add(cb);
				
				++index;
			}
		}
	}
	
	public void remove()
	{
		this.dispose();
	}
	
	public void updateResultPositions()
	{
		for (ChapterBundle c : chapterBundles)
		{
			c.updateResultPositions(scroll);
		}
	}

	public static ChapterList getList() 
	{
		return cl;
	}
}
