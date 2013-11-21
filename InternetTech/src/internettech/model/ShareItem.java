/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package internettech.model;

/**
 *
 * @author Chingo
 */
public class ShareItem {
    private String ownerId;
    private String ownerName;
    private float price;
    private int amount;
    
    public ShareItem(String ownerId, String ownerName, int amount) {
        this.ownerId = ownerId;
        this.amount = amount;
        this.ownerName = ownerName;
    }
    
    public ShareItem(String ownerId, String ownerName, float price, int amount) {
        this.ownerId = ownerId;
        this.amount = amount;
        this.price = price;
        this.ownerName = ownerName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
    
    
}
