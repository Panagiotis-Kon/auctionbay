package com.ted.auctionbay.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import com.ted.auctionbay.entities.users.messages.Mailbox;
import com.ted.auctionbay.entities.users.messages.Message;
import com.ted.auctionbay.jpautils.EntityManagerHelper;

public class QueryMailboxImpl implements QueryMailbox{

	@SuppressWarnings("unchecked")
	@Override
	public List<Message> getInboxMessages(String username) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT m.MessageID, m.FromUser, m.ToUser,"
				+ " m.Subject, m.DateCreated, m.isRead, m.MessageText "
				+ "FROM message m, mailbox mb"
				+ " WHERE m.MessageID = mb.MessageID AND mb.type = 'Inbox'"
				+ " AND mb.RegisteredUser = ?1", Message.class);
		
		query.setParameter(1,username);
		
		List<Message> resultSet = query.getResultList();
		//List resultSet  =  em.createNamedQuery("Message.inbox").setParameter("username",username).getResultList();
		return resultSet;
	}

	
	@Override
	public List<Message> getSentMessages(String username) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT m.MessageID, m.FromUser, m.ToUser, m.Subject,"
				+ " m.DateCreated, m.isRead, m.MessageText"
				+ " FROM message m, mailbox mb"
				+ " WHERE m.MessageID = mb.MessageID AND mb.type = 'Sent'"
				+ " AND mb.RegisteredUser = ?1", Message.class);
		
		query.setParameter(1,username);
		List<Message> resultSet = query.getResultList();
		//List resultSet  =  em.createNamedQuery("Message.sent").setParameter("username",username).getResultList();
		return resultSet;
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
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT * FROM message WHERE MessageID=?1",Message.class);
		query.setParameter(1, messageID);
		Message m = (Message) query.getResultList().get(0);
		m.setIsRead(Byte.parseByte("1".toString()));
		
		try {
			em.persist(m);
		}catch (PersistenceException pe) {
			pe.printStackTrace();
			return 1;
		}
		
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
			maxID = Integer.parseInt(list.get(0).toString()) + 1;
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
			maxID = Integer.parseInt(list.get(0).toString()) + 1;
		}
		return maxID;
	}

	@Override
	public int submitMessage(Message message, Mailbox from, Mailbox to) {
		try {
			EntityManager em = EntityManagerHelper.getEntityManager();
			em.persist(message);
			em.persist(to);
			em.persist(from);
		} catch (PersistenceException pe) {
			pe.printStackTrace();
			return 1;
		}
		return 0;
		
	}

}
