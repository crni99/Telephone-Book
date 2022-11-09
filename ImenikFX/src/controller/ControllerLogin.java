package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class ControllerLogin {

	private Functions function = new Functions();
	private Connection conn;

	@FXML
	private AnchorPane rootPane;

	@FXML
	private TextField txtLoginUsername, txtLoginShowPass;
	@FXML
	private PasswordField txtLoginPassword;
	@FXML
	private Button adminLogin;
	@FXML
	private CheckBox chkPass;

	// IF CREDENTIALS ARE CORRECT, DISPLAY THE ADMIN PANE
	@FXML
	private void adminLogin() throws SQLException {
		try {
			String username = txtLoginUsername.getText().trim();
			String password = txtLoginPassword.getText().trim();

			if (chkPass.isSelected()) {
				password = "";
				password = txtLoginShowPass.getText().trim();
			}

			if (username.isEmpty()) {
				function.message("Enter username!");
			} else if (password.isEmpty()) {
				function.message("Enter password!");
			} else {
				String pass = null;

				function.connection();
				conn = function.getConn();

				String queryUsername = "SELECT * FROM prijava WHERE username = ?";
				PreparedStatement stmt = conn.prepareStatement(queryUsername);
				stmt.setString(1, username);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					pass = rs.getString("password");
				}

				if (pass != null && pass.equals(password)) {

					txtLoginUsername.setText("");
					txtLoginPassword.setText("");
					txtLoginShowPass.setText("");

					AnchorPane paneAdmin = FXMLLoader.load(getClass().getResource("/view/Admin.fxml"));
					rootPane.getChildren().setAll(paneAdmin);
					function.fadeInFXML(paneAdmin);
				} else {
					function.message("Username or password are incorrect!");
					txtLoginUsername.setText("");
					txtLoginPassword.setText("");
					txtLoginShowPass.setText("");
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
	}

	// IF TURNED ON IT WILL SHOW A PASS FOR THE ADMIN LOGIN
	@FXML
	private void showPass() {
		try {
			if (chkPass.isSelected()) {
				String showPass = txtLoginPassword.getText().trim();
				txtLoginShowPass.setText(showPass);
				txtLoginShowPass.toFront();
				txtLoginPassword.toBack();
			} else if (!chkPass.isSelected()) {
				String showPass = txtLoginShowPass.getText().trim();
				txtLoginPassword.setText(showPass);
				txtLoginPassword.toFront();
				txtLoginShowPass.toBack();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
