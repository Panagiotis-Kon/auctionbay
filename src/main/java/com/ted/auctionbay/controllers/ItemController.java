package com.ted.auctionbay.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ted.auctionbay.entities.auctions.Auction;
import com.ted.auctionbay.entities.items.Category;
import com.ted.auctionbay.entities.items.Item;
import com.ted.auctionbay.services.ItemServices;
import com.ted.auctionbay.services.AuctionServices;
import com.ted.auctionbay.timeutils.TimeUtilities;

@Controller
@RequestMapping(value={"/auctions/item/{item_id}", "/user/{username}/auctions/item/{item_id}"})
public class ItemController {

	@Autowired
	ItemServices itemServices;
	
	@Autowired
	AuctionServices auctionServices;
	
	
	@RequestMapping(value = "/details-module",method = RequestMethod.GET)
	public String getAuctionsModule(){
		System.out.println("getting details module");
		return "/pages/modules/itemDetailsModule.html";
	}
	
	
	//Return details of item with given ID
	@RequestMapping(value = "/details",method = RequestMethod.GET)
	@ResponseBody
	public String getItemDetails(@RequestParam("itemID") String ItemID){
		System.out.println("...... Get item details Controller ......");
		int itemID = Integer.parseInt(ItemID);
		Item item = itemServices.getDetails(itemID);
		List<String> categories = itemServices.getCategories(itemID);
		Auction auction = auctionServices.getDetails(itemID);
		
		JSONObject jitem = new JSONObject();
		
		try {
			jitem.put("name", item.getName());
			jitem.put("id", item.getItemID());
			jitem.put("description",item.getDescription());
			jitem.put("location",item.getLocation());
			jitem.put("lat", item.getLatitude());
			jitem.put("lon",item.getLongitute());
			String allcategories = null;
			// make a string from all categories of the item
			for (int i=0;i<categories.size();i++){
				if (i==0){
					allcategories = String.valueOf(categories.get(i)) + ", ";
				}
				else if (i==categories.size()-1){
					allcategories = allcategories + String.valueOf(categories.get(i));
				}
				else {
					allcategories = allcategories + String.valueOf(categories.get(i)) + ", ";
				}
				
			}
			jitem.put("category", allcategories);
			jitem.put("seller", auction.getSeller());
			jitem.put("byuprice", auction.getBuyPrice());
			jitem.put("firstbid", auction.getFirstBid());
		}catch(JSONException e){
			System.out.println("....... get item json error .....");
		}
		return jitem.toString();
	}
}
