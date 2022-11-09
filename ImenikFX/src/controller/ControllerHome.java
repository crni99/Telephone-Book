package controller;

import java.sql.Connection;
import java.sql.SQLException;

import javafx.fxml.FXML;

public class ControllerHome {

	private Functions function = new Functions();
	private Connection conn;

	// CHECK CONNECTION TO DATABASE
	@FXML
	private void checkConnection()
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		try {
			function.connection();
			conn = function.getConn();

			if (conn == null) {
				function.message("Connection failure!");
			} else {
				function.message("Connection success!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
	}
}
