package com.ted.auctionbay.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.PathParam;

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

import com.ted.auctionbay.entities.users.Pendinguser;
import com.ted.auctionbay.entities.users.Registereduser;
import com.ted.auctionbay.services.AuctionServices;
import com.ted.auctionbay.services.ItemServices;
import com.ted.auctionbay.services.UserServices;
import com.ted.auctionbay.dao.QueryUser;

@Controller
@RequestMapping("/administrator")
public class AdminController {

	@Autowired
	ItemServices itemServices;
	
	@Autowired
	UserServices userServices;
	
	@Autowired
	AuctionServices auctionServices;
	
	static int registeredUsersNumber = 0;
	static int pendingUsersNumber=0;
	
	@RequestMapping(value = " ")
	public String index() {
		return "/pages/administrator/administrator.html";
	}
	
	@RequestMapping(value = "/count-users", method = RequestMethod.GET)
	@ResponseBody
	public String countUsers(){
	
			JSONObject answer = new JSONObject();
			try {
				registeredUsersNumber = userServices.count_registered();
				pendingUsersNumber = userServices.count_pending();
				answer.put("registered_users",registeredUsersNumber);
				answer.put("pending_users",pendingUsersNumber);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return answer.toString();
	
	}
	
	@RequestMapping(value = "/accept-user", method = RequestMethod.GET)
	@ResponseBody
	public String accept_user(@RequestParam String username, HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException{
	
		System.out.println("accepting");
		userServices.accept_user(username);
		//response.setStatus(HttpServletResponse.SC_OK);
		System.out.println("accepted");
		return "accepted";
	}
	
	@RequestMapping(value = "/pending-users",method = RequestMethod.GET)
	@ResponseBody
	public String getPendingUsers(HttpServletRequest request, 
			  HttpServletResponse response){

		System.out.println("Pending...");
		int start = Integer.parseInt(request.getParameter("start"));
		int pageSize = Integer.parseInt(request.getParameter("length"));
		int pageNumber;

		if(start == 0)
			pageNumber = 0;
		else
			pageNumber = start%pageSize;


		JSONArray answer = new JSONArray();
		JSONObject data = new JSONObject();
		List<Pendinguser> pending_users = userServices.getPendingUsers();
		
		for(Pendinguser p : pending_users){
			JSONArray puser_info = new JSONArray();
			puser_info.put(p.getUser().getUsername());
			puser_info.put(p.getUser().getFirstName());
			puser_info.put(p.getUser().getLastname());
			puser_info.put(p.getUser().getEmail());
			puser_info.put(p.getUser().getPhoneNumber());
			puser_info.put(p.getUser().getTrn());
			puser_info.put(p.getUser().getAddress().getCity());
			puser_info.put(p.getUser().getAddress().getRegion());
			puser_info.put(p.getUser().getAddress().getStreet());
			puser_info.put(p.getUser().getAddress().getZipCode());
			
			answer.put(puser_info);
		}
		try {
			data.put("draw",pageNumber);
			data.put("iTotalRecords",pendingUsersNumber);
			data.put("iTotalDisplayRecords", pendingUsersNumber);
			data.put("aaData", answer);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		System.out.println("getPendingUsers ends");
		return data.toString();

	}
	
	@RequestMapping(value = "/registered-users", method = RequestMethod.GET)
	@ResponseBody
	public String getRegisteredUsers(HttpServletRequest request, 
									  HttpServletResponse response){
		
		System.out.println("Registering...");
		int start = Integer.parseInt(request.getParameter("start"));
		int pagesize = Integer.parseInt(request.getParameter("length"));
		int pageNumber;
		
		
		if(start == 0)
			pageNumber = 0;
		else
			pageNumber = start%pagesize;
		
		List<Registereduser> regUsers = userServices.getGroupsOfUsers(start, pagesize);
		if(regUsers == null)
			return null;

		
		JSONArray answer = new JSONArray();
		JSONObject data = new JSONObject();
		for(Registereduser ru : regUsers){
			JSONArray user_data = new JSONArray();
			user_data.put(ru.getUser().getUsername());
			user_data.put(ru.getUser().getFirstName());
			user_data.put(ru.getUser().getLastname());
			user_data.put(ru.getUser().getEmail());
			user_data.put(ru.getUser().getPhoneNumber());
			user_data.put(ru.getUser().getTrn());
			user_data.put(ru.getUser().getAddress().getCity());
			user_data.put(ru.getUser().getAddress().getRegion());
			user_data.put(ru.getUser().getAddress().getStreet());
			user_data.put(ru.getUser().getAddress().getZipCode());
			user_data.put(ru.getUser().getBidderRating());
			user_data.put(ru.getUser().getSellerRating());
		
			answer.put(user_data);
		}
		System.out.println("registeredUsersNumber: " + registeredUsersNumber);
		try {
			data.put("draw",pageNumber);
			data.put("iTotalRecords",registeredUsersNumber);
			data.put("iTotalDisplayRecords", registeredUsersNumber);
			data.put("aaData", answer);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return data.toString();
	}
	
	
	@RequestMapping(value="/auctions-to-export",method = RequestMethod.GET)
	@ResponseBody
	public String auctionsToExport(HttpServletRequest request, 
			  HttpServletResponse response){
		
		int start = Integer.parseInt(request.getParameter("start"));
		int pagesize = Integer.parseInt(request.getParameter("length"));
		int pageNumber=0;
		
		int numOfAuctions = auctionServices.numOfAuctions("all");
		
		if(start == 0)
			pageNumber = 0;
		else
			pageNumber = start%pagesize;
		
		List<Object[]> auctions = auctionServices.getAuctionsForExport(start, pagesize);
		
		if(auctions == null)
			return null;
		
		JSONArray auctionsArray = new JSONArray();
		JSONObject data = new JSONObject();
		for(Object[] obj : auctions){
			JSONArray jarray = new JSONArray();
			
				jarray.put(obj[0].toString());
				jarray.put(obj[1].toString());
				jarray.put(obj[2]);
				jarray.put(obj[3]);
				
			
			auctionsArray.put(jarray);
		}
		
		
		try {
			data.put("draw",pageNumber);
			data.put("iTotalRecords",numOfAuctions);
			data.put("iTotalDisplayRecords", numOfAuctions);
			data.put("aaData", auctionsArray);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		System.out.println("---- Exporting -----");
		System.out.println(data.toString());
		return data.toString();
	}
	
	
	
	@RequestMapping(value = "/export-to-xml/{ItemID}",method = RequestMethod.GET)
	public void exportToXML(@PathVariable String ItemID,HttpServletResponse response) throws IOException {
		itemServices.exportToXML(ItemID);
		response.setStatus(HttpServletResponse.SC_OK);
	}
	
	@RequestMapping(value = "/export-all-to-xml",method = RequestMethod.GET)
	public void exportAllToXML(HttpServletResponse response) throws IOException {
		itemServices.exportAllToXML();
		response.setStatus(HttpServletResponse.SC_OK);
	}
	
}
