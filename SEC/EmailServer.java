package test1;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * IMPORTANT: You're not allowed to modify in anyway this class. You must read it to
 * become familiar with the methods on it and how they're implemented, this will help
 * you understand how it works and how to use those methods.
 *
 * This class constructs an object from where users can have easy access
 * to an email account's messages, folders, etc.
 * @author hfranqui
 *
 */
public class EmailServer {


	/**
	 * Private class that creates a Mail authenticator object for servers that require it.
	 * @author hfranqui
	 *
	 */
	private class MailAuthenticator extends Authenticator {

		// Return the PasswordAuthentication object used with the parent's
		// class username and password.
		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(username, password);
		}

	}

	/**
	 * Constructor for initializing a new email server session. The required parameters are
	 * for setting up a connection to the desired email account and be able to interact with it. 
	 * A very important note here is that the connection with email servers is done using
	 * two distinct protocols. The POP3 protocol is used to read messages from server and 
	 * the SMTP protocol for sending email messages. The most popular email providers, Google,
	 * Hotmail (aka Windows Live) and Yahoo! use this convention.
	 * 
	 * @param username The user name of the email account to be accessed
	 * @param password The password of the email account to be accessed
	 * @param incomingHost The POP3 server address of the email account which will be accessed. This
	 * varies depending on the email service provider.
	 * @param outgoingHost The SMTP server address of the email account which will be used to send
	 * email messages. This varies depending on the email service provider.
	 */
	public EmailServer(String username, String password, String incomingHost, String outgoingHost) {
		this.username = username;
		this.password = password;
		this.incomingHost = incomingHost;
		this.outgoingHost = outgoingHost;

		// Sets up the email session's properties
		emailServerProperties = new Properties();
		// SMTP or Simple Mail Transfer Protocol SSL setup
		emailServerProperties.setProperty("mail.transport.protocol", "smtp");
		// POP 3 or Post Office Protocol 3 SSL setup
		emailServerProperties.setProperty("mail.store.protocol", "pop3s");
		// Sets the SMTP host address
		emailServerProperties.setProperty("mail.smtp.host", this.outgoingHost);
		// Enables authentication for sending (most email providers require this)
		emailServerProperties.setProperty("mail.smtp.auth", "true");
		// Sets the POP3 host address
		emailServerProperties.setProperty("mail.pop3s.host", this.incomingHost);
	
		// This code was required for Hotmail to work...
		emailServerProperties.setProperty("mail.smtp.starttls.enable", "true");
		//emailServerProperties.setProperty("mail.smtps.port", "587");
		
		// Initiates the email session
		emailSession = Session.getDefaultInstance(emailServerProperties, new MailAuthenticator());
		//emailSession.setDebug(true);
	}

	/**
	 * Sends a message that is contained in a Message object
	 * @param message Any object of type Message containing the email message to be sent.
	 * @throws MessagingException 
	 */
	public void sendMessage(Message message) throws MessagingException {
		// Connect to server and send message
		Transport transport = emailSession.getTransport("smtp");
		transport.connect();
		transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
		transport.close();
	}
	
	/**
	 * IMPORTANT: This method is only for testing purposes. In the final implementation of
	 * your project you must use the object oriented method sendMessage(Message message).
	 * 
	 * Sends a new plain text message to only one recipient address.
	 * @param recipient The email address of the recipient.
	 * @param subject The subject of the email message
	 * @param messageContents The contents of the email message.
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public void sendMessage(String recipient, String subject, String messageContents) throws MessagingException {
		Transport transport = emailSession.getTransport("smtp");
		MimeMessage toSend = new MimeMessage(emailSession);

		// Creates a new message
		toSend.setSubject(subject);
		toSend.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
		toSend.setContent(messageContents, "text/plain");

		// Connects to server and sends message
		transport.connect();
		transport.sendMessage(toSend,toSend.getRecipients(Message.RecipientType.TO));
		transport.close();
	}

	/**
	 * Returns a message from the inbox folder.
	 * @param index The index of the message in the inbox folder.
	 * @return The message with the given index
	 * @throws MessagingException 
	 */
	public Message getMessage(int index) throws MessagingException {
		return inbox.getMessage(index);
	}

	/**
	 * Returns all messages in the inbox folder.
	 * @return An array of Message objects containing all email messages in the inbox.
	 * @throws MessagingExceptionif (emailStorage.size() < 25) {
					currentEnd = emailStorage.size()-1;
					pageIncrease = emailStorage.size()-1;			
				}
				else {
					pageIncrease = emailStorage.size()/8;
				}
	 */
	public Message[] getAllMessages() throws MessagingException {
		return inbox.getMessages();
	}
	
	/**
	 * Reads the inbox folder from the server. Use this only to initialize the server;
	 * in other words, do this once only and then you can use all the other methods
	 * that deal with retrieving message information from server. NOTE: This is not
	 * required to send messages, only to read them!
	 * @throws MessagingException
	 */
	public void getInboxFromServer() throws MessagingException {
		emailStore = emailSession.getStore("pop3s");
		emailStore.connect(this.incomingHost, this.username, this.password);
		inbox = emailStore.getFolder("inbox");
		inbox.open(Folder.READ_ONLY);
	}

	/**
	 * Returns the number of messages in a folder
	 * @param folder The folder to get the number of messages
	 * @return The count of messages in the folder
	 * @throws MessagingException 
	 */
	public int getMessageCount() throws MessagingException {
		return inbox.getMessageCount();
	}
 	
	/**
	 * Returns the current email session in order to use MimeMessages outside this class.
	 * @return The email session initialized when the EmailServer object was constructed.
	 */
	public Session getSession() {
		return emailSession;
	}

	private Folder inbox;
	private Store emailStore;
	private Session emailSession;
	private Properties emailServerProperties;
	private String username, password, incomingHost, outgoingHost;

}
