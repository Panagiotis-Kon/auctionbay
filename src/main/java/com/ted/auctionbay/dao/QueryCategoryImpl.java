package com.ted.auctionbay.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.ted.auctionbay.entities.items.Category;
import com.ted.auctionbay.jpautils.EntityManagerHelper;

public class QueryCategoryImpl implements QueryCategory{

	@SuppressWarnings("unchecked")
	@Override
	public List<Category> getCategories(){
		
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT * FROM Category", Category.class);
		List<Category> categoryList = query.getResultList();
		
		return categoryList;
	}
}
