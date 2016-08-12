package com.ted.auctionbay.entities.users.messages;

import java.io.Serializable;
import javax.persistence.*;

import com.ted.auctionbay.entities.users.Registereduser;


/**
 * The persistent class for the mailbox database table.
 * 
 */
@Entity
@NamedQueries({
	@NamedQuery(name="Mailbox.findAll", query="SELECT m FROM Mailbox m"),
	@NamedQuery(name="Mailbox.maxID", query="SELECT MAX(mb.id.id) FROM Mailbox mb")
})

public class Mailbox implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MailboxPK id;

	private String type;

	//bi-directional many-to-one association to Message
	@ManyToOne
	@JoinColumn(name="MessageID")
	private Message message;

	//bi-directional many-to-one association to Registereduser
	@OneToOne
	@JoinColumn(name="RegisteredUser")
	private Registereduser registereduser;
	
	public Registereduser getRegistereduser() {
		return registereduser;
	}

	public void setRegistereduser(Registereduser registereduser) {
		this.registereduser = registereduser;
	}

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