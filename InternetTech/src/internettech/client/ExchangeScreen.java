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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ExchangeScreen implements Initializable {

	private UserAccount account;
	private PrintWriter out;
	private BufferedReader in;
	private int threadID;
	private List<String> assIds;
	private List<String> assNames;

	private List<JSONObject> shareArgs;

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

	@FXML
	private Button backButton;

	public ExchangeScreen(UserAccount acc, PrintWriter out, BufferedReader in) {
		this.account = acc;
		this.out = out;
		this.in = in;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		usernameText.setText(account.getName());
		moneyText.setText(Float.toString(account.getBalance()));

		assIds = new ArrayList<>();
		assNames = new ArrayList<>();
		populateAssociationList();

		associationList.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event e) {
				int index = associationList.getSelectionModel().getSelectedIndex();

				if (index < 0)
					return;

				if (shareArgs == null) {
					String clickedAssId = assIds.get(index);
					String clickedAssName = assNames.get(index);
					onAssociationClicked(clickedAssId, clickedAssName);
				} else {
					try {

						FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml_buy_dialog.fxml"));
						loader.setController(new BuyDialogController(shareArgs.get(index), account));
						Parent root = (Parent) loader.load();
						Scene scene = new Scene(root);
						Stage dialog = new Stage();
						dialog.setResizable(false);
						dialog.initStyle(StageStyle.UTILITY);
						dialog.setScene(scene);
						dialog.show();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});

	}

	protected void onAssociationClicked(final String clickedAssId, final String clickedAssName) {

		new Thread("Thread-loadShares") {
			public void run() {
				final ObservableList<String> assList = FXCollections.observableArrayList();
				String fromServer, fromUser = "GET_SHARES " + clickedAssId;
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
									JSONObject obj = new JSONObject(content);
									final JSONArray shares = obj.getJSONArray("shares");
									Platform.runLater(new Runnable() {
										public void run() {
											shareArgs = new ArrayList<>();
											for (int i = 0; i < shares.length(); i++) {
												JSONObject share = shares.getJSONObject(i);

												assList.add(share.getString("ownerName") + " selling " + share.getInt("count")
														+ " shares for EUR" + share.getDouble("price") + " each");

												JSONObject b = new JSONObject();
												b.put("ownerId", share.getString("ownerId"));
												b.put("ownerName", share.getString("ownerName"));
												b.put("associationId", clickedAssId);
												b.put("associationName", clickedAssName);
												b.put("maxCount", share.getInt("count"));
												b.put("price", share.getDouble("price"));
												shareArgs.add(b);
											}
											associationList.setItems(assList);
											backButton.setVisible(true);
										}
									});

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
		assIds.clear();
		assNames.clear();
		final ObservableList<String> oList = FXCollections.observableArrayList();
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
										if (association.getInt("shareCount") > 0) {
											addAssociationToList(oList,
													association.getString("name") + " (" + association.getInt("shareCount")
															+ " shares for sale)", association.getString("id"),
													association.getString("name"));
										}
									}
									associationList.setItems(oList);
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

	private void addAssociationToList(ObservableList<String> list, String text, String assId, String assName) {
		list.add(text);
		assIds.add(assId);
		assNames.add(assName);
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
									account.setBalance((float) user.getDouble("money"));
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
									account.setBalance((float) user.getDouble("money"));
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

	@FXML
	private void onBackPressed(ActionEvent e) {
		backButton.setVisible(false);
		shareArgs = null;
		populateAssociationList();
	}

}
