package SEC;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.swing.JFrame;


public class Runner {

	private static EmailDataStorage emailStorage;
	private static ContactDataStorage contactStorage;
	private static EmailServer server;
	private static Properties accountProperties;
	private static JFrame frame;

	/**
	 * THIS METHOD NEEDS TO BE COMPLETED
	 * Static method that will be used to create a new email account if there's no present.
	 * @throws IOException 
	 */
	public static void createAccountProperties() throws IOException {
		accountProperties = EmailClientWindow.showNewAccountWindow();
		// If accountProperties equals null, it means the "Cancel" button was clicked
		if(accountProperties == null) {
			System.exit(0);
		}
		//Add code that we need to save the accountProperties to a file... DONE
		FileOutputStream fout = new FileOutputStream("AccountProperties.dat");
		ObjectOutputStream object = new ObjectOutputStream(fout);
		object.writeObject(accountProperties);
		object.close();
	}

	/**
	 * @param args
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		// Here we need to check, first than anything else, that there is an AccountProperties.dat
		// file. If it doesn't exists, we need to create it. You can use the previous method to do
		// so. DONE

		if(!new File("AccountProperties.dat").exists())
			try {
				createAccountProperties();
			} catch (IOException e) {
				e.printStackTrace();
			}

		//Read values
		try {
			FileInputStream fin = new FileInputStream("AccountProperties.dat");
			ObjectInputStream objectInputStream = new ObjectInputStream(fin);
			accountProperties = (Properties) objectInputStream.readObject();
			objectInputStream.close();
		} catch (IOException e) {
			// Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}


		// Also, implement here the code to check if EmailDataStorage.dat and ContactDataStorage.dat exists.
		// If they don't exist, we need to fetch emails from server and save them to the storage. If
		// there are more than 200 email messages in the server, we shall fetch and store only 200.
		// Otherwise, we only store as many emails as are present in the server. DONE
		// For the contact storage, we will only store one dummy contact. 
		server = new EmailServer(accountProperties.getProperty("username"), accountProperties.getProperty("password"), accountProperties.getProperty("incomingHost"), accountProperties.getProperty("outgoingHost"));
		if(true) {
			//!new File("EmailDataStorage.dat").exists()
			try {
				server.getInboxFromServer();
				if(server.getMessageCount() > 200) {
					Message[] messages = new Message[200];
					for (int i = 0; i < 200; i++) 
						messages[i] = server.getMessage(i);

					emailStorage = new EmailDataStorage(messages);
				}
				else {
					emailStorage = new EmailDataStorage(server.getAllMessages());
				}
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
		else {
			try {
				Scanner reader;
				reader = new Scanner(new FileReader("EmailDataStorage.dat"));
				reader.close();
				emailStorage = new EmailDataStorage();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}



		if(!new File("ContactDataStorage.dat").exists()) {
			EmailContact[] dummycontact = new EmailContact[1];
			dummycontact[0] = new EmailContact("Yamil", "yamil.asusta@upr.edu", "7875863172", "Home", "TROLOLOLO");
			contactStorage = new ContactDataStorage(dummycontact);
		}
		else {
			Scanner reader;
			String[] file;
			try
			{
				ArrayList<String> parse = new ArrayList<String>();
				reader = new Scanner(new FileReader("ContactDataStorage.dat"));
				while (reader.hasNext()) 
					parse.add(reader.nextLine());
				reader.close();	
				file = new String[parse.size()];
				for (int i = 0; i < file.length; i++) 
					file[i] = parse.get(i);
				contactStorage = new ContactDataStorage(file);
			}
			catch(Exception e)
			{
				System.out.println("Not found");
			}

		}


		//Add here the code for checking if required files to read exist. Otherwise, we need
		// to create them following the previous instructions. DONE

		// Once that code has been added we proceed to make the frame and pass it on the readed
		// fields.

		frame = new JFrame();
		frame.setSize(900,600);					// You can change this size if you wish to do so.
		frame.setTitle("Simple Email Client");	// You can change this name by your preference.
		frame.setResizable(false);				// This should remain like this.
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		EmailClientWindow gui = new EmailClientWindow(accountProperties, server, emailStorage, contactStorage);

		frame.add(gui.initializeWindow());

		// Here we are adding a WindowListener to the window frame. The WindowListener allows us
		// to do something before we exit the program. In this case, we want to save the email
		// and contact storage in case they were modified at some point in the program (the email
		// data storage by means of the refresh button and the contact data storage by adding
		// contacts). In any case, you should implement this code. What we are doing here is called
		// an internal class that implements only the method windowClosing() since is the one
		// we are interested in.

		frame.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent evt) {
				frame.setVisible(false);
				try {
					FileWriter fw = new FileWriter("ContactDataStorage.dat", false);
					String[] parse = contactStorage.toFileFormat();
					for (int i = 0; i < parse.length; i++) 
						fw.write(parse[i] + "\n");
					fw.close();
					FileOutputStream fout = new FileOutputStream("EmailDataStorage.dat");
					ObjectOutputStream objectOutputStream = new ObjectOutputStream(fout);
					objectOutputStream.writeObject(emailStorage.toFileFormat());
					objectOutputStream.close();
					emailStorage.loadFromFileFormat(emailStorage.toFileFormat());
				} 
				catch (IOException e) {
					return;
				}
				System.exit(0);

				//TODO Add here the rest of the code to save the email and contact data storage
				// to a file.

			}
		});

		frame.setVisible(true);

	}

}
