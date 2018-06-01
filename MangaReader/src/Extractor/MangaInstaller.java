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
import java.rmi.ConnectException;
import java.util.Scanner;
import java.util.Stack;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;

public class MangaInstaller 
{
	public MangaInstaller(Stack<String> stack, String mangaName, String rawMangaName, int lastChapter)
	{
		try
		{			
			String root = new File("").getAbsolutePath();
			
			File manga = new File(root + "\\Manga");
			manga.mkdir();
			
			File mangaDir = new File(root + "\\Manga\\" + mangaName);
			mangaDir.mkdir();
			
			boolean isDownloading = true;
			
			MangaWorker[] mw = new MangaWorker[2];
			
			for (int i = 0;i < mw.length;++i)
			{
				mw[i] = new MangaWorker();
			}
			
			for (int volume = 1;volume <= lastChapter;)
			{
				for (int i = 0;i < mw.length;++i)
				{
					if (!mw[i].isWorking())
					{
						mw[i].commision(mangaDir, volume, stack);
						++volume;
						break;
					}
				}
				
				Thread.sleep(50);
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
