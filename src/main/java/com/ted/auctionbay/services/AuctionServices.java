package com.ted.auctionbay.services;

import java.util.List;

import com.ted.auctionbay.entities.auctions.Auction;
import com.ted.auctionbay.entities.items.Category;

public interface AuctionServices {

	public List<Auction> getAuctions(int startpage, int endpage);
	public List<Object[]> getAllCategories();
	public int numOfAuctions();
	public Auction getDetails(int ItemID);
	public int getNumOfBids(int auction_id);
	public float getHighestBid(int auction_id);
	public List<Auction> getAuctionsByCategory(int start, int end, String Category);
	
}
