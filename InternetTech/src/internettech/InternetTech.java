/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package internettech;

import internettech.server.SaxServer;

/**
 *
 * @author Christian
 */
public class InternetTech {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SaxServer server = new SaxServer(4444);
        server.start();
    }
    
    
    
}
