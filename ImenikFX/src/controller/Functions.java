package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javafx.animation.FadeTransition;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class Functions {

	private Connection conn;
	private final String url = "jdbc:mysql://localhost:3306/";
	private final String dbName = "imenik_eng";
	private final String driver = "com.mysql.cj.jdbc.Driver";
	private final String userName = "ognjen";
	private final String password = "ognjen1999";

	private Alert alert = new Alert(AlertType.INFORMATION);
	private DialogPane dialogPane = alert.getDialogPane();

	private FadeTransition fadeTransition = new FadeTransition();

	// MAKE A CONNECTION TO THE DATABASE
	public void connection()
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		try {
			Object newInstance = Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(url + dbName, userName, password);

			if (conn == null) {
				message("CONNECTION FAILURE!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Connection getConn() {
		return conn;
	}

	// MESSAGE SHOW ON SCREEN IN NEW DIALOGUE
	public void message(String text) {
		dialogPane.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
		dialogPane.getStyleClass().add("message");
		dialogPane.setHeaderText(null);
		dialogPane.setGraphic(null);
		alert.setTitle(null);
		alert.setHeaderText(null);
		alert.setContentText(text);
		alert.showAndWait();
	}

	// WHEN SWITCHING PANE, USE FADE TRANSITION
	public void fadeInFXML(Pane pane) {
		fadeTransition.setDuration(Duration.millis(1200));
		fadeTransition.setNode(pane);
		fadeTransition.setFromValue(0);
		fadeTransition.setToValue(1);
		fadeTransition.play();
	}
}
