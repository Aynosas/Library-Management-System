
public abstract class MediaItem {
	protected String itemID;
	protected String title;
	protected String authorOrDirector;
	protected int publicationYear;
	protected boolean isAvailable;
	
	// Getter and Setters
	public String getItemID() {return itemID;}
	public void setItemID(String itemID) {this.itemID = itemID;}
	public String getTitle() {return title;}
	public void setTitle(String title) {this.title = title;}
	public String getAuthorOrDirector() {return authorOrDirector;}
	public void setAuthorOrDirector(String authorOrDirector) {this.authorOrDirector = authorOrDirector;}
	public int getPublicationYear() {return publicationYear;}
	public void setPublicationYear(int publicationYear) {this.publicationYear = publicationYear;}
	public boolean getIsAvailable() {return isAvailable;}
	public void setIsAvailable(boolean isAvailable) {this.isAvailable = isAvailable;}
	
	// Displays messages and sets isAvailable to false to let user know an item has been checked out
	protected void checkOut() 
	{
		if (isAvailable == false) 
			System.out.println("Item has already been checked/loaned out!\n");
		else 
		{
			System.out.println(title + " has been checked out\n");
			isAvailable = false;
		}
	}
	// Reverse logic from checkout
	protected void returnItem() 
	{
		if (isAvailable == true) 
			System.out.println("Item not been checked/loaned out yet!\n");
		else 
		{
			System.out.println(title + " has been returned\n");
			isAvailable = true;
		}
		
	}
	
	// There is no universal method to properly display class details so this exists to 
	// delegate responsibility to the individual classes automatically to produce the same result
	@Override
	public String toString() 
	{
		return this.getItemDetails();
	}
	
	public String getFieldKey(int sortSelection) {
	    switch(sortSelection) {
	        case 1: return itemID;
	        case 2: return title;
	        case 3: return authorOrDirector;
	        // Converted to string to work with normal logic
	        case 4: return String.valueOf(publicationYear);
	        default: return itemID;
	    }
	}
	
	public MediaItem(String itemID, String title, String authorOrDirector, int publicationYear, boolean isAvailable) 
	{
		this.itemID = itemID;
		this.title = title;
		this.authorOrDirector = authorOrDirector;
		this.publicationYear = publicationYear;
		this.isAvailable = isAvailable; 
	}
	
	
	protected abstract String getItemDetails(); 
	
	// To avoid repeating the sort logic, create an abstract method to delegate implementation to the 
	// child classes from the parent class instance that will return the appropriate field/metric to
	// use for sorting
	//protected abstract String getSortKey(int sortSelection);
}
