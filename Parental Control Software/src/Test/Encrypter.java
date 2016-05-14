package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

public class Encrypter 
{
	static int maxLength;
	
	public static void main(String[] args)
	{
		//Scanner in = new Scanner(System.in);
		//String name = in.nextLine();
			
		encrypt("DownloadFile.txt", 9);
		decrypt("DownloadFile.enc", 9);
	}
	
	public static void encrypt(String name, int ml)
	{
		maxLength = ml;
		
		try
		{
			String extension = name.substring(name.lastIndexOf('.'), name.length());
			
			BufferedReader in = new BufferedReader(new FileReader(new File(name)));
			PrintWriter out = new PrintWriter(name.substring(0, name.lastIndexOf('.')) + ".enc");
			
			//Saves the file extension, as the first line.
			out.println(extension);
			
			int a = 0;
			
			String input;
			while ((input = in.readLine()) != null)
			{
				char[] inputChar = input.toCharArray();
				
				for (int i = 0;i < input.length();++i)
				{
					if (i%2==0) out.print(format(inputChar[i]*2872947-2456+a*36));
					else if (i%3==0) out.print(format(inputChar[i]*6812542));
					else out.print(format(inputChar[i]*4172947+a*264));
				}
				
				Random r = new Random();
				
				//Adds random numbers at the end to throw people off.
				for (int i = 0;i < r.nextInt(maxLength-1);++i) out.print((char)(r.nextInt(9)+48));
				
				out.println();
				--a;
				if (a < -80) a = 0;
			}
			
			in.close();
			out.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void decrypt(String name, int ml)
	{
		maxLength = ml;
		
		try
		{
			BufferedReader in = new BufferedReader(new FileReader(new File(name)));
			
			//Gets the file extension, which is the FIRST line of the encrypted class.
			PrintWriter out = new PrintWriter(name.substring(0, name.lastIndexOf('.')) + in.readLine());
			
			int a = 0;
			
			String input;
			while ((input = in.readLine()) != null)
			{
				char[] inputChar = input.toCharArray();
				
				for (int i = 0;i < input.length()-input.length()%maxLength;i+=maxLength)
				{
					String str =  "";
					for (int b = 0;b < maxLength;++b)  str += inputChar[i+b];
					
					//Stores the char in the integer form 000, so that data is not lost.
					int num = Integer.parseInt(str);
					
					if ((i/maxLength)%2==0)
					{
						out.print((char)(((num+a*36+2456)/2872947)));
					}
					else if ((i/maxLength)%3==0)
					{
						out.print((char)(num/6812542));
					}
					else
					{
						out.print((char)((num+a*264)/4172947));
					}
				}
				
				out.println();
				++a;
				if (a > 80) a = 0;
			}
			
			in.close();
			out.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static String format(int in)
	{
		String intStr = ""+in;
		
		int num = maxLength-intStr.length();
		
		for (int i = 0;i < num;++i)
		{
			intStr = "0"+intStr;
		}
		
		return intStr;
	}
}
