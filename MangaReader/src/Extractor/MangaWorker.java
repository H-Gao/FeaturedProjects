package Extractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Stack;

import javax.net.ssl.HttpsURLConnection;

public class MangaWorker extends Thread implements Runnable
{
	File mangaDir;
	int volume;
	Stack<String> stack;
	
	boolean isWorking = false;
	
	public MangaWorker() 
	{
		this.start();
	}
	
	public void commision(File mangaDir, int volume, Stack<String> stack)
	{
		this.mangaDir = mangaDir;
		this.volume = volume;
		this.stack = stack;
		
		isWorking = true;
	}
	
	public boolean isWorking()
	{
		return isWorking;
	}
	
	@Override
	public void run()
	{
		while (true)
		{
			if (isWorking)
			{
				try
				{
					File mDir = new File(mangaDir.getAbsolutePath() + "\\Ch." + volume);
					mDir.mkdir();
				
					System.out.println("Currently downloading... Volume." + volume);
					
					URL url = new URL("http://mangaonlinehere.com" + stack.pop());
					HttpURLConnection con = (HttpURLConnection) url.openConnection();
					con.addRequestProperty("User-Agent", "Mozilla/5.0");
					
					BufferedReader bin = new BufferedReader(new InputStreamReader(con.getInputStream()));
					
					int page = 1;
					
					String input;
					while ((input = bin.readLine()) != null)
					{
						try
						{
							if (input.contains("404 not found"))
							{
								break;
							}
							
							if (input.contains("img id="))
							{
								int indexStart = input.indexOf("src=" + '"') + 5;
								
								if (indexStart > -1)
								{
									input = input.substring(indexStart, input.length());
									
									if (input.startsWith("0"))
									{
										input = bin.readLine();
										
										System.out.println(input);
										
										input = input.substring(input.indexOf("src='") + 5, input.length());
									}
									
									String[] fileExt = { ".png", ".jpeg", ".jpg" };
									
									for (int i = 0;i < fileExt.length;++i)
									{
										int indexEnd = input.indexOf(fileExt[i]);
										
										if (indexEnd != -1)
										{
											String imgUrl = input.substring(0, indexEnd) + fileExt[i];
											
											System.out.println(imgUrl);
											
											URL url2 = new URL(imgUrl);
											
											HttpURLConnection con2;
											
											if (imgUrl.startsWith("https://"))
											{
												con2 = (HttpsURLConnection) url2.openConnection();
											}
											else
											{
												con2 = (HttpURLConnection) url2.openConnection();
											}
											
											con2.addRequestProperty("User-Agent", "Mozilla/5.0");
											
											String mangeExtendedName = imgUrl.substring(imgUrl.lastIndexOf('/')+1, imgUrl.length());
											
											InputStream in = con2.getInputStream();
											FileOutputStream fout = new FileOutputStream(mDir + "\\" + page + fileExt[i]);
											
											int count;
											byte[] buffer = new byte[12 * 1024 * 1024]; //8KB/s
											
											while ((count = in.read(buffer)) > 0)
											{
												fout.write(buffer, 0, count);
											}
											
											fout.close();
											++page;
											
											Thread.sleep(50);
											
											break;
										}
									}
								}
							}
						}
						catch (Exception e)
						{
							System.out.println(volume + " could not be found...");
							
							e.printStackTrace();
						}
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				
				isWorking = false;
			}
			else
			{
				try 
				{
					Thread.sleep(50);
				} 
				catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
			}
		}
	}
}
