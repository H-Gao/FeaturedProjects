package Extractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Stack;

import javax.net.ssl.HttpsURLConnection;

public class MangaInfo extends Thread implements Runnable
{
	static boolean isDone = false;
	
	String mangaName;
	
	public MangaInfo(String mangaName)
	{
		this.mangaName = mangaName;
		
		this.start();
	}
	
	Stack<String> stack;
	
	@Override
	public void run()
	{
		try
		{
			stack = new Stack<String>();
			
			String mangaName = this.mangaName;//"yuragi-sou-no-yuuna-san";
			
			String root = new File("").getAbsolutePath();
			
			File manga = new File(root + "\\Manga");
			manga.mkdir();
			
			File mangaDir = new File(root + "\\Manga\\" + mangaName);
			mangaDir.mkdir();
			
			URL url = new URL("http://mangaonlinehere.com/manga-info/" + mangaName);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.addRequestProperty("User-Agent", "Mozilla/5.0"); 
						
			BufferedReader bin = new BufferedReader(new InputStreamReader(con.getInputStream()));
			
			PrintWriter pout = new PrintWriter(mangaDir.getAbsolutePath() + "\\Manga-Info.txt");
			
			if (!new File(mangaDir.getAbsolutePath() + "\\Manga-Count.txt").exists())
			{
				PrintWriter pout2 = new PrintWriter(mangaDir.getAbsolutePath() + "\\Manga-Count.txt");
				pout2.println("0");
				pout2.close();
			}
			
			pout.println(mangaName);
			
			String mangaUrl = null;
			int lastChapter = -1;
			
			boolean foundLastChapter = false;
				
			String input;
			while ((input = bin.readLine()) != null)
			{
				if (input.contains("img src=" + '"' + "http://cdn.mymangaonline.us"))
				{
					String imgUrl = input.substring(input.indexOf('"')+1, input.indexOf("alt")-2);
					String fileExt = imgUrl.substring(imgUrl.lastIndexOf("."));
					
					System.out.println(imgUrl);
					
					if (!new File(mangaDir + "\\Cover" + fileExt).exists())
					{
						URL url2 = new URL(imgUrl);
						HttpURLConnection con2 = (HttpURLConnection) url2.openConnection();
						con2.addRequestProperty("User-Agent", "Mozilla/5.0");
						
						InputStream in = con2.getInputStream();
						FileOutputStream fout = new FileOutputStream(mangaDir + "\\Cover" + fileExt);
						
						int count;
						byte[] buffer = new byte[8 * 1024 * 1024];
						
						while ((count = in.read(buffer)) > 0)
						{
							fout.write(buffer, 0, count);
						}
						
						fout.close();
					}
				}
				
				if (input.contains("h2") && !input.contains("read-online/"))
				{
					System.out.println(input);
					
					String name = input.substring(input.indexOf(">")+1, input.lastIndexOf("<"));
					
					pout.println(name);
					System.out.println(name);
				}
				
				if (input.contains("Status:"))
				{
					String nextLine = bin.readLine();
					
					String status = nextLine.substring(nextLine.indexOf("<span>") + 6, nextLine.indexOf("</span>"));
					
					pout.println(status);
					System.out.println(status);
				}
				
				if (input.contains("Author:"))
				{
					String nextLine = bin.readLine();
					
					String author = nextLine.substring(nextLine.indexOf("<span>") + 6, nextLine.indexOf("</span>"));
					
					pout.println(author);
					System.out.println(author);
				}
				
				if (input.contains("Genre:"))
				{
					bin.readLine();
					
					String nextLine;
					while ((nextLine = bin.readLine()).contains("href="))
					{
						String genre = nextLine.substring(nextLine.indexOf(">") + 1, nextLine.lastIndexOf("<"));
						
						pout.println(genre);
						System.out.println(genre);
					}
				}
				
				if (input.contains("Year of release:"))
				{
					String nextLine = bin.readLine();
					
					String year = nextLine.substring(nextLine.indexOf("<span>") + 6, nextLine.indexOf("</span>"));
					
					pout.println("*");
					System.out.println("*");
					
					pout.println(year);
					System.out.println(year);
				}
				
				if (input.contains("/read-online/"))
				{
					String m = input.substring(input.indexOf("/read-online/"), input.toLowerCase().lastIndexOf('"'));
					
					System.out.println(m);
					stack.add(m);
				}
				
				if (input.contains("Plot Summary"))
				{
					bin.readLine();
					bin.readLine();
					
					String nextLine = bin.readLine();
					
					String description = nextLine.replace("<p>", "").replace("</p>", "").trim();
					
					if (description.isEmpty())
					{
						description = "No description";
					}
					
					pout.println(description);
					System.out.println(description);
				}
			}
			
			lastChapter = stack.size();
			
			System.out.println(lastChapter);
			pout.println(lastChapter);
			
			pout.close();
			
			MangaInstaller mangaInstaller = new MangaInstaller(stack, mangaName, mangaUrl, lastChapter);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		isDone = true;
	}
}
