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
		q.setParameter(0, ItemID);
		List<Item> Set = q.getResultList();
		List<Double> resultSet = new ArrayList<Double>();
		resultSet.add(Set.get(0).getLatitude());
		resultSet.add(Set.get(0).getLongitute());
		return resultSet;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Category>  getCategories(int ItemID){		
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query q = em.createNativeQuery("SELECT Category FROM item WHERE ItemID=?",Item.class);
		q.setParameter(0, ItemID);
		List<Category> resultSet = q.getResultList();
		return resultSet;
	}
	
	@SuppressWarnings("unchecked")
	public String getLocation(int ItemID){
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query q = em.createNativeQuery("SELECT Location FROM item WHERE ItemID=?",Item.class);
		q.setParameter(0, ItemID);
		List<Item> Set = q.getResultList();
		return Set.get(0).getLocation();
	}
	
}
