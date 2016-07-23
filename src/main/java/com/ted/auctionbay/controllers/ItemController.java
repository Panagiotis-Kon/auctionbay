package com.ted.auctionbay.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import com.ted.auctionbay.services.ItemServices;

public class ItemController {

	@Autowired
	ItemServices itemServices;
	
}
