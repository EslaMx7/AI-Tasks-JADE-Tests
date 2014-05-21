package aiproject;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DatabaseAgent extends Agent {

	final String DB_PATH = "TV_db.sqlite";
	Connection connection = null;

	@Override
	protected void setup() {

		System.out.println("Connecting to TV Database....");

		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e1) {

			e1.printStackTrace();
		}

		try {
			connection = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}

		System.out.println("Connected Successfuly!");

		addBehaviour(new Communications());

		System.out.println("Database Agent : Waiting for Requests...");
		
	}

	public ArrayList<TVChannel> GetAllChannels() {

		ArrayList<TVChannel> list = new ArrayList<TVChannel>();
		Statement statement;
		TVChannel channel;
		try {
			statement = connection.createStatement();
			statement.setQueryTimeout(30); // set timeout to 30 sec.

			ResultSet rs = statement.executeQuery("SELECT * FROM channels");
			while (rs.next()) {
				channel = new TVChannel();

				channel.Id = rs.getInt("id");
				channel.Name = rs.getString("name");
				channel.Category = rs.getString("category");
				channel.Rate = rs.getInt("rate");

				list.add(channel);
			}

		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}

		return list;
	}

	public ArrayList<TVProgram> GetAllPrograms() {

		ArrayList<TVProgram> list = new ArrayList<TVProgram>();
		Statement statement;
		TVProgram program;
		try {
			statement = connection.createStatement();
			statement.setQueryTimeout(30); // set timeout to 30 sec.

			ResultSet rs = statement.executeQuery("SELECT * FROM programs");
			while (rs.next()) {
				program = new TVProgram();

				program.Id = rs.getInt("id");
				program.ChannelId = rs.getInt("channel_id");
				program.Name = rs.getString("name");
				program.Category = rs.getString("category");
				program.Authors = rs.getString("authors");
				program.Rate = rs.getInt("rate");
				
				program.Channel = GetChannelByID(program.ChannelId);
				

				list.add(program);
			}

		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}

		return list;
	}
	
	public boolean UpdateProgramRate(int id, int newRate) {

		String sql = "UPDATE programs SET rate = ? WHERE id=?";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);
			stm.setInt(1, newRate);
			stm.setInt(2, id);
			stm.executeUpdate();

		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return true;
	}
	
	public boolean UpdateChannelRate(int id, int newRate) {

		String sql = "UPDATE channels SET rate = ? WHERE id=?";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);
			stm.setInt(1, newRate);
			stm.setInt(2, id);
			stm.executeUpdate();

		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return true;
	}

	public TVChannel GetChannelByID(int id){
		
		for (TVChannel ch : GetAllChannels()) {
			if(ch.Id==id)
				return ch;
		}
		
		return null;
	}
	
	
	@Override
	protected void takeDown() {

		try {
			if (connection != null)
				connection.close();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}

	}

	private class Communications extends Behaviour {
		@Override
		public void action() {

			ACLMessage msg = myAgent.receive();
			if (msg != null) {

			
				if (msg.getLanguage().equals("GetAllPrograms")) {

					ACLMessage reply = msg.createReply();

					try {
						reply.setContentObject(GetAllPrograms());
						reply.setLanguage("ListOfPrograms");
					} catch (IOException e) {
						System.err.println(e.getMessage());
					}

					myAgent.send(reply);

					System.out
							.println("Database Agent : All programs has been sent.");
				}
				
				if (msg.getLanguage().equals("GetAllChannels")) {

					ACLMessage reply = msg.createReply();

					try {
						reply.setContentObject(GetAllChannels());
						reply.setLanguage("ListOfChannels");
					} catch (IOException e) {
						System.err.println(e.getMessage());
					}

					myAgent.send(reply);

					System.out
							.println("Database Agent : All channels has been sent.");
				}
				
				if (msg.getLanguage().equals("UpdateChannel")) {

					String body = msg.getContent();
					String content[] = body.split(",");
					UpdateChannelRate(Integer.parseInt(content[0]), Integer.parseInt(content[1]));

					System.out
							.println("Database Agent : Channel Rate has been updated!");
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
