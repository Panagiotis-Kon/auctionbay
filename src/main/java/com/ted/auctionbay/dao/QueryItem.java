package com.ted.auctionbay.dao;

import java.util.List;

import com.ted.auctionbay.entities.items.Category;

public interface QueryItem {
	
	public List<Double> getCoordinates(int ItemID);

	List<Category> getCategory(int ItemID);

}
