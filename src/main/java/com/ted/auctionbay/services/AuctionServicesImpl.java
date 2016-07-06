package com.ted.auctionbay.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ted.auctionbay.dao.QueryAuction;
import com.ted.auctionbay.dao.QueryCategory;
import com.ted.auctionbay.entities.auctions.Auction;
import com.ted.auctionbay.entities.items.Category;

@Service("auctionServices")
public class AuctionServicesImpl implements AuctionServices{

	@Autowired
	QueryCategory queryCategory;
	
	@Autowired
	QueryAuction queryAuction;
	
	@Override
	public List<Auction> getAuctions(int startpage,int endpage) {
		
		return queryAuction.getAuctions(startpage, endpage);
	}

	@Override
	public int numOfAuctions() {
		
		return queryAuction.numOfAuctions();
	}

	@Override
	public Auction getDetails() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNumOfBids(int auction_id) {
		
		return queryAuction.getNumOfBids(auction_id);
	}

	@Override
	public float getHighestBid(int auction_id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Auction> getAuctionsByCategory(int start, int end, String Category) {
		
		return queryAuction.getAuctionsByCategory(start,end,Category);
	}

	@Override
	public List<Object[]> getAllCategories() { 
		
		return queryCategory.getAllCategories();
	}

	 
}
