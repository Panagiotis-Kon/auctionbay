package com.ted.auctionbay.services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.ted.auctionbay.entities.auctions.Auction;
import com.ted.auctionbay.entities.users.Address;
import com.ted.auctionbay.entities.users.Pendinguser;
import com.ted.auctionbay.entities.users.Registereduser;
import com.ted.auctionbay.entities.users.User;
import com.ted.auctionbay.dao.QueryUser;
import com.ted.auctionbay.jpautils.EntityManagerHelper;

@Service("userServices")
public class UserServicesImpl implements UserServices{
	
	@Autowired
	QueryUser queryUser;

	//private int AddressID = queryUser.getAddressMaxID()+1;
	private static int AddressID;

	public static void setAddressID(int addressID) {
		AddressID = addressID;
	}


	@Override
	public void getAddressID(){
		AddressID = queryUser.getAddressMaxID()+1;
	}
	
	
	@Override
	public void userRegistration(String username, String password, String firstname
			, String lastname, String email, String trn, String phonenumber, 
			String city, String street, String region, String zipcode){
		
		System.out.println("UserRegistration starts");
		getAddressID();
		Address address = new Address();
		address.setAddressID(AddressID);
		address.setCity(city);
		address.setStreet(street);
		address.setRegion(region);
		address.setZipCode(zipcode);
		
		
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		
		user.setFirstName(firstname);
		user.setLastname(lastname);
		user.setEmail(email);
		user.setTrn(trn);
		user.setPhoneNumber(phonenumber);
		
		address.setUser(user);
		user.setAddress(address);
		
		Pendinguser penUser = new Pendinguser();
		penUser.setUser(user);
		penUser.setUsername(username);
		user.setPendinguser(penUser);
		
		if (queryUser.registerUser(user)==0){
			System.out.println("Ending UserRegistration");
			AddressID++;
		}
		else
			System.out.println("UserRegistration failed");
		
	}
	
	public static void validateUser(String Username, String password){
		
	}
	
	@Override
	public int Login(String username, String password){
		if(queryUser.fetchPendingByUsername(username))
		{
			return 0; //User is Pending
		} else {
			// validate the user if it exists in db 
			// if the user exists the redirect him to his index page
			// else forbid access
			if(queryUser.user_validator(username, password)){
				return 1; //User entered
			} else {
				return -1; //User not found
			}
			
		}
	}
	
	@Override
	public boolean userExists(String username){
		return queryUser.userExists(username);
	}
	
	@Override
	public void accept_user(String username) {
		queryUser.accept_user(username);		
	}
	
	@Override
	public int count_registered() {
		return queryUser.count_registered();
		
	}
	
	@Override
	public int count_pending() {
		return queryUser.count_pending();
	}
	
	@Override
	public List<Pendinguser>  getPendingUsers(){		
		return queryUser.getPendingUsers();
	}
	
	@Override
	public List<Registereduser> getGroupsOfUsers(int startpage, int pagesize){
		return queryUser.getGroupsOfUsers(startpage, pagesize);
	}


	@Override
	public int count_user_auctions(String username) {
		
		return queryUser.count_user_auctions(username);
	}


	@Override
	public List<Auction> get_user_auctions(String username) {
		
		return queryUser.get_user_auctions(username);
	}


	@Override
	public User getUser(String username) {
		
		return queryUser.getUser(username);
	}


	@Override
	public List<Registereduser> getRecipients() {
		
		return queryUser.getRecipients();
	}
	
}
