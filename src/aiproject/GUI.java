package aiproject;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import javax.swing.JRadioButton;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GUI extends JFrame {

	private JPanel contentPane;
	private GUIAgent guiAgent;
	public JTextArea txtrResults;
	public JLabel lblCurrentTime;
	private JRadioButton rdbtnChannels;
	private JRadioButton rdbtnPrograms;

	/**
	 * Create the frame.
	 */
	public GUI(GUIAgent gui_agent) {

		setResizable(false);
		setTitle("Best TV Channels Agent");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 411, 482);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
		}
		
		JLabel lblClear = new JLabel("Clear");
		lblClear.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				txtrResults.setText("");
			}
		});
		lblClear.setForeground(Color.BLUE);
		lblClear.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblClear.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblClear.setBounds(370, 428, 25, 14);
		contentPane.add(lblClear);

		JLabel lblWhatIsYour = new JLabel("What is your Mode : ");
		lblWhatIsYour.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblWhatIsYour.setBounds(10, 40, 193, 29);
		contentPane.add(lblWhatIsYour);

		final JComboBox comboMode = new JComboBox();
		comboMode.setForeground(Color.DARK_GRAY);
		comboMode.setFont(new Font("Tahoma", Font.PLAIN, 20));
		comboMode.setModel(new DefaultComboBoxModel(new String[] { "Films",
				"News", "Sports", "Music" }));
		comboMode.setBounds(201, 39, 193, 31);
		contentPane.add(comboMode);

		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// guiAgent.lastResult = guiAgent.bestChannel;
				if (rdbtnChannels.isSelected()) {
					String mode = comboMode.getSelectedItem().toString();
					guiAgent.SendSearchRequest(mode);
				} else if (rdbtnPrograms.isSelected()) {

					String key = JOptionPane.showInputDialog(null,
							"Enter a search key (program name or author name) :",
							"TV Agent",
							JOptionPane.QUESTION_MESSAGE
							);
					if(key!=null){
						
						guiAgent.SendSearchProgramRequest(key);
						
					}
					
				}

			}
		});
		btnSearch.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnSearch.setBounds(10, 120, 152, 37);
		contentPane.add(btnSearch);

		JButton btnFeelingLucky = new JButton("I'm Feeling Lucky");
		btnFeelingLucky.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String time = lblCurrentTime.getText().substring(15);
				if (rdbtnChannels.isSelected()) {
					guiAgent.SendLuckyRequest(time,true);
					
				} else if (rdbtnPrograms.isSelected()) {
					guiAgent.SendLuckyRequest(time,false);
				}
				
			}
		});
		btnFeelingLucky.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnFeelingLucky.setBounds(201, 120, 193, 37);
		contentPane.add(btnFeelingLucky);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 184, 384, 237);
		contentPane.add(scrollPane);

		txtrResults = new JTextArea();
		txtrResults.setText("Results : ");
		txtrResults.setLineWrap(true);
		txtrResults.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		scrollPane.setViewportView(txtrResults);

		lblCurrentTime = new JLabel("Current Time :");
		lblCurrentTime.setBounds(10, 432, 193, 14);
		contentPane.add(lblCurrentTime);

		rdbtnChannels = new JRadioButton("Channels");
		rdbtnChannels.setBackground(Color.WHITE);
		rdbtnChannels.setSelected(true);
		rdbtnChannels.setBounds(201, 76, 69, 23);
		contentPane.add(rdbtnChannels);

		rdbtnPrograms = new JRadioButton("Programs");
		rdbtnPrograms.setBackground(Color.WHITE);
		rdbtnPrograms.setBounds(313, 76, 81, 23);
		contentPane.add(rdbtnPrograms);
		DefaultCaret caret = (DefaultCaret) txtrResults.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		ButtonGroup btnGroup = new ButtonGroup();
		btnGroup.add(rdbtnChannels);
		btnGroup.add(rdbtnPrograms);

		guiAgent = gui_agent;
	}
}
