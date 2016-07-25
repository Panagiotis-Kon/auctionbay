package com.ted.auctionbay.controllers;

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
import com.ted.auctionbay.entities.items.Item;
import com.ted.auctionbay.services.ItemServices;
import com.ted.auctionbay.timeutils.TimeUtilities;

@Controller
@RequestMapping(value={"/auctions/item/{item_id}", "/user/{username}/auctions/item/{item_id}"})
public class ItemController {

	@Autowired
	ItemServices itemServices;
	
	//Return details of item with given ID
	@RequestMapping(value = "/details",method = RequestMethod.GET)
	@ResponseBody
	public String getAuctions(@RequestParam("start") String ItemID){
		System.out.println("...... Get auctions Controller ......");
		int itemID = Integer.parseInt(ItemID);
		
		Item product = itemServices.getDetails(itemID);
		
		JSONObject jproduct = new JSONObject();
		
		try {
			jproduct.put("name", product.getName());
			jproduct.put("id", product.getItemID());
			jproduct.put("description",product.getDescription());
			//j.put("category", product.getItemID)
			jproduct.put("location",product.getLocation());
			jproduct.put("lat", product.getLatitude());
			jproduct.put("lon",product.getLongitute());
		}catch(JSONException e){
			System.out.println("....... get item json error .....");
		}
		return jproduct.toString();
	}
}
