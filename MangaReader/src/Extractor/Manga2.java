package Extractor;

import java.io.BufferedReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;

public class Manga2 
{
	public static void main(String args[])
	{
		try
		{
			Scanner sin = new Scanner(System.in);
			
			System.out.println("Enter the manga name");
			String rawMangaName = sin.nextLine().replace(' ', '-').toLowerCase();
			
			System.out.println(rawMangaName);
			
			System.out.println("Enter the number of chapters");
			
			int lastChapter = sin.nextInt();
			
			String root = new File("").getAbsolutePath();
			
			File manga = new File(root + "\\Manga");
			manga.mkdir();
			
			File mangaDir = new File(root + "\\Manga\\" + rawMangaName);
			mangaDir.mkdir();
			
			boolean isDownloading = true;
			
			for (int volume = 1;volume <= lastChapter;++volume)
			{
				System.out.println("Currently downloading... Volume." + volume);
				
				URL url = new URL("http://mangaonlinehere.com/read-online/" + rawMangaName + "-ch-" + volume);
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.addRequestProperty("User-Agent", "Mozilla/5.0"); 
						
				BufferedReader bin = new BufferedReader(new InputStreamReader(con.getInputStream()));
				
				String input;
				while ((input = bin.readLine()) != null)
				{
					if (input.contains("404 not found"))
					{
						break;
					}
					
					int indexStart = input.indexOf("https://2.bp.blogspot.com/");
					
					if (indexStart > -1)
					{
						input = input.substring(indexStart, input.length());
						
						String[] fileExt = { ".jpeg", ".jpg" };
						
						for (int i = 0;i < fileExt.length;++i)
						{
							int indexEnd = input.indexOf(fileExt[i]);
							
							if (indexEnd != -1)
							{
								String imgUrl = input.substring(0, indexEnd) + fileExt[i];
								
								System.out.println(imgUrl);
								
								URL url2 = new URL(imgUrl);
								HttpsURLConnection con2 = (HttpsURLConnection) url2.openConnection();
								con2.addRequestProperty("User-Agent", "Mozilla/5.0");
								
								String mangeExtendedName = imgUrl.substring(imgUrl.lastIndexOf('/')+1, imgUrl.length());
								
								InputStream in = con2.getInputStream();
								FileOutputStream fout = new FileOutputStream(mangaDir + "\\" + mangeExtendedName);
								
								int count;
								byte[] buffer = new byte[1024];
								
								while ((count = in.read(buffer)) > 0)
								{
									fout.write(buffer, 0, count);
								}
								
								fout.close();
								
								Thread.sleep(250);
								
								break;
							}
						}
					}
				}
			}
			
			isDownloading = false;
			
			System.out.println("Finished downloading.");
	    }
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
