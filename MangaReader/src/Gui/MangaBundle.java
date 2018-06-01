package Gui;

import java.awt.Color;
import java.awt.Font;
import java.io.File;

import javax.swing.JTextField;

public class MangaBundle 
{
	double scale = 1.0d;
	
	MangaInfoBundle info;
	
	MangaCard mc;
	JTextField txtName, txtChapters;
	
	public MangaBundle(MangaInfoBundle info, String img, int x, int y, double scale)
	{
		this.info = info;
		this.scale = scale;
		
		String workingPath;
		
		if (new File(img + "\\Cover.png").exists())
		{
			workingPath = img + "\\Cover.png";
		}
		else if (new File(img + "\\Cover.jpg").exists())
		{
			workingPath = img + "\\Cover.jpg";
		}
		else
		{
			workingPath = img + "\\Cover.jpeg";
		}
		
		mc = new MangaCard(workingPath, x, y, scale);
		
		txtName = MainFrame.createInvisTextField(x, y-(int)(31 * scale), (int)(200 * scale), (int)(20 * scale), info.name, true, Color.BLACK);
		txtName.setFont(new Font("Georgia", Font.PLAIN, (int)(15 * Math.pow(scale, 0.65))));
		
		txtChapters = MainFrame.createInvisTextField(x, y+(int)(328 * scale), (int)(200 * scale), (int)(25 * scale), "Ch. " + info.currentChapter + "/" + info.lastChapter, true, Color.BLACK);//new Color(87, 150, 181));
		txtChapters.setFont(new Font("Georgia", Font.PLAIN, (int)(11 * Math.pow(scale, 0.25))));
		
		if (scale == 1.0d)
		{
			txtChapters.setForeground(new Color(87, 150, 181));
		}
	}
	
	public void remove()
	{
		MainFrame.getMainFrame().remove(mc.page);
		MainFrame.getMainFrame().remove(txtName);
		MainFrame.getMainFrame().remove(txtChapters);
	}
}
