package com.ted.auctionbay.controllers;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ted.auctionbay.dao.QueryItem;
import com.ted.auctionbay.dao.QueryUser;
import com.ted.auctionbay.dao.QueryCategory;
import com.ted.auctionbay.entities.items.Category;
import com.ted.auctionbay.services.UserServices;
import com.ted.auctionbay.services.UserServicesImpl;


/**
 * Handles requests for the application home page.
 */
@Controller
public class MainController {
	
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	private static String log_status = "offline";
	
	@Autowired
	UserServices userServices;
	
	@Autowired
	QueryCategory queryCategory;
	
	//@Autowired
	//QueryUser queryUser;
	
	@RequestMapping(value = {"","/index"})
	public static String indexRedirection() {
		return "pages/index.html";
	}
	
	@RequestMapping(value = {"/contact"})
	public static String contactRedirection() {
		
		return "/pages/contact.html";
	}
	
	@RequestMapping(value = {"/login-signup"})
	public static String loginRedirection() {
		
		return "/pages/login.html";
	}
	
	@RequestMapping(value = {"/auctions"})
	public static String auctionsRedirection() {
		
		return "/pages/auctions.html";
	}
	
	/*
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public void login( @RequestParam("username") String username,
            								@RequestParam("password") String password,
            								HttpServletRequest request,
            								HttpServletResponse response) {
		
		
		
		if(username.equals("admin") && password.equals("admin")){
			response.setStatus(HttpServletResponse.SC_OK); //Status code 200
			log_status = "admin";
			response.setHeader("Content-Location","administrator");
		} else {
		
			if(QueryUser.fetchPendingByUsername(username))
			{
				System.out.println("The user is pending");
				response.setHeader("Content-Location","user/?status=pending");
			} else {
				// validate the user if it exists in db 
				// if the user exists the redirect him to his index page
				// else forbid access
				if(QueryUser.user_validator(username, password)){
					System.out.println("user entered");
					response.setHeader("Content-Location","users/"+username);
				} else {
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				}
				
			}
			
		}
		
	}
	*/
	
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	
	public void login( @RequestParam("username") String username,
            								@RequestParam("password") String password,
            								HttpServletRequest request,
            								HttpServletResponse response) {
		
		
		System.out.println("username: " + username + "password: " + password);
		
		if(username.equals("admin") && password.equals("admin")){
			response.setStatus(HttpServletResponse.SC_OK); //Status code 200
			log_status = "admin";
			//result = "administrator";
			response.setHeader("Content-Location","administrator");
		} else {
			
			int status = userServices.Login(username,password);
			if(status==0)
			{
				System.out.println("The user is pending");
				response.setHeader("Content-Location","user/?status=pending");
			}
			else if(status==1){
					System.out.println("user entered");
					response.setHeader("Content-Location","user/"+username);
			}
			else {
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			}
				
		}
	}
	
	
	@Autowired
	@RequestMapping(value = "/signup",method = RequestMethod.POST)
	@ResponseBody
	public void signup(@RequestParam("json") String params,
							HttpServletRequest request,
							HttpServletResponse response){
		JSONObject jobj;
		String username,password,firstname,lastname,email;
		
		String trn, phonenumber, city, street, region, zipcode;
		
		try {
			jobj = new JSONObject(params);
			
			username = jobj.get("username").toString();
			password = jobj.getString("password").toString();
			firstname = jobj.getString("firstname").toString();
			lastname = jobj.getString("lastname").toString();
			email = jobj.getString("email").toString();
			trn = jobj.getString("trn").toString();
			phonenumber = jobj.getString("phonenumber").toString();
			city = jobj.getString("city").toString();
			street = jobj.getString("street").toString();
			region = jobj.getString("region").toString();
			zipcode = jobj.getString("zipcode").toString();
			
			System.out.println("JSONObject ok");
			
			/*query returns true if user exists*/
			boolean userExists = userServices.userExists(username);
			
			System.out.println("Get responce from userExists : "+userExists);
			
			if( userExists )
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			else{
				
				UserServicesImpl us = new UserServicesImpl();
				us.userRegistration(username, password, firstname,
						lastname, email, trn, phonenumber, city, street, region, zipcode);

				response.setStatus(HttpServletResponse.SC_OK);
				//response.setHeader("Content-Location","user/?username="+username+"&status=pending");
				response.setHeader("Content-Location","user/?status=pending");
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}		
	}
	
	@RequestMapping(value = "/categories",method = RequestMethod.GET)
	@ResponseBody
	public String getCategories(){
		
		JSONObject data = new JSONObject();
		JSONArray jarray = new JSONArray();
		List<Category> categoryList = queryCategory.getCategories();
		
		for(Category c : categoryList){
			//jarray.put(c.getCategoryID());
			//jarray.put(c.getName());
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
	
	
	@RequestMapping(value = "/logout")
	public void logout(HttpServletRequest request,
			HttpServletResponse response) {
		
		response.setHeader("Content", "");
	}
	
	
}
