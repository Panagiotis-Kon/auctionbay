package com.ted.auctionbay.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.ted.auctionbay.jpautils.EntityManagerHelper;
import com.ted.auctionbay.entities.items.Category;
import com.ted.auctionbay.entities.items.Item;
import com.ted.auctionbay.entities.users.Pendinguser;

public class QueryItemImpl implements QueryItem {

	@SuppressWarnings("unchecked")
	@Override
	public List<Double> getCoordinates(int ItemID){
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query q = em.createNativeQuery("SELECT latitude, longitude FROM item WHERE ItemID=?",Item.class);
		q.setParameter(0, ItemID);
		List<String> Set = q.getResultList();
		List<Double> resultSet = new ArrayList<Double>();
		resultSet.add(Double.parseDouble(Set.get(0)));
		resultSet.add(Double.parseDouble(Set.get(1)));
		
		return resultSet;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Category>  getCategory(int ItemID){		
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query q = em.createNativeQuery("SELECT Catgory FROM item WHERE ItemID=?",Item.class);
		q.setParameter(0, ItemID);
		List<Category> resultSet = q.getResultList();
		return resultSet;
	}
	
}
