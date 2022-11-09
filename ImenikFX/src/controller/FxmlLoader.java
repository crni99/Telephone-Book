package controller;

import java.net.URL;

import application.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

public class FxmlLoader {

	private Pane view;

	public Pane getPage(String fileName) {
		try {
			URL fileUrl = Main.class.getResource("/view/" + fileName + ".fxml");

			if (fileUrl == null) {
				throw new java.io.FileNotFoundException("FXML file can not be found!");
			} else {
				view = FXMLLoader.load(fileUrl);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return view;
	}
}
