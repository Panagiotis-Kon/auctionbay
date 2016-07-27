package com.ted.auctionbay.dao;

import java.util.List;

import com.ted.auctionbay.entities.auctions.Auction;

public interface QueryAuction {
	public int numOfAuctions();
	public List<Auction> getAuctions(int startpage, int endpage);
	public List<Auction> getAuctionsByCategory(int startpage, int endpage, String category);
	public int getNumOfBids(int auction_id);
	public Auction getDetails(int AuctionID);
}
