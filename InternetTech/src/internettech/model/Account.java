/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package internettech.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Christian
 */
public class Account {
    
    private String username;
    private String password;
    private double saldo = 0.0;
    private List<Share> shares;
    
    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Account(String username, String password, double saldo) {
        this.username = username;
        this.password = password;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
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
}
