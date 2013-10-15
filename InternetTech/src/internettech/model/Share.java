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
    private Association ass;
    private float price;
    
    public Share(){
        id = UUID.randomUUID().toString();
        price = 5.0f;
    }
    
    public String getId() {
        return id;
    }
    
    public Association getAssociation() {
        return ass;
    }
    
    public void setAssociation(Association ass) {
        this.ass = ass;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
