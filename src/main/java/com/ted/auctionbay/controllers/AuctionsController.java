package com.ted.auctionbay.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ted.auctionbay.entities.auctions.Auction;
import com.ted.auctionbay.entities.items.Category;
import com.ted.auctionbay.services.AuctionServices;
import com.ted.auctionbay.timeutils.TimeUtilities;

@Controller
@RequestMapping(value={"/auctions", "/user/{username}/auctions"})
public class AuctionsController {

	@Autowired
	AuctionServices auctionServices;
	
	
	@RequestMapping(value = "/template-module",method = RequestMethod.GET)
	public String getAuctionsModule(){
		System.out.println("get auctions module");
		return "/pages/modules/AuctionsListModule.html";
	}
	
	
	@RequestMapping(value = "/view-auctions",method = RequestMethod.GET)
	@ResponseBody
	public String getAuctions(@RequestParam("start") String start,
			@RequestParam("size") String size, @RequestParam("type") String type){
		System.out.println("...... Get auctions Controller ......");
		int startpage = Integer.parseInt(start);
		int endpage = Integer.parseInt(size);
		
		List<Auction> auctions_list = auctionServices.getAuctions(startpage, endpage, type);
		
		JSONArray answer = new JSONArray();
		for(Auction a: auctions_list){
			JSONObject j = new JSONObject();
			
			String timeDiff = TimeUtilities.timeDiff(new Date(),a.getEndTime());
			if(timeDiff != null ) {
				try {
					j.put("name", a.getTitle());
					j.put("id", a.getItem().getItemID());
					j.put("seller",a.getRegistereduser().getUsername());
					//j.put("category", a.getItemID)
					j.put("expires",timeDiff);
					j.put("firstBid", a.getFirstBid());
					j.put("numberOfBids",auctionServices.getNumOfBids(a.getAuctionID()));
				}catch(JSONException e){
					System.out.println("....... get auctions json error .....");
				}
				
				answer.put(j);
			}
		}
		System.out.println("Auctions: " + answer.toString());
		return answer.toString();
	}
	
	
	@RequestMapping(value = "/view-auctions-byCategory",method = RequestMethod.GET)
	@ResponseBody
	public String getAuctionsByCategory(@RequestParam("start") String start,
			@RequestParam("size") String size, @RequestParam("category") String category,
			@RequestParam("type") String type){
		System.out.println("...... Get auctions By category Controller ......");
		int startpage = Integer.parseInt(start);
		int endpage = Integer.parseInt(size);
		System.out.println("category: " + category);

		
		List<Auction> auctions_list = auctionServices.getAuctionsByCategory(startpage, endpage, category, type);
		if(auctions_list.isEmpty()){
			System.out.println("empty list");
		}
		JSONArray answer = new JSONArray();
		for(Auction a: auctions_list){
			JSONObject j = new JSONObject();
			
			String timeDiff = TimeUtilities.timeDiff(new Date(),a.getEndTime());
			if(timeDiff != null ) {
				try {
					j.put("name", a.getTitle());
					j.put("id", a.getItem().getItemID());
					j.put("seller",a.getRegistereduser().getUsername());
				
					j.put("expires",timeDiff);
					j.put("firstBid", a.getFirstBid());
					j.put("numberOfBids",auctionServices.getNumOfBids(a.getAuctionID()));
				}catch(JSONException e){
					System.out.println("....... get auctions json error .....");
				}
				
				answer.put(j);
			}
		}
		System.out.println("Auctions By Category: " + answer.toString());
		return answer.toString();
	}
	
	
	
	@RequestMapping(value = "/numberOfAuctions", method = RequestMethod.GET)
	@ResponseBody
	public String getNumberOfAuctions(@RequestParam("type") String type){
		System.out.println("...... Number of Auctions Controller ......");
		JSONObject numObject = new JSONObject();
		int num = auctionServices.numOfAuctions(type);
		try {
			numObject.put("auctionsNum", num);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("..... getNumberOfAuctions .....");
			e.printStackTrace();
		}
		System.out.println("num: " + numObject.toString());
		return numObject.toString();
	}
	
	@RequestMapping(value = "/user-expired-auctions", method = RequestMethod.GET)
	@ResponseBody
	public String UserExpiredAuctions(@RequestParam("username") String username){
		System.out.println("...... Expired Auctions ......");
		List<Object[]> expired = auctionServices.BidderExpiredAuctions(username);
		JSONArray expArray = new JSONArray();
		
		for(Object[] obj : expired){
			JSONObject jobj = new JSONObject();
			try {
				jobj.put("auctionID", obj[0]);
				jobj.put("seller",obj[1]);
				jobj.put("item_title", obj[2]);
				jobj.put("bidder", obj[3]);
			} catch (JSONException e) {
				System.out.println("Problem on json return --- user-expired-auctions");
				e.printStackTrace();
			}
			expArray.put(jobj);
		}
		System.out.println("UserExpiredAuctions: " + expArray.toString());
		return expArray.toString();
	}
	
	@RequestMapping(value = "/categories",method = RequestMethod.GET)
	@ResponseBody
	public String getCategories(@RequestParam("type") String type){
		
		
		JSONArray jarray = new JSONArray();
		List<Object[]> categoryList = auctionServices.getCategories(type);
		for(Object[] obj : categoryList){
			JSONObject data = new JSONObject();
			try {
				data.put("category", obj[0]);
				data.put("numOfItems",obj[1]);
				jarray.put(data);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	
		System.out.println("getCategories ends");
		return jarray.toString();
	}
	
	@RequestMapping(value = "/advanced-search",method = RequestMethod.POST)
	@ResponseBody
	public String advancedSearch(@RequestParam String start,@RequestParam String end,@RequestParam String search_data) {
		int start_pag = Integer.parseInt(start);
		int end_pag = Integer.parseInt(end);
		System.out.println("Advanced Search");
		String keywords = "",location = "", minBid="",maxBid = "";
		List<String> categories = new ArrayList<String>();
		try {
			JSONObject search_params = new JSONObject(search_data);
			keywords = search_params.getString("keywords");
			location = search_params.getString("country");
			minBid = search_params.getString("minBid");
			maxBid = search_params.getString("maxBid");
			JSONArray j = search_params.getJSONArray("categories");
			for(int i=0;i<j.length();i++){
				categories.add(j.getString(i));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Auction> auctions_list= auctionServices.advancedSearch(keywords, categories, location, minBid, maxBid, start_pag, end_pag);
		JSONArray answer = new JSONArray();
		for(Auction a: auctions_list){
			JSONObject j = new JSONObject();
			
			String timeDiff = TimeUtilities.timeDiff(new Date(),a.getEndTime());
			if(timeDiff != null ) {
				try {
					j.put("name", a.getTitle());
					j.put("id", a.getItem().getItemID());
					j.put("seller",a.getRegistereduser().getUsername());
					//j.put("category", a.getItemID)
					j.put("expires",timeDiff);
					j.put("firstBid", a.getFirstBid());
					j.put("numberOfBids",auctionServices.getNumOfBids(a.getAuctionID()));
				}catch(JSONException e){
					System.out.println("....... get auctions json error .....");
				}
				
				answer.put(j);
			}
		}
		System.out.print("Advanced Search returns:");
		System.out.print(answer.toString());
		return answer.toString();
	}
	
}
