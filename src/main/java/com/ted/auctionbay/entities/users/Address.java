package com.ted.auctionbay.entities.users;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the address database table.
 * 
 */
@Entity
@NamedQuery(name="Address.findAll", query="SELECT a FROM Address a")
public class Address implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="AddressID")
	private int addressID;

	@Column(name="City")
	private String city;

	@Column(name="Region")
	private String region;

	@Column(name="Street")
	private String street;

	@Column(name="ZipCode")
	private String zipCode;

	@OneToOne(mappedBy="address")
	private User user;
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	//bi-directional many-to-one association to User
	@OneToMany(mappedBy="address")
	private List<User> users;

	public Address() {
	}

	public int getAddressID() {
		return this.addressID;
	}

	public void setAddressID(int addressID) {
		this.addressID = addressID;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getRegion() {
		return this.region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getStreet() {
		return this.street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getZipCode() {
		return this.zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public List<User> getUsers() {
		return this.users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public User addUser(User user) {
		getUsers().add(user);
		user.setAddress(this);

		return user;
	}

	public User removeUser(User user) {
		getUsers().remove(user);
		user.setAddress(null);

		return user;
	}

}