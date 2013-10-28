/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internettech.server;

import internettech.json.JSONObject;
import internettech.model.SaxRequest;
import internettech.model.SaxResponse;
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
public class MultiSaxServerThread extends Thread {

	private final Socket socket;

	MultiSaxServerThread(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {

			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			String inputLine = "", outputLine;

			System.out.println("connected");
			
			while ((inputLine = in.readLine()) != null) {
				System.out.println("input found");
				
				SaxProtocol sp = new SaxProtocol();

				JSONObject json = new JSONObject(inputLine);
				String method = json.getString("METHOD");
				String url = json.getString("URL");
				long date = json.getLong("DATE");
				String content = json.getString("CONTENT");

				SaxRequest request = new SaxRequest(method, url, content, date);
				System.out.println(request.toString());

				SaxResponse response = sp.processInput(request);

				outputLine = response.format();
				out.println(outputLine);
			}
			System.out.println("while");

		} catch (IOException ex) {
			// Logger.getLogger(MultiSaxServerThread.class.getName()).log(Level.,
			// null, ex);
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
			}
		}

	}

}
