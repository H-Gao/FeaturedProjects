package StaffOrder;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import javax.swing.JButton;
import javax.swing.JTextField;

import WindowDevelopmentClassesHenry.SimpleWindow;

public class OrderingScreen extends SimpleWindow implements ActionListener
{
	JTextField[] labels;
	
	int width = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	int height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	
	JButton completedOrder;
	
	public List<JButton> orders = new ArrayList<JButton>();
	public List<JButton> deliever = new ArrayList<JButton>();
	
	public OrderingScreen() 
	{
		super("Orders", (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(), true);
		
		onInit(true);
		
		repaint();
	}
	
	public static void main(String[] args)
	{
		OrderingScreen orderingScreen = new OrderingScreen();
	}
	
	public void onInit(Object afterInit)
	{
		completedOrder = new JButton("Finished Order");
		completedOrder.addActionListener(this);
		completedOrder.setBounds((int)((double)width/1.4), (int)((double)height/1.14), width/4, height/20);
		add(completedOrder);
		
		labels = new JTextField[2];
		
		for (int i = 0;i != 2;++i)
		{
			labels[i] = new JTextField();
			
			if (i == 0)
				labels[i].setText("Orders");
			
			else if (i == 1)
				labels[i].setText("To be delievered");
			
			labels[i].setBounds(i*(width/2), 0, (int)((double)width/2.5), height/20);
			add(labels[i]);
		}
		
		for (int i = 0;i != 20;++i)
			addToList("An order" + i, orders);
	}
	
	public void addToList(String name, List<JButton> list)
	{
		JButton order = new JButton(name);
		
		order.addActionListener(this);
		
		int x = 0;
		
		if (list == this.orders)
			x = width/80;
		
		else if (list == this.deliever)
			x = width/2;
		
		order.setBounds(x, height/80 + (list.size()+1)*height/20, width/4, height/20);
		add(order);
		
		list.add(order);
		
		repaint();
	}

	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == completedOrder && !orders.isEmpty())
		{
			orders.get(0).setVisible(false);
			orders.remove(0);
		}
		else
		{
			for (int i = 0;i < orders.size();++i)
			{
				if (e.getSource().equals(orders.get(i)))
				{
					this.addToList(orders.get(i).getText(), deliever);
					orders.get(i).setVisible(false);
					orders.remove(i);
				}
			}
			
			for (int i = 0;i < deliever.size();++i)
			{
				if (e.getSource().equals(deliever.get(i)))
				{
					deliever.get(i).setVisible(false);
					deliever.remove(i);
				}
			}
		}
	}
}
