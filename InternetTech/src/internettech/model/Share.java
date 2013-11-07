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
public class Share {
    private final String shareId;
    private final String assId;
    private Account owner;
    
    private float price = 5.0f;
    private boolean forSale = false;
    
    public Share(String assId) {
        this.assId = assId;
        shareId = UUID.randomUUID().toString();
    }
    
    public void setOwner(Account owner) {
        this.owner = owner;
    }

    public boolean isForSale() {
        return forSale;
    }

    public void setForSale(boolean forSale) {
        this.forSale = forSale;
    }
    
    public Account getOwner() {
        return owner;
    }
    
    public String getId() {
        return shareId;
    }
    
    public String getAssociation() {
        return assId;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
