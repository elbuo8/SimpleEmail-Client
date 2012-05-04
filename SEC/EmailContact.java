package test1;

import java.io.Serializable;


/**
 * IMPORTANT: This class is to be implemented by students!
 * This class describes a contact to be used on the email client. 
 * @author 
 *
 */
public class EmailContact implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2796637422692758004L;
	public EmailContact(String name, String email, String phone, String address,
			String note) {
		this.email = email;
		this.name = name;
		this.address = address;
		this.phone = phone;
		this.note = note;
	}

	public String getEmail() {
		return email;
	}

	public String getNote() {
		return note;
	}

	public String getAddress() {
		return address;
	}

	public String getPhoneNumber() {
		return phone;
	}

	public String getEmailAddress() {
		return getEmail();
	}

	public String getName() {
		return name;
	}

	private String email;
	private String note;
	private String address;
	private String phone;
	private String name;
	
}
