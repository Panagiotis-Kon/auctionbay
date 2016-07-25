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
		int num = Integer.parseInt(query.getResultList().get(0).toString());
	
		return num;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Auction> getAuctions(int startpage, int endpage) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		/*String sql = "SELECT a.AuctionID, a.ItemID, a.Seller, a.Title, a.BuyPrice, a.FirstBid, a.StartTime, a.EndTime, c.Name"+ 
				" FROM auction a, aitem_has_category ihc, category c" +
				" WHERE a.ItemID = ihc.ItemID and ihc.CategoryID = c.CategoryID";
		*/
		Query query = em.createNativeQuery("SELECT * FROM auction",Auction.class);
		//Query query = em.createNativeQuery(sql,Auction.class);
		query.setFirstResult(startpage);
		query.setMaxResults(endpage);
		
		return query.getResultList();
		
	}

	@Override
	public int getNumOfBids(int auction_id) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT count(rba.AuctionID) FROM registereduser_bidsin_auction rba"
				+" WHERE rba.AuctionID LIKE ?1 GROUP BY rba.AuctionID");
		query.setParameter(1, auction_id).getFirstResult();
		List<?> list = query.getResultList();
		if(!list.isEmpty()){
			return Integer.parseInt(list.get(0).toString());
		}
		return -1;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Auction> getAuctionsByCategory(int startpage, int endpage, String category) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT a.AuctionID,a.ItemID,a.Seller,a.Title,a.BuyPrice,a.FirstBid,a.StartTime,a.EndTime "
				+ "FROM auction a,category c,item_has_category ihc, item i"
				+ " where a.ItemID = i.ItemID and i.ItemID = ihc.ItemID and ihc.CategoryID = c.CategoryID"
				+ " and c.Name = ?1",Auction.class) ;
		
		query.setParameter(1, category);
		query.setFirstResult(startpage);
		query.setMaxResults(endpage);
		
		return query.getResultList();
	}

}
