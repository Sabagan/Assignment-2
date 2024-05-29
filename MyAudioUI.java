import java.util.ArrayList;
import java.util.Scanner;

// Simulation of a Simple Text-based Music App (like Apple Music)

public class MyAudioUI
{
	private static Scanner scanner = new Scanner(System.in);
	public static void main(String[] args)
	{
		// Simulation of audio content in an online store
		// The songs, podcasts, audiobooks in the store can be downloaded to your mylibrary
		AudioContentStore store = new AudioContentStore();
		
		// Create my music mylibrary
		Library mylibrary = new Library();

		//Scanner scanner = new Scanner(System.in);
		System.out.print(">");

		// Process keyboard actions
		while (scanner.hasNextLine())
		{
			String action = scanner.nextLine();
			try
			{
				if (action == null || action.equals("")) {
					System.out.print("\n>");
					continue;
				}
				else if (action.equalsIgnoreCase("Q") || action.equalsIgnoreCase("QUIT"))
					return;
				else if (action.equalsIgnoreCase("STORE"))    // List all songs
				{
					store.listAll();
				}
				else if (action.equalsIgnoreCase("SONGS"))    // List all songs
				{
					mylibrary.listAllSongs();
				}
				else if (action.equalsIgnoreCase("BOOKS"))    // List all songs
				{
					mylibrary.listAllAudioBooks();
				}
				else if (action.equalsIgnoreCase("ARTISTS"))    // List all songs
				{
					mylibrary.listAllArtists();
				}
				else if (action.equalsIgnoreCase("PLAYLISTS"))    // List all play lists
				{
					mylibrary.listAllPlaylists();
				}
				else if (action.equalsIgnoreCase("SEARCH"))    // Lists all Content with given title
				{
					String title = InputWord("Title");
					store.findTitle(title);
				}
				else if (action.equalsIgnoreCase("SEARCHA"))    // Lists all Content with given artist
				{
					String creator = InputWord("Artist");
					store.findArtist(creator);
				}
				else if (action.equalsIgnoreCase("SEARCHG"))    // Lists all Songs with the given genre
				{
					String genre = InputWord("Genre [POP, ROCK, JAZZ, HIPHOP, RAP, CLASSICAL]");
					store.findGenre(genre);
				}
				else if (action.equalsIgnoreCase("SEARCHP"))    // Lists all Content with the target string
				{
					String target = InputWord("General Search Term");
					store.findGeneral(target);
				}
				else if (action.equalsIgnoreCase("DOWNLOAD"))  // Download multiple audiocontent (song/audiobook) from the store
				{
					int index = InputInteger("From Store Content #");
					int end = InputInteger("To Store Content #");
					AudioContent content;
					if (index == 0 || end == 0 || end < index) // For these cases, we return an error
					{
						content = store.getContent(0);
						mylibrary.download(content);
					}
					else
					{
						for (int i = index; i <= end; i++)
						{
							try
							{
								content = store.getContent(i);
								mylibrary.download(content); // Completes the download
							} catch (AudioContentNotFoundException e)
							{
								System.out.println(e.getMessage());
								break; // we only want to print the error once
							}
							catch (AlreadyDownloadedException e) {
								System.out.println(e.getMessage()); // print for every song/audiobook
							}
						}
					}
				}
				else if (action.equalsIgnoreCase("DOWNLOADA"))  // Download audiocontent (song/audiobook/podcast) with specific artist name from the store
				{
					String creator = InputWord("Artist Name");
					ArrayList<Integer> indices = store.getContentWithArtist(creator);
					for (int i : indices)
					{
						// The process of downloading from the previous statement is repeated here
						try
						{
							AudioContent content = store.getContent(i + 1);
							mylibrary.download(content);
						}
						catch (AlreadyDownloadedException e)
						{
							System.out.println(e.getMessage());
						}
					}
				}
				else if (action.equalsIgnoreCase("DOWNLOADG"))  // Download song with specific genre from the store
				{
					String genre = InputWord("Genre");
					ArrayList<Integer> indices = store.getContentWithGenre(genre);
					for (int i : indices)
					{
						// The process of downloading from the previous statement is repeated here
						try
						{
							AudioContent content = store.getContent(i + 1);
							mylibrary.download(content);
						}
						catch (AlreadyDownloadedException e)
						{
							System.out.println(e.getMessage());
						}
					}
				}
				// Get the *library* index (index of a song based on the songs list)
				// of a song from the keyboard and play the song
				else if (action.equalsIgnoreCase("PLAYSONG"))
				{
					int index = InputInteger("Song Number");
					mylibrary.playSong(index);
				}
				// Print the table of contents (TOC) of an audiobook that has been downloaded to the library.
				// Get the desired book index from the keyboard - the index is based on the list of books in the library
				else if (action.equalsIgnoreCase("BOOKTOC"))
				{
					int index = InputInteger("Audio Book Number");
					mylibrary.printAudioBookTOC(index);
				}
				// Similar to playsong above except for audio book. In addition to the book index, read the chapter
				// number from the keyboard
				else if (action.equalsIgnoreCase("PLAYBOOK"))
				{
					int index = InputInteger("Audio Book Number");
					int chapter = InputInteger("Chapter");
					mylibrary.playAudioBook(index, chapter);
				}
				// Specify a playlist title (string)
				// Play all the audio content (songs, audiobooks, podcasts) of the playlist
				else if (action.equalsIgnoreCase("PLAYALLPL"))
				{
					String title = InputWord("Playlist Title");
					mylibrary.playPlaylist(title);
				}
				// Specify a playlist title (string)
				// Read the index of a song/audiobook/podcast in the playist from the keyboard
				// Play all the audio content
				else if (action.equalsIgnoreCase("PLAYPL"))
				{
					String title = InputWord("Playlist Title");
					int index = InputInteger("Content Number");
					mylibrary.playPlaylist(title, index);
				}
				// Delete a song from the list of songs in mylibrary and any play lists it belongs to
				// Read a song index from the keyboard
				else if (action.equalsIgnoreCase("DELSONG"))
				{
					int index = InputInteger("Library Song #");
					mylibrary.deleteSong(index);
				}
				// Read a title string from the keyboard and make a playlist
				else if (action.equalsIgnoreCase("MAKEPL"))
				{
					String title = InputWord("Playlist TItle");
					mylibrary.makePlaylist(title);
				}
				// Print the content information (songs, audiobooks, podcasts) in the playlist
				// Read a playlist title string from the keyboard
				else if (action.equalsIgnoreCase("PRINTPL"))    // print playlist content
				{
					String title = InputWord("Playlist Title");
					mylibrary.printPlaylist(title);
				}
				// Add content (song, audiobook, podcast) from mylibrary (via index) to a playlist
				// Read the playlist title, the type of content ("song" "audiobook" "podcast")
				// and the index of the content (based on song list, audiobook list etc) from the keyboard
				else if (action.equalsIgnoreCase("ADDTOPL"))
				{
					String title = InputWord("Playlist Title");
					String contentType = InputWord("Content Type [SONG, AUDIOBOOK]");
					int index = InputInteger("Library Content #");
					mylibrary.addContentToPlaylist(contentType, index, title);
				}
				// Delete content from play list based on index from the playlist
				// Read the playlist title string and the playlist index
				else if (action.equalsIgnoreCase("DELFROMPL"))
				{
					String title = InputWord("Playlist Title");
					int index = InputInteger("Playlist Content #");
					mylibrary.delContentFromPlaylist(index, title);
				}
				else if (action.equalsIgnoreCase("SORTBYYEAR")) // sort songs by year
				{
					mylibrary.sortSongsByYear();
				}
				else if (action.equalsIgnoreCase("SORTBYNAME")) // sort songs by name (alphabetic)
				{
					mylibrary.sortSongsByName();
				}
				else if (action.equalsIgnoreCase("SORTBYLENGTH")) // sort songs by length
				{
					mylibrary.sortSongsByLength();
				}
			}
			catch (AudioContentNotFoundException | AlreadyDownloadedException | AlreadyExistsException | NoMatchException | PlayListNotFoundException e)
			{
				System.out.println(e.getMessage());
			}
			System.out.print("\n>");
		}
	}

	/**
	 * Collect an integer as input, if not an integer, keep asking until you get an integer
	 * @param askFor
	 * @return The input collected from the user
	 */
	private static int InputInteger(String askFor)
	{
		int input = 0;
		String inputWord = InputWord(askFor);
		try {
			input = Integer.parseInt(inputWord);
		}
		catch (NumberFormatException e) {
			System.out.print("");
		}
		return input;
	}

	/**
	 * This method is used to collect user input where expected input is a string
	 * @param askFor
	 * @return The input collected from the user
	 */
	private static String InputWord(String askFor)
	{
		String vars = "";
		System.out.print(askFor + ": ");
		if (scanner.hasNextLine())
		{
			vars = scanner.nextLine();
		}
		return vars;
	}

}
