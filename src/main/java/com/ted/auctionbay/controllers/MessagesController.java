package com.ted.auctionbay.controllers;

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

import com.google.gson.Gson;
import com.ted.auctionbay.entities.users.messages.Message;
import com.ted.auctionbay.services.MailboxServices;

@Controller
@RequestMapping(value={"/mailbox"})
public class MessagesController {
	
	@Autowired
	MailboxServices mailboxServices;
	
	
	@RequestMapping(value = "/inbox-module",method = RequestMethod.GET)
	public String getInboxModule(){
		System.out.println("getting inbox module");
		return "/pages/modules/inboxMessagesModule.html";
	}
	
	@RequestMapping(value = "/inbox",method = RequestMethod.GET)
	@ResponseBody
	public String getInbox(@RequestParam("username") String username){
		System.out.println("getting inbox messages");
		List<Message> messages = mailboxServices.getInboxMessages(username);
		JSONArray inbox = new JSONArray();
		for(Message m : messages){
			JSONObject message = new JSONObject();
			try{
				message.put("messageID", m.getMessageID());
				message.put("sender", m.getSender().getUsername());
				message.put("recipient", m.getRecipient().getUsername());
				message.put("subject", m.getSubject());
				message.put("dateCreated", m.getDateCreated());
				message.put("isRead", m.getIsRead());
				message.put("messageBody", m.getMessageText());
				
			} catch(JSONException e){
				e.printStackTrace();
			}
			inbox.put(message);
		}
		
		
		return new Gson().toJson(inbox);
	}
	
	
	@RequestMapping(value = "/sent",method = RequestMethod.GET)
	@ResponseBody
	public String getSent(@RequestParam("username") String username){
		System.out.println("getting inbox messages");
		List<Message> messages = mailboxServices.getSentMessages(username);
		JSONArray sent = new JSONArray();
		for(Message m : messages){
			JSONObject message = new JSONObject();
			try{
				message.put("messageID", m.getMessageID());
				message.put("sender", m.getSender().getUsername());
				message.put("recipient", m.getRecipient().getUsername());
				message.put("subject", m.getSubject());
				message.put("dateCreated", m.getDateCreated());
				message.put("isRead", m.getIsRead());
				message.put("messageBody", m.getMessageText());
				
			} catch(JSONException e){
				e.printStackTrace();
			}
			sent.put(message);
		}
		
		
		return new Gson().toJson(sent);
	}
	
	
	

}
