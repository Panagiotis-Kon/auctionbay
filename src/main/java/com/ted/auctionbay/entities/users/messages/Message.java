package com.ted.auctionbay.entities.users.messages;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the message database table.
 * 
 */
@Entity
@NamedQuery(name="Message.findAll", query="SELECT m FROM Message m")
public class Message implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int messageID;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreated;

	private String fromUsername;

	private int isRead;

	@Lob
	private String messageText;

	private String subject;

	private String toUsername;

	//bi-directional many-to-one association to Mailbox
	@OneToMany(mappedBy="message")
	private List<Mailbox> mailboxs;

	public Message() {
	}

	public int getMessageID() {
		return this.messageID;
	}

	public void setMessageID(int messageID) {
		this.messageID = messageID;
	}

	public Date getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getFromUsername() {
		return this.fromUsername;
	}

	public void setFromUsername(String fromUsername) {
		this.fromUsername = fromUsername;
	}

	public int getIsRead() {
		return this.isRead;
	}

	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}

	public String getMessageText() {
		return this.messageText;
	}

	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	public String getSubject() {
		return this.subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getToUsername() {
		return this.toUsername;
	}

	public void setToUsername(String toUsername) {
		this.toUsername = toUsername;
	}

	public List<Mailbox> getMailboxs() {
		return this.mailboxs;
	}

	public void setMailboxs(List<Mailbox> mailboxs) {
		this.mailboxs = mailboxs;
	}

	public Mailbox addMailbox(Mailbox mailbox) {
		getMailboxs().add(mailbox);
		mailbox.setMessage(this);

		return mailbox;
	}

	public Mailbox removeMailbox(Mailbox mailbox) {
		getMailboxs().remove(mailbox);
		mailbox.setMessage(null);

		return mailbox;
	}

}