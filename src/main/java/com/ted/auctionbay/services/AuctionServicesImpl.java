package com.ted.auctionbay.services;

import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.eclipse.persistence.jpa.jpql.parser.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ted.auctionbay.dao.QueryAuction;
import com.ted.auctionbay.dao.QueryCategory;
import com.ted.auctionbay.dao.QueryItem;
import com.ted.auctionbay.dao.QueryUser;
import com.ted.auctionbay.entities.auctions.Auction;
import com.ted.auctionbay.entities.items.Category;
import com.ted.auctionbay.entities.items.Item;
import com.ted.auctionbay.entities.users.User;

@Service("auctionServices")
public class AuctionServicesImpl implements AuctionServices{

	@Autowired
	QueryCategory queryCategory;
	
	@Autowired
	QueryAuction queryAuction;
	
	@Autowired
	QueryItem queryItem;
	
	@Autowired
	QueryUser queryUser;
	
	private static int auctionID;
	private static int itemID;
	private static int categoryID;
	
	
	@Override
	public List<Auction> getAuctions(int startpage,int endpage) {
		
		return queryAuction.getAuctions(startpage, endpage);
	}

	@Override
	public int numOfAuctions() {
		return queryAuction.numOfAuctions();
	}

	@Override
	public Auction getDetails(int AuctionID) {
		return queryAuction.getDetails(AuctionID);
	}

	@Override
	public int getNumOfBids(int auction_id) {
		return queryAuction.getNumOfBids(auction_id);
	}

	@Override
	public float getHighestBid(int auction_id) {
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

	@Override
	public int createAuction(String username, JSONObject auction_params) {
		
		auctionID = queryAuction.maxAuctionID();
		itemID = queryItem.maxItemID();
		categoryID = queryCategory.maxCategoryID();
		
		User user = queryUser.getUser(username);
		if(user == null) {
			return -1;
		}
		Item i = new Item();
		i.setItemID(itemID);
		try {
			i.setName(auction_params.getString("auction_name"));
			i.setDescription(auction_params.getString("auction_desc"));
			float lat = Float.parseFloat(auction_params.get("lat").toString());
			i.setLatitude(lat);
			float lon = Float.parseFloat(auction_params.get("lon").toString());
			i.setLongitute(lon);
			i.setLocation(auction_params.getString("auction_country"));
			
			
		} catch (JSONException e) {
			System.out.println("Could not get data from json object, 101");
			e.printStackTrace();
		}
		
		
		List<Category> cat_list = queryCategory.fetchCategories();
		HashMap<String,Category> cat_map = new HashMap<String,Category>();
		for(Category c:cat_list){
			cat_map.put(c.getName(), c);
		}
		
		
		try {
			JSONArray categories_arr = auction_params.getJSONArray("auction_category");
			
			for(int j=0; j<categories_arr.length(); j++) {
				Category category = null;
				String cat_name = categories_arr.getString(j);
				if(cat_map.containsKey(cat_name)) {
					category = cat_map.get(cat_name);
				} else {
					category = new Category();
					category.setCategoryID(categoryID);
					category.setName(cat_name);
				}
				i.insertCategory(category);
				categoryID++;
			}
		} catch (JSONException e) {
			System.out.println("Could not get categories JSONArray");
			e.printStackTrace();
		}
		
		Auction auction = new Auction();
		auction.setAuctionID(auctionID);
		try {
			auction.setTitle(auction_params.getString("auctiona_name"));
			float buyPrice = Float.parseFloat(auction_params.get("buyPrice").toString());
			float firstBid = Float.parseFloat(auction_params.get("first_bid").toString());
			auction.setBuyPrice(buyPrice);
			auction.setFirstBid(firstBid);
			auction.setSeller(username);
			auction.setItem(i);
			
			// now we need to adjust correctly the start and end time
			Calendar calendar = Calendar.getInstance();
	        Date start =  calendar.getTime();
	        auction.setStartTime(start);
	        
	        String deadline = auction_params.getString("deadline");
	        org.joda.time.format.DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
	        org.joda.time.DateTime dt = formatter.parseDateTime(deadline);
	        Date endDate = dt.toDate();
	        auction.setEndTime(endDate);
	        
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		queryAuction.submitAuction(auction);
		
		
		
		return 0;
	}

	 
}
