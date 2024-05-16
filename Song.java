// Name: Sabagan Chandrakanthan
// Student ID: 501175020

/*
 * A Song is a type of AudioContent. A Song has extra fields such as Artist (person(s) singing the song) and composer 
 */
public class Song extends AudioContent implements Comparable<Song>// implement the Comparable interface
{
	public static final String TYPENAME = "SONG";
	
	public static enum Genre {POP, ROCK, JAZZ, HIPHOP, RAP, CLASSICAL}; 
	private String artist; 		// Can be multiple names separated by commas
	private String composer; 	// Can be multiple names separated by commas
	private Genre  genre; 
	private String lyrics;
	
	
	
	public Song(String title, int year, String id, String type, String audioFile, int length, String artist,
			String composer, Song.Genre genre, String lyrics)
	{
		// Make use of the constructor in the super class AudioContent.
		super(title, year, id, type, audioFile, length);
		// Initialize additional Song instance variables.
		this.artist = artist;
		this.composer = composer;
		this.genre = genre;
		this.lyrics = lyrics;
	}
	
	public String getType()
	{
		return TYPENAME;
	}

	public String getInfo()
	{
		String info = super.getInfo() + artist + "\t" + composer + "\t" + genre + "\t" + lyrics;
		return info.toUpperCase();
	}

	// Print information about the song. First print the basic information of the AudioContent
	// by making use of the printInfo() method in superclass AudioContent and then print artist, composer, genre
	public void printInfo()
	{
		super.printInfo();
		System.out.println("Artist: " + artist + " Composer: " + composer + " Genre: " + genre);
	}
	
	// Play the song by setting the audioFile to the lyrics string and then calling the play() method of the superclass
	public void play()
	{
		setAudioFile(lyrics);
		super.play();
	}
	
	public String getComposer()
	{
		return composer;
	}
	public void setComposer(String composer)
	{
		this.composer = composer;
	}
	
	public String getArtist()
	{
		return artist;
	}
	public void setArtist(String artist)
	{
		this.artist = artist;
	}
	
	public String getLyrics()
	{
		return lyrics;
	}
	public void setLyrics(String lyrics)
	{
		this.lyrics = lyrics;
	}

	public Genre getGenre()
	{
		return genre;
	}

	public void setGenre(Genre genre)
	{
		this.genre = genre;
	}	
	
	// Two songs are equal if their AudioContent information is equal and both the composer and artists are the same
	// Make use of the superclass equals() method
	public boolean equals(Object other)
	{
		Song othercon = (Song) other;
		return super.equals(othercon) && composer.equals(othercon.composer) && artist.equals(othercon.artist);
	}
	
	// Implement the Comparable interface 
	// Compare two songs based on their title
	// This method will allow songs to be sorted alphabetically
	public int compareTo(Song other)
	{
		return this.getTitle().compareTo(other.getTitle());
	}

	// Allows us to convert the string input of genre to the enum Genre
	public static Song.Genre convertToGenre(String genre)
	{
		genre = genre.toUpperCase();
		if (genre.equals("JAZZ"))
			return Genre.JAZZ;
		else if (genre.equals("POP"))
			return Genre.POP;
		else if (genre.equals("ROCK"))
			return Genre.ROCK;
		else if (genre.equals("HIPHOP"))
			return Genre.HIPHOP;
		else if (genre.equals("CLASSICAL"))
			return Genre.CLASSICAL;
		else if (genre.equals("RAP"))
			return Genre.RAP;
		else
			return null;
	}
}
