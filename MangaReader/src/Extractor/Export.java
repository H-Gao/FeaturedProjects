package Extractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/*
 * Exports the manga into a single folder, making it easier to use with other
 * manga readers on mobile devices.
 * 
 * Must be used manually
 */
public class Export 
{
	public static void main(String args[])
	{
		String root = new File("").getAbsolutePath();
		
		File exports = new File(root + "\\Exports\\");
		exports.mkdir();
		
		//File[] manga = new File(root + "\\Manga\\").listFiles();
		
		File[] manga = { new File(root + "\\Nisekoi") }; //FOR SINGLE USE ONLY
		
		for (int i = 0;i < manga.length;++i)
		{
			File[] chapter = manga[i].listFiles();
			
			File exportManga = new File(exports.getAbsolutePath() + "\\" + manga[i].getName());
			exportManga.mkdir();
			
			for (int j = 0;j < chapter.length;++j)
			{
				File[] files = chapter[j].listFiles();
				
				if (chapter[j].getName().startsWith("Ch."))
				{
					for (int k = 0;k < files.length;++k)
					{
						try
						{
							FileInputStream in = new FileInputStream(files[k].getAbsolutePath());
							FileOutputStream fout = new FileOutputStream(exportManga.getAbsolutePath() + "\\" + chapter[j].getName() + "_" + files[k].getName());
							
							int count;
							byte[] buffer = new byte[12 * 1024 * 1024]; //8KB/s
							
							while ((count = in.read(buffer)) > 0)
							{
								fout.write(buffer, 0, count);
							}
							
							fout.close();
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
}
