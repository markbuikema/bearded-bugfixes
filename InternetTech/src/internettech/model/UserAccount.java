/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internettech.model;

import internettech.json.JSONObject;

/**
 *
 * @author Christian
 */
public class UserAccount extends Account {

    private final String password;
    private float balance;

    public UserAccount(String username, String password, float balance) {
        super(username);
        this.password = password;
        this.balance = balance;

    }
    
    public String getPassword() {
        return password;
    } 

    protected void withdraw(float amount) {
        balance = balance - amount;
//        System.out.println("withdraw " + amount + ", new amount: " + balance);
    }

    protected void deposit(float amount) {
        balance = balance + amount;
//        System.out.println("deposit " + amount + ", new amount: " + balance);
    }
    
    public void setBalance(float balance) {
        this.balance = balance;
    }

    public boolean passwordMatches(String password) {
        return this.password.equals(password);
    }
    
    public float getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        JSONObject account = new JSONObject();
        account.put("id", id);
        account.put("username", name);
        account.put("password", password);
        account.put("money", balance);
        return account.toString();
    }

}
