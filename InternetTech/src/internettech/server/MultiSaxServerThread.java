/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internettech.server;

import internettech.json.JSONException;
import internettech.json.JSONObject;
import internettech.model.Exchange;
import internettech.model.SaxResponse;
import internettech.model.SaxStatus;
import internettech.model.UserAccount;
import internettech.protocol.SaxProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

/**
 * 
 * @author Christian
 */
public class MultiSaxServerThread extends Thread {

	private final Socket socket;
	private UserAccount user;

	MultiSaxServerThread(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		System.out.println("new connection - " + new Date());
		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			String inputLine, outputLine;

			out.println((new SaxResponse(SaxStatus.CONNECTION_CREATED)).toString());

			while ((inputLine = in.readLine()) != null) {

				SaxResponse response = SaxProtocol.processRequest(inputLine, user);

				if (inputLine.split("\\s")[0].equals("LOGIN_ACCOUNT")) {
					try {
						if (response.getContent() != null) {
							JSONObject account = new JSONObject(response.getContent());
							if (account.has("username") && account.has("password")) {
								user = Exchange.getInstance().login(account.getString("username"), account.getString("password"));
							}
						}
					} catch (JSONException e) {
					}
				}

				out.println(response.toString());
			}

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
