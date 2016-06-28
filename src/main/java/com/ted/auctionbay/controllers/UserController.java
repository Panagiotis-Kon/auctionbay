package com.ted.auctionbay.controllers;




import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	
	@RequestMapping(value = "/{username}",method = RequestMethod.GET)
	public String access_index(@PathVariable String username){
		return "/pages/index.html";
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
	
}
