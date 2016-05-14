package Classes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Connection 
{
	static final char QM = '"';
	
	public static void main(String args[]) throws Exception
	{
		String urlStr = "https://www.reddit.com/r/pics/comments/4brhlj/inside_the_specimen_collections_of_the/";
		
		viewComments(urlStr);
	}
	
	public static void viewComments(String urlStr) throws Exception
	{
		HttpsURLConnection url = (HttpsURLConnection)(new URL(urlStr).openConnection());
		BufferedReader in = new BufferedReader(new InputStreamReader(url.getInputStream()));
		
		String input;
		while ((input = in.readLine()) != null)
		{
			int index1 = input.indexOf("data-author=" + QM);
			
			if (index1 != -1)
			{
				input = input.substring(index1+13);
				index1 = input.indexOf(QM);
				
				System.out.println();
				System.out.println(input.substring(0, index1) + " says:");
			}
			
			int index = input.indexOf("<div class=" + QM + "md" + QM + "><p>");
			
			if (index != -1)
			{
				input = input.substring(index+19);
				index = input.indexOf("</p>");
				
				if (index != -1)
				{
					String comment = input.substring(0, index);
					
					comment = comment.replaceAll("&#39;", "'");
					comment = comment.replaceAll("&quot;", ""+QM);
					
					System.out.println(comment);
				}
			}
		}
	}
}
