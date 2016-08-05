package com.ted.auctionbay.entities.auctions;

import java.io.Serializable;
import javax.persistence.*;

import com.ted.auctionbay.entities.items.Item;
import com.ted.auctionbay.entities.users.Registereduser;

import java.util.Date;


/**
 * The persistent class for the auction database table.
 * 
 */
@Entity
@NamedQueries({
	@NamedQuery(name="Auction.findAll", query="SELECT a FROM Auction a"),
	@NamedQuery(name="Auction.auctionMaxID", query="SELECT MAX(a.auctionID) FROM Auction a")

})
public class Auction implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int auctionID;

	private float buyPrice;

	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;

	private float firstBid;

	//private int itemID;

	//private String seller;

	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;

	private String title;

	//bi-directional many-to-one association to Product
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="ItemID")
	private Item item;

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Registereduser getRegistereduser() {
		return registereduser;
	}

	public void setRegistereduser(Registereduser registereduser) {
		this.registereduser = registereduser;
	}

	//bi-directional many-to-one association to Registereduser
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="Seller")
	private  Registereduser registereduser;
	
	
	public Auction() {
	}

	public int getAuctionID() {
		return this.auctionID;
	}

	public void setAuctionID(int auctionID) {
		this.auctionID = auctionID;
	}

	public float getBuyPrice() {
		return this.buyPrice;
	}

	public void setBuyPrice(float buyPrice) {
		this.buyPrice = buyPrice;
	}

	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public float getFirstBid() {
		return this.firstBid;
	}

	public void setFirstBid(float firstBid) {
		this.firstBid = firstBid;
	}

	/*public int getItemID() {
		return this.itemID;
	}

	public void setItemID(int itemID) {
		this.itemID = itemID;
	}*/

	/*public String getSeller() {
		return this.seller;
	}

	public void setSeller(String seller) {
		this.seller = seller;
	}*/

	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}