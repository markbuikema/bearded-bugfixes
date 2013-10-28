package internettech.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginScreenController implements Initializable {

	@FXML
	private TextField username;

	@FXML
	private PasswordField password;

	private PrintWriter out;
	private BufferedReader in;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		int serverPort = 4444;
		String hostname = "localhost";
		try {
			Socket socket = new Socket(hostname, serverPort);
			out = new PrintWriter(socket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
		}
	}

	public void onLoginClick(ActionEvent e) {
		System.out.println("Login clicked");
		System.out.println(username.getText() + ", " + password.getText());

		String fromServer;
		String fromUser;
		System.out.println("Client started");

		try {
			while ((fromServer = in.readLine()) != null) {
				System.out.println("Server: " + fromServer);

				fromUser = username.getText() + "tries to log in with password " + password.getText();
				if (fromUser != null) {
					System.out.println("Client");
					out.println(fromUser);
				}
			}
		} catch (IOException e1) {
		}
	}
}
