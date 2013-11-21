/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package internettech.model;

import java.util.UUID;

/**
 *
 * @author Christian
 */
public class Association extends Account {
    private float balance = 3000000;
    
    public Association(String name) {
        super(name);
        id = UUID.randomUUID().toString();
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }
}
