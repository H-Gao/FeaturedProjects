package ServerAPI;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;


public class Encryption 
{
	static int maxNum;
	static int encryptionNum;
	
	public Encryption(boolean testPerformance, int maxVal, int encryptionVal) 
	{
		System.out.println("The encryption process has loaded.");
	}
	
	public Encryption(int maxVal, int encryptionVal)
	{
		long currentTime = System.nanoTime();
		
		maxNum = maxVal;
		encryptionNum = encryptionVal;
		
		System.out.println("Encryption value: " + encryptionNum);
		System.out.println("Testing the encryption process.");
		
		String username = "defaultUsername";
		String password = "The encryption process has loaded successfully.";
		
		String encrypted = encrypt(username, password);
		
		System.out.println("Encrypted data took up: " + encrypted.length()/1024 + " kb");
		System.out.println(decrypt(username, encrypted));
		
		long tempTime = System.nanoTime();
		
		long sum = 0;
		
		int length = (username+password).length()*encryptionNum+(username+password).length()+1;
		
		for (int i = 0;i < length;++i)
			sum += (length-i)*248;
		
		System.out.println((sum/10000)/3600);
		System.out.println("Encryption completed in: " + (tempTime-currentTime)/1000000 + " ms");
	}
	
	public static void main(String[] args)
	{
		Encryption encryption = new Encryption(40, 0);
	}
	
	public String randString(int length)
	{
		String out = "";
		
		Random randomGenerator = new Random();
		
		for (int i = 0;i < length;++i)
			out += (char)(randomGenerator.nextInt(maxNum) + 33);
		
		return out;
	}
	
	public String encrypt(String username, String password)
	{
		//The output.
		String out = "";
		
		//The values at which the characters will increase by.
		int[] inc = { 2, 4, 8 };
		
		//Gets a unique password, so someone with the same password cannot be discovered.
		password = username + password;
		
		//The password as a char[].
		char[] passwordChar = password.toCharArray();
		
		//Gets a random number. It then sets it as the first character, so it can be easily parsed.
		int randNum = new Random().nextInt(6) + 2;
		
		out += (char)(randNum);
		
		//Goes through the char[].
		for (int i = 0;i < password.length();++i)
		{
			//The current character.
			char currentChar = passwordChar[i];
			
			//Adds the modulo, so it will remain the same if it does not divide evenly.
			out += (char)(currentChar/3 + currentChar%3 + inc[0] + randNum) + randString(encryptionNum);
			
			//Adds the rest.
			for (int n = 1;n < 3;++n)
				out += (char)(currentChar/3 + inc[n] + randNum) + randString(encryptionNum);
		}
		
		//Returns the output.
		return out;
	}
	
	public String decrypt(String username, String password)
	{
		//The output, where out[0] is equal to the unencrypted password, and out[1] is equal to the extracted username and password.
		String out = "";
		
		//The number that the numbers will increase by.
		int incTotal = 14;
		
		//The password as a char[].
		char[] passwordChar = password.toCharArray();
		
		//The random number.
		int randNum = passwordChar[0];
		
		int val = 0;
		
		//Finds the encrypted password.
		for (int i = 1;i < passwordChar.length;i += encryptionNum+1)
		{
			//Stores the values, until it is put together.
			val += passwordChar[i];
			
			//The values will be put together on the third time a value is recorded.
			if ((i+encryptionNum)%(3*encryptionNum+3)==0)
			{
				out += (char)(val - 3*randNum - incTotal);
				val = 0;
			}
		}
		
		//Outputs both the extracted and unextracted usernames and passwords.
		return out.substring(username.length(), out.length());
	}
}
