
public class Book extends MediaItem implements Loanable {
	// Has unique variables genre and isbn and inherits the rest from MediaItem class
	private String genre;
	private long isbn;
	
	// Prints item details
	@Override
	protected String getItemDetails() 
	{
		return "ITEM: " + itemID + "," +  "TITLE " + title + "," + "AUTHOR " + authorOrDirector + "," +
				   "PUBLICATION_YEAR: "+ publicationYear + "," + "AVAILABLE: " + isAvailable + "," + "GENRE: " + genre + "," + "ISBN: " + isbn;
	}
	// Unique implementation of loanable behavior
	public void loan() 
	{
		if (this.isAvailable) 
		{
			System.out.println(this.title + " has been loaned out.\n");
			this.isAvailable = false;
		}
		else 
			System.out.println("Cannot loan unavailable item!\n");
		
	}
	
	
	// Constructor
	public Book(String itemID, String title, String authorOrDirector, int publicationYear, boolean isAvailable,
			String genre, long isbn) 
	{
		// super to get inherited values
		super(itemID, title, authorOrDirector, publicationYear, isAvailable);
		this.genre = genre;
		this.isbn = isbn; 
	}
}
