package test1;

import java.util.ArrayList;


/**
 * This class describes an object that stores contacts
 * @author yamilasusta
 *
 */
public class ContactDataStorage implements StorageDataStructure<EmailContact> {

	/**
	 * Constructor for when the file exists.
	 * @param file
	 */
	public ContactDataStorage(String[] file) {
		loadFromFileFormat(file);
	}
	
	/**
	 * Constructor for when the file doesn't exist
	 * @param dummycontact First contact
	 */
	public ContactDataStorage(EmailContact[] dummycontact) {
		emails = dummycontact;
	}

	/**
	 * Adds a new object to the storage. If a key already exists in the storage, the object
	 * with that key is replaced by the given one.
	 * @param key The key of the object to be stored
	 * @param obj The object to be stored.
	 */
	public void add(String key, EmailContact obj) {
		EmailContact[] temp = new EmailContact[Integer.parseInt(key)];
		for (int i = 0; i < emails.length; i++) 
			temp[i] = emails[i];
		temp[temp.length-1] = obj;
		emails = temp;
	}

	/**
	 * Removes the object with the specified key from the storage.
	 * @param key The key of the object to be removed.
	 * @return True if the object was successfully removed, false otherwise.
	 */
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

	/**
	 * Returns the object with the given key in the storage.
	 * @param key The key of the object to be returned.
	 * @return The object that has the key specified.
	 */
	public EmailContact get(String key) {
		return emails[Integer.parseInt(key)-1];
	}

	/**
	 * Checks whether an object with the given key exists in the storage.
	 * @param key The key of the object to be checked.
	 * @return True if the object is in the storage, false otherwise.
	 */
	public boolean contains(String key) {
		if(Integer.parseInt(key) <= emails.length-1)
			return true;
		return false;
	}

	/**
	 * Loads the storage from a given String array, that could be loaded from a file.
	 * @param file The String array containing a storage object.
	 * @return True if the String array contained a proper storage object, false otherwise.
	 */
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

	/**
	 * Returns a String array describing the data storage that can be saved to a file. 
	 * @return The String array containing the data storage.
	 */
	public String[] toFileFormat() {
		String[] parser = new String[emails.length];
		for (int i = 0; i < parser.length; i++) 
			parser[i] = emails[i].getName() + "-,-" + emails[i].getEmail() + "-,-" + emails[i].getPhoneNumber() + "-,-" + emails[i].getAddress() + "-,-" + emails[i].getNote();
		return parser;
	}

	/**
	 * Returns an array of all the objects of type E being stored in the storage.
	 * @return Array of objects stored.
	 */
	public EmailContact[] getAllValues() {
		return emails;
	}

	/**
	 * Returns the actual size of the storage. 
	 * @return The size of the storage.
	 */
	public int size() {
		return emails.length;
	}

	/**
	 * Checks whether the data storage is empty or not.
	 * @return True if it's empty, false otherwise.
	 */
	public boolean isEmpty() {
		if(emails[0] == null)
			return true;
		return false;
	}

	/**
	 * Empties the Data Storage.
	 */
	public void clear() {
		emails = new EmailContact[1];
	}

	private EmailContact[] emails;
}
