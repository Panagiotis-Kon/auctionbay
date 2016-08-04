package com.ted.auctionbay.dao;

import java.util.List;

import com.ted.auctionbay.entities.items.Category;

public interface QueryCategory {
	public List<Object[]> getAllCategories();
	public int maxCategoryID();
	public List<Category> fetchCategories();
}
