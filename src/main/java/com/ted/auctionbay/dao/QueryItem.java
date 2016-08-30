package com.ted.auctionbay.dao;

import java.util.Date;
import java.util.List;

import com.ted.auctionbay.entities.items.Category;
import com.ted.auctionbay.entities.items.Item;

public interface QueryItem {
	
	public List<Item> getItems();
	
	public List<Double> getCoordinates(int ItemID);

	public List<String> getCategories(int ItemID);
	public List<Integer> getCategories_ID(int ItemID);
	
	public int getNumberofItems();
	
	public Item getDetails(int ItemID);
	
	public String getLocation(int ItemID);
	
	public int maxItemID();
	
	public int deleteItem(int itemID);
	
	public List<Integer> getItemIDs();
	
	public int updateItem(int itemID, String name, String description, String location, Double latitude, Double longitude);
	
	public int addCategory(int categoryID, int itemID);
	
	public int removeCategory(int categoryID, int itemID);

}
