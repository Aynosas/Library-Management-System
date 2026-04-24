
public class DVD extends MediaItem implements Loanable  {
	private int duration;
	private String rating;
	
	
	@Override
	protected String getItemDetails() 
	{
		return "ITEM: " + itemID + "," +  "TITLE " + title + "," + "AUTHOR " + authorOrDirector + "," +
				   "PUBLICATION_YEAR: "+ publicationYear + "," + "AVAILABLE: " + isAvailable + "," + "DURATION: " + duration + "," + "RATING: " + rating;
	}
	
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
	
	
	public DVD(String itemID, String title, String authorOrDirector, int publicationYear, boolean isAvailable,
			int duration, String rating) 
	{
		super(itemID, title, authorOrDirector, publicationYear, isAvailable);
		this.duration = duration;
		this.rating = rating; 
	}
}
