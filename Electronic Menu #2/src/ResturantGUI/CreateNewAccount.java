package ResturantGUI;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Scanner;

public class CreateNewAccount 
{
	String mainDirectory = System.getProperty("java.class.path").substring(0, System.getProperty("java.class.path").indexOf("bin"));
	
	Encryption encryption = new Encryption();
	
	Scanner scanner = new Scanner(System.in);
	
	public CreateNewAccount()
	{
		try 
		{
			doCommand(null);
		} 
		catch (FileNotFoundException e) 
		{
			System.out.println("We could not find the file...");
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException
	{
		CreateNewAccount createNewAccount = new CreateNewAccount();
	}
	
	public void doCommand(String input) throws FileNotFoundException
	{
		String in = null;
		
		if (input == null)
		{
			System.out.println("Enter a command");
			in = scanner.nextLine();
		}
		else
		{
			in = input;
			input = null;
		}
		
		while (!in.equals("Done"))
		{
			if (in.equals("Account"))
			{
				String username = null;
				String password = null;
				
				System.out.println("Enter a username.");
				in = scanner.nextLine();
				username = in;
			
				if (username.equals("Done"))
					break;
			
				System.out.println("Enter a password.");
				in = encryption.encrypt(scanner.nextLine());
				password = in;
			
				PrintWriter out = new PrintWriter(mainDirectory + "Accounts\\" + username + ".acf");
				out.println(password);
				out.println(0);
				out.close();
			
				System.out.println("The account " + username + " was created with the password " + encryption.decrypt(password));
			}
			else if (in.equals("Change Password"))
			{
				System.out.println("Enter a username.");
				in = scanner.nextLine();
				String username = in;
				
				File userFile = new File(mainDirectory + "Accounts\\" + username + ".acf");
				
				if (userFile.exists())
				{
					System.out.println("The user " + username + " has been found.");
					
					Scanner currentBalanceObtainer = new Scanner(new FileReader(userFile));
					
					String skipLine = currentBalanceObtainer.nextLine();
					int currentBalance = Integer.parseInt(currentBalanceObtainer.nextLine());
					
					PrintWriter out = new PrintWriter(mainDirectory + "Accounts\\" + username + ".acf");
					
					System.out.println("Enter the new password.");
					String password = scanner.nextLine();
					
					out.println(encryption.encrypt(password));
					out.println(currentBalance);
					out.close();
					
					System.out.println("The user " + username + " has changed his password to " + password);
				}
				else
				{
					System.out.println("The user does not exist...");
					System.out.println("Did you want to create a new account instead?");
					
					if (scanner.nextLine().equals("Yes"))
					{
						doCommand("Account");
					}
					
					break;
				}
			}
			
			else if (in.equals("Staff"))
			{
				System.out.println("Enter the staff member's name");
				
				String name = scanner.nextLine();
				
				PrintWriter out = new PrintWriter(mainDirectory + "Staff\\" + name + ".stf");
				
				System.out.println("Enter the ten letter PIN.");
				String password = scanner.nextLine();
				
				//Checks two things; it makes sure password is a Integer, and the length is equal to ten.
				try
				{
					if ((Long.parseLong(password) + "").length() == 10)
					{
						out.println(encryption.encryptPIN(password));
						
						System.out.println("Enter the number of roles they have.");
						
						int numberOfRoles = Integer.parseInt(scanner.nextLine());
						
						System.out.println("Enter a role.");
						out.println(scanner.nextLine());
						
						for (int i = 1;i < numberOfRoles;++i)
						{
							System.out.println("Enter a role.");
							out.println(scanner.nextLine());
						}
						
						System.out.println("The staff account has been created with the name " + name + " and the password " + password);
						out.close();
					}
					else
					{
						System.out.println("The number was not 10 digits.");
						doCommand("Staff");
					}
				}
				catch (NumberFormatException exception)
				{
					System.out.println("The number was not a Integer.");
					doCommand("Staff");
				}
			}
			else if (in.equals("Menu"))
			{
				String location = null;
				
				System.out.println("What type of dish is it?");
				in = scanner.nextLine();
				location = in;
				
				if (location.equals("Main"))
				{
					String name = null;
					String price = null;
					
					System.out.println("What is the name?");
					in = scanner.nextLine();
					name = in;
					
					PrintWriter out = new PrintWriter(mainDirectory + "Menu\\Main\\" + name + ".mnf");
					
					System.out.println("How many lines for the description");
					int numberOfDescription = scanner.nextInt();
					
					out.println(numberOfDescription);
					
					String skip = scanner.nextLine();
					
					for (int i = 0;i != numberOfDescription;++i)
					{
						out.println(scanner.nextLine());
					}
					
					System.out.println("How much does it cost?");
					in = scanner.nextLine();
					price = in;
					out.println(price);
					
					out.close();
				}
			}
			
			in = scanner.nextLine();
		}
	}
}
