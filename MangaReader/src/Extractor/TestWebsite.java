package Extractor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class TestWebsite 
{
	public static void main(String args[]) throws Exception
	{
		URL url = new URL("http://mangakakalot.com/chapter/non_non_biyori/chapter_39");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.addRequestProperty("User-Agent", "Mozilla/5.0");
		
		BufferedReader bin = new BufferedReader(new InputStreamReader(con.getInputStream()));
		
		String input;
		while ((input = bin.readLine()) != null)
		{
			System.out.println(input);
		}
	}
}
