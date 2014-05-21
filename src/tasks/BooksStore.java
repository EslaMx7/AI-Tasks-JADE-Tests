package tasks;

import java.util.Hashtable;

public class BooksStore {

	public static Hashtable<String, Double> BooksList;

	public  BooksStore() {
		BooksList = new Hashtable<String, Double>();
		
		BooksList.put("CS", 77.5);
		BooksList.put("IT", 100.0);
		BooksList.put("IS", 80.0);
		BooksList.put("OR", 60.0);
		BooksList.put("AI", 95.5);
	}

}
