package Gui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JTextField;

public class ChapterBundle 
{
	JButton download;
	JTextField txtSearch;
	
	ChapterList chapterList;
	
	int index = 0;
	
	public ChapterBundle(String dir, String name, int index, ChapterList cl)
	{
		chapterList = cl;
		
		this.index = index;
		
		download = new JButton();
		download.setText("Read");
		download.setEnabled(true);
		download.setName(dir + "^" + name);
		download.setSize(82, 35);
		download.setLocation(204, 35*index);
		download.addActionListener(new ButtonListener("ReadChapter"));
		download.addMouseWheelListener(new ScrollListener(null));
		ChapterList.getList().add(download);
		
		txtSearch = new JTextField();
		txtSearch.setHorizontalAlignment(JTextField.CENTER);
		txtSearch.setFont(new Font("Georgia", Font.PLAIN, 14));
		txtSearch.setForeground(Color.LIGHT_GRAY);
		txtSearch.setText(name);
		txtSearch.setOpaque(false);	
		txtSearch.setBorder(null);
		txtSearch.setSize(200, 35);
		txtSearch.setLocation(2, 35*index);
		txtSearch.addMouseWheelListener(new ScrollListener(null));
		ChapterList.getList().add(txtSearch);
		
		ChapterList.getList().repaint();
	}

	public void updateResultPositions(int scroll) 
	{
		download.setLocation(204, 35*index + scroll);
		txtSearch.setLocation(2, 35*index + scroll);
	}
}
