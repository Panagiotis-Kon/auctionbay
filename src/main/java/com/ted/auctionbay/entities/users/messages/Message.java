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

	private String fromUser;

	private byte isRead;

	@Lob
	private String messageText;

	private String subject;

	private String toUser;

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

	public String getFromUser() {
		return this.fromUser;
	}

	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	public byte getIsRead() {
		return this.isRead;
	}

	public void setIsRead(byte isRead) {
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

	public String getToUser() {
		return this.toUser;
	}

	public void setToUser(String toUser) {
		this.toUser = toUser;
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