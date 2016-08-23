package com.ted.auctionbay.dao;

import java.util.HashMap;
import java.util.List;

import com.ted.auctionbay.entities.auctions.Auction;

public interface QueryAuction {
	
	public int numOfAuctions();
	
	public int numOfActiveAuctions();
	
	public List<Auction> getAuctions();
	
	public List<Auction> getAuctions(int startpage, int endpage);
	
	public List<Auction> getActiveAuctions(int startpage, int endpage);
	
	public List<Auction> getExpiredAuctions();
	
	public List<Auction> getAuctionsByCategory(int startpage, int endpage, String category);
	
	public List<Auction> getActiveAuctionsByCategory(int startpage, int endpage, String category);
	
	public int getNumOfBids(int auction_id);
	
	public float getHighestBid(int auction_id);
	
	public Auction getDetails(int ItemID);
	
	public int maxAuctionID();
	
	public int submitAuction(Auction auction);
	
	public int deleteAuction(String username, int itemID,int auctionID);
	
	public boolean alreadyBidded(String username, int itemID);
	
	public void updateBid(String username, int itemID, float bid_amount);
	
	public List<Object[]> getBidHistory(int auctionID);
	
	public List<Auction> advancedSearch(String keywords, List<String> Categories, String Location, String minBid, String maxBid);
	
	public int delAuction(String Username, int auctionID, int ItemID);
	
	public List<Object[]> BidderExpiredAuction(String username);
}
