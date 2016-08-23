package com.ted.auctionbay.services;

import java.util.List;

import javax.swing.text.Document;

import com.ted.auctionbay.entities.items.Category;
import com.ted.auctionbay.entities.items.Item;

public interface ItemServices {

	public List<Item> getAllItems();
	
	public Item getDetails(int ItemID);
	
	public int getNumberofItems();
	
	public List<String> getCategories(int ItemID);
	
	public List<Double> getCoordinates(int ItemID);
	
	public String getLocation(int ItemID);
	
	public Document XMLExporter(String ItemID);
	
	public void exportAllToXML();
	
	public void exportToXML(String ItemID);
	
	public void initializeRatingData();
	
}
