package com.ted.auctionbay.controllers;

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
@RequestMapping("/auctions")
public class AuctionsController {

	@Autowired
	AuctionServices auctionServices;
	
	
	@RequestMapping(value = "/template-module",method = RequestMethod.GET)
	public String getAuctionsModule(){
		System.out.println("get auctions module");
		return "/pages/modules/ItemsListing.html";
	}
	
	
	@RequestMapping(value = "/view-auctions",method = RequestMethod.GET)
	@ResponseBody
	public String getAuctions(@RequestParam("start") String start,
			@RequestParam("size") String size){
		System.out.println("...... Get auctions Controller ......");
		int startpage = Integer.parseInt(start);
		int endpage = Integer.parseInt(size);
		
		List<Auction> auctions_list = auctionServices.getAuctions(startpage, endpage);
		
		JSONArray answer = new JSONArray();
		for(Auction a: auctions_list){
			JSONObject j = new JSONObject();
			
			String timeDiff = TimeUtilities.timeDiff(new Date(),a.getEndTime());
			if(timeDiff != null ) {
				try {
					j.put("name", a.getTitle());
					j.put("id", a.getItemID());
					j.put("seller",a.getSeller());
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
	
	@RequestMapping(value = "/numberOfAuctions", method = RequestMethod.GET)
	@ResponseBody
	public String getNumberOfAuctions(){
		System.out.println("...... Number of Auctions Controller ......");
		JSONObject numObject = new JSONObject();
		int num = auctionServices.numOfAuctions();
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
	
	@RequestMapping(value = "/categories",method = RequestMethod.GET)
	@ResponseBody
	public String getCategories(){
		
		
		JSONArray jarray = new JSONArray();
		List<Object[]> categoryList = auctionServices.getAllCategories();
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
		//return data.toString();
	}
	
}
