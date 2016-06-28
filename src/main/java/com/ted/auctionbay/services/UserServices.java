package com.ted.auctionbay.services;

public interface UserServices {

	public void userRegistration(String username, String password, String firstname
			, String lastname, String email, String trn, String phonenumber, 
			String city, String street, String region, String zipcode);
	
	public void getAddressID();
	
	public int Login(String username, String password);
	
	public boolean userExists(String username);
}
