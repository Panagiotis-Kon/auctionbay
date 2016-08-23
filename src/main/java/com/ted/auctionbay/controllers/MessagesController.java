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
	
	
	@RequestMapping(value = "/unread-number",method = RequestMethod.GET)
	@ResponseBody
	public String unread_number(@RequestParam("username") String username){
		
		int num = mailboxServices.countNewMessages(username);
		if(num > 0) {
			return new Gson().toJson(num);
		}
		return new Gson().toJson("0");
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
		/*List<Message> messages = mailboxServices.getInboxMessages(username);
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
		}*/
		
		
		List<Object[]> messages = mailboxServices.inbox(username);
		JSONArray inbox = new JSONArray();
		
		for(Object[] o :messages){
			JSONObject jobj = new JSONObject();
			try {
				jobj.put("messageID", o[0]);
				jobj.put("sender",o[1]);
				jobj.put("recipient", o[2]);
				jobj.put("subject", o[3]);
				jobj.put("dateCreated", o[4]);
				jobj.put("isRead", o[5]);
				jobj.put("messageBody", o[6]);
			} catch (JSONException e) {
				System.out.println("Problem on json return --- inbox message");
				e.printStackTrace();
			}
			inbox.put(jobj);
		}
		
		return inbox.toString();
	}
	
	
	@RequestMapping(value = "/sent",method = RequestMethod.GET)
	@ResponseBody
	public String getSent(@RequestParam("username") String username){
		System.out.println("getting sent messages");
		/*List<Message> messages = mailboxServices.getSentMessages(username);
	
		JSONArray sent = new JSONArray();
		for(Message m : messages){
			System.out.println("message: " + m.getSubject());
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
		}*/
		
		List<Object[]> messages = mailboxServices.sent(username);
		JSONArray sent = new JSONArray();
		
		for(Object[] o :messages){
			JSONObject jobj = new JSONObject();
			try {
				jobj.put("messageID", o[0]);
				jobj.put("sender",o[1]);
				jobj.put("recipient", o[2]);
				jobj.put("subject", o[3]);
				jobj.put("dateCreated", o[4]);
				jobj.put("isRead", o[5]);
				jobj.put("messageBody", o[6]);
			} catch (JSONException e) {
				System.out.println("Problem on json return --- sent message");
				e.printStackTrace();
			}
			sent.put(jobj);
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
	
	@RequestMapping(value = "/markAsRead",method = RequestMethod.GET)
	@ResponseBody
	public String markAsRead(@RequestParam("messageID") String messageID){
		
		if(mailboxServices.markAsRead(Integer.parseInt(messageID)) == 0){
			return new Gson().toJson("Marked as Read");
		}
		return new Gson().toJson("Cannot mark as read");
	}
	
	@RequestMapping(value = "/delete-message",method = RequestMethod.DELETE)
	@ResponseBody
	public String deleteMessage(@RequestParam("username") String username,@RequestParam("messages") String messages){
		
		try{
			JSONArray jarray = new JSONArray(messages);
			for(int i=0; i<jarray.length();i++){
				int m_id = Integer.parseInt(jarray.get(i).toString());
				System.out.println("m_id to delete: " + m_id);
				if(mailboxServices.deleteMessage(username, m_id) != 0){
					//return new Gson().toJson("A problem occured");
					System.out.println("Cannot delete message with id: " + m_id);
				}
				
			}
		} catch(JSONException e){
			System.out.println("JSON parse problem in deleteMessage");
		}
		
		
		
		return new Gson().toJson("ok");
	}
	

}
