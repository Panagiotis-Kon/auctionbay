package com.ted.auctionbay.entities.users.messages;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the mailbox database table.
 * 
 */
@Entity
@NamedQuery(name="Mailbox.findAll", query="SELECT m FROM Mailbox m")
public class Mailbox implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MailboxPK id;

	private String type;

	//bi-directional many-to-one association to Message
	@ManyToOne
	@JoinColumn(name="MessageID")
	private Message message;

	public Mailbox() {
	}

	public MailboxPK getId() {
		return this.id;
	}

	public void setId(MailboxPK id) {
		this.id = id;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Message getMessage() {
		return this.message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

}