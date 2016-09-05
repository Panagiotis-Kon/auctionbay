package com.ted.auctionbay.dao;

import java.util.List;

import com.ted.auctionbay.entities.users.messages.Conversation;

public interface QueryConversation {
	
	public List<Conversation> getInboxMessages(String username);
	
	public List<Conversation> getSentMessages(String username);
	
	public int countNewMessages(String username);
	
	public int markAsRead(int messageID);
	
	public int getMaxConversationID();
	
	public int getMaxMessageID();
	
	public int submitMessage(Conversation conversation);
	
	public int deleteMessage(String username, int messageID);
	
	public List<Object[]> inbox(String username);
	
	public List<Object[]> sent(String username);
}
