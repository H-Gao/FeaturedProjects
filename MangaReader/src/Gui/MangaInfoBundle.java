package Gui;

public class MangaInfoBundle 
{
	String dir, name, status, author, genre, description;
	int lastChapter, currentChapter;
	
	public MangaInfoBundle(String dir, String name, String status, String author, String genre, String description, int lastChapter, int currentChapter)
	{
		this.dir = dir;
		this.name = name;
		this.status = status;
		this.author = author;
		this.genre = genre;
		this.description = description;
		this.lastChapter = lastChapter;
		this.currentChapter = currentChapter;
	}
}
