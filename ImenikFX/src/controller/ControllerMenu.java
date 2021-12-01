package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class ControllerMenu implements Initializable {
	
	private Functions function = new Functions();
	
	@FXML
	private BorderPane mainPane;
	
	
	// LOAD PANE/SCREEN DEPENDING ON THE SCREEN/BUTTON NAME
	private void loadPane(String buttonName) {
		FxmlLoader fxml = new FxmlLoader();
		Pane view = fxml.getPage(buttonName);
		mainPane.setCenter(view);
		function.fadeInFXML(view);
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		loadPane("HOME");
	}
	
	@FXML
	private void buttonHome(ActionEvent event) {
		loadPane("HOME");
	}
	
	@FXML
	private void buttonUser(ActionEvent event) {
		loadPane("USER");
	}
	
	@FXML
	private void buttonAdmin(ActionEvent event) {
		loadPane("LOGIN");
	}

	@FXML
	private void buttonAbout(ActionEvent event) {
		loadPane("ABOUT");
	}
	
}
