package test1;

import java.io.Serializable;


/**
 * This class describes a contact to be used on the email client. 
 * @author yamilasusta
 *
 */
public class EmailContact implements Serializable{

	/**
	 * Avoid Eclipse warnings
	 */
	private static final long serialVersionUID = -2796637422692758004L;
	
	/**
	 * Default constructor
	 * @param name Name
	 * @param email Email
	 * @param phone Phone
	 * @param address Address
	 * @param note Note
	 */
	public EmailContact(String name, String email, String phone, String address,
			String note) {
		this.email = email;
		this.name = name;
		this.address = address;
		this.phone = phone;
		this.note = note;
	}

	/**
	 * 
	 * @return The email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * 
	 * @return The note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * 
	 * @return The address 
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * 
	 * @return Phone Number
	 */
	public String getPhoneNumber() {
		return phone;
	}

	/**
	 * 
	 * @return Email address
	 */
	public String getEmailAddress() {
		return getEmail();
	}

	/**
	 * 
	 * @return The name
	 */
	public String getName() {
		return name;
	}

	private String email;
	private String note;
	private String address;
	private String phone;
	private String name;
	
}
