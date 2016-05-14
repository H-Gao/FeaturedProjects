package Classes;

import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.LinkedList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;

public class BlockList extends JFrame implements MouseListener, MouseMotionListener
{
	//Links to the control panel, so it can access it's features. Ex. The currentDirectory.
	ControlPanel cp;
	
	//A component which is used to display the background IMAGE to make this application look better.
	JLabel background;
		
	//Used to select tools, such as home, edit and settings.
	JLabel toolbar;
	
	//Used to open the file menu, to select an application to block.
	JLabel addBlock;
	
	//Stores the information of the blocked apps.
	LinkedList<BlockedInfo> info = new LinkedList<BlockedInfo>();
		
	//Initalizes the JFrame.
	public BlockList(ControlPanel c)
	{
		//Stores the location of the control panel for further use.
		cp = c;
		
		//Creates an empty JFrame
		this.setTitle("Lockr");
		this.setSize(500, 650);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(null);
		this.setUndecorated(true);
		this.setVisible(true);
		
		//Initalizes the components, so they can be used.
		init();
		
		this.addBlocked("C:\\Program Files (x86)\\Audacity\\audacity.exe");
		this.addBlocked("C:\\Program Files (x86)\\Origin\\Origin.exe");
		this.addBlocked("C:\\Program Files (x86)\\Nero\\Nero 2015\\Nero Launcher\\NeroLauncher.exe");
		
		//Loads the applications that will be blocked.
		loadBlockedApplications();
	}
	
	public void init()
	{
		//Initalizes the toolbar, which can select different options.
		addBlock = new JLabel();
		addBlock.setIcon(new ImageIcon(cp.currentDirectory + "AddNew.png"));
		addBlock.addMouseListener(this);
				
		addBlock.setSize(addBlock.getIcon().getIconWidth(), //Sets the size to be the same as the icon.
				addBlock.getIcon().getIconHeight());
		
		addBlock.setLocation(0, 650 - addBlock.getHeight());
		this.add(addBlock);
		
		
		//Initalizes the toolbar, which can select different options.
		toolbar = new JLabel();
		toolbar.setIcon(new ImageIcon(cp.currentDirectory + "BlockingToolbar_0.png"));
		toolbar.addMouseListener(this);
		toolbar.addMouseMotionListener(this);
				
		toolbar.setSize(toolbar.getIcon().getIconWidth(), //Sets the size to be the same as the icon.
				toolbar.getIcon().getIconHeight());
				
		this.add(toolbar);
				
				
		//Initalizes the background, purely for asthetics (serves no purpose).
		background = new JLabel();
		background.setIcon(new ImageIcon(cp.currentDirectory + "LockrBackground_2.png"));
				
		background.setSize(background.getIcon().getIconWidth(), //Sets the size to be the same as the icon.
			background.getIcon().getIconHeight());
				
		this.add(background);
				
		this.repaint();
	}
	
	public void createBlockedApplication(String path)
	{
		BlockedInfo bl = new BlockedInfo(this, path);
		info.add(bl);
	}
	
	//Creates the images for the blocked applications, so the user can interact with them.
	public void loadBlockedApplications()
	{
		//Removes the applications that are already existing.
		for (int i = 0;i < info.size();++i)
		{
			//The blocked app that is being checked.
			BlockedInfo target = info.get(i);
			
			this.remove(target.back);
			this.remove(target.icon);
			this.remove(target.name);
		}
		
		info = new LinkedList<BlockedInfo>();
		
		//Gets an array of blocked apps.
		Object[] blockedApps = cp.b.blockedApps.keySet().toArray();
		
		//Returns the path of the blocked application and creates the images.
		for (int i = 0;i < blockedApps.length;++i)
			createBlockedApplication(cp.b.path.get(blockedApps[i]));
		
		this.repaint();
	}
	
	/*Checks to see which section the mouse hits on the toolbar.
	The corresponding section will light up.*/
	public void checkToolbarSection(MouseEvent e)
	{
		if (e.getSource() == toolbar)
		{
			if (e.getX() > 370)
				toolbar.setIcon(new ImageIcon(cp.currentDirectory + "BlockingToolbar_1.png"));
			else if (e.getX() > 288)
				toolbar.setIcon(new ImageIcon(cp.currentDirectory + "BlockingToolbar_2.png"));
			else if (e.getX() > 197)
				toolbar.setIcon(new ImageIcon(cp.currentDirectory + "BlockingToolbar_3.png"));
			else
				toolbar.setIcon(new ImageIcon(cp.currentDirectory + "BlockingToolbar_4.png"));
			
			if (e.getX() > 485 && e.getY() < 15)
				toolbar.setIcon(new ImageIcon(cp.currentDirectory + "BlockingToolbar_0.png"));
		}
	}
	
	public class BlockedInfo
	{
		//The layer that decorates the icon and name.
		public JLabel back;
		public JLabel icon;
		public JTextField name;
		
		public BlockedInfo(BlockList bl, String path)
		{
			icon = new JLabel();
			
			Icon ic = FileSystemView.getFileSystemView().getSystemIcon(new File(path));
			BufferedImage im = new BufferedImage(ic.getIconWidth(), ic.getIconHeight(), BufferedImage.TYPE_4BYTE_ABGR);
			ic.paintIcon(null, im.getGraphics(), 0, 0);
			
			icon.setIcon(new ImageIcon(im.getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
			icon.setSize(icon.getIcon().getIconWidth(), icon.getIcon().getIconHeight());
			icon.setLocation(5, info.size()*64+5+64);
			
			bl.add(icon);
			
			
			name = new JTextField();
			name.setFont(new Font("Corsiva Hebrew", Font.BOLD + Font.ITALIC, 24));
			name.setText(extractName(path));
			name.setEditable(false);
			name.setOpaque(false);
			name.setBorder(null);
			name.setSize(340, icon.getHeight());
			name.setLocation(icon.getWidth()+28, icon.getY());
			bl.add(name);
			
			
			back = new JLabel();
			
			back.setIcon(new ImageIcon(cp.currentDirectory + "BlockedBack.png"));
			back.setName("BlockedApplication_" + path.substring(path.lastIndexOf("\\")+1, path.length()));
			back.addMouseListener(bl);
			back.setSize(back.getIcon().getIconWidth(), back.getIcon().getIconHeight());
			back.setLocation(0, icon.getY()-5);
			
			bl.add(back);
			
			
			bl.add(background);
			
			bl.repaint();
		}
	}
	
	public String extractName(String path)
	{
		//Seperates the name and file extension.
		String refined = (path = path.substring(path.lastIndexOf("\\")+1, path.length())).substring(0, path.lastIndexOf('.'));
		
		//The following code capitalizes the first letter and returns the refined output.
		char firstChar = refined.charAt(0);
		
		if (firstChar >= 'a' && firstChar <= 'z') refined = ((char)(firstChar-32)) + refined.substring(1, refined.length());
		
		return refined;
	}
	
	public void mouseEntered(MouseEvent e) 
	{
		Component source = (Component)e.getSource();
		
		checkToolbarSection(e);
		
		if (source.getName() != null && source.getName().startsWith("BlockedApplication_") && e.getX() > 420)
		{
			String name = source.getName();
			
			if (e.getY() < 31)
			{
				for (int i = 0;i < info.size();++i)
				{
					//The blocked app that is being checked.
					BlockedInfo target = info.get(i);
					
					System.out.println();
					
					//If the name matches with the components, it get deleted.
					if (target.back.getName().equals("BlockedApplication_" + name))
					{
						target.back.setIcon(new ImageIcon(cp.currentDirectory + "BlockedBack_0.png"));
						
						this.repaint();
						break;
					}
				}
			}
			else
				for (int i = 0;i < info.size();++i)
				{
					//The blocked app that is being checked.
					BlockedInfo target = info.get(i);
					
					//If the name matches with the components, it get deleted.
					if (target.back.getName().equals("BlockedApplication_" + name))
					{
						target.back.setIcon(new ImageIcon(cp.currentDirectory + "BlockedBack_1.png"));
						
						this.repaint();
						break;
					}
				}
		}
	}
	
	public void mouseExited(MouseEvent e) 
	{
		Component source = (Component)e.getSource();
		
		if (e.getSource() == toolbar)
		{
			toolbar.setIcon(new ImageIcon(cp.currentDirectory + "BlockingToolbar_0.png"));
		}
		
		if (source.getName() != null && source.getName().startsWith("BlockedApplication_") && e.getX() > 420)
		{
			String name = source.getName();
			
			
			for (int i = 0;i < info.size();++i)
			{
				//The blocked app that is being checked.
				BlockedInfo target = info.get(i);
					
				//If the name matches with the components, it get deleted.
				if (target.back.getName().equals("BlockedApplication_" + name))
				{
					target.back.setIcon(new ImageIcon(cp.currentDirectory + "BlockedBack.png"));
						
					this.repaint();
					break;
				}
			}
		}
	}
	
	//Blocks an application, given it's path.
	public void addBlocked(String p)
	{
		//Gets the name of the file, without it's directory.
		String name = p.substring(p.lastIndexOf("\\")+1, p.length());
		
		cp.b.blockedApps.put(name, true); //Notifies the system that the app is blocked.
		cp.b.path.put(name, p); //Stores the location of the app.
	}
	
	//Unblocks an application, given it's name.
	public void removeBlocked(String name)
	{
		for (int i = 0;i < info.size();++i)
		{
			//The blocked app that is being checked.
			BlockedInfo target = info.get(i);
			
			//If the name matches with the components, it get deleted.
			if (target.back.getName().equals("BlockedApplication_" + name))
			{
				this.remove(target.back);
				this.remove(target.icon);
				this.remove(target.name);
				info.remove(i);
				
				this.repaint();
				break;
			}
		}
		
		cp.b.blockedApps.remove(name); //Notifies the system that the app is blocked.
		cp.b.path.remove(name); //Stores the location of the app.
		
		//Reloads the blocked applications, the space that the deleted application left behind, is used up.
		loadBlockedApplications();
	}

	public void mouseClicked(MouseEvent e) 
	{
		Component source = (Component)e.getSource();
		
		//If the mouse clicks on the close button, the application closes.
		if (e.getSource() == toolbar && e.getX() > 485 && e.getY() < 15) this.dispose();
		
		if (source.getName() != null && source.getName().startsWith("BlockedApplication_") && e.getX() > 420)
		{
			String name = source.getName();
			
			if (e.getY() < 31)
				this.removeBlocked(name.substring(name.indexOf('_')+1, name.length()));
			else
				System.out.println("remove.");
		}
	}
	
	public void mousePressed(MouseEvent e) {}
	
	public void mouseReleased(MouseEvent e) 
	{
		checkToolbarSection(e);
	}

	public void mouseDragged(MouseEvent e) 
	{
		checkToolbarSection(e);
	}

	public void mouseMoved(MouseEvent e) 
	{
		checkToolbarSection(e);
	}
}
