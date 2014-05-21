package aiproject;

import java.io.Serializable;

public class TVProgram implements Serializable {

	public int Id;
	public int ChannelId;
	public String Name;
	public String Category;
	public String Authors;
	public int Rate;
	
	// Reference to the Channel of this Program.  
	public TVChannel Channel;
}
