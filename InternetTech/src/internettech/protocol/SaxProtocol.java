/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package internettech.protocol;

import internettech.model.SaxRequest;
import internettech.model.SaxResponse;
import internettech.model.SaxStatus;

/**
 *
 * @author Christian
 */
public class SaxProtocol {
    public SaxResponse processInput(SaxRequest input) {
        
        
        
        
        
        return new SaxResponse(SaxStatus.MONEY_WITHDRAWN,"Your money has been withdrawn");
    }
    
    
}
