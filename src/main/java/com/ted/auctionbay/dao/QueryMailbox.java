package com.ted.auctionbay.dao;

import java.util.List;

import com.ted.auctionbay.entities.users.messages.Mailbox;
import com.ted.auctionbay.entities.users.messages.Message;

public interface QueryMailbox {
	
	public List<Message> getInboxMessages(String username);
	
	public List<Message> getSentMessages(String username);
	
	public int countNewMessages(String username);
	
	public int markAsRead(int messageID);
	
	public int getMaxMailboxID();
	
	public int getMaxMessageID();
	
	public int submitMessage(Message message, Mailbox from, Mailbox to);
	
	public int deleteMessage(String username, int messageID);
	
	public List<Object[]> inbox(String username);
	
	public List<Object[]> sent(String username);
}
