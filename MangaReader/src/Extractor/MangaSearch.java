package Extractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;

import Gui.MangaDownloader;

public class MangaSearch extends Thread implements Runnable
{
	static boolean isDone = false;
	
	String mangaName;
	MangaDownloader md;
	
	public MangaSearch(String mangaName, MangaDownloader mangaDownloader)
	{
		this.mangaName = mangaName;
		md = mangaDownloader;
		
		this.start();
	}
	
	/*public static void main(String args[])
	{
		MangaSearch ms = new MangaSearch("", null);
	}*/
	
	@Override
	public void run()
	{
		try
		{
			mangaName = mangaName.replaceAll(" ", "+");//"yuragi-sou-no-yuuna-san";
			
			URL url = new URL("http://mangaonlinehere.com/search.html?keyword=" + mangaName);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.addRequestProperty("User-Agent", "Mozilla/5.0"); 
						
			BufferedReader bin = new BufferedReader(new InputStreamReader(con.getInputStream()));
			
			String urlName = "";
				
			String input;
			while ((input = bin.readLine()) != null)
			{
				if (input.contains("/manga-info/"))
				{
					input = input.substring(input.indexOf("/manga-info/"));
					
					urlName = input.substring(12, input.lastIndexOf('"'));
				}
				
				int index = input.indexOf("<img src=" + '"' + "http://cdn.mymangaonline.us");
				
				if (index > -1)
				{
					String imgUrl = input.substring(input.indexOf('"')+1, input.indexOf('"' + " "));
					String name = input.substring(input.indexOf("alt=" + '"')+5, input.lastIndexOf('"'));
					
					System.out.println(imgUrl + " " + name + " " + urlName);
					
					md.addResult(ImageIO.read(new URL(imgUrl)), name, urlName);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		isDone = true;
	}
}
