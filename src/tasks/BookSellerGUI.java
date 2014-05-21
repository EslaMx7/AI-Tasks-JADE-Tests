package tasks;

import java.util.Enumeration;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class BookSellerGUI extends JFrame {

	private JPanel contentPane;
	public static JList lstBooks;
	private JTextField txtBookName;
	private JTextField txtPrice;


	/**
	 * Create the frame.
	 */
	public BookSellerGUI(final BookSeller agent) {
		setTitle("Book Seller");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JLabel lblStore = new JLabel("Books Store :");
		lblStore.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblStore.setBounds(10, 11, 125, 28);
		contentPane.add(lblStore);
		
		lstBooks = new JList();
		lstBooks.setBounds(10, 50, 125, 200);
		contentPane.add(lstBooks);
		
		JLabel lblAddBook = new JLabel("Add Books :");
		lblAddBook.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblAddBook.setBounds(210, 11, 125, 28);
		contentPane.add(lblAddBook);
		
		JLabel lblBookname = new JLabel("Name :");
		lblBookname.setBounds(150, 102, 72, 14);
		contentPane.add(lblBookname);
		
		txtBookName = new JTextField();
		txtBookName.setBounds(237, 99, 149, 20);
		contentPane.add(txtBookName);
		txtBookName.setColumns(10);
		
		JLabel lblPrice = new JLabel("Price :");
		lblPrice.setBounds(153, 142, 74, 14);
		contentPane.add(lblPrice);
		
		txtPrice = new JTextField();
		txtPrice.setColumns(10);
		txtPrice.setBounds(237, 139, 149, 20);
		contentPane.add(txtPrice);
		
		JButton btnAddToStore = new JButton("Add to Store");
		btnAddToStore.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String key = txtBookName.getText();
				int value = Integer.parseInt(txtPrice.getText());
				agent.BooksList.put(key, value);
				RefreshList(agent);
			}
		});
		btnAddToStore.setBounds(237, 186, 149, 23);
		contentPane.add(btnAddToStore);
		
		RefreshList(agent);
		
	}
	
	public DefaultListModel GetBooks(BookSeller agent){
		DefaultListModel m = new DefaultListModel();
		Enumeration<String> books = agent.BooksList.keys();
		while(books.hasMoreElements()){
			String key = books.nextElement();
			m.addElement(key+" ("+agent.BooksList.get(key)+")");
		}
		return m;
	}
	
	public void RefreshList(BookSeller agent){
		lstBooks.setModel(GetBooks(agent));
	}
}
