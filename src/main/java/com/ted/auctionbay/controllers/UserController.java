package com.ted.auctionbay.controllers;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.format.DateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.ted.auctionbay.dao.QueryCategory;
import com.ted.auctionbay.entities.auctions.Auction;
import com.ted.auctionbay.entities.items.Category;
import com.ted.auctionbay.entities.items.Item;
import com.ted.auctionbay.entities.users.Pendinguser;
import com.ted.auctionbay.services.AuctionServices;
import com.ted.auctionbay.services.ConversationServices;
import com.ted.auctionbay.services.ItemServices;
import com.ted.auctionbay.services.UserServices;
import com.ted.auctionbay.timeutils.TimeUtilities;
import com.ted.auctionbay.recommendations.RecommendationService;
import com.ted.auctionbay.timeutils.TimeUtilities;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserServices userServices;
	
	@Autowired
	AuctionServices auctionServices;
	
	@Autowired
	ConversationServices conversationServices;
	
	@Autowired
	ItemServices itemServices;
	
	@Autowired
	QueryCategory queryCategory;
	
	
	@Autowired
	private RecommendationService recommendationServices;
	
	private static int user_auctions_num;
	
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	
	@RequestMapping(value = "/{username}",method = RequestMethod.GET)
	public String access_index(@PathVariable String username){
		return "/pages/index.html";
	}
	
	@RequestMapping(value = {"/{username}/auctions"})
	public static String auctionsRedirection() {
		
		return "/pages/auctions.html";
	}
	
	@RequestMapping(value = {"/{username}/auctions/item/{item_id}"})
	public static String itemRedirection() {
		
		return "/pages/item.html";
	}
	
	@RequestMapping(value = {"/{username}/conversation"})
	public static String mailRedirection() {
		
		return "/pages/user/conversation.html";
	}
	
	@RequestMapping(value = {"/{username}/manage-auctions"})
	public static String manageAuctionsRedirection() {
		
		return "/pages/user/manage_auctions.html";
	}
	
	@RequestMapping(value = {"/{username}/contact"})
	public static String contactRedirection() {
		
		return "/pages/blank.html";
	}
	
	@RequestMapping(value = {"/{username}/profile"})
	public static String profileRedirection() {
		
		return "/pages/blank.html";
	}
	
	@RequestMapping(value = {"/{username}/manage-auctions/edit-module"})
	public static String editAuctionModule() {
		
		return "/pages/modules/auctionEditModule.html";
	}
	
	@RequestMapping(value = {"/{username}/rates"})
	public static String ratesRedirection() {
		
		return "/pages/user/rates.html";
	}
	
	
	@RequestMapping( value = "",params = {"status"} ,method = RequestMethod.GET)
	public String pending() {
		return "/pages/user/pending.html";
	}
	
	
	@RequestMapping(value = "/{username}/auctions/item/{item_id}/submit-bid",method = RequestMethod.POST)
	@ResponseBody
	public String submitBid(@RequestParam("username") String username, 
			@RequestParam("itemID") String ItemID, @RequestParam("bid_amount") String bid_amount){
		int itemID = Integer.parseInt(ItemID);
		float bidAmount = Float.parseFloat(bid_amount);
		System.out.println("Making the bid with: " + itemID + " --- " + bidAmount);
		if(auctionServices.submitBid(username, itemID, bidAmount) == 0){
			return new Gson().toJson("Your offer ( " + bidAmount + " $ ) has been submitted");
		}
		
		return new Gson().toJson("cannot submit the bid");
	}
	
	@RequestMapping(value = "/{username}/auctions/item/{item_id}/buy",method = RequestMethod.POST)
	@ResponseBody
	public String buyItem(@RequestParam("username") String username, 
			@RequestParam("itemID") String ItemID){
		int itemID = Integer.parseInt(ItemID);
		System.out.println("Buying item");
		if(auctionServices.buyItem(username, itemID) == 0){
			if(conversationServices.notifyUser(itemID, username) == 0){
				return new Gson().toJson("success");
			}
			
		}
		
		
		
		return new Gson().toJson("problem");
	}
	
	
	@RequestMapping(value = "/{username}/manage-auctions/count-user-auctions", method = RequestMethod.GET)
	@ResponseBody
	public String countUserAuctions(@RequestParam String username, @RequestParam("type") String type){
	
			JSONObject answer = new JSONObject();
			try {
				user_auctions_num = userServices.count_user_auctions(username,type);
				answer.put("user_auctions_num",user_auctions_num);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return answer.toString();
	
	}
	
	@RequestMapping(value = "/{username}/manage-auctions/create-auction", method = RequestMethod.POST)
	@ResponseBody
	public String createAuction(@RequestParam String username, @RequestParam String input){
		System.out.println("username: " + username);
		System.out.println("Auction data: " + input);
		int res = 1;
		try {
			JSONObject auction_params = new JSONObject(input);
			res = auctionServices.createAuction(username, auction_params);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if(res == 0) {
			System.out.println("yiiiiihaaaaa");
			return new Gson().toJson("The auction was created");
		}
		return new Gson().toJson("Sorry an error occurred");
	}
	
	
	@RequestMapping(value = "/{username}/manage-auctions/update-auction", method = RequestMethod.POST)
	@ResponseBody
	public String updateAuction(@RequestParam String username, @RequestParam String input){
		
		System.out.println("Updating...");
		System.out.println("username: " + username);
		System.out.println("Auction data: " + input);
		try {
			JSONObject auction_params = new JSONObject(input);
			int auctionID=0;
			auctionID = Integer.parseInt(auction_params.getString("auctionID"));
			auctionServices.updateAuction(auctionID, auction_params);
			
		
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return new Gson().toJson("problem");
	}
	
	
	@RequestMapping(value = "/{username}/manage-auctions/delete-auction", method = RequestMethod.POST)
	@ResponseBody
	public String deleteAuction(@RequestParam String username, @RequestParam String auctionID,
			@RequestParam String itemID){
		int auction_id = Integer.parseInt(auctionID);
		int item_id = Integer.parseInt(itemID);
		
		if (auctionServices.deleteAuction(username, auction_id, item_id) == 0)
			return new Gson().toJson("OK");
		
		return new Gson().toJson("Problem in delete");
	}
	
	@RequestMapping(value = {"/{username}/manage-auctions/auction-details"})
	@ResponseBody
	public String auctionDetails(@RequestParam String auction_id, @RequestParam String item_id){
		
		int auctionID = Integer.parseInt(auction_id);
		int itemID = Integer.parseInt(item_id);
		Item item = itemServices.getDetails(itemID);
		List<String> categories = itemServices.getCategories(itemID);
		Auction auction = auctionServices.getDetails(itemID);
		
		JSONObject jsonDetails = new JSONObject();
		try {
			jsonDetails.put("name", item.getName());
			jsonDetails.put("itemID", item.getItemID());
			jsonDetails.put("auctionID", auctionID);
			jsonDetails.put("description",item.getDescription());
			jsonDetails.put("location",item.getLocation());
			jsonDetails.put("lat", item.getLatitude());
			jsonDetails.put("lon",item.getLongitute());
			String allcategories = null;
			// make a string from all categories of the item
			for (int i=0;i<categories.size();i++){
				if (i==0){
					allcategories = String.valueOf(categories.get(i));
				}
				else if (i==categories.size()-1){
					if (i == 1){
						allcategories = allcategories + ", ";
					}
					allcategories = allcategories + String.valueOf(categories.get(i));
				}
				else {
					if (i == 1){
						allcategories = allcategories + ", ";
					}
					allcategories = allcategories + String.valueOf(categories.get(i)) + ", ";
				}
				
			}
			jsonDetails.put("category", allcategories);
			jsonDetails.put("seller", auction.getRegistereduser().getUsername());
			jsonDetails.put("buyprice", auction.getBuyPrice());
			jsonDetails.put("firstbid", auction.getFirstBid());
			jsonDetails.put("endTime", auction.getEndTime().toString());
			
		}catch(JSONException e){
			System.out.println("....... get details json error .....");
		}
		if(jsonDetails.length() != 0) {
			System.out.println(".... Returning the Details ....");
			System.out.println(jsonDetails.toString());
			return jsonDetails.toString();
		}
		return new Gson().toJson("Cannot edit auction");
	}
	
	@RequestMapping(value = {"/{username}/manage-auctions/get-user-auctions"})
	@ResponseBody
	public String getUserAuctions(HttpServletRequest request, 
			  HttpServletResponse response, @RequestParam String username, @RequestParam String type) {
		
		System.out.println("getUserAuctions ...");
		int start = Integer.parseInt(request.getParameter("start"));
		int pageSize = Integer.parseInt(request.getParameter("length"));
		int pageNumber;

		if(start == 0)
			pageNumber = 0;
		else
			pageNumber = start%pageSize;


		JSONArray answer = new JSONArray();
		JSONObject data = new JSONObject();
		List<Auction> users_auctions = userServices.get_user_auctions(username,start,pageSize,type);
		
		for(Auction a : users_auctions){
			JSONObject auctions = new JSONObject();
			boolean can_edit = auctionServices.auctionCanBeEdited(a.getAuctionID());
			if(!can_edit){
				continue;
			}
			try {
				auctions.put("AuctionID",a.getAuctionID());
				auctions.put("ItemID",a.getItem().getItemID());
				auctions.put("Title",a.getTitle());
				auctions.put("Seller",a.getRegistereduser().getUsername());
				auctions.put("BuyPrice",a.getBuyPrice());
				auctions.put("StartTime",a.getStartTime());
				auctions.put("EndTime",a.getEndTime());

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
			answer.put(auctions);
		}
		try {
			data.put("draw",pageNumber);
			data.put("iTotalRecords",user_auctions_num);
			data.put("iTotalDisplayRecords", user_auctions_num);
			data.put("data", answer);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		System.out.println("----------- get user auctions end ----------");
		System.out.println("");
		System.out.println(data.toString());
		System.out.println("");
		return data.toString();
	}
	
	
	@RequestMapping(value = {"/{username}/manage-auctions/view-user-auctions"})
	@ResponseBody
	public String viewUserAuctions(HttpServletRequest request, 
			  HttpServletResponse response, @RequestParam String username, @RequestParam String type) {
		
		System.out.println("viewUserAuctions ...");
		int start = Integer.parseInt(request.getParameter("start"));
		int pageSize = Integer.parseInt(request.getParameter("length"));
		int pageNumber;

		if(start == 0)
			pageNumber = 0;
		else
			pageNumber = start%pageSize;


		JSONArray reply = new JSONArray();
		JSONObject data = new JSONObject();
		List<Auction> users_auctions = userServices.get_user_auctions(username,start,pageSize,type);
		
		for(Auction auction : users_auctions){
			JSONObject aObj = new JSONObject();
			JSONArray bidsHistory = auctionServices.getBidHistory(auction.getAuctionID());
			int numOfBids = auctionServices.getNumOfBids(auction.getAuctionID());
			float highestBid = auctionServices.getHighestBid(auction.getAuctionID());
			
			try {
				aObj.put("AuctionID", auction.getAuctionID());
				aObj.put("ItemID", auction.getItem().getItemID());
				aObj.put("Title", auction.getTitle());
				aObj.put("Seller", auction.getRegistereduser().getUsername());
				aObj.put("BuyPrice", auction.getBuyPrice());
				aObj.put("FirstBid", auction.getFirstBid());
				aObj.put("numOfBids", numOfBids);
				aObj.put("BidsHistory", bidsHistory);
				aObj.put("HighestBid", highestBid);
				aObj.put("StartTime", auction.getStartTime());
				aObj.put("EndTime", auction.getEndTime());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			reply.put(aObj);
		}
		
		
	
		try {
			data.put("draw",pageNumber);
			data.put("iTotalRecords",user_auctions_num);
			data.put("iTotalDisplayRecords", user_auctions_num);
			data.put("data", reply);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		System.out.println("----------- view user's auctions end ----------");
		System.out.println("");
		System.out.println(data.toString());
		System.out.println("");
		System.out.println("**********************************************");
		return data.toString();
	}
	
	
	@RequestMapping(value = "/{username}/manage-auctions/get-user-bids",method = RequestMethod.GET)
	@ResponseBody
	public String getUserBids(HttpServletRequest request, 
			  HttpServletResponse response,@RequestParam String username) {
		
		System.out.print("User bids");
		int start = Integer.parseInt(request.getParameter("start"));
		int pageSize = Integer.parseInt(request.getParameter("length"));
		int pageNumber;

		int user_bids_num = userServices.getUserBidsNum(username);
			
		if(start == 0)
			pageNumber = 0;
		else
			pageNumber = start%pageSize;

		JSONObject data = new JSONObject();
		JSONArray reply = new JSONArray();
		List<Object[]> bids = userServices.getUserBids(username, start, pageSize);
		for(Object[] obj : bids){
			
			JSONObject jObj = new JSONObject();
			try {
				jObj.put("AuctionID", obj[0]);
				jObj.put("ItemID",obj[1]);
				jObj.put("Title",obj[2]);
				jObj.put("Seller",obj[3]);
				jObj.put("Deadline",obj[4]);
				jObj.put("BuyPrice",obj[5]);
				jObj.put("HighestBid",obj[6]);
				jObj.put("myBid",obj[7]);
				jObj.put("myBidTime",obj[8]);
				reply.put(jObj);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		try {
			data.put("draw",pageNumber);
			data.put("iTotalRecords",user_bids_num);
			data.put("iTotalDisplayRecords", user_bids_num);
			data.put("data", reply);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		System.out.println("----------- get user bids end ----------");
		System.out.println("");
		System.out.println(data.toString());
		System.out.println("");
		System.out.println("**********************************************");
		return data.toString();
	
	}
	
	
	@RequestMapping(value = {"/{username}/rates/submit-rates"})
	@ResponseBody
	public String submitRatings(@RequestParam("ratings") String ratings){
		
		try {
			JSONArray data = new JSONArray(ratings);
			userServices.submitRating(data);
			return new Gson().toJson("Your ratings have been submitted");
		} catch(JSONException e){
			System.out.println("Parsing json problem on user controller - { submit-rates }");
			e.printStackTrace();
		}
	
		return new Gson().toJson("A problem on submitting ratings");
	}
	
	@RequestMapping(value = "/{username}/recommendations",method = RequestMethod.GET)
	@ResponseBody
	public String recommendations(@RequestParam("username") String username) {
		System.out.println("Recommendations...");
		Set<Integer> auctionIDs = recommendationServices.getRecommendationForUser(username);
		
		if(auctionIDs == null){
			System.out.println("auctionIDs are null");
			return new Gson().toJson("problem");
		}
		
		
		JSONArray a = new JSONArray();
		for(int auctionID:auctionIDs){
			JSONObject o = new JSONObject();
			Auction auction = auctionServices.getAuctionByID(auctionID);
			System.out.println("Auction: " + auction.getTitle());
			try {
				o.put("itemID", auction.getItem().getItemID());
				o.put("name", auction.getItem().getName());
				o.put("firstBid", auction.getFirstBid());
				o.put("remainingTime", TimeUtilities.timeDiff(auction.getStartTime(), auction.getEndTime()));
				a.put(o);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			//System.out.print("Auction: "+auction.getItem().getName()+" Item: "+auction.getItem().getItemID());
		}
		return a.toString();
		//return new Gson().toJson("OK REC");
	}
	
	
	
}
