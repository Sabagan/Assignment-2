import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/*
 * This class manages, stores, and plays audio content such as songs, podcasts and audiobooks. 
 */
public class Library
{
	private ArrayList<Song> 			songs; 
	private ArrayList<AudioBook> 	audiobooks;
	private ArrayList<Playlist> 	playlists;

	public Library()
	{
		songs 		= new ArrayList<Song>();
		audiobooks 	= new ArrayList<AudioBook>(); ;
		playlists   = new ArrayList<Playlist>();
	}

	/**
	 * Download audio content from the store. Since we have decided (design decision) to keep 3 separate lists in our library
	 * to store our songs, podcasts and audiobooks (we could have used one list) then we need to look at the type of
	 * audio content to determine which list it belongs to above
	 *
	 * @param content
	 */
	public void download(AudioContent content)
	{
		if (content == null)
			throw new AudioContentNotFoundException("Content Not Found in Store");
		else if (content.getType().equals(Song.TYPENAME)) {
			Song song = (Song) content;
			for (int i = 0; i < songs.size(); i++) {
				if (songs.get(i).equals(song)) {
					throw new AlreadyDownloadedException("Song " + song.getTitle() + " Already Downloaded");
				}
			}
			songs.add(song);
			System.out.println(Song.TYPENAME + " " + song.getTitle() + " Added to Library");
		}
		else if (content.getType().equals(AudioBook.TYPENAME)) {
			AudioBook audioBook = (AudioBook) content;
			for (int i = 0; i < audiobooks.size(); i++) {
				if (audiobooks.get(i).equals(audioBook)) {
					throw new AlreadyDownloadedException("AudioBook " + audioBook.getTitle() + " Already Downloaded");
				}
			}
			audiobooks.add(audioBook);
			System.out.println(AudioBook.TYPENAME + " " + audioBook.getTitle() + " Added to Library");
		}
	}

	// Print Information (printInfo()) about all songs in the array list
	public void listAllSongs()
	{
		for (int i = 0; i < songs.size(); i++) {
			int index = i + 1;
			System.out.print("" + index + ". ");
			songs.get(i).printInfo();
			System.out.println();
		}
	}
	
	// Print Information (printInfo()) about all audiobooks in the array list
	public void listAllAudioBooks()
	{
		for (int i = 0; i < audiobooks.size(); i++) {
			int index = i + 1;
			System.out.print("" + index + ". ");
			audiobooks.get(i).printInfo();
			System.out.println();
		}
	}

  // Print the name of all playlists in the playlists array list
	// First print the index number as in listAllSongs() above
	public void listAllPlaylists()
	{
		for (int i = 0; i < playlists.size(); i++) {
			int index = i + 1;
			System.out.println("" + index + ". " + playlists.get(i).getTitle());
		}
	}
	
  // Print the name of all artists. 
	public void listAllArtists()
	{
		// First create a new (empty) array list of string 
		// Go through the songs array list and add the artist name to the new arraylist only if it is
		// not already there. Once the artist arrayl ist is complete, print the artists names
		boolean artist_recorded = true;      // whether artist name is already recorded in the list
		ArrayList<String> artist_name = new ArrayList<>();   // Stores all the artist names
		artist_name.add(songs.get(0).getArtist());    // Add the name of the first artist
		// Stores artist names in a list without duplicates
		for (int i = 1; i < songs.size(); i++) {
			for (int j = 0; j < artist_name.size(); j++) {
				if (songs.get(i).getArtist().equals(artist_name.get(j))) {
					artist_recorded = true;
					break; // Because artist name was already recorded
				}
				else
					artist_recorded = false;
			}
			if (!artist_recorded)
				artist_name.add(songs.get(i).getArtist());
		}
		// Prints the artist names
		for (int i = 0; i < artist_name.size(); i++) {
			System.out.println((i + 1) + ". " + artist_name.get(i));
		}
	}

	// Delete a song from the library (i.e. the songs list) - 
	// also go through all playlists and remove it from any playlist as well if it is part of the playlist
	public void deleteSong(int index)
	{
		// remove the song from playlist
		for (int i = 0; i < playlists.size(); i++) {
			for (int j = 0; j < playlists.get(i).getContentSize(); j++) {
				if (songs.get(index - 1).equals(playlists.get(i).getContent().get(j))) {
					playlists.get(i).getContent().remove(j);
					break; // Because there's no duplicates in a playlist
				}
			}
		}
		// remove song from song list
		if (index < 1 || index > songs.size()) {
			throw new AudioContentNotFoundException("Song Not Found");
		}
		songs.remove(index-1);
	}
	
  	//Sort songs in library by year
	public void sortSongsByYear()
	{
		Collections.sort(songs, new SongYearComparator());
	}
  	// Write a class SongYearComparator that implements
	// the Comparator interface and compare two songs based on year
	private class SongYearComparator implements Comparator<Song>
	{
		public int compare(Song s1, Song s2) {
			if (s1.getYear() > s2.getYear())
				return 1;
			else if (s1.getYear() < s2.getYear())
				return -1;
			else
				return 0;
		}
	}

	// Sort songs by length
	public void sortSongsByLength()
	{
	 	Collections.sort(songs, new SongLengthComparator());
	}
  	// Write a class SongLengthComparator that implements
	// the Comparator interface and compare two songs based on length
  	private class SongLengthComparator implements Comparator<Song>
  	{
	  public int compare(Song s1, Song s2) {
		  if (s1.getLength() > s2.getLength())
			  return 1;
		  else if (s1.getLength() < s2.getLength())
			  return -1;
		  else
			  return 0;
	  }
  	}

	// Sort songs by title 
	public void sortSongsByName()
	{
	  // Use Collections.sort()
		// class Song should implement the Comparable interface
		// see class Song code
		Collections.sort(songs, Song::compareTo);
	}

	/*
	 * Play Content
	 */
	
	// Play song from songs list
	public void playSong(int index)
	{
		if (index < 1 || index > songs.size()) {
			throw new AudioContentNotFoundException("Song Not Found");
		}
		songs.get(index-1).play();
	}
	
	// Play a chapter of an audiobook from list of audiobooks
	public void playAudioBook(int index, int chapter)
	{
		// Checks the validity of the given index (for the audiobook)
		if (index >= 1 && index <= audiobooks.size()) {
			// Checks the validity of the given chapter in the chosen audiobook
			if (chapter >= 1 && chapter <= audiobooks.get(index - 1).getNumberOfChapters()) {
				audiobooks.get(index - 1).selectChapter(chapter);
				audiobooks.get(index - 1).play();
				return;
			}
			throw new AudioContentNotFoundException("Audiobook Not Found");
		}
		throw new AudioContentNotFoundException("Audiobook Not Found");
	}
	
	// Print the chapter titles (Table Of Contents) of an audiobook
	public void printAudioBookTOC(int index)
	{
		if (index < 1 || index > audiobooks.size()) {
			throw new AudioContentNotFoundException("Audiobook Not Found");
		}
		audiobooks.get(index-1).printTOC();
	}
	
  /*
   * Playlist Related Methods
   */
	
	// Make a new playlist and add to playlists array list
	// Make sure a playlist with the same title doesn't already exist
	public void makePlaylist(String title)
	{
		for (int i = 0; i < playlists.size(); i++) {
			if (playlists.get(i).getTitle().equals(title)) {
				throw new AlreadyExistsException("Playlist " + title + " Already Exists");
			}
		}
		Playlist playlist = new Playlist(title);
		playlists.add(playlist);
	}
	
	// Print list of content information (songs, audiobooks etc) in playlist named title from list of playlists
	public void printPlaylist(String title)
	{
		boolean exists = false;
		for (int i = 0; i < playlists.size(); i++) {
			if (playlists.get(i).getTitle().equals(title)) {
				playlists.get(i).printContents();
				exists = true;
			}
		}
		if (!exists)
			throw new PlayListNotFoundException("Playlist Not Found");
	}
	
	// Play all content in a playlist
	public void playPlaylist(String playlistTitle)
	{
		boolean exists = false;
		for (int i = 0; i < playlists.size(); i++) {
			if (playlists.get(i).getTitle().equals(playlistTitle)) {
				playlists.get(i).playAll();
				exists = true;
			}
		}
		if (!exists)
			throw new PlayListNotFoundException("Playlist Not Found");
	}
	
	// Play a specific song/audiobook in a playlist
	public void playPlaylist(String playlistTitle, int indexInPL)
	{
		boolean exists = false;
		for (int i = 0; i < playlists.size(); i++) {
			if (playlists.get(i).getTitle().equals(playlistTitle)) {
				System.out.println(playlistTitle);
				playlists.get(i).play(indexInPL);
				exists = true;
			}
		}
		if (!exists)
			throw new PlayListNotFoundException("Playlist Not Found");
	}

	// Checks to see if a specific song, audiobook or podcast is in a specific playlist
	// Gets a content parameters which refers to the specific audio content
	// Gets an index which represents the specific playlist
	public boolean findContentInPlaylist(AudioContent content, int playlist_index) {
		for (int i = 0; i < playlists.get(playlist_index).getContentSize(); i++) {
			// checks to see if we are dealing with audiocontent of the same type
			if (content.getType().equals(playlists.get(playlist_index).getContent().get(i).getType())) {
				// same audiocontent --> compare the content
				if (content.equals(playlists.get(playlist_index).getContent().get(i)))
					return false; // if content already in playlist
			}
		}
		return true; // if content isn't already in playlist
	}

	// Adds a song/audiobook/podcast from library lists at top to a playlist
	// Use the type parameter and compare to Song.TYPENAME etc
	// to determine which array list it comes from then use the given index
	// for that list
	public void addContentToPlaylist(String type, int index, String playlistTitle)
	{
		if (type.equalsIgnoreCase(Song.TYPENAME)) {
			// Search for playlist with given title and check validity of given index
			for (int i = 0; i < playlists.size(); i++) {
				if (playlists.get(i).getTitle().equals(playlistTitle)) {
					if (index >= 1 && index <= songs.size()) {
						// If song doesn't already exist in the playlist, add it
						if (findContentInPlaylist(songs.get(index - 1), i)) {
							playlists.get(i).addContent(songs.get(index - 1));
							return;
						}
						throw new AlreadyExistsException("Song Already Found In Playlist");
					}
					throw new AudioContentNotFoundException("Song Not Found");
				}
			}
			throw new PlayListNotFoundException("Playlist Not Found");
		}
		else if (type.equalsIgnoreCase(AudioBook.TYPENAME)) {
			for (int i = 0; i < playlists.size(); i++) {
				if (playlists.get(i).getTitle().equals(playlistTitle)) {
					if (index >= 1 && index <= audiobooks.size()) {
						if (findContentInPlaylist(audiobooks.get(index - 1), i)) {
							playlists.get(i).addContent(audiobooks.get(index - 1));
							return;
						}
						throw new AlreadyExistsException("AudioBook Already Found In Playlist");
					}
					throw new AudioContentNotFoundException("AudioBook Not Found");
				}
			}
			throw new PlayListNotFoundException("Playlist Not Found");
		}
		throw new AudioContentNotFoundException("Content Not Found in Store");
	}

  	// Delete a song/audiobook/podcast from a playlist with the given title
	// Make sure the given index of the song/audiobook/podcast in the playlist is valid 
	public void delContentFromPlaylist(int index, String title) {
		for (int i = 0; i < playlists.size(); i++) {
			// Check if a playlist with the given name exists
			if (playlists.get(i).getTitle().equals(title)) {
				if (playlists.get(i).contains(index)) {
					playlists.get(i).deleteContent(index);
					return;
				}
				throw new AudioContentNotFoundException("AudioContent Not Found");
			}
		}
		throw new PlayListNotFoundException("Playlist Not Found");
	}
	
}

class AudioContentNotFoundException extends RuntimeException
{
	public AudioContentNotFoundException() {}
	public AudioContentNotFoundException(String msg) { super(msg); }
}

class PlayListNotFoundException extends RuntimeException
{
	public PlayListNotFoundException() {}
	public PlayListNotFoundException(String msg) { super(msg); }
}

class AlreadyDownloadedException extends RuntimeException
{
	public AlreadyDownloadedException() {}
	public AlreadyDownloadedException(String msg) { super(msg); }
}

class AlreadyExistsException extends RuntimeException
{
	public AlreadyExistsException() {}
	public AlreadyExistsException(String msg) { super(msg); }
}

class NoMatchException extends RuntimeException
{
	public NoMatchException() {}
	public NoMatchException(String msg) { super(msg); }
}
