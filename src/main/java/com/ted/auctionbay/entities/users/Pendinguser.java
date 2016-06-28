package com.ted.auctionbay.entities.users;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the pendinguser database table.
 * 
 */
@Entity
@NamedQueries({
	@NamedQuery(name="Pendinguser.findAll", 
				query="SELECT p FROM Pendinguser p"),
    @NamedQuery(name="Pendinguser.findUser",
                query="SELECT u FROM Pendinguser u WHERE u.username LIKE :username"),
    @NamedQuery(name="Pendinguser.delete",
    			query="DELETE FROM Pendinguser p WHERE p.username LIKE :username"),
})
public class Pendinguser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String username;

	//bi-directional one-to-one association to User
	@OneToOne
	@PrimaryKeyJoinColumn(name="Username")
	private User user;

	public Pendinguser() {
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}