package Gui;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Viewer extends JFrame
{
	String start;
	
	int chapter, page;
	
	JLabel prevCover, cover;
	File[] pages = new File[0];
	
	static final int PREV = -1, NEXT = 1;
	
	Image prevImage, image;
	Dimension screenSize;
	
	public Viewer(String s, int chapterStart)
	{
		start = s;
		chapter = chapterStart;
		
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		initViewer();
		initCover();
		loadPage(+1);
		loadPage(+1);
	}
	
	public void initViewer()
	{
		this.setTitle("Manga Viewer");
		this.setName("Manga Viewer");
		this.setSize(100, 100);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public void initCover()
	{
		cover = new JLabel();
		cover.addMouseListener(new ClickPositionListener(this));
		this.add(cover);
		
		prevCover = new JLabel();
		prevCover.addMouseListener(new ClickPositionListener(this));
		this.add(prevCover);
	}
	
	public void loadChapter(int i)
	{
		chapter += i;
		
		pages = new File(start + "\\Ch." + chapter).listFiles();
		
		Arrays.sort(pages, new Comparator<File>(){

			@Override
			public int compare(File arg0, File arg1) 
			{
				int intVal1 = Integer.parseInt(arg0.getName().substring(0, arg0.getName().indexOf("."))), 
						intVal2 = Integer.parseInt(arg1.getName().substring(0, arg1.getName().indexOf(".")));
				
				return intVal1 - intVal2;
			}
			
		});
		
		if (i == NEXT)
		{
			page = 0;
		}
		else
		{
			page = pages.length-1;
		}
	}
	
	public void loadPage(int i)
	{
		page += i;
		
		if (page >= pages.length)
		{
			loadChapter(NEXT);
		}
		else if (page < 0)
		{
			if (chapter > 1)
			{
				loadChapter(PREV);
			}
			else
			{
				page -= i;
			}
		}
		
		updateImage();
	}
	
	public Image getImage(int index) throws IOException
	{
		Image img = ImageIO.read(pages[index]);
		
		if (img.getWidth(null) > screenSize.getWidth())
		{
			img = img.getScaledInstance((int)(screenSize.getWidth()), (int)(img.getHeight(null) * img.getWidth(null)/screenSize.getHeight()), BufferedImage.SCALE_SMOOTH);
		}
		
		if (img.getHeight(null) > screenSize.getHeight())
		{
			img = img.getScaledInstance((int)(img.getWidth(null) * img.getHeight(null)/screenSize.getWidth()), (int)(screenSize.getHeight()), BufferedImage.SCALE_SMOOTH);
		}
		
		return img;
	}
	
	public void updateImage()
	{
		try
		{
			image = getImage(page);
			prevImage = getImage(page-1);
			
			if (prevImage != null)
			{
				prevCover.setIcon(new ImageIcon(prevImage));
				prevCover.setSize(prevCover.getIcon().getIconWidth(), prevCover.getIcon().getIconHeight());
			}
			
			cover.setIcon(new ImageIcon(image));
			cover.setSize(cover.getIcon().getIconWidth(), cover.getIcon().getIconHeight());
			cover.setLocation(prevCover.getWidth(), 0);
			
			this.setSize(cover.getWidth() + prevCover.getWidth(), Math.max(cover.getHeight(), prevCover.getHeight()));
			this.setLocation((int)(screenSize.getWidth() - this.getWidth())/2, (int)(screenSize.getHeight() - this.getHeight())/2);
			
			this.repaint();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
