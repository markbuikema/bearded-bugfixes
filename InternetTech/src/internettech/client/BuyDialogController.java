package internettech.client;

import internettech.json.JSONObject;
import internettech.model.UserAccount;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class BuyDialogController implements Initializable {

	@FXML
	private Label dialogTitle;

	@FXML
	private Label costText;

	@FXML
	private TextField amountInput;

	private String assId, assName, sellerId, sellerName;
	private int count;
	private double price;

	private UserAccount account;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		boolean associationIsOwner = sellerId.equals(assId);
		dialogTitle.setText("Purchase shares in " + assName + (associationIsOwner ? "" : " from " + sellerName));
		costText.setText("");
		amountInput.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String text) {
				try {
					int amount = Integer.valueOf(text);
					if (amount > count)
						costText.setText("Not available");
					else
						costText.setText(Double.toString(amount * price));

					if (amount * price > account.getBalance()) {
						costText.setText("Insufficient funds");
					}
				} catch (NumberFormatException e) {
					costText.setText("Not available");
				}
			}
		});
	}

	public BuyDialogController(JSONObject args, UserAccount account) {
		System.out.println("BUY ARGS: " + args.toString());
		assId = args.getString("associationId");
		assName = args.getString("associationName");
		sellerId = args.getString("ownerId");
		sellerName = args.getString("ownerName");
		count = args.getInt("maxCount");
		price = args.getDouble("price");

		this.account = account;
	}

	@FXML
	private void onConfirmClick(ActionEvent e) {

	}

	@FXML
	private void onCancelClick(ActionEvent e) {
		System.out.println("Cancel clicked");
		Stage stage = (Stage) dialogTitle.getScene().getWindow();
		stage.close();

	}
}
