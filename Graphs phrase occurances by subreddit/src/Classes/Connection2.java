package Classes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

public class Connection2
{
	static final char QM = '"';
	
	public static void main(String args[]) throws Exception
	{
		Scanner key = new Scanner(System.in);
		
		//String word = "checkmate+atheist";
		String word = key.nextLine().replaceAll(" ", "+");
		String outputFile = key.nextLine();
		
		int count = 50;
		
		String urlStr = "https://www.reddit.com/search?q=selftext%3A" + word + "&sort=new&restrict_sr=&t=all";
		
		HashMap<String, Integer> num = new HashMap<String, Integer>();
		
		boolean stop = false;
		
		while (!stop)
		{
			//System.out.println("\nTrying to connect to..." + urlStr);
			
			try
			{
				boolean found = false;
				
				HttpsURLConnection url = (HttpsURLConnection)(new URL(urlStr).openConnection());
				BufferedReader in = new BufferedReader(new InputStreamReader(url.getInputStream()));
				
				if (count == 50)
				{
					System.out.println("Working on page 1.");
				}
				else
				{
					System.out.println("Working on page " + (count/25 - 1) + ".");
				}
				
				String input;
				while ((input = in.readLine()) != null)
				{
					int index2 = input.indexOf("https://www.reddit.com/search?q=selftext%3A" + word + "&amp;sort=new&amp;restrict_sr=&amp;t=all&amp;count=25&amp;after=t3_");
					
					if (index2 != -1)
					{
						input = input.substring(index2, input.length());
						urlStr = input.substring(0, input.indexOf(QM));
						found = true;
					}
					
					int index3 = input.indexOf("https://www.reddit.com/search?q=selftext%3A" + word + "&amp;amp=&amp;sort=new&amp;restrict_sr=&amp;t=all&amp;count=" + count + "&amp;after=");
					
					if (index3 != -1)
					{
						input = input.substring(index3, input.length());
						urlStr =  input.substring(0, input.indexOf(QM));
						found = true;
						count += 25;
					}
					
					int index1 = input.indexOf("class=" + QM + "search-subreddit-link may-blank" + QM);
					
					if (index1 != -1)
					{
						input = input.substring(index1+39);
						index1 = input.indexOf(">");
						
						String str = input.substring(index1+1, input.indexOf("</"));
						
						if (num.get(str) == null)
						{
							num.put(str, 1);
						}
						else
						{
							num.put(str, num.get(str)+1);
						}
						
						System.out.println(str);
					}
				}
				
				if (!found)
				{
					stop = true;
				}
			}
			catch (Exception e)
			{
				//System.out.println("Connection failed.");
			}
			
			PrintWriter out = new PrintWriter(outputFile + ".txt");
			
			Object[] keys = num.keySet().toArray();
			
			for (int i = 0;i < keys.length;++i)
			{
				out.println(keys[i] + " " + num.get(keys[i]));
			}
			
			out.close();
		}
	}
}
