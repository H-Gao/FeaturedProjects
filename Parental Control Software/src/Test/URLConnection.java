package Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;

/*
 * Purpose: To establish a connection via the URL classes, and read information from it.
 * Status: Cancelled until further notice (perhaps redone, elsewhere).
 */

public class URLConnection 
{
	public static void main(String args[])
	{
		try
		{
			URL url = new URL("");
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
	
			String inputLine;
			while ((inputLine = in.readLine()) != null) System.out.println(inputLine);
			
			in.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
