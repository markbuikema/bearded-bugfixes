package internettech.client;

import internettech.json.JSONObject;
import internettech.model.Account;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ExchangeScreen implements Initializable {

	private Account account;
	private PrintWriter out;
	private BufferedReader in;
	private int threadID;

	@FXML
	private Label usernameText;

	@FXML
	private Label moneyText;

	@FXML
	private Label status;

	@FXML
	private TextField moneyInput;

	@FXML
	private Button depositButton;

	@FXML
	private Button withdrawButton;

	public ExchangeScreen(Account acc, PrintWriter out, BufferedReader in) {
		this.account = acc;
		this.out = out;
		this.in = in;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		usernameText.setText(account.getUsername());
		moneyText.setText(Float.toString(account.getSaldo()));
	}

	@FXML
	private void onWithdrawClick(ActionEvent e) {
		new Thread("Thread-withdraw") {
			public void run() {
				String fromServer, fromUser = "MONEY_WITHDRAW " + moneyInput.getText();
				if (fromUser != null) {
					System.out.println("Client: \n" + fromUser);
					out.println(fromUser);
				}
				try {
					if ((fromServer = in.readLine()) != null) {
						System.out.println("Server: \n" + fromServer);
						final float statusCode = Float.valueOf(fromServer.split("\\s")[0]);
						final String content = statusCode == 1.7f ? fromServer.split("content: ")[1] : "";
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								if (statusCode == 1.7f) {
									JSONObject user = new JSONObject(content);
									moneyText.setText(Double.toString(user.getDouble("money")));
									setStatus("Money withdrawn");
								} else if (statusCode == 2.5f) {
									setStatus("Insufficient funds!");
								} else {
									setStatus("Something went wrong.");
								}
							}
						});
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ArrayIndexOutOfBoundsException e) {

				}
			}
		}.start();
	}

	@FXML
	private void onDepositClick(ActionEvent e) {
		new Thread("Thread-deposit") {
			public void run() {
				String fromServer, fromUser = "MONEY_DEPOSIT " + moneyInput.getText();
				if (fromUser != null) {
					System.out.println("Client: \n" + fromUser);
					out.println(fromUser);
				}
				try {
					if ((fromServer = in.readLine()) != null) {
						System.out.println("Server: \n" + fromServer);
						final float statusCode = Float.valueOf(fromServer.split("\\s")[0]);
						final String content = statusCode == 1.6f ? fromServer.split("content: ")[1] : "";
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								if (statusCode == 1.6f) {
									JSONObject user = new JSONObject(content);
									moneyText.setText(Double.toString(user.getDouble("money")));
									setStatus("Money stored");
								} else if (statusCode == 2.6f)
									setStatus("Amount too much.");
								else
									setStatus("Something went wrong.");
							}
						});
					}

				} catch (IOException e) {
					e.printStackTrace();
				} catch (ArrayIndexOutOfBoundsException e) {
				}
			}

		}.start();
	}

	private void setStatus(final String status) {
		this.status.setText(status);
		new Thread("StatusThread-" + ++threadID) {
			public void run() {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						if (getName().equals("StatusThread-" + threadID))
							ExchangeScreen.this.status.setText(" ");
					}

				});
			}
		}.start();

	}
}
