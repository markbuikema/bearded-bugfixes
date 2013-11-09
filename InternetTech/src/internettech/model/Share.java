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

    private final String id;
    private final String assId;
    private String accId;

    private float price = 5.0f;
    private boolean forSale = true;

    public Share(String assId) {
        /**
         * Initially association is the owner *
         */
        this.accId = assId;
        this.assId = assId;
        id = UUID.randomUUID().toString();
    }

    public String getOwnerId() {
        return accId;
    }

    public void setOwnerId(String accId) {
        this.accId = accId;
    }

    public boolean isForSale() {
        return forSale;
    }

    public void setForSale(boolean forSale) {
        this.forSale = forSale;
    }

    public String getId() {
        return id;
    }

    public String getAssociationId() {
        return assId;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
