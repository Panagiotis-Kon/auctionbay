package com.ted.auctionbay.services;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ted.auctionbay.dao.QueryMailbox;
import com.ted.auctionbay.dao.QueryUser;
import com.ted.auctionbay.dao.QueryUserImpl;
import com.ted.auctionbay.entities.users.messages.Mailbox;
import com.ted.auctionbay.entities.users.messages.MailboxPK;
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
		
		return queryMail.markAsRead(messageID);
	}

	@Override
	public int submitMessage(String sender, String recipient, String subject, String message_body) {
		int messageID = queryMail.getMaxMessageID(); // returns the id that should be used, not the previous
		int mailboxID = queryMail.getMaxMailboxID();
		
		System.out.println("Message body: " + message_body);
		
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
		System.out.println("Setting the sender PK");
		/* For the sender */
		MailboxPK mbpk_sender = new MailboxPK();
		mbpk_sender.setId(mailboxID);
		mbpk_sender.setMessageID(messageID);
		mbpk_sender.setRegisteredUser(sender);
		
		System.out.println("Setting the sender");
		Mailbox mb_sender = new Mailbox();
		mb_sender.setId(mbpk_sender);
		mb_sender.setMessage(m);
		
		mb_sender.setRegistereduser(queryUser.getUser(sender).getRegistereduser());
		mb_sender.setType("Sent");
		System.out.println("Sender ----> Setting mailbox id: " + mailboxID);
		mailboxID++;
		
		/* For the recipient*/
		System.out.println("Setting the recipient PK");
		MailboxPK mbpk_recipient = new MailboxPK();
		mbpk_recipient.setId(mailboxID);
		mbpk_recipient.setMessageID(messageID);
		mbpk_recipient.setRegisteredUser(sender);
		
		System.out.println("Setting the recipient");
		Mailbox mb_recipient = new Mailbox();
		mb_recipient.setId(mbpk_recipient);
		mb_recipient.setMessage(m);
		mb_recipient.setRegistereduser(queryUser.getUser(recipient).getRegistereduser());
		mb_recipient.setType("Inbox");
		System.out.println("Recipient ----> Setting mailbox id: " + mailboxID);
		mailboxID++;
		
		if(queryMail.submitMessage(m, mb_sender, mb_recipient) == 0){
			System.out.println("The message was submitted, messageID: " + messageID);
			messageID++;
			return 0;
		}
		return -1;
	}

}
