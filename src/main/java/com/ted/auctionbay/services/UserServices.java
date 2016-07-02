package com.ted.auctionbay.services;

import java.util.List;

import com.ted.auctionbay.entities.users.Pendinguser;
import com.ted.auctionbay.entities.users.Registereduser;

public interface UserServices {

	public void userRegistration(String username, String password, String firstname
			, String lastname, String email, String trn, String phonenumber, 
			String city, String street, String region, String zipcode);
	
	public void getAddressID();
	
	public int Login(String username, String password);
	
	public boolean userExists(String username);
	
	public void accept_user(String username);
	
	public int count_registered();
	
	public int count_pending();
	
	public List<Pendinguser>  getPendingUsers();
	
	public List<Registereduser> getGroupsOfUsers(int startpage, int pagesize);
}
