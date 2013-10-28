package internettech.client;

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
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}
	
	public void onLoginClick(ActionEvent e) {
		System.out.println("Login clicked");
		System.out.println(username.getText() + ", " + password.getText());
	}
	
	
	

}
