package controller;

import javafx.fxml.FXML;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class ControllerAbout {

	private final Clipboard clipboard = Clipboard.getSystemClipboard();
	private final ClipboardContent content = new ClipboardContent();

	@FXML
	private void emailCopy() {
		copyToClipboard("andjelicb.ognjen@gmail.com");
	}

	@FXML
	private void githubCopy() {
		copyToClipboard("https://github.com/crni99");
	}

	private void copyToClipboard(String textToCopy) {
		content.putString(textToCopy);
		clipboard.setContent(content);
	}
}
