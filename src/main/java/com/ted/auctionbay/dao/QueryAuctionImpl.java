package com.ted.auctionbay.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.ted.auctionbay.entities.auctions.Auction;
import com.ted.auctionbay.jpautils.EntityManagerHelper;

public class QueryAuctionImpl implements QueryAuction{

	@Override
	public int numOfAuctions() {
		EntityManager em = EntityManagerHelper.getEntityManager();
		
		Query query = em.createNativeQuery("SELECT count(*) FROM auction");
		int num = query.getFirstResult();
	
		return num;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Auction> getAuctions(int startpage, int endpage) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createQuery("SELECT a FROM Auction a");
		query.setFirstResult(startpage);
		query.setMaxResults(endpage);
		
		return query.getResultList();
		
	}

}
