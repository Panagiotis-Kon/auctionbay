package com.ted.auctionbay.entities.users;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the sellerrating database table.
 * 
 */
@Entity
@NamedQuery(name="Sellerrating.findAll", query="SELECT s FROM Sellerrating s")
public class Sellerrating implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private SellerratingPK id;

	private int rate;

	//bi-directional many-to-one association to Registereduser
	@ManyToOne
	@JoinColumn(name="Username")
	private Registereduser registereduser;

	public Sellerrating() {
	}

	public SellerratingPK getId() {
		return this.id;
	}

	public void setId(SellerratingPK id) {
		this.id = id;
	}

	public int getRate() {
		return this.rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public Registereduser getRegistereduser() {
		return this.registereduser;
	}

	public void setRegistereduser(Registereduser registereduser) {
		this.registereduser = registereduser;
	}

}