package com.ted.auctionbay.services;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.w3c.dom.Document;

import com.ted.auctionbay.entities.items.Item;

public interface ItemServices {

	public List<Item> getAllItems();
	
	public Item getDetails(int ItemID);
	
	public int getNumberofItems();
	
	public List<String> getCategories(int ItemID);
	
	public List<Double> getCoordinates(int ItemID);
	
	public String getLocation(int ItemID);
	
	public Document XMLExporter(String ItemID);
	
	public void exportAllToXML() throws IOException;
	
	public void exportToXML(String ItemID) throws IOException;
	
	public void initializeRatingData() throws IOException;
	
	public int addCategory(int categoryID, int itemID);
	
}
