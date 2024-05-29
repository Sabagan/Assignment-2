import java.util.ArrayList;

/*
 * An AudioBook is a type of AudioContent.
 * It is a recording made available on the internet of a book being read aloud by a narrator
 * 
 */
public class AudioBook extends AudioContent
{
	public static final String TYPENAME =	"AUDIOBOOK";
	
	private String author; 
	private String narrator;
	private ArrayList<String> chapterTitles;
	private ArrayList<String> chapters;
	private int currentChapter = 0;

	
	public AudioBook(String title, int year, String id, String type, String audioFile, int length,
									String author, String narrator, ArrayList<String> chapterTitles, ArrayList<String> chapters)
	{
		// Make use of the constructor in the super class AudioContent.
		super(title, year, id, type, audioFile, length);
		// Initialize additional AudioBook instance variables.
		this.author = author;
		this.narrator = narrator;
		this.chapterTitles = chapterTitles;
		this.chapters = chapters;
	}
	
	public String getType()
	{
		return TYPENAME;
	}

	public String getInfo()
	{
		String info = super.getInfo() + author + "\t" + narrator;
		for (int i = 0; i < chapterTitles.size(); i++) {
			info += "\n" + chapterTitles.get(i) + "\t" + chapters.get(i);
		}
		return info.toUpperCase();
	}

	// Print information about the audiobook. First print the basic information of the AudioContent
	// by making use of the printInfo() method in superclass AudioContent and then print author and narrator
	public void printInfo()
	{
		super.printInfo();
		System.out.println("Author: " + author + " Narrated by: " + narrator);
	}
	
  	// Play the audiobook by setting the audioFile to the current chapter title (from chapterTitles array list)
	// followed by the current chapter (from chapters array list)
	public void play()
	{
		this.setAudioFile(chapterTitles.get(currentChapter) + ".\n" + chapters.get(currentChapter));
		super.play();
	}
	
	// Print the table of contents of the book - i.e. the list of chapter titles
	public void printTOC()
	{
		for (int i = 0; i < chapterTitles.size(); i++)
		{
			System.out.println("Chapter " + (i+1) + ". " + chapterTitles.get(i) + "\n");
		}
	}

	// Select a specific chapter to play
	public void selectChapter(int chapter)
	{
		if (chapter >= 1 && chapter <= chapters.size())
		{
			currentChapter = chapter - 1;
		}
	}
	
	//Two AudioBooks are equal if their AudioContent information is equal and both the author and narrators are equal
	public boolean equals(Object other)
	{
		AudioBook othercon = (AudioBook) other;
		return super.equals(othercon) && author.equals(othercon.author) && narrator.equals(othercon.narrator);
	}
	
	public int getNumberOfChapters()
	{
		return chapters.size();
	}

	public String getAuthor()
	{
		return author;
	}

	public void setAuthor(String author)
	{
		this.author = author;
	}

	public String getNarrator()
	{
		return narrator;
	}

	public void setNarrator(String narrator)
	{
		this.narrator = narrator;
	}

	public ArrayList<String> getChapterTitles()
	{
		return chapterTitles;
	}

	public void setChapterTitles(ArrayList<String> chapterTitles)
	{
		this.chapterTitles = chapterTitles;
	}

	public ArrayList<String> getChapters()
	{
		return chapters;
	}

	public void setChapters(ArrayList<String> chapters)
	{
		this.chapters = chapters;
	}

}
