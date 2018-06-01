package Gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MainFrame extends JFrame
{
	JButton btnSettings, btnScrollLeft, btnScrollRight, btnDownloadManga, btnDownloadList, btnFavourite, btnUpdateManga, btnSearch;
	JTextField txtName, txtSearch, txtStatus, txtGenre, txtAuthour, txtStatusInfo, txtGenreInfo, txtAuthourInfo;
	JTextArea txtArDescription;
	
	 //MangaInfoBundle[] mangBundle = new MangaInfoBundle[3]; //Contains enlarge/shrunk functions
	
	static MainFrame mf;
	
	public MainFrame()
	{
		mf = this;
		
		this.setTitle("Manga Zombie V.0.1 BETA");
		this.setName("Example");
		this.setSize(600, 908);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(null);
		this.setIconImage(new ImageIcon("C:\\Users\\henry\\OneDrive\\Pictures\\MangaRipper\\Icon.png").getImage());
		
		createTextFields();
		createImageButtons();
		createBackground();
		
		loadManga();
		
		updateCovers();
		
		this.repaint();
	}
	
	public static MainFrame getMainFrame()
	{
		return mf;
	}
	
	public File[] files;
	
	static int pointer = 0;
	
	public void loadManga()
	{
		String root = new File("").getAbsolutePath();
		
		File file = new File(root + "\\Manga\\");
		
		files = file.listFiles();
	}
	
	public void updateBackground()
	{
		this.remove(background);
		createBackground();
	}
	
	static MangaBundle[] mangaBundles = new MangaBundle[3];
	
	public void updateCovers()
	{
		if (files.length == 0)
		{
			return;
		}
		
		for (int i = 0;i < mangaBundles.length;++i)
		{
			MangaBundle mangaBundle = mangaBundles[i];
			
			if (mangaBundle != null)
			{
				mangaBundle.remove();
			}
		}
		
		int left = (pointer-1+files.length)%files.length, right = (pointer+1)%files.length;
		
		System.out.println(pointer + " " + left + " " + right);
		
		if (files.length == 1)
		{
			left = -1; right = -1;
		}
		else if (files.length == 2)
		{
			left = -1;
		}
		
		try 
		{
			MangaInfoBundle mangaInfoBundle = getMangaInfo(files[pointer].getAbsolutePath());
			
			txtName.setText(mangaInfoBundle.name);
			txtStatusInfo.setText(mangaInfoBundle.status);
			txtGenreInfo.setText(mangaInfoBundle.genre);
			txtAuthourInfo.setText(mangaInfoBundle.author);
			txtArDescription.setText(mangaInfoBundle.description);
			
			mangaBundles[0] = new MangaBundle(mangaInfoBundle, files[pointer].getAbsolutePath(), 185, 185, 1.0d);
			
			if (left != -1)
			{
				mangaBundles[1]  = new MangaBundle(getMangaInfo(files[left].getAbsolutePath()), files[left].getAbsolutePath(), 48, 269, 0.5d);
			}
			
			if (right != -1)
			{
				mangaBundles[2]  = new MangaBundle(getMangaInfo(files[right].getAbsolutePath()), files[right].getAbsolutePath(), 420, 269, 0.5d);
			}
			
			this.updateBackground();
			this.repaint();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public static MangaBundle getBundle()
	{
		return mangaBundles[0];
	}
	
	public MangaInfoBundle getMangaInfo(String path)
	{
		try
		{
			String url, name, status, author, genre, description;
			int lastChapter, currentChapter;
			
			BufferedReader bin = new BufferedReader(new FileReader(path + "\\Manga-Info.txt"));
			
			url = bin.readLine(); //Implement autoupdates.
			name = bin.readLine();
			status = bin.readLine();
			author = bin.readLine();
			
			genre = "";
			
			String input;
			while (!(input = bin.readLine()).equals("*"))
			{
				genre += input + " ";
			}
			
			author += ", " + bin.readLine();
			description = bin.readLine();
			
			lastChapter = Integer.parseInt(bin.readLine());
			
			//BufferedReader bin2 = new BufferedReader(new FileReader(files[pointer].getAbsolutePath() + "\\Manga-Count.txt"));
			
			File[] fi = new File(path).listFiles();
			
			int largest = 0;
			
			for (File f : fi)
			{
				String p = f.getAbsolutePath();
				
				if ((p = p.substring(p.lastIndexOf("\\")+1)).startsWith("Ch."))
				{
					int chapter = Integer.parseInt(p.substring(3));
					
					largest = Math.max(largest, chapter);
				}
			}
			
			currentChapter = largest;
			
			bin.close();
			
			return new MangaInfoBundle(url, name, status, author, genre, description, lastChapter, currentChapter);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void createImageButtons()
	{
		JButton[] buttons = new JButton[8];
		String[] imgs = { "Setting", "ScrollLeft", "ScrollRight", "DownloadManga", "DownloadList", "Favourite", "UpdateManga", "Search" };
		int[] location = { 12, 14,   79, 464,   438, 464,   16, 767,   301, 767,   387, 155,   374, 515,   547, 84 };
		
		for (int i = 0;i < buttons.length;++i)
		{
			buttons[i] = createImageButton(imgs[i], location[2 * i], location[2 * i + 1]);
		}
		
		mapButtons(buttons);
	}
	
	public void mapButtons(JButton[] buttons)
	{
		btnSettings = buttons[0];
		btnScrollLeft = buttons[1]; 
		btnScrollRight = buttons[2]; 
		btnDownloadManga = buttons[3]; 
		btnDownloadList = buttons[4]; 
		btnFavourite = buttons[5]; 
		btnUpdateManga = buttons[6]; 
		btnSearch = buttons[7];
	}
	
	JButton background;
	
	public void createBackground()
	{
		background = createImageButton("background", 0, 0);
	}
	
	public static JButton createImageButton(String img, int x, int y)
	{
		JButton temp = new JButton();
		temp.setBorderPainted(false);
		temp.setFocusPainted(false);
		temp.setContentAreaFilled(false);
		temp.setIcon(new ImageIcon(getPath() + img + ".png"));
		temp.setSize(temp.getIcon().getIconWidth(), temp.getIcon().getIconHeight());
		temp.setLocation(x, y);
		temp.addActionListener(new ButtonListener(img));
		getMainFrame().add(temp);
		
		return temp;
	}
	
	public void createTextFields()
	{
		JTextField[] textFields = new JTextField[8];
		int[] loc = { 46, 17, 2, 85,   10, 575,   9, 597,   322, 574,   59, 573,   57, 594,   378, 573 };
		int[] size = { 475, 49, 545, 33,   48, 11,   46, 11,   52, 12,    80, 17,   300, 17,   205, 14 };
		String[] defaultStr = { "Loading...", "Search here", "Status:", "Genre:", "Author:", "Loading...", "Loading...", "Loading..." };
		boolean[] isCentered = { true, true, false, false, false, false, false, false };
		Color[] color = { Color.BLACK, Color.LIGHT_GRAY, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE };
		
		for (int i = 0;i < textFields.length;++i)
		{
			textFields[i] = createInvisTextField(loc[i * 2], loc[i * 2 + 1], size[i * 2], size[i * 2 + 1], defaultStr[i], isCentered[i], color[i]);
		}
		
		
		mapTextFields(textFields);
		
		txtName.setFont(new Font("Georgia", Font.PLAIN, 22));
		
		txtArDescription = new JTextArea();
		txtArDescription.setFont(new Font("Georgia", Font.PLAIN, 10));
		txtArDescription.setForeground(Color.WHITE);
		txtArDescription.setText("description loading...");
		txtArDescription.setOpaque(false);	
		txtArDescription.setBorder(null);
		txtArDescription.setSize(567, 100);
		txtArDescription.setLocation(5, 630);
		txtArDescription.setLineWrap(true);
		txtArDescription.setWrapStyleWord(true);
		this.add(txtArDescription);
	}
	
	public void mapTextFields(JTextField[] textFields)
	{
		txtName = textFields[0];
		txtSearch = textFields[1];
		txtStatus = textFields[2];
		txtGenre = textFields[3];
		txtAuthour = textFields[4]; 
		txtStatusInfo = textFields[5]; 
		txtGenreInfo = textFields[6]; 
		txtAuthourInfo = textFields[7]; 
	}

	public static JTextField createInvisTextField(int x, int y, int width, int height, String str, boolean isCentered, Color color)
	{
		JTextField temp = new JTextField();
		
		if (isCentered)
		{
			temp.setHorizontalAlignment(JTextField.CENTER);
		}
		
		temp.setFont(new Font("Georgia", Font.PLAIN, 14));
		temp.setForeground(color);
		temp.setText(str);
		temp.setOpaque(false);	
		temp.setBorder(null);
		temp.setSize(width, height);
		temp.setLocation(x, y);
		getMainFrame().add(temp);
		
		return temp;
	}
	
	public static String getPath()
	{
		return "C:\\Users\\henry\\OneDrive\\Pictures\\MangaRipper\\";//new File("").getAbsolutePath();
	}
	
	public static void main(String args[])
	{
		MainFrame mainFrame = new MainFrame();
	}
}
