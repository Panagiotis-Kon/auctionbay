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
import com.ted.auctionbay.entities.users.Registereduser;
import com.ted.auctionbay.entities.users.RegistereduserBidsinAuction;
import com.ted.auctionbay.entities.users.RegistereduserBidsinAuctionPK;
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
	public List<Auction> getAuctions(int startpage,int endpage, String type) {
		if(type.equals("active")){
			return queryAuction.getActiveAuctions(startpage, endpage);
		}
		return queryAuction.getAuctions(startpage, endpage);
	}

	@Override
	public int numOfAuctions(String type) {
		if(type.equals("active")){
			return queryAuction.numOfActiveAuctions();
		}
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
		return queryAuction.getHighestBid(auction_id);
	}

	@Override
	public List<Auction> getAuctionsByCategory(int start, int end, String Category, String type) {
		if(type.equals("active")){
			return queryAuction.getActiveAuctions(start, end);
		}
		return queryAuction.getAuctionsByCategory(start,end,Category);
	}

	@Override
	public List<Object[]> getCategories(String type) { 
		if(type.equals("active")){
			return queryCategory.getActiveCategories();
		}
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
					System.out.println("New category: " + cat_name);
					System.out.println("Category ID: " + categoryID);
					category = new Category();
					category.setCategoryID(categoryID);
					category.setName(cat_name);
					categoryID++;
				}
				i.insertCategory(category);
				
			}
		} catch (JSONException e) {
			System.out.println("Could not get categories JSONArray");
			e.printStackTrace();
		}
		
		Auction auction = new Auction();
		auction.setAuctionID(auctionID);
		try {
			auction.setTitle(auction_params.getString("auction_name"));
			float buyPrice = Float.parseFloat(auction_params.get("buyPrice").toString());
			float firstBid = Float.parseFloat(auction_params.get("first_bid").toString());
			auction.setBuyPrice(buyPrice);
			auction.setFirstBid(firstBid);
			//auction.setSeller(username);
			auction.setItem(i);
			
			
			Calendar calendar = Calendar.getInstance();
			Date start =  calendar.getTime();
	        auction.setStartTime(start);
	        
	        String deadline = auction_params.getString("deadline");
	        org.joda.time.format.DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
	        org.joda.time.DateTime dt = formatter.parseDateTime(deadline);
	        Date endDate = dt.toDate();
	        auction.setEndTime(endDate);
	        System.out.println("startTime: " + start + " --- EndTime: " + endDate);
	        
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		auction.setRegistereduser(user.getRegistereduser());
		if(queryAuction.submitAuction(auction) == -1){
			System.out.println("Could not register auction");
			return -2;
		}
	
		return 0;
	}

	@Override
	public int deleteAuction(String username, int auctionID, int itemID) {
		
		//queryUser.deleteBidderFromAuction(username, auctionID);
		queryAuction.deleteAuction(username,itemID,auctionID);
		Item item = queryItem.getDetails(itemID);
		List<Category> categories_list = item.getCategories();
		for(Category c:categories_list){
			c.deleteItem(item);
		}
		queryItem.deleteItem(itemID);	
		/*
		 * Something more is needed...
		 * */
		categories_list = null;
		categories_list = queryCategory.fetchCategories();
		
		for(Category c : categories_list){
			if(c.getItems().size() == 0){
				
			}
		}
		
		return 0;
	}

	@Override
	public int editAuction(String username, JSONObject params) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getAuctionIDByItem(int item_id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int submitBid(String username, int itemID, float bid_amount) {
		// Check if the user has already bidded on the product, then update it
		if(queryAuction.alreadyBidded(username, itemID)) {
			// update row
			System.out.println("Updating " + username + " Auction!! ");
			queryAuction.updateBid(username, itemID, bid_amount);
			return 0;
		} else {
			
			// else register a new bid for the user
			
			Auction auction = queryAuction.getDetails(itemID);
			Registereduser reg_user = queryUser.getUser(username).getRegistereduser();
			System.out.println("Registered user: " + reg_user.getUsername() + " bids for auction with id: "+auction.getAuctionID() +
					" and item: " + auction.getItem().getItemID()+"/" + itemID);
			
			RegistereduserBidsinAuctionPK rbaPK = new RegistereduserBidsinAuctionPK();
			rbaPK.setBidder_Username(username);
			rbaPK.setAuctionID(auction.getAuctionID());
			
			RegistereduserBidsinAuction rba = new RegistereduserBidsinAuction();
			rba.setBidPrice(bid_amount);
			rba.setId(rbaPK);
			Date current_time = new Date();
			rba.setBidTime(current_time);
			
			if(queryUser.createBidInUser(rba) == 0){
				System.out.println("Bid created");
				return 0;
			}
				
		}
		
		return -1;
	}

	@Override
	public JSONArray getBidHistory(int auctionID) {
		List<Object[]> bidsList = queryAuction.getBidHistory(auctionID);
		JSONArray bidsHistory = new JSONArray();
		for(Object[] obj : bidsList) {
			JSONObject jobj = new JSONObject();
			try {
				jobj.put("Bidder", obj[0].toString());
				jobj.put("BidPrice", obj[1].toString());
				bidsHistory.put(jobj);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return bidsHistory;
	}

	@Override
	public List<Auction> advancedSearch(String keywords,
			List<String> Categories, String Location, String minBid,
			String maxBid) {
		return queryAuction.advancedSearch(keywords, Categories, Location, minBid, maxBid);
	}

	@Override
	public int delAuction(String Username, int auctionID, int ItemID) {
		queryAuction.delAuction(Username,auctionID,ItemID);
		return 0;
	}

	

	@Override
	public List<Auction> getExpiredAuctions() {
		return queryAuction.getExpiredAuctions();
	}

	@Override
	public int buyItem(String username, int itemID) {
		Auction a = queryAuction.getDetails(itemID);
		queryAuction.deleteAuction(username, itemID, a.getAuctionID());
		if(queryUser.appendBuyerHistory(username, itemID) == 0){
			return 0;	
		}
		return 1;
	}

	@Override
	public List<Object[]> BidderExpiredAuctions(String username) {
		
		return queryAuction.BidderExpiredAuction(username);
	}

	@Override
	public List<RegistereduserBidsinAuction> getAuctionsOfAllUsers() {
		return queryAuction.getAuctionsOfAllUsers();
	}

	@Override
	public Auction getAuctionByID(int AuctionID) {
		return queryAuction.getAuctionByID(AuctionID);
	}

	@Override
	public List<Object[]> getAuctionsForExport(int startpage, int endpage) {
		return queryAuction.getAuctionsForExport(startpage, endpage);
	}

	@Override
	public boolean auctionCanBeEdited(int auctionID) {
		return queryAuction.auctionCanBeEdited(auctionID);
	}

	@Override
	public int updateAuction(int auctionID, String title, float buyprice,
			float firstbid, Date starttime, Date endtime,
			String name, String description, String location, Double latitude,
			Double longitude) {
		//Auction details
		int results = 0;
		int countofnull = 0; //count null arguments if all are null, then don't update
		if (title.equals("-1")){
			title = queryAuction.getAuctionByID(auctionID).getTitle();
			countofnull++;
		}
		if (buyprice == -1){
			buyprice = queryAuction.getAuctionByID(auctionID).getBuyPrice();
			countofnull++;
		}
		if (firstbid == -1){
			firstbid = queryAuction.getAuctionByID(auctionID).getFirstBid();
			countofnull++;
		}
		if (starttime == null){
			starttime = queryAuction.getAuctionByID(auctionID).getStartTime();
			countofnull++;
		}
		if (endtime == null){
			endtime = queryAuction.getAuctionByID(auctionID).getStartTime();
			countofnull++;
		}
		if (countofnull < 5){
			results = queryAuction.updateAuction(auctionID, title, buyprice, firstbid, starttime, endtime);
		}
		//Item of the Auction details
		countofnull=0;
		int itemID = queryAuction.getAuctionByID(auctionID).getItem().getItemID();
		if (name.equals("-1")){
			name = queryItem.getDetails(itemID).getName();
			countofnull++;
		}
		if (description.equals("-1")){
			description = queryItem.getDetails(itemID).getDescription();
			countofnull++;
		}
		if (location.equals("-1")){
			location = queryItem.getDetails(itemID).getLocation();
			countofnull++;
		}
		if (latitude.isNaN()){
			latitude = queryItem.getDetails(itemID).getLatitude();
			countofnull++;
		}
		if (longitude.isNaN()){
			longitude = queryItem.getDetails(itemID).getLongitute();
			countofnull++;
		}
        if (countofnull < 5){
			results = results + queryItem.updateItem(itemID, name, description, location, latitude, longitude);
		}
        return results;
	}
}
