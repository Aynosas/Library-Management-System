
public class Magazine extends MediaItem implements Loanable  {
	private int issueNumber;
	private String month;
	
	
	@Override
	protected String getItemDetails() 
	{
		return "ITEM: " + itemID + "," +  "TITLE " + title + "," + "AUTHOR " + authorOrDirector + "," +
				   "PUBLICATION_YEAR: "+ publicationYear + "," + "AVAILABLE: " + isAvailable + "," + "ISSUE#: " + issueNumber + "," + "MONTH: " + month;
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
	
	
	public Magazine(String itemID, String title, String authorOrDirector, int publicationYear, boolean isAvailable,
			int issueNumber, String month) 
	{
		super(itemID, title, authorOrDirector, publicationYear, isAvailable);
		this.issueNumber = issueNumber; 
		this.month = month;
	}
}
