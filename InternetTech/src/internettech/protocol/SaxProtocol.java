/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package internettech.protocol;

/**
 *
 * @author Christian
 */
public class SaxProtocol {
    public String processInput(String input) {
        String[] b = input.split("\\r");
        System.out.println(b);
        
        return input;
    }
    
    
}
