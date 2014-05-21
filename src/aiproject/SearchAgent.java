package aiproject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class SearchAgent extends Agent {

	public ArrayList<TVChannel> ChannelsList;
	public ArrayList<TVProgram> ProgramsList;
	
	
	@Override
	protected void setup() {

		ChannelsList = new ArrayList<TVChannel>();
		ProgramsList = new ArrayList<TVProgram>();
		
		addBehaviour(new Communications());

		getAllChannels();
		getAllPrograms();
	}

	public void getAllChannels() {
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(new AID("db", false));
		msg.setLanguage("GetAllChannels");

		send(msg);
	}
	
	public void getAllPrograms() {
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(new AID("db", false));
		msg.setLanguage("GetAllPrograms");

		send(msg);
	}
	

	
	public TVChannel getBestChannel(String mode){
		
		

		
		TVChannel best = ChannelsList.get(0);
		TVChannel worst = ChannelsList.get(0);
		// Linear Search
		
		for (TVChannel channel : ChannelsList) {
			if(channel.Rate>best.Rate && channel.Category.equals(mode))
				best = channel;
			
		}
		
		
		// Tree Search
		/*
		BinaryTree<TVChannel> tree = new BinaryTree<TVChannel>(best);
		for (TVChannel channel : ChannelsList) {
			if(channel.Rate>=best.Rate && channel.Category.equals(mode)){
				tree.InsertRight(best, channel);
				best = channel;
			}
			else{
				tree.InsertLeft(worst, channel);
				//worst = channel;
				}
		}
		
		tree.PrintTree(tree.Root);

		*/
		
		return best;
	}
	
	public TVChannel getChannelByTime(String time){
		TVChannel channel;
		String tokens[] = time.split(":");
		int hour = Integer.parseInt(tokens[0]);
		String mode="";
		if(hour>=0 && hour<=6)
			mode="Films";
		else if(hour>6 && hour <=12)
			mode="News";
		else if(hour>12 && hour <=18)
			mode="Music";
		else if(hour>18 && hour <=24)
			mode="Sports";
		
		int i=0;
		do{
		i= randInt(0, ChannelsList.size()-1);
		channel = ChannelsList.get(i);
		}while(!channel.Category.equals(mode));
		
		return channel;
		
	}
	
	public TVProgram getProgramByTime(String time){
		TVProgram program;
		String tokens[] = time.split(":");
		int hour = Integer.parseInt(tokens[0]);
		String mode="";
		if(hour>=0 && hour<=6)
			mode="Films";
		else if(hour>6 && hour <=12)
			mode="News";
		else if(hour>12 && hour <=18)
			mode="Music";
		else if(hour>18 && hour <=24)
			mode="Sports";
		
		int i=0;
		do{
		i= randInt(0, ProgramsList.size()-1);
		program = ProgramsList.get(i);
		}while(!program.Category.equals(mode));
		
		return program;
	}

	public ArrayList<TVProgram> getProgramsByKey(String key){
		
		ArrayList<TVProgram> list = new ArrayList<TVProgram>();
		
		for (TVProgram program : ProgramsList) {
			if(program.Name.contains(key) || program.Authors.contains(key))
				list.add(program);
			
		}
		
		return list;
	}
	
	private  int randInt(int min, int max) {

	    // Usually this should be a field rather than a method variable so
	    // that it is not re-seeded every call.
	    Random rand = new Random();

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}
	private class Communications extends Behaviour {
		@Override
		public void action() {

			ACLMessage msg = myAgent.receive();
			if (msg != null) {

				if(msg.getLanguage().equals("LuckyChannelRequest")){
					String time = msg.getContent();
					TVChannel best = getChannelByTime(time);
					ACLMessage reply = msg.createReply();
					reply.setLanguage("LuckyChannelResponse");
					try {
						reply.setContentObject(best);
					} catch (IOException e) {
					}
					send(reply);
					
				}
				
				if(msg.getLanguage().equals("LuckyProgramRequest")){
					String time = msg.getContent();
					TVProgram program = getProgramByTime(time);
					ACLMessage reply = msg.createReply();
					reply.setLanguage("LuckyProgramResponse");
					try {
						reply.setContentObject(program);
					} catch (IOException e) {
					}
					send(reply);
					
				}
				
				
				if(msg.getLanguage().equals("SearchRequest")){
					String mode = msg.getContent();
					TVChannel best = getBestChannel(mode);
					ACLMessage reply = msg.createReply();
					reply.setLanguage("SearchResponse");
					try {
						reply.setContentObject(best);
					} catch (IOException e) {
					}
					send(reply);
					
				}
				
				if(msg.getLanguage().equals("SearchProgramRequest")){
					String key = msg.getContent();
					ArrayList<TVProgram> programs = getProgramsByKey(key);
					ACLMessage reply = msg.createReply();
					reply.setLanguage("SearchProgramResponse");
					try {
						reply.setContentObject(programs);
					} catch (IOException e) {
					}
					send(reply);
					
				}
				
				if(msg.getLanguage().equals("UpdateListsRequest")){
					
					getAllChannels();
					getAllPrograms();
					
					System.out.println("Search Agent : All Lists has been updated.");
					
				}
				
				
				
				if (msg.getLanguage().equals("ListOfPrograms")) {
					try {
						Object o = msg.getContentObject();
						ArrayList<TVProgram> list = (ArrayList<TVProgram>) o;
						
						ProgramsList = list;
						
						System.out.println("Search Agent : Programs List Received.");

					} catch (UnreadableException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				
				
				if (msg.getLanguage().equals("ListOfChannels")) {
					try {
						Object o = msg.getContentObject();
						ArrayList<TVChannel> list = (ArrayList<TVChannel>) o;
						
						ChannelsList = list;
						
						System.out.println("Search Agent : Channels List Received.");

					} catch (UnreadableException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			} else {
				block();
			}

		}

		@Override
		public boolean done() {

			return false;
		}
	}
}
