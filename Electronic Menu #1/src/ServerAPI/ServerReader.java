package ServerAPI;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class ServerReader
{
	public ServerReader(Socket client) throws IOException
	{
		BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		
		BasicProtocol bprot = new BasicProtocol(null, null, client);
		
		while (true)
		{
			String input = in.readLine();
			
			if (input != null)
				System.out.println(bprot.parseCommands(input));
		}
	}
}
