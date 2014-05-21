package aiproject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JOptionPane;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class GUIAgent extends Agent {

	public GUI gui;
	public TVChannel lastResult;
	public TVChannel bestChannel;
	public TVChannel LuckyChannel;
	public TVProgram LuckyProgram;

	@Override
	protected void setup() {

		gui = new GUI(this);
		gui.setVisible(true);

		addBehaviour(new Communications());

		addBehaviour(new TickerBehaviour(this, 1000) {

			@Override
			protected void onTick() {
				String time = new SimpleDateFormat("HH:mm:ss").format(Calendar
						.getInstance().getTime());
				gui.lblCurrentTime.setText("Current Time : " + time);

			}
		});

	}

	public void SendSearchRequest(String mode) {

		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(new AID("search", false));
		msg.setLanguage("SearchRequest");
		msg.setContent(mode);

		send(msg);

		System.out
				.println("GUI Agent : Search Request sent with mode :" + mode);

	}

	public void SendSearchProgramRequest(String key) {

		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(new AID("search", false));
		msg.setLanguage("SearchProgramRequest");
		msg.setContent(key);

		send(msg);

		System.out.println("GUI Agent : Search Program Request sent with key :"
				+ key);

	}

	public void SendLuckyRequest(String time, boolean isChannel) {

		if (isChannel) {
			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			msg.addReceiver(new AID("search", false));
			msg.setLanguage("LuckyChannelRequest");
			msg.setContent(time);

			send(msg);

			System.out
					.println("GUI Agent : Lucky Channel Request sent with time :"
							+ time);
		} else {
			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			msg.addReceiver(new AID("search", false));
			msg.setLanguage("LuckyProgramRequest");
			msg.setContent(time);

			send(msg);

			System.out
					.println("GUI Agent : Lucky Program Request sent with time :"
							+ time);

		}

	}
	
	public void SendUpdateListsRequest() {

		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(new AID("search", false));
		msg.setLanguage("UpdateListsRequest");
		

		send(msg);

		System.out
				.println("GUI Agent : Update Lists Request sent");

	}

	private class Communications extends Behaviour {

		@Override
		public void action() {

			ACLMessage msg = myAgent.receive();
			if (msg != null) {

				if (msg.getLanguage().equals("LuckyChannelResponse")) {

					try {

						LuckyChannel = (TVChannel) msg.getContentObject();
						gui.txtrResults.append("\nLucky Channel Is : "
								+ LuckyChannel.Name);

					} catch (UnreadableException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				if (msg.getLanguage().equals("LuckyProgramResponse")) {

					try {

						LuckyProgram = (TVProgram) msg.getContentObject();
						gui.txtrResults.append("\nLucky Program Is : "
								+ LuckyProgram.Name + " Watch it On : "
								+ LuckyProgram.Channel.Name);

					} catch (UnreadableException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				if (msg.getLanguage().equals("SearchProgramResponse")) {

					try {

						ArrayList<TVProgram> programs = (ArrayList<TVProgram>) msg.getContentObject();
						if(programs.size()<=0)
							gui.txtrResults.append("\nNo search results found with this key.");
						else{
							gui.txtrResults.append("\nYou Can Watch : ");
							for (TVProgram program : programs) {
								gui.txtrResults.append("\n "+program.Name +" by : "+program.Authors + " On : " + program.Channel.Name);								
							}
						}
						

					} catch (UnreadableException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				if (msg.getLanguage().equals("SearchResponse")) {
					// TVChannel best;

					try {
						bestChannel = (TVChannel) msg.getContentObject();

						gui.txtrResults.append("\nBest Channel Is : "
								+ bestChannel.Name);

						myAgent.addBehaviour(new WakerBehaviour(myAgent, 3000) {
							@Override
							protected void onWake() {

								int choice = JOptionPane.showOptionDialog(gui,
										"Do you liked this channel ? ["
												+ bestChannel.Name + "]",
										"TV Channels Agent",
										JOptionPane.YES_NO_OPTION,
										JOptionPane.QUESTION_MESSAGE, null,
										null, null);

								if (choice == 0) { // YES
									ACLMessage msg = new ACLMessage(
											ACLMessage.CONFIRM);
									msg.addReceiver(new AID("db", false));
									msg.setLanguage("UpdateChannel");
									msg.setContent(bestChannel.Id + ","
											+ (bestChannel.Rate + 2));

									myAgent.send(msg);
								} else {
									ACLMessage msg = new ACLMessage(
											ACLMessage.CONFIRM);
									msg.addReceiver(new AID("db", false));
									msg.setLanguage("UpdateChannel");
									msg.setContent(bestChannel.Id + ","
											+ (bestChannel.Rate - 10));

									myAgent.send(msg);
								}
								SendUpdateListsRequest();

							}

						});
						// gui.txtrResults.setCaretPosition(gui.txtrResults.getDocument().getLength());
					} catch (UnreadableException e) {
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
