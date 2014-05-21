package tasks;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;

public class BookBuyerGUI extends JFrame {

	private JPanel contentPane;
	private JTextField txtBookName;

	/**
	 * Launch the application.
	 */
	

	/**
	 * Create the frame.
	 */
	public BookBuyerGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 357, 391);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblLogo = new JLabel("Book Buyer");
		lblLogo.setFont(new Font("Tahoma", Font.BOLD, 24));
		lblLogo.setBounds(91, 11, 142, 53);
		contentPane.add(lblLogo);
		
		JLabel lblIWantTo = new JLabel("I want to Buy :");
		lblIWantTo.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblIWantTo.setBounds(20, 127, 113, 25);
		contentPane.add(lblIWantTo);
		
		txtBookName = new JTextField();
		txtBookName.setHorizontalAlignment(SwingConstants.CENTER);
		txtBookName.setText("CS");
		txtBookName.setFont(new Font("Tahoma", Font.BOLD, 16));
		txtBookName.setBounds(51, 163, 232, 39);
		contentPane.add(txtBookName);
		txtBookName.setColumns(10);
		
		JButton btnBuy = new JButton("Buy");
		btnBuy.setFont(new Font("Tahoma", Font.BOLD, 19));
		btnBuy.setBounds(120, 237, 113, 47);
		contentPane.add(btnBuy);
	}
}
