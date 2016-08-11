package com.ted.auctionbay.dao;

import java.util.HashMap;
import java.util.List;

import com.ted.auctionbay.entities.auctions.Auction;

public interface QueryAuction {
	public int numOfAuctions();
	
	public List<Auction> getAuctions(int startpage, int endpage);
	
	public List<Auction> getAuctionsByCategory(int startpage, int endpage, String category);
	
	public int getNumOfBids(int auction_id);
	
	public float getHighestBid(int auction_id);
	
	public Auction getDetails(int ItemID);
	
	public int maxAuctionID();
	
	public int submitAuction(Auction auction);
	
	public int deleteAuction(int auctionID);
	
	public boolean alreadyBidded(String username, int itemID);
	
	public void updateBid(String username, int itemID, float bid_amount);
	
	public List<Object[]> getBidHistory(int auctionID);
	
	public List<Auction> advancedSearch(String keywords, String description, List<String> Categories, String Location, String minBid, String maxBid);
}
