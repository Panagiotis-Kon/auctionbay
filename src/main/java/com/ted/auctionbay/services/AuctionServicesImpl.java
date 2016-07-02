package com.ted.auctionbay.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ted.auctionbay.dao.QueryCategory;
import com.ted.auctionbay.entities.auctions.Auction;
import com.ted.auctionbay.entities.items.Category;

@Service("auctionServices")
public class AuctionServicesImpl implements AuctionServices{

	@Autowired
	QueryCategory queryCategory;
	
	
	@Override
	public List<Auction> getAuctions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int numOfAuctions() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Auction getDetails() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNumOfBids(int auction_id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getHighestBid(int auction_id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Auction> getAuctionByCategory(int start, int end, String Category) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Category> getAllCategories() {
		
		List<Category> categoryList = queryCategory.getAllCategories(); 
		
		
		return queryCategory.getAllCategories();
	}

	 
}
