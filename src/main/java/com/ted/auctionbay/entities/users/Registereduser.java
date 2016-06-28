package com.ted.auctionbay.entities.users;

import java.io.Serializable;
import javax.persistence.*;
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

	//bi-directional one-to-one association to User
	@OneToOne
	@PrimaryKeyJoinColumn(name="Username")
	private User user;

	//bi-directional many-to-one association to RegistereduserBidsinAuction
	@OneToMany(mappedBy="registereduser")
	private List<RegistereduserBidsinAuction> registereduserBidsinAuctions;

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

	public List<RegistereduserBidsinAuction> getRegistereduserBidsinAuctions() {
		return this.registereduserBidsinAuctions;
	}

	public void setRegistereduserBidsinAuctions(List<RegistereduserBidsinAuction> registereduserBidsinAuctions) {
		this.registereduserBidsinAuctions = registereduserBidsinAuctions;
	}

	public RegistereduserBidsinAuction addRegistereduserBidsinAuction(RegistereduserBidsinAuction registereduserBidsinAuction) {
		getRegistereduserBidsinAuctions().add(registereduserBidsinAuction);
		registereduserBidsinAuction.setRegistereduser(this);

		return registereduserBidsinAuction;
	}

	public RegistereduserBidsinAuction removeRegistereduserBidsinAuction(RegistereduserBidsinAuction registereduserBidsinAuction) {
		getRegistereduserBidsinAuctions().remove(registereduserBidsinAuction);
		registereduserBidsinAuction.setRegistereduser(null);

		return registereduserBidsinAuction;
	}

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