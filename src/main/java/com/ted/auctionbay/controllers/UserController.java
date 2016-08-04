package com.ted.auctionbay.controllers;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.ted.auctionbay.entities.auctions.Auction;
import com.ted.auctionbay.entities.users.Pendinguser;
import com.ted.auctionbay.services.AuctionServices;
import com.ted.auctionbay.services.UserServices;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserServices userServices;
	
	@Autowired
	AuctionServices auctionServices;
	
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
	
	@RequestMapping(value = {"/{username}/manage-auctions"})
	public static String manageAuctionsRedirection() {
		
		return "/pages/user/manage_auctions.html";
	}
	
	@RequestMapping(value = {"/{username}/manage-auctions/edit-module"})
	public static String editAuctionModule() {
		
		return "/pages/modules/auctionEditModule.html";
	}
	/*
	@RequestMapping(value = "",method = RequestMethod.GET)
	public String access_index(@PathVariable String username){
		return "/pages/user/index.html";
	}*/
	
	/*@RequestMapping( value = "",params = {"username","status"} ,method = RequestMethod.GET)
	public String pending(@RequestParam("username") String username) {
		return "/pages/user/pending.html";
	}*/
	
	@RequestMapping( value = "",params = {"status"} ,method = RequestMethod.GET)
	public String pending() {
		return "/pages/user/pending.html";
	}
	
	
	@RequestMapping(value = "/{username}/manage-auctions/count-user-auctions", method = RequestMethod.GET)
	@ResponseBody
	public String countUserAuctions(@RequestParam String username){
	
			JSONObject answer = new JSONObject();
			try {
				user_auctions_num = userServices.count_user_auctions(username);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(res == 0) {
			return "The auction was created";
		}
		return "Some problem occurred";
	}
	
	@RequestMapping(value = {"/{username}/manage-auctions/get-user-auctions"})
	@ResponseBody
	public String getUserAuctions(HttpServletRequest request, 
			  HttpServletResponse response, @RequestParam String username) {
		
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
		List<Auction> users_auctions = userServices.get_user_auctions(username);
		
		for(Auction a : users_auctions){
			JSONArray auctions = new JSONArray();
			auctions.put(a.getAuctionID());
			auctions.put(a.getItemID());
			auctions.put(a.getTitle());
			auctions.put(a.getSeller());
			try {
				auctions.put(a.getBuyPrice());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			auctions.put(a.getStartTime());
			auctions.put(a.getEndTime());
			try {
				auctions.put(a.getFirstBid());
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
			data.put("aaData", answer);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		System.out.println("----------- get user auctions end ----------");
		System.out.println("");
		System.out.println(data.toString());
		System.out.println("");
		return data.toString();
	}
	
}
