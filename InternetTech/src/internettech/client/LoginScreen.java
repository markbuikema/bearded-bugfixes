package internettech.client;

import internettech.json.JSONObject;
import internettech.model.Account;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginScreen extends Application implements Initializable {

	@Override
	public void start(Stage stage) throws Exception {

		Parent root = FXMLLoader.load(getClass().getResource("fxml_loginscreen.fxml"));
		Scene scene = new Scene(root);

		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	@FXML
	private TextField username;

	@FXML
	private PasswordField password;

	@FXML
	private Label message;

	@FXML
	private Button registerButton;

	@FXML
	private Label usernameText;

	@FXML
	private Label moneyText;

	@FXML
	private TextField moneyInput;

	@FXML
	private Button depositButton;

	@FXML
	private Button withdrawButton;

	private PrintWriter out;
	private BufferedReader in;
	private Socket socket;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		int serverPort = 4444;
		String hostname = "localhost";
		try {
			socket = new Socket(hostname, serverPort);
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String fromServer;

			if ((fromServer = in.readLine()) != null) {
				System.out.println("Server: \n" + fromServer);
			}
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host " + hostname);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to " + hostname);
			System.exit(1);
		}

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					socket.close();
					out.close();
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@FXML
	private void onLoginClick(final ActionEvent e) {
		new Thread("Thread-login") {
			public void run() {
				String fromServer, fromUser = "LOGIN_ACCOUNT " + System.currentTimeMillis() + " " + username.getText() + " "
						+ password.getText();
				if (fromUser != null) {
					System.out.println("Client: \n" + fromUser);
					out.println(fromUser);
				}
				try {
					if ((fromServer = in.readLine()) != null) {
						System.out.println("Server: \n" + fromServer);
						final float statusCode = Float.valueOf(fromServer.split("\\s")[0]);

						final String content = fromServer.split("content: ")[1];
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								if (statusCode == 1.3f) {
									JSONObject json = new JSONObject(content);
									Account account = new Account(json.getString("username"), json.getString("password"), (float) json
											.getDouble("money"));
									onLoginSuccess(account, e);
								} else if (statusCode == 2.2f) {
									message.setText("Incorrect username or password!");
								} else {
									message.setText("User already logged in");
								}
							}
						});
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ArrayIndexOutOfBoundsException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	protected void onLoginSuccess(Account account, ActionEvent event) {
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("fxml_exchangescreen.fxml"));
			Stage stage = new Stage();
			stage.setTitle("SaxExchange");
			stage.setResizable(false);
			stage.setScene(new Scene(root));
			stage.show();

			((Node) (event.getSource())).getScene().getWindow().hide();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void onRegisterClick(ActionEvent e) {
		new Thread("Thread-register") {
			public void run() {
				String fromServer, fromUser = "CREATE_ACCOUNT";
				if (fromUser != null) {
					System.out.println("Client: \n" + fromUser);
					out.println(fromUser);
				}
				try {
					if ((fromServer = in.readLine()) != null) {
						try {
							System.out.println("Server: \n" + fromServer);
							if (fromServer.startsWith("1.2")) {
								String json = fromServer.split("content: ")[1];
								JSONObject object = new JSONObject(json);
								final String info = "Username: " + object.getString("username") + "\nPassword: "
										+ object.getString("password");
								Platform.runLater(new Runnable() {
									@Override
									public void run() {
										message.setText(info);
										registerButton.setDisable(true);
									}
								});

							} else {
								final boolean noMoreAccounts = fromServer.startsWith("2.1");
								Platform.runLater(new Runnable() {
									@Override
									public void run() {
										message.setText(noMoreAccounts ? "No more accounts available" : "Something went wrong");
										registerButton.setDisable(true);
									}
								});

							}
						} catch (ArrayIndexOutOfBoundsException e) {
							return;
						}
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}.start();
	}

}
