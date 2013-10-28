/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internettech.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

/**
 *
 * @author Christian
 */
public class SaxServer extends Thread {

    private final int port;
    private ServerSocket serverSocket;
    boolean listening = true;
    

    public SaxServer(int port) {
        this.port = port;
    }
    
    @Override
    public void run() {
        
        
        try {
            serverSocket = new ServerSocket(port);
           
            while (listening) {
                new MultiSaxServerThread(serverSocket.accept()).start();
            }
            serverSocket.close();
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + port);
            System.exit(-1);
        }
    }
    
    public InetAddress getInetAddress(){
        return serverSocket.getInetAddress();
    }
}
