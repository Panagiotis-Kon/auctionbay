package com.ted.auctionbay.dao;

import java.util.List;

import com.ted.auctionbay.entities.items.Category;
import com.ted.auctionbay.entities.items.Item;

public interface QueryItem {
	
	public List<Item> getItems();
	
	public List<Double> getCoordinates(int ItemID);

	public List<String> getCategories(int ItemID);
	
	public int getNumberofItems();
	
	public Item getDetails(int ItemID);
	
	public String getLocation(int ItemID);
	
	public int maxItemID();
	
	public void deleteItem(int itemID);
	
	public List<Integer> getItemIDs();

}
