package Extractor;

public class Alaising 
{
	public static void main(String args[])
	{
		F a = new F(1);
		F b = a;
		b.i = 10;
		
		System.out.println(a.i);
	}
	
	public static class F
	{
		int i;
		
		public F(int i)
		{
			this.i = i;
		}
	}
}
