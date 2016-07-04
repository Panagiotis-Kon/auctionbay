package com.ted.auctionbay.services;

import java.util.List;

import com.ted.auctionbay.entities.auctions.Auction;
import com.ted.auctionbay.entities.items.Category;

public interface AuctionServices {

	public List<Auction> getAuctions();
	public List<Object[]> getAllCategories();
	public int numOfAuctions();
	public Auction getDetails();
	public int getNumOfBids(int auction_id);
	public float getHighestBid(int auction_id);
	public List<Auction> getAuctionByCategory(int start, int end, String Category);
	
}
