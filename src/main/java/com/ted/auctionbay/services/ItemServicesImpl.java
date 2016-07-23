package com.ted.auctionbay.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ted.auctionbay.dao.QueryItem;
import com.ted.auctionbay.entities.items.Category;
import com.ted.auctionbay.entities.items.Item;

public class ItemServicesImpl  implements ItemServices{
	
	@Autowired
	QueryItem queryItem;
	
	@Override
	public List<Item> getAllItems() {
		return queryItem.getItems();
	}

	@Override
	public Item getDetails(int ItemID) {
		return queryItem.getDetails(ItemID);
	}

	@Override
	public int getNumberofItems() {
		return queryItem.getNumberofItems();
	}

	@Override
	public List<Category> getCategories(int ItemID) {
		return queryItem.getCategories(ItemID);
	}

	@Override
	public List<Double> getCoordinates(int ItemID) {
		return queryItem.getCoordinates(ItemID);
	}

	@Override
	public String getLocation(int ItemID) {
		return queryItem.getLocation(ItemID);
	}

}
