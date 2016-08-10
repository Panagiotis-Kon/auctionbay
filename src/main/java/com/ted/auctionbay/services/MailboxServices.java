package com.ted.auctionbay.services;

import java.util.List;

import com.ted.auctionbay.entities.users.messages.Message;

public interface MailboxServices {

	public List<Message> getInboxMessages(String username);
	
	public List<Message> getSentMessages(String username);
	
	public int countNewMessages(String username);
	
	public int markAsRead(int messageID);
}
