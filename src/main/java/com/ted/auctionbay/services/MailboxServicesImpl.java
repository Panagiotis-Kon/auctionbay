package com.ted.auctionbay.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ted.auctionbay.dao.QueryMailbox;
import com.ted.auctionbay.entities.users.messages.Message;

public class MailboxServicesImpl implements MailboxServices{

	@Autowired
	QueryMailbox queryMail;
	
	
	@Override
	public List<Message> getInboxMessages(String username) {
		
		return queryMail.getInboxMessages(username);
	}

	@Override
	public List<Message> getSentMessages(String username) {
		
		return queryMail.getSentMessages(username);
	}

	@Override
	public int countNewMessages(String username) {
		
		return queryMail.countNewMessages(username);
	}

	@Override
	public int markAsRead(int messageID) {
		// TODO Auto-generated method stub
		return 0;
	}

}
