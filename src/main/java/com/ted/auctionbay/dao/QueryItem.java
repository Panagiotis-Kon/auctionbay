package com.ted.auctionbay.dao;

import java.util.List;

import com.ted.auctionbay.entities.items.Category;
import com.ted.auctionbay.entities.items.Item;

public interface QueryItem {
	
	List<Item> getItems();
	
	public List<Double> getCoordinates(int ItemID);

	List<Category> getCategories(int ItemID);

}
