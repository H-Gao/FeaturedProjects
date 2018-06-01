package Gui;

import java.io.File;

public class SortableFile extends File implements Comparable<File>
{
	public SortableFile(String pathname) 
	{
		super(pathname);
	}

	public int compareTo(File arg0) 
	{
		return arg0.compareTo(this);
	}	
}
