package com.ted.auctionbay.services;

import java.util.List;

import com.ted.auctionbay.entities.items.Category;
import com.ted.auctionbay.entities.items.Item;

public interface ItemServices {

	public List<Item> getAllItems();
	
	public Item getDetails(int ItemID);
	
	public int getNumberofItems();
	
	public List<Category> getCategories(int ItemID);
	
	public List<Double> getCoordinates(int ItemID);
	
	public String getLocation(int ItemID);
	
}
