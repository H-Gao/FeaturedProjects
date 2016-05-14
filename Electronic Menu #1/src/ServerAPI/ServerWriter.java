package ServerAPI;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class ServerWriter
{
	Encryption encryption = new Encryption(218, 8);
	
	String decryptionKey = "basicServer";
	
	public ServerWriter(Socket client) throws IOException
	{
		Scanner in = new Scanner(System.in);
		PrintWriter out = new PrintWriter(client.getOutputStream(), true);
		
		while (true)
		{
			String input = in.nextLine();
			
			String encryptedRequest = encryption.encrypt(decryptionKey, input);		
			out.println(encryptedRequest);
			out.flush();
		}
	}
	
}
