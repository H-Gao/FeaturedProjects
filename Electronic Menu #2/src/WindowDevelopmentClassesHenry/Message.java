package WindowDevelopmentClassesHenry;

import javax.swing.JTextField;

public class Message extends SimpleWindow
{
	JTextField message;
	Done done;
	
	int width;
	int height;
	
	public Message(int width, int height, String message) 
	{
		super("", width, height, "disabled");
		this.width = width;
		this.height = height;
		
		onInit(message);
	}
	
	public void onInit(String m)
	{
		message = new JTextField(m);
		message.setBounds(0, 0, width, height*3/4);
		add(message);
		
		done = new Done(this, 0, height*3/4, width, height/4);
		
		repaint();
	}
	
	public void onDone()
	{
		this.dispose();
	}
}
