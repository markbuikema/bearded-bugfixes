/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package internettech.server;

import internettech.protocol.SaxProtocol;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author Christian
 */
public class MultiSaxServerThread extends Thread{
    private final Socket socket;
    
    MultiSaxServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() 
    {
        try {
            
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                    socket.getInputStream()));
            
            String inputLine, outputLine;
            SaxProtocol sp = new SaxProtocol();
            outputLine = sp.processInput("");
            out.println(outputLine);
            
            while ((inputLine = in.readLine()) != null) {
                
                outputLine = sp.processInput(inputLine); // Response object
                
                
                // Process request
                // Create response
                // Process response in protocol
                // send response
                
                
                
                out.println(outputLine);
                
                
                
                
            }
            
           socket.close(); 
            
        } catch (IOException ex) {
//            Logger.getLogger(MultiSaxServerThread.class.getName()).log(Level., null, ex);
        } 
       
    }

}
