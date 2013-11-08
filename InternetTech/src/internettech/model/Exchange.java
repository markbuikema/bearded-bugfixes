/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internettech.model;

import internettech.manager.AccountManager;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * 
 * @author Christian
 */
public class Exchange {

	private static Exchange instance;
	private List<Account> accounts;
	private Stack<String> unusedUsernames;
	private List<Association> asses;
	private List<Share> shares;

	private Exchange() {
		asses = new ArrayList<>();
		accounts = new ArrayList<>();
		accounts.add(new Account("admin", "admin", 1337));
		unusedUsernames = new Stack<>();
		for (int i = 100000; i < 1000000; i++) {
			unusedUsernames.push(String.valueOf(i));
		}

		Collections.shuffle(unusedUsernames, new SecureRandom());
	}

	public static Exchange getInstance() {
		if (instance == null) {
			instance = new Exchange();
		}
		return instance;
	}

	public Account getAccountById(String id) {
		for (Account account : accounts) {
			if (account.getId().equals(id)) {
				return account;
			}
		}
		return null;
	}

	public Account generateAccount() {
		Account account = new Account(unusedUsernames.pop(), generatePassword(), -10.0f);
		AccountManager.getInstance().store(account);
		return account;
	}

	public void addShare(Account account, Share share) {
		for (Account acc : accounts) {
			if (acc.getShares().contains(share)) {
				acc.removeShare(share);
			}
		}
		account.addShare(share);
	}

	private String generatePassword() {
		SecureRandom random = new SecureRandom();
		long passwordLong = random.nextLong();
		String password = "";

		for (int i = 0; i < 64; i += 8) {
			int number = (int) ((passwordLong >> i) & 0xf);
			switch (number) {
			case 0:
				password += "0";
				break;
			case 1:
				password += "1";
				break;
			case 2:
				password += "2";
				break;
			case 3:
				password += "3";
				break;
			case 4:
				password += "4";
				break;
			case 5:
				password += "5";
				break;
			case 6:
				password += "6";
				break;
			case 7:
				password += "7";
				break;
			case 8:
				password += "8";
				break;
			case 9:
				password += "9";
				break;
			case 10:
				password += "a";
				break;
			case 11:
				password += "b";
				break;
			case 12:
				password += "c";
				break;
			case 13:
				password += "d";
				break;
			case 14:
				password += "e";
				break;
			case 15:
				password += "f";
				break;

			}
		}
		return password;
	}

	public boolean withdraw(Account user, float amount) {

		if (user.getSaldo() >= amount && user.getSaldo() - amount >= Integer.MIN_VALUE) {
			user.withdraw(amount);
			return true;
		}
		return false;
	}

	public boolean deposit(Account user, float amount) {
		if (user.getSaldo() + amount <= Integer.MAX_VALUE) {
			user.deposit(amount);
			return true;
		}
		return false;
	}

	public Account login(String username, String password) {
		for (Account account : accounts) {
			if (account.usernameMatches(username) && account.passwordMatches(password)) {
				account.setOnline(true);
				return account;
			}
		}
		return null;
	}

}
