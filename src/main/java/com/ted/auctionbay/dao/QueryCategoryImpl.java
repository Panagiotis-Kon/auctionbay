package com.ted.auctionbay.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.ted.auctionbay.entities.items.Category;
import com.ted.auctionbay.jpautils.EntityManagerHelper;

public class QueryCategoryImpl implements QueryCategory{

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getAllCategories(){
		
		EntityManager em = EntityManagerHelper.getEntityManager();
		//Query query = em.createNativeQuery("SELECT * FROM Category", Category.class);
		Query query = em.createNativeQuery("SELECT c.name,COUNT(*) FROM category c, item_has_category ihc "
				+ "where c.categoryID = ihc.categoryID GROUP by c.categoryID");
		List<Object[]> categoryList = query.getResultList();
		
		return categoryList;
	}
}
