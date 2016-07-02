package com.ted.auctionbay.controllers;

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

import com.ted.auctionbay.entities.items.Category;
import com.ted.auctionbay.services.AuctionServices;

@Controller
@RequestMapping("*/auctions")
public class AuctionsController {

	@Autowired
	AuctionServices auctionServices;
	
	
	
	@RequestMapping(value = "",method = RequestMethod.GET)
	@ResponseBody
	public String getAuctions(@RequestParam("startPos") String startPos,
			@RequestParam("pageSize") String pageSize){
		return "/pages/index.html";
	}
	
	@RequestMapping(value = "/numberOfAuctions", method = RequestMethod.GET)
	@ResponseBody
	public String getNumberOfAuctions(){
		JSONObject answer = new JSONObject();
		int num;
		/*try {
			num = AuctionQueries.AuctionsNumber();
			answer.put("products",auctionNumber);
			answer.put("auctions",auctionNumber);
		} catch (JSONException e) {
			e.printStackTrace();
		}*/
		return answer.toString();
	}
	
	@RequestMapping(value = "/categories",method = RequestMethod.GET)
	@ResponseBody
	public String getCategories(){
		
		JSONObject data = new JSONObject();
		JSONArray jarray = new JSONArray();
		List<Category> categoryList = auctionServices.getAllCategories();
		
		for(Category c : categoryList){
			jarray.put(c.getCategoryID());
			jarray.put(c.getName());
			System.out.println("ID: " + c.getCategoryID() + " Name: " + c.getName());
		}
		/*try {
			data.put("categories", jarray);
		} catch (JSONException e) {
			e.printStackTrace();
		}*/
		System.out.println("getCategories ends");
		return "ok";
		//return data.toString();
	}
	
}
