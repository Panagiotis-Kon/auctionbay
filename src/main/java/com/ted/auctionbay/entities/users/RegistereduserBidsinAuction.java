package com.ted.auctionbay.entities.users;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the registereduser_bidsin_auction database table.
 * 
 */
@Entity
@Table(name="registereduser_bidsin_auction")
@NamedQuery(name="RegistereduserBidsinAuction.findAll", query="SELECT r FROM RegistereduserBidsinAuction r")
public class RegistereduserBidsinAuction implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private RegistereduserBidsinAuctionPK id;

	private float bid;

	@Temporal(TemporalType.TIMESTAMP)
	private Date bidTime;

	//bi-directional many-to-one association to Registereduser
	@ManyToOne
	@JoinColumn(name="Bidder_Username")
	private Registereduser registereduser;

	public RegistereduserBidsinAuction() {
	}

	public RegistereduserBidsinAuctionPK getId() {
		return this.id;
	}

	public void setId(RegistereduserBidsinAuctionPK id) {
		this.id = id;
	}

	public float getBid() {
		return this.bid;
	}

	public void setBid(float bid) {
		this.bid = bid;
	}

	public Date getBidTime() {
		return this.bidTime;
	}

	public void setBidTime(Date bidTime) {
		this.bidTime = bidTime;
	}

	public Registereduser getRegistereduser() {
		return this.registereduser;
	}

	public void setRegistereduser(Registereduser registereduser) {
		this.registereduser = registereduser;
	}

}