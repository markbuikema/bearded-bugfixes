/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internettech.model;

import internettech.manager.AccountManager;
import internettech.manager.AssociationManager;
import internettech.manager.ShareManager;

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
	private Stack<String> unusedUsernames;
	private List<UserAccount> onlineUsers;

	private Exchange() {
		unusedUsernames = new Stack<>();
		onlineUsers = new ArrayList<>();

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
		for (Account account : AccountManager.getInstance().getUserAccounts()) {
			if (account.getId().equals(id)) {
				return account;
			}
		}
		return null;
	}

	public Account generateUserAccount() {
		UserAccount account = new UserAccount(unusedUsernames.pop(), generatePassword(), 0.0f);
		AccountManager.getInstance().createUser(account);
		return account;
	}

	public boolean shareTransaction(String buyerId, String sellerId, String assId, int amount) {
		return ShareManager.getInstance().transaction(buyerId, sellerId, assId, amount);
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

	public boolean withdraw(UserAccount user, float amount) {

		if (user.getBalance() >= amount && user.getBalance() - amount >= Integer.MIN_VALUE) {
			user.withdraw(amount);
			return true;
		}
		return false;
	}

	public boolean deposit(UserAccount user, float amount) {
		if (user.getBalance() + amount <= Integer.MAX_VALUE) {
			user.deposit(amount);
			return true;
		}
		return false;
	}

	public UserAccount login(String username, String password) {
		for (UserAccount account : AccountManager.getInstance().getUserAccounts()) {
			if (account.nameMatches(username) && account.passwordMatches(password)) {
				onlineUsers.add(account);
				return account;
			}
		}
		return null;
	}

	public boolean logout(Account account) {
		for (int i = 0; i < onlineUsers.size(); i++) {
			if (onlineUsers.get(i).equals(account)) {
				onlineUsers.remove(i);
				return true;
			}
		}
		return false;
	}

}
