package com.ted.auctionbay.services;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ted.auctionbay.dao.QueryConversation;
import com.ted.auctionbay.dao.QueryUser;
import com.ted.auctionbay.dao.QueryUserImpl;
import com.ted.auctionbay.entities.users.messages.Conversation;
import com.ted.auctionbay.entities.users.messages.ConversationPK;

public class ConversationServicesImpl implements ConversationServices{

	@Autowired
	QueryConversation queryConversation;
	
	@Autowired
	QueryUser queryUser;
	
	@Override
	public List<Conversation> getInboxMessages(String username) {
		
		return queryConversation.getInboxMessages(username);
	}

	@Override
	public List<Conversation> getSentMessages(String username) {
		
		return queryConversation.getSentMessages(username);
	}

	@Override
	public int countNewMessages(String username) {
		
		return queryConversation.countNewMessages(username);
	}

	@Override
	public int markAsRead(int messageID) {
		
		return queryConversation.markAsRead(messageID);
	}

	@Override
	public int submitMessage(String sender, String recipient, String subject, String message_body) {
		
		int conversationID = queryConversation.getMaxConversationID();
		
		System.out.println("Message body: " + message_body);
		
		ConversationPK convpk = new ConversationPK();
		convpk.setConversationID(conversationID);
		convpk.setRecipient(recipient);
		
		
		Conversation conv = new Conversation();
		conv.setId(convpk);
		conv.setRecipient(queryUser.getUser(recipient).getRegistereduser());
		conv.setSender(sender);
		
		Calendar calendar = Calendar.getInstance();
        Date date =  calendar.getTime();
		conv.setDateCreated(date);
		
		conv.setIsRead(Byte.parseByte("0".toString()));
		conv.setSubject(subject);
		conv.setMessageText(message_body);
		
		
		if(queryConversation.submitMessage(conv) == 0){
			System.out.println("The message was submitted, messageID: " + conversationID);
			conversationID++;
			return 0;
		}
		return -1;
		
		/*Message m = new Message();
		m.setMessageID(messageID);
		m.setSubject(subject);
		m.setMessageText(message_body);
		m.setIsRead(Byte.parseByte("0".toString()));
		Calendar calendar = Calendar.getInstance();
        Date date =  calendar.getTime();
		m.setDateCreated(date);
		*/
		
		/* conversation pk set , one for sender one for recipient*/
		//System.out.println("Setting the sender PK");
		/* For the sender */
		/*ConversationPK convpk_sender = new ConversationPK();
		convpk_sender.setId(mailboxID);
		convpk_sender.setMessageID(messageID);
		convpk_sender.setRegisteredUser(sender);
		
		System.out.println("Setting the sender");
		Conversation conv_sender = new Conversation();
		conv_sender.setId(convpk_sender);
		conv_sender.setMessage(m);
		
		conv_sender.setRegistereduser(queryUser.getUser(sender).getRegistereduser());
		conv_sender.setType("Sent");
		System.out.println("Sender ----> Setting conversation id: " + mailboxID);
		mailboxID++;*/
		
		/* For the recipient*/
		/*System.out.println("Setting the recipient PK");
		ConversationPK convpk_recipient = new ConversationPK();
		convpk_recipient.setId(mailboxID);
		convpk_recipient.setMessageID(messageID);
		convpk_recipient.setRegisteredUser(sender);
		
		System.out.println("Setting the recipient");
		Conversation conv_recipient = new Conversation();
		conv_recipient.setId(convpk_recipient);
		conv_recipient.setMessage(m);
		
		conv_recipient.setRegistereduser(queryUser.getUser(recipient).getRegistereduser());
		conv_recipient.setType("Inbox");
		System.out.println("Recipient ----> Setting conversation id: " + mailboxID);
		mailboxID++;*/
		
		
		
		
		
		
	}

	@Override
	public int deleteMessage(String username, int messageID) {
		if(queryConversation.deleteMessage(username, messageID) == 0){
			return 0;
		}
		return 1;
	}

	@Override
	public List<Object[]> inbox(String username) {
		
		return queryConversation.inbox(username);
	}

	@Override
	public List<Object[]> sent(String username) {
		// TODO Auto-generated method stub
		return queryConversation.sent(username);
	}

}
