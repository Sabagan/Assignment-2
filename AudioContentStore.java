// Name: Sabagan Chandrakanthan
// Student ID: 501175020

import java.io.File;
import java.io.IOException;
import java.util.*;

// Simulation of audio content in an online store
// The songs, podcasts, audiobooks listed here can be "downloaded" to your library
public class AudioContentStore
{
	private ArrayList<AudioContent> contents;
	private HashMap<String, Integer> titleMap;
	private HashMap<String, ArrayList<Integer>> artistMap;
	private HashMap<String, ArrayList<Integer>> genreMap;

	public AudioContentStore()  {
		try {
			readFile();
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
		// We will initialize these Maps in the constructor and use an update method to add values
		titleMap = new HashMap<String, Integer>();
		updateTitleMap();
		artistMap = new HashMap<String, ArrayList<Integer>>();
		updateArtistMap();
		genreMap = new HashMap<String, ArrayList<Integer>>();
		updateGenreMap();
	}

	/**
	 * Create the file and the scanner that we will use to read and fill in the store
	 * Based on whether we are reading in info about a Song/Audiobook, we can call the specific read Method
	 * @throws IOException
	 */
	private void readFile() throws IOException {
		contents = new ArrayList<AudioContent>();
		File store = new File("store.txt");
		Scanner in = new Scanner(store);
		// We check nextLine to see if there's another AudioContent
		while (in.hasNextLine())
		{
			String line = in.nextLine(); // the type of AudioContent we are dealing with

			String id = in.nextLine();
			String title = in.nextLine();
			int year = in.nextInt();
			in.nextLine();
			int length = in.nextInt();
			in.nextLine();
			String artist = in.nextLine(); // author
			String composer = in.nextLine(); // narrator

			if (line.equals(Song.TYPENAME))
			{
				String genre = in.nextLine();
				int numOfLines = in.nextInt();
				in.nextLine();

				String file = ""; // we read the lyrics here
				for (int i = 0; i < numOfLines; i++) {
					file += in.nextLine() + "\n";
				}

				System.out.println("Loading " + Song.TYPENAME);
				contents.add(new Song(title, year, id, Song.TYPENAME, file, length, artist, composer, Song.convertToGenre(genre), file));
			}
			else if (line.equals(AudioBook.TYPENAME))
			{
				int numOfChapters = in.nextInt();
				in.nextLine();

				ArrayList<String> titles = new ArrayList<String>();
				for (int i = 0; i < numOfChapters; i++)
				{
					titles.add(in.nextLine()); // we read the titles of the chapter here
				}

				int numOfLinesInChapter = in.nextInt();
				in.nextLine();
				ArrayList<String> chapters = new ArrayList<String>();
				for (int i = 0; i < numOfChapters; i++)
				{
					String chapter = "";
					for (int j = 0; j < numOfLinesInChapter; j++)
					{
						chapter += in.nextLine() + "\n"; // we read the chapters for each chapter title here
					}
					chapters.add(chapter); // This is where we add the chapters content to the chapters
					if (in.hasNextInt())
					{
						numOfLinesInChapter = in.nextInt(); // The number of lines in the next chapter are different, so we read to find out how many lines
						in.nextLine(); // consume the next line character
					}
				}

				System.out.println("Loading " + AudioBook.TYPENAME);
				contents.add(new AudioBook(title, year, id, AudioBook.TYPENAME, "", length, artist, composer, titles, chapters));
			}
		}
		in.close(); // Added on 2024-04-27
	}

	/**
	 * Adds the Titles of a piece of AudioContent along with its associated rank in the store into a HashMap
	 */
	private void updateTitleMap()
	{
		for (int i = 0; i < contents.size(); i++)
		{
			titleMap.put(contents.get(i).getTitle(), i);
		}
	}

	/**
	 * Adds the Artist/Author name of a piece of AudioContent along with its associated rank in the store into a HashMap
	 */
	private void updateArtistMap() {
		for (int i = 0; i < contents.size(); i++) {
			if (contents.get(i).getType().equals(Song.TYPENAME)) {
				Song song = (Song) contents.get(i);
				if (!artistMap.containsKey(song.getArtist())) // the artist hasn't already been added to the map
				{
					artistMap.put(song.getArtist(), new ArrayList<>()); // we add the key (artist) to map with empty array list (represents indices)
					artistMap.get(song.getArtist()).add(i); // we add to the indices array list now
				}
				else
					artistMap.get(song.getArtist()).add(i); // add to the indices array list of the specified artist in the map
			}
			else if (contents.get(i).getType().equals(AudioBook.TYPENAME)) {
				AudioBook book = (AudioBook) contents.get(i);
				if (!artistMap.containsKey(book.getAuthor())) {
					artistMap.put(book.getAuthor(), new ArrayList<>());
					artistMap.get(book.getAuthor()).add(i);
				} else
					artistMap.get(book.getAuthor()).add(i);
			}
		}
	}

	/**
	 * Adds the Genre of a Song along with its associated rank in the store into a HashMap
	 */
	private void updateGenreMap()
	{
		for (int i = 0; i < contents.size(); i++) {
			if (contents.get(i).getType().equals(Song.TYPENAME)) {
				Song song = (Song) contents.get(i);
				if (!genreMap.containsKey(song.getGenre().toString())) // if a genre isn't in the map
				{
					genreMap.put(song.getGenre().toString(), new ArrayList<>()); // add the genre in string form with an Integer array list
					genreMap.get(song.getGenre().toString()).add(i);  // add the index of the song to the array list since genre matches
				} else
					genreMap.get(song.getGenre().toString()).add(i); // map index of song to specified genre
			}
		}
	}

	/**
	 * Print information about the Content with the specific title, used for SEARCH
	 * @param title
	 */
	public void findTitle(String title)
	{
		if (titleMap.get(toTitleCase(title)) == null)
			throw new NoMatchException("No matches for " + title);
		int index = titleMap.get(toTitleCase(title));
		System.out.print(index+1 + ". ");
		contents.get(index).printInfo();
	}

	/**
	 * Prints information about the Content with the specific artist/author name, used for SEARCHA
	 * @param artist
	 */
	public void findArtist(String artist)
	{
		if (artistMap.get(toTitleCase(artist)) == null)
			throw new NoMatchException("No matches for " + artist);
		ArrayList<Integer> indices = artistMap.get(toTitleCase(artist));
		for (int i = 0; i < indices.size(); i++)
		{
			System.out.print(indices.get(i)+1 + ". ");
			contents.get(indices.get(i)).printInfo();
			System.out.println();
		}
	}

	/**
	 * Prints information about the Song with the specific Genre, used for SEARCHG
	 * @param genre
	 */
	public void findGenre(String genre)
	{
		if (genreMap.get(genre.toUpperCase()) == null)
			throw new NoMatchException("No matches for " + genre);
		ArrayList<Integer> indices = genreMap.get(genre.toUpperCase());
		for (int i = 0; i < indices.size(); i++)
		{
			System.out.print(indices.get(i)+1 + ". ");
			contents.get(indices.get(i)).printInfo();
			System.out.println();
		}
	}

	/**
	 * Prints information about the Song with the specific target, used for SEARCHP
	 * @param target
	 */
	public void findGeneral(String target)
	{
		boolean searchResultFound = false;
		// Use the contains() method provided by the String class to check for the presence of a substring
		for (int i = 0; i < contents.size(); i++) {
			AudioContent content = contents.get(i);
			if (content.getInfo().contains(target.toUpperCase())) {
				System.out.print(i+1 + ". ");
				content.printInfo();
				searchResultFound = true;
			}
		}
		if (!searchResultFound) // if no search result was found
			throw new NoMatchException("No matches for " + target);
	}

	/**
	 * Used to assist with DOWNLOADA
	 * @param artist
	 * @return An Array List of indices indicating the Store ranks for Content with the specific artist/author name
	 */
	public ArrayList<Integer> getContentWithArtist(String artist)
	{
		artist = toTitleCase(artist);
		if (artistMap.get(artist) == null)
			throw new AudioContentNotFoundException("Content Not Found in Store");
		return artistMap.get(artist);
	}

	/**
	 * Used to assist with DOWNLOADG
	 * @param genre
	 * @return An Array list of indices indicating the store rank of a song with the specific Genre
	 */
	public ArrayList<Integer> getContentWithGenre(String genre)
	{
		if (genreMap.get(genre.toUpperCase()) == null)
			throw new AudioContentNotFoundException("Content Not Found in Store");
		return genreMap.get(genre.toUpperCase());
	}

	/**
	 * @param index
	 * @return A Content in the list of contents in the store with the given rank in the store
	 */
	public AudioContent getContent(int index)
	{
		if (index < 1 || index > contents.size())
		{
			return null;
		}
		return contents.get(index-1);
	}

	/**
	 * Prints information about the Content in the store
	 */
	public void listAll()
	{
		for (int i = 0; i < contents.size(); i++)
		{
			int index = i + 1;
			System.out.print("" + index + ". ");
			contents.get(i).printInfo();
			System.out.println();
		}
	}

	/**
	 * This is an alternative to equalsIgnoreCase and the use of looping in the map to see if there is a match for
	   a key value
	 * Used for Title and Genre, both search and download, to be compatible with the "Map.get(KEY)"
	 * @return the given string with first letter of every word capitalized
	 */
	private String toTitleCase(String adj)
	{
		String str = "";
		Scanner scan = new Scanner(adj);
		while (scan.hasNext()) {
			String word = scan.next();
			if (word.contains("."))
				str += word.toUpperCase() + " ";
			else
				str += word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase() + " ";
		}
		scan.close(); // Added on 2024-04-27
		return str.trim();
	}

}