package ResturantGUI;

public class Encryption 
{
	public Encryption()
	{
		System.out.println("Encryption is active.");
	}
	
	public static void main(String[] args)
	{
		Encryption encryption = new Encryption();
		System.out.println(encryption.encryptPIN("12425"));
		System.out.println(encryption.decryptPIN(encryption.encryptPIN("12425")));
	}
	
	public String encrypt(String in)
	{
		String out = "";
		
		for (int i = 0;i != in.length();++i)
		{
			out += (char)((in.charAt(i)/2) + i);
			out += (char)(in.charAt(i)/2 + (in.charAt(i)%2) + 2);
		}
		
		return out;
	}
	
	public String decrypt(String in)
	{
		String out = "";
		
		for (int i = 0;i != in.length();i += 2)
		{
			out += (char)((in.charAt(i) + in.charAt(i+1) - (i/2)) - 2);
		}
		
		return out;
	}
	
	public String encryptPIN(String in)
	{
		String out = "";
		
		for (int i = 0;i != in.length();++i)
		{
			out += (char)(in.charAt(i) + (2*i));
		}
		
		return out;
	}
	
	public String decryptPIN(String in)
	{
		String out = "";
		
		for (int i = 0;i != in.length();++i)
		{
			out += (char)(in.charAt(i) - (i*2));
		}
		
		return out;
	}
}
