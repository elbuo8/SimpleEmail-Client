package test1;

import java.util.ArrayList;


/**
 * IMPORTANT: This class is to be implemented by students!
 * This class describes an object that stores contacts
 * @author yamilasusta
 *
 */
public class ContactDataStorage implements StorageDataStructure<EmailContact> {

	public ContactDataStorage(String[] file) {
		loadFromFileFormat(file);
	}
	
	public ContactDataStorage(EmailContact[] dummycontact) {
		emails = dummycontact;
	}

	@Override
	public void add(String key, EmailContact obj) {
		EmailContact[] temp = new EmailContact[Integer.parseInt(key)];
		for (int i = 0; i < emails.length; i++) 
			temp[i] = emails[i];
		temp[temp.length-1] = obj;
		emails = temp;
	}

	@Override
	public boolean remove(String key) {
		try {
			ArrayList<EmailContact> parse = new ArrayList<EmailContact>();
			for (int i = 0; i < emails.length; i++) 
				parse.add(emails[i]);
			parse.remove(Integer.parseInt(key));
			EmailContact[] temp = new EmailContact[parse.size()];
			for (int i = 0; i < parse.size(); i++) 
				temp[i] = parse.get(i);
			emails = temp;
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public EmailContact get(String key) {
		return emails[Integer.parseInt(key)-1];
	}

	@Override
	public boolean contains(String key) {
		for (int i = 0; i < emails.length; i++) 
			if(emails[i].getPhoneNumber().equalsIgnoreCase(key) || emails[i].getNote().equalsIgnoreCase(key) || emails[i].getName().equalsIgnoreCase(key) || emails[i].getEmail().equalsIgnoreCase(key) || emails[i].getAddress().equalsIgnoreCase(key))
				return true;
		return false;
	}

	@Override
	public boolean loadFromFileFormat(String[] file) {
		try {
			emails = new EmailContact[file.length];
			for (int i = 0; i < file.length; i++) {
				String[] parsed = file[i].split("-,-");
				emails[i] = new EmailContact(parsed[0], parsed[1], parsed[2], parsed[3], parsed[4]);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public String[] toFileFormat() {
		String[] parser = new String[emails.length];
		for (int i = 0; i < parser.length; i++) 
			parser[i] = emails[i].getName() + "-,-" + emails[i].getEmail() + "-,-" + emails[i].getPhoneNumber() + "-,-" + emails[i].getAddress() + "-,-" + emails[i].getNote();
		return parser;
	}

	@Override
	public EmailContact[] getAllValues() {
		return emails;
	}

	@Override
	public int size() {
		return emails.length;
	}

	@Override
	public boolean isEmpty() {
		if(emails[0] == null)
			return true;
		return false;
	}

	@Override
	public void clear() {
		emails = new EmailContact[1];
	}

	private EmailContact[] emails;
}
