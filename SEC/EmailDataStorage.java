package SEC;

import java.io.IOException;
import java.util.ArrayList;

import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;



/**
 * IMPORTANT: THIS CLASS IS TO BE IMPLEMENTED BY STUDENTS!
 * This class describes some kind of database in which email messages are stored.
 * @author 
 *
 */
public class EmailDataStorage implements StorageDataStructure<Message> {

	public EmailDataStorage() {

	}

	public EmailDataStorage(Message[] messages) {
		mails = messages;
	}

	@Override
	public void add(String key, Message obj) {
		Message[] temp = new Message[Integer.parseInt(key)];
		for (int i = 0; i < mails.length; i++) 
			temp[i] = mails[i];
		temp[temp.length-1] = obj;
		mails = temp;	
	}

	@Override
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

	@Override
	public Message get(String key) {
		return mails[Integer.parseInt(key)];
	}

	@Override
	public boolean contains(String key) {
		for (int i = 0; i < mails.length; i++)
			try {
				if(mails[i].getSubject().equalsIgnoreCase(key))
					return true;
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return false;
	}

	@Override
	public boolean loadFromFileFormat(String[] file) {
		Message[] mail = new Message[file.length];
		for (int i = 0; i < file.length; i++) {
			//mail[i] = new Me;
			String[] parse = file[i].split("-,-");
			for (int j = 0; j < parse.length; j++) {
				System.out.println(parse[j]);
			}
			try {
				mail[i].setSubject(parse[0]);
				mail[i].setFrom(new InternetAddress(parse[1]));
				mail[i].setRecipient(RecipientType.TO, new InternetAddress(parse[2]));
				mail[i].setContent(parse[4], "text/html");
				System.out.println(mail[i]);
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return false;
	}

	@Override
	public String[] toFileFormat() {
		String[] parser = new String[mails.length];
		try {
			for (int i = 0; i < parser.length; i++) {
				Address[] mailAddresses = mails[i].getFrom();
				String from = mailAddresses[0].toString();
				mailAddresses = mails[i].getAllRecipients();
				String reply = mailAddresses[0].toString();
				parser[i] = mails[i].getSubject() + "-,-" + from + "-,-" + reply + "-,-" + getText(mails[i]);
			}
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return parser;
	}

	@Override
	public Message[] getAllValues() {
		return mails;
	}

	@Override
	public int size() {
		return mails.length;
	}

	@Override
	public boolean isEmpty() {
		if(mails[0] == null)
			return true;
		return false;
	}

	@Override
	public void clear() {
		mails = new Message[1];
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
}
