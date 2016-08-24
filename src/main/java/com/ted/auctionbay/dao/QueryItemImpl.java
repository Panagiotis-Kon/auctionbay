package com.ted.auctionbay.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.ted.auctionbay.jpautils.EntityManagerHelper;
import com.ted.auctionbay.entities.items.Category;
import com.ted.auctionbay.entities.items.Item;

public class QueryItemImpl implements QueryItem {
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Item>  getItems(){		
		EntityManager em = EntityManagerHelper.getEntityManager();
		List<Item> resultSet = em.createNativeQuery("SELECT * FROM item",Item.class).getResultList();
		return resultSet;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Double> getCoordinates(int ItemID){
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query q = em.createNativeQuery("SELECT Latitude, Longitude FROM item WHERE ItemID=?",Item.class);
		q.setParameter(1, ItemID);
		List<Item> Set = q.getResultList();
		List<Double> resultSet = new ArrayList<Double>();
		resultSet.add(Set.get(0).getLatitude());
		resultSet.add(Set.get(0).getLongitute());
		return resultSet;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String>  getCategories(int ItemID){		
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query q = em.createNativeQuery("SELECT Name FROM category, item_has_category WHERE item_has_category.CategoryID = category.CategoryID and item_has_category.ItemID=?");
		q.setParameter(1, ItemID);
		List<String> resultSet = q.getResultList();
		return resultSet;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String getLocation(int ItemID){
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query q = em.createNativeQuery("SELECT Location FROM item WHERE ItemID=?",Item.class);
		q.setParameter(1, ItemID);
		List<Item> Set = q.getResultList();
		return Set.get(0).getLocation();
	}

	@Override
	public int getNumberofItems() {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query q = em.createNativeQuery("SELECT COUNT(ItemID) FROM item",Item.class);
		List<Item> Set = q.getResultList();
		int count = Integer.parseInt(Set.get(0).toString());
		return count;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Item getDetails(int ItemID) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query q = em.createNativeQuery("SELECT * FROM item WHERE ItemID=?",Item.class);
		q.setParameter(1, ItemID);
		List<Item> Set = q.getResultList();
		return Set.get(0);
	}

	@Override
	public int maxItemID() {
		EntityManager em = EntityManagerHelper.getEntityManager();
		int maxID;
		Object idSet  =  em.createNamedQuery("Item.itemMaxID").getResultList().get(0);
		if(idSet == null) {
			maxID = 0;
		} else {
			maxID = Integer.parseInt(idSet.toString()) + 1;
		}
		return maxID;
	}

	@Override
	public void deleteItem(int itemID) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("DELETE FROM item WHERE ItemID=?");
		query.setParameter(1, itemID);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getItemIDs() {
		EntityManager em = EntityManagerHelper.getEntityManager();
		List<String> resultSet = em.createNativeQuery("SELECT ItemID FROM item").getResultList();
		return resultSet;
	}
	
}
