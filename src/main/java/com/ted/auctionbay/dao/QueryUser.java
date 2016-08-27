package com.ted.auctionbay.dao;

import java.util.List;

import com.ted.auctionbay.entities.auctions.Auction;
import com.ted.auctionbay.entities.users.Bidderrating;
import com.ted.auctionbay.entities.users.Pendinguser;
import com.ted.auctionbay.entities.users.Registereduser;
import com.ted.auctionbay.entities.users.RegistereduserBidsinAuction;
import com.ted.auctionbay.entities.users.Sellerrating;
import com.ted.auctionbay.entities.users.User;

public interface QueryUser {

	public boolean userExists(String username);
	
	public boolean fetchPendingByUsername(String username);
	
	public User getUser(String username);
	
	public int count_registered();
	
	public int count_pending();
	
	public boolean user_validator(String username, String password);
	
	public List<Pendinguser>  getPendingUsers();
	
	public int getAddressMaxID();
	
	public List<Registereduser> getGroupsOfUsers(int startpage, int pagesize);
	
	public List<Registereduser> getRecipients();
	
	public int registeredNumber();
	
	public void accept_user(String username);
	
	public int registerUser(User ruser);
	
	public int count_all_user_auctions(String username);
	
	public int count_active_user_auctions(String username);
	
	public int count_expired_user_auctions(String username);
	
	public List<Auction> get_all_user_auctions(String username);
	
	public List<Auction> get_active_user_auctions(String username);
	
	public List<Auction> get_expired_user_auctions(String username);
	
	public void deleteBidderFromAuction(String username, int auctionID);
	
	public int createBidInUser(RegistereduserBidsinAuction rba);
	
	public int appendBuyerHistory(String username, int itemID);
	
	public int maxBidderRatingID();
	
	public int maxSellerRatingID();
	
	public void submitBidderRating(Bidderrating bidder_rate);
	
	public void submitSellerRating(Sellerrating seller_rate);
	
	public List<User> getUsers();
	
	public List<String> getBiddersbyRate();
	
	public List<String> getSellersbyRate();
	
}
