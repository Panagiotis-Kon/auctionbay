package com.ted.auctionbay.dao;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ted.auctionbay.entities.auctions.Auction;
import com.ted.auctionbay.entities.users.Pendinguser;
import com.ted.auctionbay.entities.users.Registereduser;
import com.ted.auctionbay.entities.users.User;
import com.ted.auctionbay.jpautils.EntityManagerHelper;
import com.ted.auctionbay.services.UserServicesImpl;

@Component
public class QueryUserImpl implements QueryUser{
	
	@Override
	public boolean userExists(String username) {	
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT Username FROM User WHERE Username =?");
		query.setParameter(1, username); 
		boolean res = !query.getResultList().isEmpty(); //then user exists already
		//Query query = em.createNativeQuery("SELECT EXISTS (SELECT Username FROM User WHERE Username =?)");
		//query.setParameter(1, username); 
		//boolean res = (boolean) query.getResultList().get(0); //then user exists already
		System.out.println("user exist: " + res);
		return res;
	}
	
	@Override
	public boolean fetchPendingByUsername(String username){
		
		boolean result = false;
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT Username FROM Pendinguser WHERE Username =?");
		query.setParameter(1, username); 
		result = !query.getResultList().isEmpty(); //then the user is pending 		
		return result;
	}
	
	@Override
	public int count_registered() {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query q = em.createNativeQuery("SELECT count(*) FROM registereduser");
		int number = Integer.parseInt(q.getResultList().get(0).toString());
		
		return number;
		
	}
	
	@Override
	public int count_pending() {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query q = em.createNativeQuery("SELECT count(*) FROM pendinguser");
		int number = Integer.parseInt(q.getResultList().get(0).toString());
		
		return number;
		
	}
	
	@Override
	public boolean user_validator(String username, String password){
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query q = em.createNativeQuery("SELECT Username,Password FROM User WHERE Username =? AND Password=?");
		q.setParameter(1,username);
		q.setParameter(2, password);
		if(!q.getResultList().isEmpty()){
			return true;
		}
		return false;
		
	}
	
	@Override
	public List<Pendinguser>  getPendingUsers(){		
		EntityManager em = EntityManagerHelper.getEntityManager();
		List<Pendinguser> resultSet = em.createNativeQuery("SELECT * FROM pendinguser",Pendinguser.class).getResultList();
		
		return resultSet;
	}
	
	@Override
	public int getAddressMaxID(){
		System.out.println("address Max");
		EntityManager em = EntityManagerHelper.getEntityManager();
		List<?> resultSet  =  em.createNamedQuery("Address.maxID").getResultList();
		int id;
		if( resultSet.get(0) == null)
			id = 0;
		else
			id = Integer.parseInt(resultSet.get(0).toString());
		 
		return id;
	
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Registereduser> getGroupsOfUsers(int startpage, int pagesize){
		
		EntityManager em = EntityManagerHelper.getEntityManager();
		System.out.println("startpage: " + startpage + " pagesize: " + pagesize);
		Query query = em.createNativeQuery("SELECT * FROM registereduser",Registereduser.class);

		query.setFirstResult(startpage);
		query.setMaxResults(pagesize);
		List<Registereduser> regUsers = query.getResultList();
		
		return regUsers;
		
	
		
	}
	
	@Override
	public int registeredNumber() {
		EntityManager em = EntityManagerHelper.getEntityManager();
		int rn =  Integer.parseInt( em.createNativeQuery("SELECT count(*) FROM registereduser")
				 				.getResultList().get(0).toString());
		return rn;
		
	}
	

	@Override
	public void accept_user(String username) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		//System.out.println(username);
		System.out.println("EM is Open = "+em.isOpen());
		
		// Check with native query if it exists
		
		//Pendinguser penUser = (Pendinguser) em.createNamedQuery("Pendinguser.findUser").setParameter("username",username).getResultList().get(0);
		Pendinguser penUser = (Pendinguser) em.createNativeQuery("SELECT Username FROM pendinguser WHERE Username=?",Pendinguser.class).setParameter(1, username).getResultList().get(0);
		System.out.println("penUser username: " + penUser.getUsername());
		Registereduser regUser = new Registereduser();
		regUser.setUsername(username);
		penUser.getUser().setRegistereduser(regUser);
		regUser.setUser(penUser.getUser());
		
		
		em.createNativeQuery("DELETE FROM pendinguser WHERE Username=?",Pendinguser.class).setParameter(1, username).executeUpdate();
		//em.createNamedQuery("Pendinguser.delete").setParameter("username",username).executeUpdate();
		System.out.println("regUser: " + regUser.getUsername());
		em.persist(regUser);
		
	}
	
	@Override
	public int registerUser(User ruser){
		try {
			EntityManager em = EntityManagerHelper.getEntityManager();
			em.persist(ruser);
		} catch (PersistenceException pe) {
			pe.printStackTrace();
			return 1;
		}
		return 0;
	}

	@Override
	public int count_user_auctions(String username) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT count(*) FROM auction WHERE Seller=?");
		query.setParameter(1, username);
		return Integer.parseInt(query.getResultList().get(0).toString());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Auction> get_user_auctions(String username) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT * FROM auction WHERE Seller=?",Auction.class);
		
		query.setParameter(1, username);
		
		return query.getResultList();
	}
	
	
	
	
}
