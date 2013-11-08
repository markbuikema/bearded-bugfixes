/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internettech.model;

import internettech.json.JSONObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * 
 * @author Christian
 */
public class Account {

	private final String username;
	private final String password;
	private float saldo;
	private List<Share> shares;
	private boolean online;
	private final String id;

	public Account(String username, String password, float saldo) {
		this.username = username;
		this.password = password;
		this.saldo = saldo;
		this.id = UUID.randomUUID().toString();
	}

	public Account(String id, String username, String password, float saldo) {
		this.username = username;
		this.password = password;
		this.saldo = saldo;
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public float getSaldo() {
		return saldo;
	}

	public List<Share> getShares() {
		return Collections.unmodifiableList(shares);
	}

	public void addShare(Share... share) {
		shares.addAll(Arrays.asList(share));
	}

	public void removeShare(Share... share) {
		shares.removeAll(Arrays.asList(share));
	}

	protected void withdraw(float amount) {
		saldo = saldo - amount;
		System.out.println("withdraw " + amount + ", new amount: " + saldo);
	}

	protected void deposit(float amount) {
		saldo = saldo + amount;
		System.out.println("deposit " + amount + ", new amount: " + saldo);
	}

	@Override
	public String toString() {
		JSONObject account = new JSONObject();
		account.put("username", username);
		account.put("password", password);
		account.put("money", saldo);
		return account.toString();
	}

	public boolean usernameMatches(String username) {
		return this.username.equals(username);
	}

	public boolean passwordMatches(String password) {
		return this.password.equals(password);
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

}
