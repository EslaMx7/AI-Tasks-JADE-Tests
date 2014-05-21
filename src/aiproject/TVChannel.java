package aiproject;

import java.io.Serializable;

public class TVChannel implements Serializable{

	public int Id;
	public String Name;
	public String Category;
	public int Rate;
	
	
	@Override
	public String toString() {
	
		return Id + " "+ Name + " " + Category + " " + Rate; 
	
	}
	
}
