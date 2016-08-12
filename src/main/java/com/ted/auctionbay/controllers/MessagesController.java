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
import com.ted.auctionbay.entities.users.Registereduser;
import com.ted.auctionbay.entities.users.messages.Message;
import com.ted.auctionbay.services.MailboxServices;
import com.ted.auctionbay.services.UserServices;

@Controller
@RequestMapping(value={"/user/{username}/mailbox"})
public class MessagesController {
	
	@Autowired
	MailboxServices mailboxServices;
	
	@Autowired
	UserServices userServices;
	
	
	@RequestMapping(value = "/inbox-module",method = RequestMethod.GET)
	public String getInboxModule(){
		System.out.println("getting inbox module");
		return "/pages/modules/inboxMessagesModule.html";
	}
	
	@RequestMapping(value = "/sent-module",method = RequestMethod.GET)
	public String getSentModule(){
		System.out.println("getting sent module");
		return "/pages/modules/sentMessagesModule.html";
	}
	
	@RequestMapping(value = "/recipients",method = RequestMethod.GET)
	@ResponseBody
	public String getRecipients(){
		
		List<Registereduser> reg_users = userServices.getRecipients();
		JSONArray recipients = new JSONArray();
		if(reg_users.size() != 0){
			for(Registereduser reg : reg_users){
				
				recipients.put(reg.getUsername());
			}
			return recipients.toString();
		}
		
		return new Gson().toJson("No recipients found");
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
		
		
		return inbox.toString();
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
		
		
		return sent.toString();
	}
	
	@RequestMapping(value = "/message",method = RequestMethod.POST)
	@ResponseBody
	public String submitMessage(@RequestParam("username") String username, 
			@RequestParam("message") String message){
		
		JSONObject message_json=null;
		String sender,recipient,subject,message_body;
		try {
			message_json = new JSONObject(message);
			sender = username.toString();
			recipient = message_json.get("recipient").toString();
			subject = message_json.get("subject").toString();
			message_body = message_json.get("message_body").toString();
			if(mailboxServices.submitMessage(sender, recipient, subject, message_body) == 0){
				return new Gson().toJson("Your message to " + recipient + " was sent");
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return new Gson().toJson("Your message was not sent");
	}
	
	

}
