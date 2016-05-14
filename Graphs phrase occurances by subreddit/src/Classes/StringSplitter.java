package Classes;

import java.util.Scanner;

public class StringSplitter 
{
	public static void main(String args[])
	{
		Scanner in = new Scanner(System.in);
		
		int a = 0;
		
		String input;
		while (!(input = in.nextLine()).equals("****"))
		{
			if (a%2 == 1)
			{
				System.out.println(input);
			}
			
			++a;
		}
	}
}
