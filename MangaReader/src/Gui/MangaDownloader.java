package Gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import Extractor.MangaInfo;
import Extractor.MangaSearch;

public class MangaDownloader extends JFrame
{
	LinkedList<MangaSearchBundle> searchResults;
	
	JButton btnSearch;
	JTextField txtSearch;
	
	static MangaDownloader md;
	
	public MangaDownloader()
	{
		md = this;
		
		this.setTitle("Manga Zombie V.0.1 BETA");
		this.setName("Example");
		this.setSize(600, 908);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(null);
		this.setIconImage(new ImageIcon("C:\\Users\\henry\\OneDrive\\Pictures\\MangaRipper\\Icon.png").getImage());
		this.addMouseWheelListener(new ScrollListener(this));
		
		btnSearch = new JButton();
		btnSearch.setBorderPainted(false);
		btnSearch.setFocusPainted(false);
		btnSearch.setContentAreaFilled(false);
		btnSearch.setIcon(new ImageIcon(MainFrame.getPath() + "Search.png"));
		btnSearch.setSize(btnSearch.getIcon().getIconWidth(), btnSearch.getIcon().getIconHeight());
		btnSearch.setLocation(547, 1);
		btnSearch.addActionListener(new ButtonListener("Search2"));
		this.add(btnSearch);
		
		txtSearch = new JTextField();
		txtSearch.setHorizontalAlignment(JTextField.CENTER);
		txtSearch.setFont(new Font("Georgia", Font.PLAIN, 14));
		txtSearch.setForeground(Color.LIGHT_GRAY);
		txtSearch.setText("Search Here");
		txtSearch.setOpaque(false);	
		txtSearch.setBorder(null);
		txtSearch.setSize(545, 35);
		txtSearch.setLocation(2, 1);
		this.add(txtSearch);
		
		JButton background = new JButton();
		background.setBorderPainted(false);
		background.setFocusPainted(false);
		background.setContentAreaFilled(false);
		background.setIcon(new ImageIcon(MainFrame.getPath() + "Background2.png"));
		background.setSize(background.getIcon().getIconWidth(), 38);
		background.setLocation(0, 0);
		this.add(background);
		
		searchResults = new LinkedList<MangaSearchBundle>();
		
		this.repaint();
	}
	
	public LinkedList<MangaSearchBundle> getResults()
	{
		return searchResults;
	}
	
	int scroll = 0;
	
	public void updateResultPositions()
	{
		int index = 0;
		
		for (MangaSearchBundle s : searchResults)
		{
			s.getButton().setLocation(2, 50 + 310*index + scroll);
			s.getTextField().setLocation(202, 50 + 310*index + scroll);
			s.getDownload().setLocation(202, 50 + 155 + 310*index + scroll);
			++index;
		}
	}
	
	public void doSearch()
	{
		for (MangaSearchBundle r : searchResults)
		{
			this.remove(r.getButton());
			this.remove(r.getTextField());
		}
		
		searchResults.clear();
		
		System.out.println("****" + txtSearch.getText());
		
		MangaSearch mangaSearch = new MangaSearch(txtSearch.getText(), this);
	}
	
	public void addResult(BufferedImage bi, String name, String urlName)
	{
		searchResults.add(new MangaSearchBundle(this, searchResults.size(), bi, name, urlName));
		updateResultPositions();
		
		this.repaint();
	}
	
	public static MangaDownloader getDownloader()
	{
		return md;
	}

	public void downloadManga(String name) 
	{
		System.out.println("Starting download for " + name);
		MangaInfo mangaInfo = new MangaInfo(name);
	}
}
