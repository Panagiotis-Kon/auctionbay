package com.ted.auctionbay.services;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ted.auctionbay.dao.QueryMailbox;
import com.ted.auctionbay.dao.QueryUser;
import com.ted.auctionbay.dao.QueryUserImpl;
import com.ted.auctionbay.entities.users.messages.Message;

public class MailboxServicesImpl implements MailboxServices{

	@Autowired
	QueryMailbox queryMail;
	
	@Autowired
	QueryUser queryUser;
	
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

	@Override
	public int submitMessage(String sender, String recipient, String subject, String message_body) {
		int messageID = queryMail.getMaxMessageID();
		int mailboxID = queryMail.getMaxMailboxID();
		
		Message m = new Message();
		m.setMessageID(messageID);
		m.setSubject(subject);
		m.setMessageText(message_body);
		m.setIsRead(Byte.parseByte("0".toString()));
		Calendar calendar = Calendar.getInstance();
        Date date =  calendar.getTime();
		m.setDateCreated(date);
		m.setSender(queryUser.getUser(sender).getRegistereduser());
		m.setRecipient(queryUser.getUser(recipient).getRegistereduser());
		
		/* mailbox pk set , one for sender one for recipient*/
		return 0;
	}

}
