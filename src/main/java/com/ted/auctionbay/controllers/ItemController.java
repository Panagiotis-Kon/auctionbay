package com.ted.auctionbay.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ted.auctionbay.services.ItemServices;

@Controller
@RequestMapping(value={"/item", "/user/{username}/item"})
public class ItemController {

	@Autowired
	ItemServices itemServices;
	
	
	
}
