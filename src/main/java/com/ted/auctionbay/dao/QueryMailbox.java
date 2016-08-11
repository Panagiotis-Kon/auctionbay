package com.ted.auctionbay.dao;

import java.util.List;

import com.ted.auctionbay.entities.users.messages.Message;

public interface QueryMailbox {
	
	public List<Message> getInboxMessages(String username);
	
	public List<Message> getSentMessages(String username);
	
	public int countNewMessages(String username);
	
	public int markAsRead(int messageID);
	
	public int getMaxMailboxID();
	
	public int getMaxMessageID();
}
