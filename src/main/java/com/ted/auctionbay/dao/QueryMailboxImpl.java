package com.ted.auctionbay.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.ted.auctionbay.entities.users.messages.Message;
import com.ted.auctionbay.jpautils.EntityManagerHelper;

public class QueryMailboxImpl implements QueryMailbox{

	@SuppressWarnings("unchecked")
	@Override
	public List<Message> getInboxMessages(String username) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT * FROM message m, mailbox mb"
				+ " WHERE m.MessageID = mb.MessageID AND mb.type = 'Inbox'"
				+ " AND mb.RegisteredUser = ?1",Message.class);
		
		query.setParameter(1,username);
		
		return query.getResultList();
	}

	@Override
	public List<Message> getSentMessages(String username) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT * FROM message m, mailbox mb"
				+ " WHERE m.MessageID = mb.MessageID AND mb.type = 'Sent'"
				+ " AND mb.RegisteredUser = ?1",Message.class);
		
		query.setParameter(1,username);
		
		return query.getResultList();
	}

	@Override
	public int countNewMessages(String username) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT COUNT(m.MessageID)"
				+ " FROM message m, mailbox mb"
				+ " WHERE m.MessageID = mb.messageID AND mb.RegisteredUser = ?1"
				+ " AND mb.type = 'Inbox' AND M.isRead = 0");
		query.setParameter(1, username);
		int count = -1;
		if(query.getResultList().get(0) == null){
			count = 0;
		} else {
			count = Integer.parseInt(query.getResultList().get(0).toString());
		}
		
		return count;
	}

	@Override
	public int markAsRead(int messageID) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxMailboxID() {
		EntityManager em = EntityManagerHelper.getEntityManager();
		List list = em.createNamedQuery("Mailbox.maxID").getResultList();
		int maxID = -1;
		if(list.get(0) == null){
			maxID=0;
		} else {
			maxID = Integer.parseInt(list.get(0).toString());
		}
		return maxID;
	}

	@Override
	public int getMaxMessageID() {
		EntityManager em = EntityManagerHelper.getEntityManager();
		List list = em.createNamedQuery("Message.maxID").getResultList();
		int maxID = -1;
		if(list.get(0) == null){
			maxID=0;
		} else {
			maxID = Integer.parseInt(list.get(0).toString());
		}
		return maxID;
	}

}
