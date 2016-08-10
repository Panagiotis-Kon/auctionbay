package com.ted.auctionbay.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

@Controller
@RequestMapping(value={"/mailbox"})
public class MessagesController {
	
	@RequestMapping(value = "/inbox-module",method = RequestMethod.GET)
	public String getInboxModule(){
		System.out.println("getting inbox module");
		return "/pages/modules/inboxMessagesModule.html";
	}
	
	@RequestMapping(value = "/inbox",method = RequestMethod.GET)
	@ResponseBody
	public String getInbox(@RequestParam("username") String username){
		System.out.println("getting inbox messages");
		
		
		
		return new Gson().toJson("No inbox");
	}
	
	
	

}
