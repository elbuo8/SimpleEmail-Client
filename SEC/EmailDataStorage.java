package test1;

import java.io.IOException;
import java.util.ArrayList;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;



/**
 * This class describes some kind of database in which email messages are stored.
 * @author yamilasusta
 *
 */
public class EmailDataStorage implements StorageDataStructure<Message> {

	/**
	 * Constructor for when the file doesn't exist
	 * @param messages Message array
	 * @param server Server session
	 * 
	 */
	public EmailDataStorage(Message[] messages, EmailServer server) {
		mails = messages;
		this.server = server;
	}

	/**
	 * Constructor for when the file exits
	 * @param server
	 */
	public EmailDataStorage(EmailServer server) {
		this.server = server;
	}

	/**
	 * Adds a new object to the storage. If a key already exists in the storage, the object
	 * with that key is replaced by the given one.
	 * @param key The key of the object to be stored
	 * @param obj The object to be stored.
	 */
	public void add(String key, Message obj) {
		if (isEmpty()) {
			mails = new Message[1];
			mails[0] = obj;
		}
		else {
			Message[] temp = new Message[Integer.parseInt(key)];
			for (int i = 0; i < mails.length; i++) 
				temp[i] = mails[i];
			temp[temp.length-1] = obj;
			mails = temp;				
		}
	}

	/**
	 * Removes the object with the specified key from the storage.
	 * @param key The key of the object to be removed.
	 * @return True if the object was successfully removed, false otherwise.
	 */
	public boolean remove(String key) {
		try {
			ArrayList<Message> parse = new ArrayList<Message>();
			for (int i = 0; i < mails.length; i++) 
				parse.add(mails[i]);
			parse.remove(Integer.parseInt(key));
			Message[] temp = new Message[parse.size()];
			for (int i = 0; i < parse.size(); i++) 
				temp[i] = parse.get(i);
			mails = temp;
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Returns the object with the given key in the storage.
	 * @param key The key of the object to be returned.
	 * @return The object that has the key specified.
	 */
	public Message get(String key) {
		return mails[Integer.parseInt(key)];
	}

	/**
	 * Loads the storage from a given String array, that could be loaded from a file.
	 * @param file The String array containing a storage object.
	 * @return True if the String array contained a proper storage object, false otherwise.
	 */
	public boolean contains(String key) {
		if(Integer.parseInt(key) <= size()-1)
			return true;
		return false;
	}
	/**
	 * Returns a String array describing the data storage that can be saved to a file. 
	 * @return The String array containing the data storage.
	 */
	public boolean loadFromFileFormat(String[] file) {
		clear();
		MimeMessage[] mail = new MimeMessage[file.length];
		if(mails == null)
			mails = new Message[file.length];

		for (int i = 0; i < file.length; i++) {
			mail[i] = new MimeMessage(server.getSession());
			String[] parse = file[i].split("-,-");
			try {
				mail[i].setSubject(parse[0]);
				mail[i].setFrom(new InternetAddress(parse[1]));
				mail[i].setRecipient(RecipientType.TO, new InternetAddress(parse[2]));
				mail[i].setContent(parse[3], "text/html");
				mails[i] = mail[i];
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
		return true;	
	}

	/**
	 * Returns an array of all the objects of type E being stored in the storage.
	 * @return Array of objects stored.
	 */
	public String[] toFileFormat() {
		String[] parser = new String[mails.length];
		try {
			for (int i = 0; i < parser.length; i++) {
				Address[] mailAddresses = mails[i].getFrom();
				String from = mailAddresses[0].toString();
				mailAddresses = mails[i].getAllRecipients();
				String reply = mailAddresses[0].toString();

				String subject = mails[i].getSubject().toString();
				if(subject.equals(""))
					subject = "(nosubject)";

				String message = getText(mails[i]);
				if(message.equals(""))
					message = "(emptyemail)";

				parser[i] = subject + "-,-" + from + "-,-" + reply + "-,-" + message;
			}
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return parser;
	}

	/**
	 * Returns the actual size of the storage. 
	 * @return The size of the storage.
	 */
	public Message[] getAllValues() {
		return mails;
	}

	/**
	 * Checks whether the data storage is empty or not.
	 * @return True if it's empty, false otherwise.
	 */
	public int size() {
		if(isEmpty())
			return 1;
		return mails.length;
	}

	/**
	 * Checks whether the data storage is empty or not.
	 * @return True if it's empty, false otherwise.
	 */
	public boolean isEmpty() {
		if(mails == null)
			return true;
		return false;
	}

	/**
	 * Empties the Data Storage.
	 */
	public void clear() {
		mails = null;
	}

	/**
	 * Return the primary text content of the message.
	 */
	static public String getText(Part p) throws MessagingException, IOException {

		if (p.isMimeType("text/*")) {
			String s = (String)p.getContent();
			return s;
		}

		if (p.isMimeType("multipart/alternative")) {
			Multipart mp = (Multipart)p.getContent();
			String text = null;
			for (int i = 0; i < mp.getCount(); i++) {
				Part bp = mp.getBodyPart(i);
				if (bp.isMimeType("text/plain")) {
					if (text == null)
						text = getText(bp);
					continue;
				} else if (bp.isMimeType("text/html")) {
					String s = getText(bp);
					if (s != null)
						return s;
				} else {
					return getText(bp);
				}
			}
			return text;
		} else if (p.isMimeType("multipart/*")) {
			Multipart mp = (Multipart)p.getContent();
			for (int i = 0; i < mp.getCount(); i++) {
				String s = getText(mp.getBodyPart(i));
				if (s != null)
					return s;
			}
		}
		return null;
	}

	private Message[] mails;
	private EmailServer server;
}
