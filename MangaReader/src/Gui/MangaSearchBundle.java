package Gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextField;

public class MangaSearchBundle 
{
	
	JButton btnSearch, download;
	JTextField txtSearch;
	
	public MangaSearchBundle(MangaDownloader md, int index, BufferedImage bi, String name, String urlName)
	{
		btnSearch = new JButton();
		btnSearch.setBorderPainted(false);
		btnSearch.setFocusPainted(false);
		btnSearch.setContentAreaFilled(false);
		btnSearch.setIcon(new ImageIcon(bi));
		btnSearch.setSize(btnSearch.getIcon().getIconWidth(), btnSearch.getIcon().getIconHeight());
		btnSearch.setLocation(2, 50 + 310*index);
		btnSearch.addMouseWheelListener(new ScrollListener(md));
		MangaDownloader.getDownloader().add(btnSearch);
		
		download = new JButton();
		//download.setBorderPainted(false);
		//download.setFocusPainted(false);
		//download.setContentAreaFilled(false);
		download.setText("Download");
		download.setName(urlName);
		download.setEnabled(true);
		download.setSize(202, 155);
		download.setLocation(202, 50 + 155 + 310*index);
		download.addActionListener(new ButtonListener("GetManga"));
		download.addMouseWheelListener(new ScrollListener(md));
		MangaDownloader.getDownloader().add(download);
		
		txtSearch = new JTextField();
		txtSearch.setHorizontalAlignment(JTextField.CENTER);
		txtSearch.setFont(new Font("Georgia", Font.PLAIN, 14));
		txtSearch.setForeground(Color.LIGHT_GRAY);
		txtSearch.setText(name);
		txtSearch.setOpaque(false);	
		txtSearch.setBorder(null);
		txtSearch.setSize(200, 155);
		txtSearch.setLocation(202, 50 + 310*index);
		txtSearch.addMouseWheelListener(new ScrollListener(md));
		MangaDownloader.getDownloader().add(txtSearch);
	}
	
	public JButton getButton()
	{
		return btnSearch;
	}
	
	public JTextField getTextField()
	{
		return txtSearch;
	}

	public JButton getDownload() 
	{
		return download;
	}
}
