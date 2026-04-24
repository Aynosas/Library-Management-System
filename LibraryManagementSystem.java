import java.util.ArrayList;
import java.util.InputMismatchException;
import java.io.File;
import java.util.Scanner;

// Runs the interactable interface and parses inventory file
public class LibraryManagementSystem {
	// Using a string to be able to check blank input creates the problem of other characters being valid input, as they will
	// be converted to int (ASCII). A simple fix is to check the itemStr vs a list of valid inputs (0-9) and throw the
	// correct exception. This is const, so it can be global to reduce function signature.
	final static char[] VALID_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
	final static int MAIN_MENU_CHOICES = 6;
	final static int COLLECTION_MENU_CHOICES = 4;
	final static int SORT_MENU_CHOICES = 4;
	
	
	// Save inventory path in a const var bc it doesn't change
	private final static String inventory = "inventory.txt";
	// ArrayList to easily add items into a list. The type specified is the parent class, which all child classes
	// are deriving from, so type issues don't occur.
	private static ArrayList<MediaItem> mediaList = new ArrayList<MediaItem>();
	
	// Flow control
	public static void main(String[] args) 
	{
		loadFile(inventory);
		runInterface();
	}
	
	// Displays each item and if it's available
	private static void displayItems() 
	{
		for(int i = 0; i < mediaList.size(); i++) 
		{
			// Current Media object as string
			String currMediaStr = mediaList.get(i).getItemDetails();
			String[] currMediaList = currMediaStr.split(",");
			
			// Force aligns entries to left
			System.out.printf("%-10s", (i+1) + ". ");
			for (int j = 0; j < currMediaList.length; j++) 
			{
				// Display all object attributes
				// Allots 30 characters worth of space for each column and aligns to the left
				System.out.printf("|%-30s|", currMediaList[j]);
			}
			System.out.println();
	
		}
		System.out.println();
	}
	
	// Checks if collection is empty and displays collection. Returns a bool that represents if the collection is empty
	private static boolean isCollectionEmpty(ArrayList<ArrayList<MediaItem>> collectionList) 
	{
		// Immediately return back if there's no collection to delete
		if (collectionList.isEmpty()) {
			System.out.println("No collection(s) have been created yet!\n");
			return true;
		}
		return false;
	}
	// Checks if checkedItems is empty
	private static boolean isCheckedItemsEmpty(ArrayList<MediaItem> currentCheckedItems) 
	{
		// Immediately return back if there's no collection to delete
		if (currentCheckedItems.isEmpty()) {
			System.out.println("No currently checked out items!\n");
			return true;
		}
		return false;
	}
	// Displays current collections
	private static void displayCollections(ArrayList<ArrayList<MediaItem>> collectionList) 
	{
		int count = 0;
		// Display current collection(s)
		for (ArrayList<MediaItem> collection : collectionList)  
		{
			System.out.println(++count + ". " + collection);
		}
		System.out.println();
	}
	
	// Gets the selected collection
	private static int getCollectionSelection(ArrayList<ArrayList<MediaItem>> collectionList, Scanner scnObj) 
	{
		String collectionStr = scnObj.nextLine();
		// Pass in nothing for type. the "" indicates type is optional in this case because there are no specific behavior changes
		int collectionSelection = getValidSelection(collectionStr, scnObj, "");
		// Because there is no specific handling of the valid delete ranges in the general function, we do a quick check here
		if (collectionSelection < 1 || collectionSelection > collectionList.size())
			throw new NumberFormatException();
		
		return collectionSelection; 
	}
	
	// Used to perform all operations on collections
	private static ArrayList<ArrayList<MediaItem>> collectionOperations(int collectionSelection, ArrayList<ArrayList<MediaItem>> collectionList,
			ArrayList<MediaItem> currentCheckedItems) throws NumberFormatException
	{
		Scanner scn = new Scanner(System.in);
		int count = 0;
		
		switch(collectionSelection) 
		{
		// Adds new collection
		case 1: 
			// Make new ArrayList to store collection, add collection to collection list
			ArrayList<MediaItem> newCollection = new ArrayList<MediaItem>();
			collectionList.add(newCollection);
			return collectionList;
			
		// Deletes the selected collection
		case 2:
			// Check if empty, display collection list, and get valid selection
			if(isCollectionEmpty(collectionList)) break;
			
			displayCollections(collectionList);
			System.out.print("Enter the number of the collection you'd like to delete: ");
			int deleteSelection = getCollectionSelection(collectionList, scn);
			
			collectionList.remove(deleteSelection-1);
			return collectionList;
		
		// Sorts collection(s)
		case 3:
			if(isCollectionEmpty(collectionList) || isCheckedItemsEmpty(currentCheckedItems)) break;
			
			displayCollections(collectionList);
			System.out.println("Which collection would you like to sort? ");
			int collectionToSortSelection = getCollectionSelection(collectionList, scn);
			
			// Store selected collection to sort
			ArrayList<MediaItem> selectedSortCollection = collectionList.get(collectionToSortSelection-1);
			
			System.out.println("1. Item ID\n2. Title\n3. Author/Director\n4. Publication Year\n");
			System.out.print("Enter the number of the metric you would like to sort by: ");
			String sortStr = scn.nextLine();
			int sortSelection = getValidSelection(sortStr, scn, "");
			
			if (sortSelection < 1 || sortSelection > SORT_MENU_CHOICES)
				throw new NumberFormatException();
			
			// Once we get to a valid sort option, pass sorting responsibility to getSortedCollection(), which will return a
			// sorted list to display
			// The function performs the same sorting logic differentiated by the selection, which is passed in getSortKey() to
			// return the correct field. Another potentially easier option would be a lambda, but that hasn't been covered yet.
			
			ArrayList<MediaItem> sortedCollection = getSortedCollection(sortSelection, selectedSortCollection);
			
			// Display sorted list
			System.out.println("Sorted List Result: " + sortedCollection.toString() + "\n");
			// Replace the selected collection with the new sorted collection
			collectionList.set(collectionToSortSelection-1, sortedCollection);
			break;
			
		// Searches collection(s)
		case 4:
			if(isCollectionEmpty(collectionList) || isCheckedItemsEmpty(currentCheckedItems)) break;
			
			displayCollections(collectionList);
			System.out.println("Which collection would you like to search? ");
			int collectionToSearchSelection = getCollectionSelection(collectionList, scn);
			
			// Store selected collection to search
			ArrayList<MediaItem> selectedSearchCollection = collectionList.get(collectionToSearchSelection-1);

			System.out.println("1. Item ID\n2. Title\n3. Author/Director\n4. Publication Year\n");
			System.out.print("Enter the number of the metric you would like to search by: ");
			String searchStr = scn.nextLine();
			int searchSelection = getValidSelection(searchStr, scn, "");
			
			// The sort menu choices are the same as search
			if (searchSelection < 1 || searchSelection > SORT_MENU_CHOICES)
				throw new NumberFormatException();;
			
			System.out.print("Enter the corresponding info for your search (Ex. B001 for Item ID): ");	
			String searchEntry = scn.nextLine();
			System.out.println();
			
			findSearchedItem(searchSelection, selectedSearchCollection, searchEntry);
			break;
			
		// Adds items to collection
		case 5:	
			// If there are no collections or checked out items, return immediately
			if (isCollectionEmpty(collectionList) || isCheckedItemsEmpty(currentCheckedItems)) break;
			displayCollections(collectionList);
			
			System.out.print("Enter the number of the collection you want to add to: ");
			System.out.println();
			String collectionToAddToStr = scn.nextLine();
			int collectionToAddToSelection = getValidSelection(collectionToAddToStr, scn, "");
			
			if (collectionToAddToSelection < 1 || collectionToAddToSelection > collectionList.size())
				throw new NumberFormatException();
			
			// Display current checked items to add to collection
			count = 0;
			System.out.println("Current checked items to add to collection(s): ");
			for (MediaItem item : currentCheckedItems) 
			{
				System.out.println(++count + ". " + item.toString());
			}
			System.out.print("Enter the number of the item you want to add to the collection: ");
			String itemToAddStr = scn.nextLine();
			int itemToAddSelection = getValidSelection(itemToAddStr, scn, "");
			System.out.println();
			
			if (itemToAddSelection < 1 || itemToAddSelection > currentCheckedItems.size())
				throw new NumberFormatException();
			
			// Add new item to collection
			ArrayList<MediaItem> updatedCollection = collectionList.get(collectionToAddToSelection-1);
			MediaItem selectedItem = currentCheckedItems.get(itemToAddSelection-1);
			
			// If the selected item is already in a collection, return 
			if(updatedCollection.contains(selectedItem)) {
				System.out.println("The selected item is already in this collection!\n");
				break;
			}
			updatedCollection.add(selectedItem);
			break;	
		}
		
		return collectionList;
	}
	
	// Searches through collections for selected metric
	private static void findSearchedItem(int searchSelection, ArrayList<MediaItem> selectedCollection, String searchEntry) 
	{
		System.out.println("Found Items: ");
		for (MediaItem item : selectedCollection) 
		{
			if (item.getFieldKey(searchSelection).equalsIgnoreCase(searchEntry))
			{
				System.out.println(item.toString());
			}
		}
		System.out.println();
	}
	
	// Sorts collection based selected metric
	private static ArrayList<MediaItem> getSortedCollection(int sortSelection, ArrayList<MediaItem> selectedCollection) 
	{
		// New list to return
		ArrayList<MediaItem> sortedList = new ArrayList<MediaItem>();
		
		// Copies items into sortedList
	    sortedList.addAll(selectedCollection);
	    
	    // Compares leftmost element (current i index) to the rest of the items to see if they are smaller- in which case the
	    // minIndex is updated. Once the min index for the current iteration is determined, use a third  var to swap the position
	    // of the current item in the i index with the min index, which naturally shifts the biggest elements to the right. 
	    for (int i = 0; i < sortedList.size() - 1; i++) 
	    {
	        int minIndex = i;
	        for (int j = i + 1; j < sortedList.size(); j++) 
	        {
	            if (sortedList.get(j).getFieldKey(sortSelection).compareTo(sortedList.get(minIndex).getFieldKey(sortSelection)) < 0)
	                minIndex = j;
	        }
	        // Swap minimum element into position with set using a temporary storage variable
	        MediaItem tempMin = sortedList.get(i);
	        sortedList.set(i, sortedList.get(minIndex));
	        sortedList.set(minIndex, tempMin);
	    }
	    return sortedList;

	}
	
	// Performs double check of empty input and incorrect range
	private static int getValidSelection(String choiceStr, Scanner scnObj, String type)  
	{
		// Specific case to return early so that -1 case works properly. Otherwise it gets parsed as '-' and '1', returning IME
		if (choiceStr.equals("-1"))
			return Integer.parseInt(choiceStr); 
					
		// Delegates responsibility to check exceptions
		checkIMEAndBlank(choiceStr, scnObj);
		
		int itemOrChoiceSelect = Integer.parseInt(choiceStr);
		
		// Uses type key to find proper valid ranges
		if (type == "Choice") {
			if (itemOrChoiceSelect < 1 || itemOrChoiceSelect > MAIN_MENU_CHOICES)
				throw new NumberFormatException(); 
		}
		if (type == "Item") {
			// If user is out of range, throw exception
			if (itemOrChoiceSelect < 1 || itemOrChoiceSelect > mediaList.size())
				throw new NumberFormatException(); 
		}
		
		return itemOrChoiceSelect;
	}
	
	private static void checkIMEAndBlank(String choiceSelect, Scanner scnObj) 
	{
		// Check empty input
		if (choiceSelect.isBlank() || choiceSelect.isEmpty())
			throw new NumberFormatException();
		
		// Checks to ensure chars are 0-9
		// This will use nested loops. The outside loop tracks the char position in the user selection while the inside loop
		// will compare it to every entry in the validChar list with the bool containsChar. If containsChar isn't true by
		// the end of that iteration, then invalid input has been found
		for (int i = 0; i < choiceSelect.length(); i++) 
		{
			// Variable to track valid char
			boolean containsChar = false;
			for (int j = 0; j < VALID_CHARS.length; j++) 
			{
				if (choiceSelect.charAt(i) == (VALID_CHARS[j]))
				{
					containsChar = true;
					// Move to next char if match is found
					continue;
				}		
			}
			// As soon as an invalid char is found, throw IME
			if (!containsChar) 
				throw new InputMismatchException();
		}
	}
	
	private static void runInterface() {
		Scanner scn = new Scanner(System.in);
		// Tracks if user has finished
		boolean isFinished = false;
		// Tracks if exception occurred to keep format consistent
		boolean exceptionOccurred = false;
		// Made to keep track of which books the user has checked out of returned to 
		ArrayList<MediaItem> currentCheckedItems = new ArrayList<MediaItem>();
		// Made to store media collections. A List of lists
		ArrayList<ArrayList<MediaItem>> collectionList = new ArrayList<ArrayList<MediaItem>>();
		// Stores current loaned items
		ArrayList<MediaItem> currentLoanedItems = new ArrayList<MediaItem>();
		
		final String[] choiceList = {"View inventory", "Check out an item", "Return an item", "Collection Operations", 
				"View Checked Out Items", "Get a Loan"}; 
		final String[] collectionOptionList = {"Add New Collection", "Delete Collection", "Sort Collection", "Search Collection",
				"Add to Existing Collection"};
		
		while(!isFinished) 
		{
			if (exceptionOccurred) 
			{
				exceptionOccurred = false;
			}
			// Variable for first list of options. Start as string first to check if user 
			// just presses Enter without actually inputting a number
			String choiceStr = "";
			int choiceSelect = -1;
			// Variable to select the item after action is decided
			String itemStr = "";
			int itemSelect = -1;
			// Collection selection
			String collectionStr = "";
			int collectionSelection = -1;
			// Classifications
			String choiceLabel = "Choice";
			String itemLabel = "Item";
			String collectionLabel = "Collection";
			
			
			// Variable for current item
			MediaItem currentItem = null;
			// Count variable to display list items
			int count = 0;
			
			try {
				// Display menu options
				for (int i = 0; i < choiceList.length; i++) 
					System.out.print((i+1) + ". " + choiceList[i] + "\n"); 
				
				System.out.print("\nType in the number of your desired selection (-1 to quit): ");
				choiceStr = scn.nextLine();
				System.out.println();
				
				// Get choice. Returns as int
				choiceSelect = getValidSelection(choiceStr, scn, choiceLabel);
				
				switch(choiceSelect) 
				{
				case -1: 
					System.out.println("---EXITING PROGRAM---");
					isFinished = true;
					break; 
					
				// Prompt user to choose item, then finds it in the mediaList and calls the getItemDetails method
				case 1:
					displayItems();
					break;
				
				case 2:
					displayItems();
					System.out.print("Enter the number of the item you'd like to check out: ");
					// Validifies input first as string
					itemStr = scn.nextLine();
					// Gets actual selection as int after various checks are performed
					itemSelect = getValidSelection(itemStr, scn, itemLabel);
					currentItem = mediaList.get(itemSelect-1);
					
					// Check if item has been loaned/checked out already
					if (currentLoanedItems.contains(currentItem) || currentCheckedItems.contains(currentItem)) 
					{
						System.out.println("Item was already loaned/checked out\n");
						continue;
					}
					
					// Call checkout method and add item to checked items
					currentItem.checkOut();
					currentCheckedItems.add(currentItem);
					break;
					
				case 3:
					displayItems();
					System.out.print("Enter the number of the item you'd like to return: ");
					itemStr = scn.nextLine();
					itemSelect = getValidSelection(itemStr, scn, itemLabel);
					currentItem = mediaList.get(itemSelect-1);
					
					// Call return method and remove item from checked/loaned items
					currentItem.returnItem();
					currentCheckedItems.remove(currentItem);
					currentLoanedItems.remove(currentItem);
					
					// Needed to remove item from collection
					for(ArrayList<MediaItem> collection : collectionList) 
					{
					    for(int i = 0; i < collection.size(); i++) 
					    {
					        if(currentItem.getItemID().equals(collection.get(i).getItemID())) {
					            collection.remove(i);
					            // Length reduces by 1, so reduce i
					            i--;
					        }
					    }
					}
					break;
					
				case 4:
					System.out.println("Which collection operation would you like to perform?");
					for (int i = 0; i < collectionOptionList.length; i++) 
						System.out.print((i+1) + ". " + collectionOptionList[i] + "\n");
					
					collectionStr = scn.nextLine();
					// Verify input is valid before parsing
					collectionSelection = getValidSelection(collectionStr, scn, collectionLabel);
					
					// Get updated collection list then reassign to regular collection list
					ArrayList<ArrayList<MediaItem>> currentCollectionList = collectionOperations(collectionSelection, collectionList, 
							currentCheckedItems);
					collectionList = currentCollectionList;
					
					System.out.println("Current collection(s): ");
					// If there are no collections just skip
					if (isCollectionEmpty(collectionList)) break;
					
					// Displays current collections
					displayCollections(collectionList);
					break;
					
				case 5:
					int numCheckedItems = currentCheckedItems.size();
					System.out.println("You currently have checked out " + numCheckedItems + " items: ");
					// Display checked media by title
					for (int i = 0; i < numCheckedItems; i++) 
					{
						System.out.println((i+1) + ". " + currentCheckedItems.get(i).title);
					}
					System.out.println();
					break;
					
				case 6:
					displayItems();
					System.out.print("Enter the number of the book you'd like to loan: ");
					itemStr = scn.nextLine();
					itemSelect = getValidSelection(itemStr, scn, itemLabel);
					
					currentItem = mediaList.get(itemSelect-1);
					if (currentItem.isAvailable)
						currentLoanedItems.add(currentItem);
						
					// Use instanceof to downcast more safely. loan() is an abstract method, so sadly it cannot be called
					// on instances of parent objects (which the list is), so for the purposes of this project, case matching
					// will be used
					if (currentItem instanceof Book book) {
						 book.loan();		 
					}  
					else if (currentItem instanceof Magazine mag) {
						mag.loan(); 
					}  
					else if (currentItem instanceof DVD dvd){
					    dvd.loan();
					}
					
					// Display current loaned books
					System.out.println("Current Loaned Items: ");
					for (MediaItem item : currentLoanedItems) 
					{
						System.out.println((++count) + ". " + item.title + "\n"); 
					}
					break;
				
				}
			}
			// List of the most likely exceptions to occur, triggers exception format handling
			catch(NumberFormatException nfe) {
				System.out.println("Selection is out of range/Empty Input!\n");
				exceptionOccurred = true;
			}
			catch(InputMismatchException ime) {
				System.out.println("Numbers only!\n");
				exceptionOccurred = true;
				
			}
			catch (IndexOutOfBoundsException iobe) {
				System.out.println("Incorrect input entered!\n");
				exceptionOccurred = true;
			}	
		}
		
		// Close resources
		scn.close();
	}
	
	private static void loadFile(String inventory) 
	{
		//Keep track of entry count
		int entryCount = 0;
		try 
		{
			// Uses scanner object to read file object
			Scanner scn = new Scanner(new File(inventory));
			// Keep going as long as there are lines to read
			while(scn.hasNextLine()) 
			{
				// Split each line and store individual elements as entries in one list. Trim is eliminate extra ws
				String[] mediaEntry = scn.nextLine().split(",");
				String mediaType = mediaEntry[0].trim();
				String itemID = mediaEntry[1].trim();
				String title = mediaEntry[2].trim();
				String authorOrDirector = mediaEntry[3].trim();
				int publicationYear = Integer.parseInt(mediaEntry[4].trim());
				// Entries will be available by default since there is no entry for them in the inventory
				boolean isAvailable = true;
				
				// Explicit cases based on the itemID to initialize and assign the correct values to the 
				// class specific variables and initialize that specific object afterwards
				if(mediaType.equals("Book")) {
					String genre = mediaEntry[5].trim();
					long isbn = Long.parseLong(mediaEntry[6].trim());
					mediaList.add(new Book(itemID, title, authorOrDirector, publicationYear, isAvailable,
							genre, isbn));
					entryCount++;
					
				}
				else if(mediaType.equals("Magazine")) {
					int issueNumber = Integer.parseInt(mediaEntry[5].trim());
					String month = mediaEntry[6].trim();
					mediaList.add(new Magazine(itemID, title, authorOrDirector, publicationYear, isAvailable,
							issueNumber, month));
					entryCount++;
				}
				else{
					int duration = Integer.parseInt(mediaEntry[5].trim());
					String rating = mediaEntry[6].trim();
					mediaList.add(new DVD(itemID, title, authorOrDirector, publicationYear, isAvailable,
							duration, rating));
					entryCount++;
				}
				
			}
			System.out.println(entryCount + " entries loaded\n");
			// Close resources
			scn.close();
		}
		// Catches whatever exception happens (Could really be anything so use the full class)
		catch (Exception e) 
		{
			System.out.println("File error");
			e.printStackTrace();
		}
		
	}
}
