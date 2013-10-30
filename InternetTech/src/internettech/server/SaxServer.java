/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internettech.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Christian
 */
public class SaxServer extends Thread {
	
	private static int clientCount;

    public static void main(String[] args) {
        boolean listening = true;
        int port = 4444;
        ServerSocket serverSocket;
        
        try {
            serverSocket = new ServerSocket(port);
           
            while (listening) {
                System.out.println("Clients connected: " + clientCount);
                new MultiSaxServerThread(serverSocket.accept()).start();
                clientCount++;
            }
            serverSocket.close();
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + port);
            System.exit(-1);
        }
    }
    
    public static void onDisconnect(Socket socket) {
    	clientCount--;
        System.out.println("Clients connected: " + clientCount);
    }

}
