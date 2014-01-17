/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internettech.model;

import java.util.List;
import java.util.UUID;

/**
 * 
 * @author Christian
 */
public class Account {
        protected String id;
	protected final String name;
	private List<Share> shares;
	private boolean online;
	
        public Account(String username) {
		this.name = username;
		this.id = UUID.randomUUID().toString();
	}
        
	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public boolean nameMatches(String username) {
		return this.name.equals(username);
	}
        
        @Override
        public String toString() {
            return name;
        }
}
