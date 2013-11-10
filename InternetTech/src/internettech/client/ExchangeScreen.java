package internettech.client;

import internettech.json.JSONArray;
import internettech.json.JSONObject;
import internettech.model.UserAccount;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class ExchangeScreen implements Initializable {

	private UserAccount account;
	private PrintWriter out;
	private BufferedReader in;
	private int threadID;
	private List<String> assIds;

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

	@FXML
	private ListView<String> associationList;

	private ObservableList<String> list;

	public ExchangeScreen(UserAccount acc, PrintWriter out, BufferedReader in) {
		this.account = acc;
		this.out = out;
		this.in = in;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		usernameText.setText(account.getName());
		moneyText.setText(Float.toString(account.getBalance()));

		list = FXCollections.observableArrayList();
		associationList.setItems(list);

		assIds = new ArrayList<>();
		populateAssociationList();

		associationList.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event e) {
				int index = associationList.getSelectionModel().getSelectedIndex();
				String clickedAssId = assIds.get(index);
				onAssociationClicked(clickedAssId);
			}
		});
	}

	protected void onAssociationClicked(String clickedAssId) {
		ObservableList<String> assList = FXCollections.observableArrayList();

		new Thread("Thread-loadShares") {
			public void run() {
				String fromServer, fromUser = "GET_ASSOCIATIONS";
				if (fromUser != null) {
					System.out.println("Client: \n" + fromUser);
					out.println(fromUser);
				}
				try {
					if ((fromServer = in.readLine()) != null) {
						System.out.println("Server: \n" + fromServer);
						final float statusCode = Float.valueOf(fromServer.split("\\s")[0]);
						final String content = statusCode == 1.8f ? fromServer.split("content: ")[1] : "";
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								if (statusCode == 1.8f) {
									JSONObject list = new JSONObject(content);
									JSONArray associations = list.getJSONArray("associations");
									for (int i = 0; i < associations.length(); i++) {
										JSONObject association = associations.getJSONObject(i);
										addAssociationToList(association.getString("name") + " (" + association.getInt("shareCount")
												+ " shares for sale)", association.getString("id"));
									}
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

	private void populateAssociationList() {
		new Thread("Thread-loadAssociations") {
			public void run() {
				String fromServer, fromUser = "GET_ASSOCIATIONS";
				if (fromUser != null) {
					System.out.println("Client: \n" + fromUser);
					out.println(fromUser);
				}
				try {
					if ((fromServer = in.readLine()) != null) {
						System.out.println("Server: \n" + fromServer);
						final float statusCode = Float.valueOf(fromServer.split("\\s")[0]);
						final String content = statusCode == 1.8f ? fromServer.split("content: ")[1] : "";
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								if (statusCode == 1.8f) {
									JSONObject list = new JSONObject(content);
									JSONArray associations = list.getJSONArray("associations");
									for (int i = 0; i < associations.length(); i++) {
										JSONObject association = associations.getJSONObject(i);
										addAssociationToList(association.getString("name") + " (" + association.getInt("shareCount")
												+ " shares for sale)", association.getString("id"));
									}
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

	private void addAssociationToList(String text, String assId) {
		list.add(text);
		assIds.add(assId);
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
