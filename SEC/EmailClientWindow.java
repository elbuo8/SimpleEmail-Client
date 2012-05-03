package SEC;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.Vector;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 * IMPORTANT: This class doesn't needs any modification. However you may need to change
 * the parts that has to do with contacts depending on your implementation. Those are the
 * ONLY PARTS YOU ARE ALLOWED TO MODIFY. You're encouraged to implement the Contact class
 * as it follows from this class, however you're not restricted to that way only. TODOS 
 * has been added so you can identify where you change the code in places you shall do it. 
 * Describes the program GUI. Contains all possible dialog windows.
 * @author hfranqui
 *
 */
public class EmailClientWindow extends JPanel implements ListSelectionListener, MouseListener {

	/**
	 * Serial ID to stop warning messages
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Listener object for New Email Account Window
	 * @author hfranqui
	 *
	 */
	private static class NewAccountWindowListener implements ActionListener {

		public NewAccountWindowListener(JFrame frame, EntryField f1, EntryField f2, EntryField f3, EntryField f4) {
			this.f1 = f1;
			this.f2 = f2;
			this.f3 = f3;
			this.f4 = f4;
			this.newProperties = new Properties();
			this.frame = frame;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton temp = (JButton) e.getSource();
			if(temp.getText().equals("OK")) {
				newProperties.setProperty("username", f1.getText());
				newProperties.setProperty("password", f2.getText());
				newProperties.setProperty("incomingHost", f3.getText());
				newProperties.setProperty("outgoingHost", f4.getText());
				frame.setVisible(false);
			}
			if(temp.getText().equals("Cancel")) {
				frame.setVisible(false);
			}
		}

		public Properties getProperties() {
			return newProperties;
		}

		private EntryField f1,f2,f3,f4;
		private JFrame frame;
		private Properties newProperties;
	}

	/**
	 * Listener object for Contact Window Listener
	 * @author hfranqui
	 *
	 */
	private class ContactWindowListener implements ActionListener {

		public ContactWindowListener(JFrame frame, String key, EntryField f1, EntryField f2, EntryField f3, EntryField f4, EntryField f5, int type) {
			this.frame = frame;
			this.f1 = f1;
			this.f2 = f2;
			this.f3 = f3;
			this.f4 = f4;
			this.f5 = f5;
			this.type = type;
			this.key = key;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton temp = (JButton) e.getSource();
			if(temp.getText().equals("OK")) {
				// TODO Change this depending of your constructor
				EmailContact temp1 = new EmailContact(f1.getText(), f2.getText(), f3.getText(), f4.getText(), f5.getText());
				if(type == 0) {
					contactStorage.add(Integer.toString(contactStorage.size()+1), temp1);
				}
				else if (type == 1) {
					//System.out.println(key);
					contactStorage.add(key, temp1);
				}
				contactList.setListData(getContactNames(25));
				contactScroller.revalidate();
				contactScroller.repaint();
				frame.setVisible(false);
			}
			if(temp.getText().equals("Cancel")) {
				frame.setVisible(false);
				frame.dispose();
			}

		}

		private JFrame frame;
		private EntryField f1,f2,f3,f4,f5;
		private String key;
		private int type;

	}

	private class NewEmailWindowListener implements ActionListener {

		public NewEmailWindowListener(JFrame frame, EntryField f1, EntryField f2, JTextArea area) {
			this.frame = frame;
			this.f1 = f1;
			this.f2 = f2;
			this.area = area;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			JButton temp = (JButton) arg0.getSource();
			if(temp.getText().equals("Send")) {
				MimeMessage message = new MimeMessage(server.getSession());

				try{
					if(f1.getText().indexOf(',') > 0) {
						message.setRecipients(Message.RecipientType.TO, f1.getText());
					}
					else {
						message.setRecipient(Message.RecipientType.TO, new InternetAddress(f1.getText()));
						//System.out.println(InternetAddress.toString(message.getAllRecipients()));
					}
					// For DEBUG purposes
					//System.out.println(accountProperties.getProperty("username"));
					message.setFrom(new InternetAddress(accountProperties.getProperty("username")));
					message.setSubject(f2.getText());
					message.setContent(area.getText(), "text/plain");
					server.sendMessage(message);
					JOptionPane.showMessageDialog(frame, "Message sent successfuly!");
				}
				catch (MessagingException e) {
					System.out.println("Failure to send message for some reason.");
					JOptionPane.showMessageDialog(frame, "Failure to send message for some reason. Please\ncheckyour internet connection and try again and/or recipient\n address and try again.");
					e.printStackTrace();
				}

				frame.setVisible(false);
			}
			if(temp.getText().equals("Cancel")) {
				frame.setVisible(false);
				frame.dispose();
			}

		}

		private JFrame frame;
		private EntryField f1, f2;
		private JTextArea area;
	}

	/**
	 * Private class to handle toolbar actions
	 * @author hfranqui
	 *
	 */
	private class ToolBarListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			JButton temp = (JButton) arg0.getSource();

			// New email message button action
			if(temp.getText().equals("New Email")) {
				showNewEmailWindow("");
			}

			// New contact button action
			if(temp.getText().equals("New Contact")) {
				showContactWindow(null, 0, "");
			}

			// Refresh button action
			if(temp.getText().equals("Refresh")) {
				Message[] messages = null;
				statusBar.setText("Now refreshing... please wait...");
				statusBar.repaint();
				try {
					server.getInboxFromServer();
					messages = server.getAllMessages();
				} catch (MessagingException e) {
					e.printStackTrace();
					statusBar.setText("Fail!!");
				}

				// Very important, otherwise we are just adding the same emails back to the storage
				// thus creating duplicates
				emailStorage.clear();
				
				int serverMessages = messages.length;
				int serverStart = messages.length-1, serverEnd = messages.length-200;
				if (serverStart < 0)
					serverStart = 0;
				if (serverMessages < 200)
					serverEnd = 0;
				
				currentStart = 0;
				
				
				for(int i = serverStart; i>=(serverEnd); i--) {
					emailStorage.add(Integer.toString((-1)*(i-(messages.length-1))), messages[i]);
				}
				
				if (emailStorage.size() < 25) {
					currentEnd = emailStorage.size()-1;
					pageIncrease = emailStorage.size()-1;			
				}
				else {
					pageIncrease = emailStorage.size()/8;
				}
				
				DefaultTableModel model = new DefaultTableModel(getHeaderList(currentStart,currentEnd), columnNames);
				emailList.setModel(model);
				
				updateStatusBar();
			}

			// Next Page
			if(temp.getText().equals("Next Page")) {
				if((currentEnd+1) != emailStorage.size()) {
					currentStart += pageIncrease;
					currentEnd += pageIncrease;

					updateStatusBar();

					DefaultTableModel model = new DefaultTableModel(getHeaderList(currentStart,currentEnd), columnNames);
					emailList.setModel(model);
				}
			}

			// Previous Page
			if(temp.getText().equals("Prev Page")) {
				if((currentStart) != 0) {
					currentStart -= pageIncrease;
					currentEnd -= pageIncrease;

					updateStatusBar();

					DefaultTableModel model = new DefaultTableModel(getHeaderList(currentStart,currentEnd), columnNames);
					emailList.setModel(model);
				}
			}

			// Send Email using contact
			if(temp.getText().equals("Send Email")) {
				//TODO You may change this if you need to do so
				showNewEmailWindow(selectedContact.getEmail());
			}

			// View/Edit Contact
			if(temp.getText().equals("View/Edit Contact")) {
				showContactWindow(selectedContact, 1, Integer.toString(contactList.getSelectedIndex() + 1));
			}

		}

	}

	/**
	 * Auxiliary class that creates a new entry object. Basically, you got a text label beside a text field entry.
	 * You can specify if the label is above or at the left of the text field.
	 * @author hfranqui
	 *
	 */
	private static class EntryField extends JPanel {

		/**
		 * Serial ID to stop warning messages
		 */
		private static final long serialVersionUID = 1L;
		
		public EntryField(String label, int pos, int width) {
			this.label = new JLabel(label);
			this.entry = new JTextField();
			this.add(this.label);
			this.add(this.entry);
			this.pos = pos;
			if (this.pos == 0) {
				height = 25;
			}
			else if(this.pos == 1) {
				height = 50;
			}
			this.width = width;
		}

		public Dimension getPreferredSize(){
			return new Dimension(325,height);
		}

		public Dimension getMinimumSize(){
			return new Dimension(325,height);
		}

		public Dimension getMaximumSize(){
			return new Dimension(325,height);
		}

		public String getText() {
			return this.entry.getText();
		}

		public void setText(String s) {
			this.entry.setText(s);
		}

		public void doLayout() {
			if(pos == 0) {
				this.label.setBounds(5, 5, width, 25);
				this.entry.setBounds(width+5, 5, 225, 25);
			}
			else if (pos == 1) {
				this.label.setBounds(5, 5, width, 25);
				this.entry.setBounds(5, 25, 320, 25);
			}
		}

		private JTextField entry;
		private JLabel label;
		private int pos,width,height;
	}


	/**
	 * 
	 * @param accountProperties
	 * @param server
	 * @param storage
	 * @param storage1
	 */
	public EmailClientWindow(Properties accountProperties, EmailServer server, EmailDataStorage storage, ContactDataStorage storage1) {
		this.accountProperties = accountProperties;
		this.server = server;
		this.emailStorage = storage;
		this.contactStorage = storage1;
		
		this.statusBar = new JLabel("Viewing " + (currentStart+1) + " - " + (currentEnd+1) + " of " + emailStorage.size());
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		this.currentStart = 0;
		
		if (emailStorage.size() < 25) {
			this.currentEnd = emailStorage.size()-1;
			this.pageIncrease = emailStorage.size()-1;			
		}
		else {
			this.pageIncrease = this.emailStorage.size()/8;
		}
	}

	public JPanel initializeWindow() {

		JPanel windowPanel = new JPanel();
		JPanel viewPanel = new JPanel();
		viewPanel.setLayout(new BoxLayout(viewPanel, BoxLayout.X_AXIS));
		windowPanel.setLayout(new BorderLayout());



		// Email list view
		emailList = new JTable(this.getHeaderList(currentStart,currentEnd),columnNames);
		emailList.setRowHeight(25);
		emailList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		emailList.addMouseListener(this);

		// Scroll panel for email list view
		listScroller = new JScrollPane(emailList);
		listScroller.setMaximumSize(new Dimension(700,530));
		listScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		// Contact view panel
		JPanel contactView = new JPanel();
		contactView.setLayout(new BoxLayout(contactView, BoxLayout.Y_AXIS));

		JPanel contactButtonsPanel = new JPanel();
		sendEmail = new JButton("Send Email");
		viewEdit = new JButton("View/Edit Contact");
		sendEmail.setEnabled(false);
		viewEdit.setEnabled(false);
		contactButtonsPanel.add(sendEmail);
		contactButtonsPanel.add(viewEdit);
		contactButtonsPanel.setMaximumSize(new Dimension(300,40));

		contactView.add(contactButtonsPanel);

		// Contact list view
		contactList = new JList(this.getContactNames(25));
		contactList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		contactList.addListSelectionListener(this);

		// Scroll panel for contact list view
		contactScroller = new JScrollPane(contactList);
		contactScroller.setMaximumSize(new Dimension(300,530));
		contactScroller.setMinimumSize(new Dimension(200,530));
		contactView.add(contactScroller);

		// Window Toolbar
		JToolBar toolBar = new JToolBar("");

		ToolBarListener toolbarListener = new ToolBarListener();
		JButton b1 = new JButton("New Email"), b2 = new JButton("New Contact"), b3 = new JButton("Refresh"), b4= new JButton("Next Page"), b5 = new JButton("Prev Page");
		b1.addActionListener(toolbarListener);
		b1.setIcon(new ImageIcon(System.getProperty("user.dir") + File.separatorChar + "resources" + File.separatorChar + "gnome-stock-mail-new.png"));
		
		b2.addActionListener(toolbarListener);
		b2.setIcon(new ImageIcon(System.getProperty("user.dir") + File.separatorChar + "resources" + File.separatorChar + "contact-new.png"));
		
		b3.addActionListener(toolbarListener);
		b3.setIcon(new ImageIcon(System.getProperty("user.dir") + File.separatorChar + "resources" + File.separatorChar + "view-refresh.png"));

		b4.addActionListener(toolbarListener);
		b4.setIcon(new ImageIcon(System.getProperty("user.dir") + File.separatorChar + "resources" + File.separatorChar + "go-next.png"));

		b5.addActionListener(toolbarListener);
		b5.setIcon(new ImageIcon(System.getProperty("user.dir") + File.separatorChar + "resources" + File.separatorChar + "go-previous.png"));


		sendEmail.addActionListener(toolbarListener);
		viewEdit.addActionListener(toolbarListener);

		// Add buttons to the toolbar
		toolBar.add(b1);
		toolBar.add(b2);
		toolBar.addSeparator();
		toolBar.add(b3);
		toolBar.add(b5);
		toolBar.add(b4);

		toolBar.setFloatable(false);
		toolBar.setPreferredSize(new Dimension(450, 35));

		windowPanel.add(toolBar, BorderLayout.PAGE_START);
		viewPanel.add(listScroller);
		viewPanel.add(contactView);

		windowPanel.add(viewPanel,BorderLayout.CENTER);
		windowPanel.add(statusBar, BorderLayout.SOUTH);
		
		return windowPanel;
	}


	/**
	 * Method that handles selections on the list
	 * @param arg0
	 */
	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		if(arg0.getSource() == contactList) {
			if(contactList.getSelectedIndex() != -1) {
				sendEmail.setEnabled(true);
				viewEdit.setEnabled(true);
				selectedContact = contactStorage.get(Integer.toString(contactList.getSelectedIndex() + 1));
			}
			else {
				sendEmail.setEnabled(false);
				viewEdit.setEnabled(false);
				selectedContact = null;
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		JTable temp = (JTable) e.getSource();
		showViewEmailWindow(emailStorage.get(Integer.toString(temp.getSelectedRow()+currentStart)));
	}

	// Method not implemented.
	@Override
	public void mouseEntered(MouseEvent e) {
	
	}

	// Method not implemented.
	@Override
	public void mouseExited(MouseEvent e) {

	}

	// Method not implemented.
	@Override
	public void mousePressed(MouseEvent e) {

	}

	// Method not implemented.
	@Override
	public void mouseReleased(MouseEvent e) {

	}

	/**
	 * Auxiliary method to get Subject and Sender of all emails
	 * @param initial The first email to be displayed in the current page
	 * @param end The last email to be displayed in the current page
	 * @return A double array of String which contains two columns, the first the
	 * email sender, and the second the email subject.
	 */
	public String[][] getHeaderList(int initial, int end) {
		String[][] result = new String[(end-initial)+1][2];
		for (int i = initial; i<=end; i++) {
			try {
				// For DEBUG purposes
				//System.out.println("Getting header...");
				result[i-initial][0] = InternetAddress.toString(emailStorage.get(Integer.toString(i)).getFrom());
				// For DEBUG purposes
				//System.out.println("Getting subject...");
				result[i-initial][1] = emailStorage.get(Integer.toString(i)).getSubject();

			} catch (MessagingException e) {
				System.out.println("Problem retrieving either sender's address or subject of email. Malformed email message...");
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * Auxiliary method to get all contact names
	 * @param count
	 * @return
	 */
	public Vector<String> getContactNames(int count) {
		EmailContact[] contacts = contactStorage.getAllValues();
		Vector<String> result = new Vector<String>();

		for(int i = 0; i < contactStorage.size(); i++) {
			//For DEBUG purposes
			//System.out.println(contacts[i] + ", " + i);
			//TODO You should change this depending on the name of your method.
			result.add(contacts[i].getName());
		}

		return result;
	}

	/**
	 * Displays a new window for creating a new account. This window should only be used in the first run on the program
	 * if there's no email account.
	 * @return
	 */
	public static Properties showNewAccountWindow() {
		JFrame frame = new JFrame();
		frame.setSize(350,280);
		frame.setTitle("Create New Email Account");
		frame.setResizable(false);

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		EntryField f1 = new EntryField("Email Address:",1, 130);
		EntryField f2 = new EntryField("Password:",1, 130);
		EntryField f3 = new EntryField("POP3 Host Address:",1, 150);
		EntryField f4 = new EntryField("SMTP Host Address:",1, 150);

		panel.add(f1);
		panel.add(f2);
		panel.add(f3);
		panel.add(f4);

		NewAccountWindowListener listener = new NewAccountWindowListener(frame,f1,f2,f3,f4);

		JButton okButton = new JButton("OK");
		JButton cancelButton = new JButton("Cancel");
		okButton.addActionListener(listener);
		cancelButton.addActionListener(listener);

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		panel.add(Box.createRigidArea(new Dimension(10,10)));
		panel.add(buttonPanel);
		frame.add(panel);
		frame.setVisible(true);

		while(frame.isVisible()) {
			if(!listener.getProperties().isEmpty()) {
				return listener.getProperties();
			}
		}
		return null;
	}

	/**
	 * Displays a window to create, view or edit a contact
	 * @param contact
	 * @param type
	 */
	public void showContactWindow(EmailContact contact, int type, String key) {
		String winTitle = "";

		JFrame frame = new JFrame();
		frame.setSize(350,200);

		frame.setResizable(false);

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));

		EntryField f1 = new EntryField("Name:",0, 125);
		EntryField f2 = new EntryField("Email address:",0, 125);
		EntryField f3 = new EntryField("Phone number:",0, 125);
		EntryField f4 = new EntryField("Address:",0, 125);
		EntryField f5 = new EntryField("Note:",0, 125);

		if (type == 0) {
			winTitle = "New ";
		}
		else if (type == 1) {
			winTitle = "View/Edit ";
			//TODO You should change this depending on the fields you have.
			f1.setText(contact.getName());
			f2.setText(contact.getEmailAddress());
			f3.setText(contact.getPhoneNumber());
			f4.setText(contact.getAddress());
			f5.setText(contact.getNote());
		}
		frame.setTitle(winTitle + "Contact");

		panel.add(f1);
		panel.add(f2);
		panel.add(f3);
		panel.add(f4);
		panel.add(f5);

		ContactWindowListener listener = new ContactWindowListener(frame, key, f1, f2, f3, f4, f5, type);
		JButton okButton = new JButton("OK");
		JButton cancelButton = new JButton("Cancel");
		okButton.addActionListener(listener);
		cancelButton.addActionListener(listener);

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		panel.add(Box.createRigidArea(new Dimension(10,10)));
		panel.add(buttonPanel);

		frame.add(panel);
		frame.setVisible(true);

		if(!frame.isVisible()) {
			return;
		}

	}

	/**
	 * Displays a window to create and send a new email message
	 */
	public void showNewEmailWindow(String contact) {
		JFrame frame = new JFrame();
		frame.setSize(400,400);
		frame.setTitle("New Email Message");

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		EntryField f1 = new EntryField("To:", 1, 375);
		f1.setText(contact);
		EntryField f2 = new EntryField("Subject:", 1, 375);
		JTextArea message = new JTextArea();
		JScrollPane scroller = new JScrollPane(message);
		scroller.setPreferredSize(new Dimension(300,350));
		message.setMargin(new Insets(5,5,5,5));
		message.setLineWrap(true);

		panel.add(f1);
		panel.add(f2);
		panel.add(Box.createRigidArea(new Dimension(10,7)));
		panel.add(scroller);

		NewEmailWindowListener listener = new NewEmailWindowListener(frame,f1,f2,message);

		JButton okButton = new JButton("Send");
		JButton cancelButton = new JButton("Cancel");
		okButton.addActionListener(listener);
		cancelButton.addActionListener(listener);

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		panel.add(Box.createRigidArea(new Dimension(10,10)));
		panel.add(buttonPanel);

		frame.add(panel);
		frame.setVisible(true);

	}

	/**
	 * Displays a window containing an email message. 
	 * @param message The message to be displayed.
	 */
	public void showViewEmailWindow(Message message) {
		JFrame frame = new JFrame();
		frame.setSize(600,600);
		frame.setTitle("View Email Message");

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JEditorPane viewer = new JEditorPane();
		viewer.setContentType("text/html");
		JLabel f1 = null, f2 = null, f3 = null;
		try {
			f1 = new JLabel("Sender: " + InternetAddress.toString(message.getFrom()));
			f2 = new JLabel("To: " + InternetAddress.toString(message.getAllRecipients()));
			f3 = new JLabel("Subject: " + message.getSubject());
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		JScrollPane scroller = new JScrollPane(viewer);
		scroller.setPreferredSize(new Dimension(300,350));
		viewer.setMargin(new Insets(5,5,5,5));

		try {
			viewer.setText(EmailDataStorage.getText(message));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		viewer.setEditable(false);

		panel.add(f1);
		panel.add(f2);
		panel.add(f3);
		panel.add(Box.createRigidArea(new Dimension(10,7)));
		panel.add(scroller);

		frame.add(panel);
		frame.setVisible(true);

	}

	/**
	 * Auxiliary method to simply update the status bar message.
	 */
	public void updateStatusBar() {
		statusBar.setText("Viewing " + (currentStart+1) + " - " + (currentEnd+1) + " of " + emailStorage.size());
	}

	private int currentStart, currentEnd, pageIncrease;

	private EmailDataStorage emailStorage;
	private ContactDataStorage contactStorage;
	private EmailServer server;
	private Properties accountProperties;
	private JList contactList;
	private JScrollPane contactScroller, listScroller;
	private JTable emailList;
	private JLabel statusBar;
	private JButton sendEmail, viewEdit;
	private EmailContact selectedContact;
	public static final String[] columnNames = {"From:", "Subject:"};


}
