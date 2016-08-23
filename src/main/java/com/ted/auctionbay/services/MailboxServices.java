package com.ted.auctionbay.services;

import java.util.List;

import com.ted.auctionbay.entities.users.messages.Message;

public interface MailboxServices {

	public List<Message> getInboxMessages(String username);
	
	public List<Message> getSentMessages(String username);
	
	public int countNewMessages(String username);
	
	public int submitMessage(String sender, String recipient, String subject, String message_body);
	
	public int markAsRead(int messageID);
	
	public int deleteMessage(String username, int messageID);
	
	public List<Object[]> inbox(String username);
	
	public List<Object[]> sent(String username);
}
