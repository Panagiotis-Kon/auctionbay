package com.ted.auctionbay.entities.users;

import java.io.Serializable;
import javax.persistence.*;

import com.ted.auctionbay.entities.auctions.Auction;
import com.ted.auctionbay.entities.users.messages.Mailbox;
import com.ted.auctionbay.entities.users.messages.Message;

import java.util.List;


/**
 * The persistent class for the registereduser database table.
 * 
 */
@Entity
@NamedQuery(name="Registereduser.findAll", query="SELECT r FROM Registereduser r")
public class Registereduser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String username;

	//bi-directional many-to-one association to Bidderrating
	@OneToMany(mappedBy="registereduser")
	private List<Bidderrating> bidderratings;

	//bi-directional many-to-one association to Message
	@OneToMany(mappedBy="recipient",cascade=CascadeType.ALL)
	private List<Message> outBoxMessages;
		
	//bi-directional many-to-one association to Mailbox
	@OneToOne(mappedBy="registereduser")
	private Mailbox mailboxs;
	
	//bi-directional many-to-one association to Message
	@OneToMany(mappedBy="sender",cascade=CascadeType.ALL)
	private List<Message> inboxMessages;

	
	
	
	public List<Message> getInboxMessages() {
		return inboxMessages;
	}

	public void setInboxMessages(List<Message> inboxMessages) {
		this.inboxMessages = inboxMessages;
	}

	public List<Message> getOutBoxMessages() {
		return outBoxMessages;
	}

	public void setOutBoxMessages(List<Message> outBoxMessages) {
		this.outBoxMessages = outBoxMessages;
	}

	
	
	public Mailbox getMailboxs() {
		return mailboxs;
	}

	public void setMailboxs(Mailbox mailboxs) {
		this.mailboxs = mailboxs;
	}

	//bi-directional one-to-one association to User
	@OneToOne
	@PrimaryKeyJoinColumn(name="Username")
	private User user;

	/*
	 * 
	 * Info: 
	 * http://stackoverflow.com/questions/28755832/jpa-many-to-many-relation-not-inserting-into-generated-table
	 * 
	 * */
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(
		name="registereduser_bidsin_auction"
		, joinColumns={
			@JoinColumn(name="bidder_Username")
			}
		, inverseJoinColumns={
			@JoinColumn(name="AuctionID")
			}
		)
	private List<Auction> auctionsBids;
	
	//bi-directional many-to-one association to RegistereduserBidsinAuction
	//@OneToMany(mappedBy="registereduser")
	//private List<RegistereduserBidsinAuction> registereduserBidsinAuctions;

	//bi-directional many-to-one association to Sellerrating
	@OneToMany(mappedBy="registereduser")
	private List<Sellerrating> sellerratings;

	public Registereduser() {
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<Bidderrating> getBidderratings() {
		return this.bidderratings;
	}

	public void setBidderratings(List<Bidderrating> bidderratings) {
		this.bidderratings = bidderratings;
	}

	public Bidderrating addBidderrating(Bidderrating bidderrating) {
		getBidderratings().add(bidderrating);
		bidderrating.setRegistereduser(this);

		return bidderrating;
	}

	public Bidderrating removeBidderrating(Bidderrating bidderrating) {
		getBidderratings().remove(bidderrating);
		bidderrating.setRegistereduser(null);

		return bidderrating;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	/*public List<RegistereduserBidsinAuction> getRegistereduserBidsinAuctions() {
		return this.registereduserBidsinAuctions;
	}

	public void setRegistereduserBidsinAuctions(List<RegistereduserBidsinAuction> registereduserBidsinAuctions) {
		this.registereduserBidsinAuctions = registereduserBidsinAuctions;
	}*/

	/*public RegistereduserBidsinAuction addRegistereduserBidsinAuction(RegistereduserBidsinAuction registereduserBidsinAuction) {
		getRegistereduserBidsinAuctions().add(registereduserBidsinAuction);
		registereduserBidsinAuction.setRegistereduser(this);

		return registereduserBidsinAuction;
	}

	public RegistereduserBidsinAuction removeRegistereduserBidsinAuction(RegistereduserBidsinAuction registereduserBidsinAuction) {
		getRegistereduserBidsinAuctions().remove(registereduserBidsinAuction);
		registereduserBidsinAuction.setRegistereduser(null);

		return registereduserBidsinAuction;
	}*/

	public List<Sellerrating> getSellerratings() {
		return this.sellerratings;
	}

	public void setSellerratings(List<Sellerrating> sellerratings) {
		this.sellerratings = sellerratings;
	}

	public Sellerrating addSellerrating(Sellerrating sellerrating) {
		getSellerratings().add(sellerrating);
		sellerrating.setRegistereduser(this);

		return sellerrating;
	}

	public Sellerrating removeSellerrating(Sellerrating sellerrating) {
		getSellerratings().remove(sellerrating);
		sellerrating.setRegistereduser(null);

		return sellerrating;
	}

}