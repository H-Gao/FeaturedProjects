package ResturantGUI;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTextField;

import WindowDevelopmentClassesHenry.Done;
import WindowDevelopmentClassesHenry.SimpleWindow;

public class CheckOut extends SimpleWindow implements ActionListener
{
	Encryption encryption = new Encryption();
	
	UserInterface ui;
	
	static int screenWidth = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	static int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	
	JTextField[] confirmationField;
	
	JButton exit;
	
	JButton withdrawFromAccount;
	
	Done proceed;
	
	public CheckOut(UserInterface ui) 
	{
		super("Check Out", screenWidth, screenHeight, "undecorated");
		
		this.ui = ui;
	}
	
	public void onInit()
	{
		exit = new JButton("Exit");
		exit.addActionListener(this);
		exit.setBounds(screenWidth-screenWidth/6, 0, screenWidth/6, screenHeight/18);
		add(exit);
		
		withdrawFromAccount = new JButton("Checkout with Account Credit");
		withdrawFromAccount.addActionListener(this);
		withdrawFromAccount.setBounds(0, 0, screenWidth/4, screenHeight/4);
		add(withdrawFromAccount);
	}
	
	public void initConfirmationField(int numberOfFields)
	{
		confirmationField = new JTextField[1];
		
		for (int i = 0;i != numberOfFields;++i)
		{
			confirmationField[i] = new JTextField();			
			confirmationField[i].setBounds(0, screenHeight/38 + (i*(screenHeight/8)), screenWidth/4, screenHeight/18);
			add(confirmationField[i]);
		}
		
		proceed = new Done(this, 0, screenHeight/38 + (numberOfFields*(screenHeight/8)), screenWidth/4, screenHeight/18);
		
		repaint();
	}
	
	public void onDone()
	{
		if (confirmationField[0].getText().equals(encryption.decrypt(ui.password)))
		{
			String costs = ui.cost.getText();
			int cost = Integer.parseInt(costs.substring(costs.indexOf("$")+1, (costs.length())));
			String funds = ui.fund.getText();
			double fund = Double.parseDouble(funds.substring(costs.indexOf("$")+1, (funds.length()))) - cost;
			
			if (fund >= cost)
			{
				ui.fund.setText("$" + fund);
			
				ui.cost.setText("$0");
			}
			else
			{
				CheckOut checkOut = new CheckOut(ui);
				sendMessage(280, 110, "Please add $" + (cost-fund) + " to your account. Or select another method.");
			}
			
		    this.dispose();
		}
		else
		{
			sendMessage(280, 110, "The password does not match.");
		}
	}

	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == exit)
		{
			this.dispose();
		}
		else if (e.getSource() == withdrawFromAccount)
		{
			withdrawFromAccount.setVisible(false);
			initConfirmationField(1);
		}
	}
}
