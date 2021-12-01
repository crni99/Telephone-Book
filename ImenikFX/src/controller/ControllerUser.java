package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.UserModelTable;

public class ControllerUser implements Initializable {

	private Functions function = new Functions();
	private Connection conn;
	
	@FXML
	private TextField txtUser;
	private ObservableList<String> cmbUserData = FXCollections.observableArrayList("NAME", "SURNAME", "CITY", "PHONE");
	@FXML
	private ComboBox<String>cmbUser = new ComboBox<String>();
	
	@FXML
	private TableView<UserModelTable> tableUser;
	@FXML
	private TableColumn<UserModelTable, String> colUserName;
	@FXML
	private TableColumn<UserModelTable, String> colUserSurname;
	@FXML
	private TableColumn<UserModelTable, String> colUserCity;
	@FXML
	private TableColumn<UserModelTable, String> colUserPhone;
	private ObservableList<UserModelTable> userList = FXCollections.observableArrayList();
	
	
	// THIS FUNC TAKE QUERY AND EXECUTE
	private void execQuery(String searchData, String query) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		try {
			function.connection();
			conn = function.getConn();
			
			userList.removeAll(userList);
			
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, "%" + searchData + "%");
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				userList.add(new UserModelTable(rs.getString("ime"), rs.getString("prezime"), 
						rs.getString("mesto"), rs.getString("telefon")));
			}
			colUserName.setCellValueFactory(new PropertyValueFactory<>("name"));
			colUserSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
			colUserCity.setCellValueFactory(new PropertyValueFactory<>("city"));
			colUserPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
			tableUser.setItems(userList);
			
			if(userList.isEmpty()) {
				function.message("Search data does not exist!");
			}
			stmt.close();
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	// TAKE DATA FROM COMBO BOX, THEN EXEC QUERY DEPENDING ON THE SELECTED VALUE
	@FXML
	private void userSearch() {
		try {
			
			String txtData = txtUser.getText().trim();
			String cmbData = cmbUser.getValue();
			
			if (txtData.isEmpty() || cmbData.isEmpty()) {
				function.message("The field is empty or value is not selected!");
			}
			else if(cmbData.equals("NAME")) {
				
				String queryName = "SELECT osoba.id, adresa.id, telefon.id, povezuje.id, osoba.ime, osoba.prezime, "
						+ "adresa.mesto, telefon.telefon, povezuje.id_osoba, povezuje.id_adresa, povezuje.id_telefon FROM "
						+ "osoba, adresa, telefon, povezuje WHERE osoba.id=povezuje.id_osoba AND adresa.id=povezuje.id_adresa AND "
						+ "telefon.id=povezuje.id_telefon AND osoba.ime LIKE ?";
				
				execQuery(txtData, queryName);
			}
			else if(cmbData.equals("SURNAME")) {
			
				String querySurname = "SELECT osoba.id, adresa.id, telefon.id, povezuje.id, osoba.ime, osoba.prezime, "
					+ "adresa.mesto, telefon.telefon, povezuje.id_osoba, povezuje.id_adresa, povezuje.id_telefon FROM "
					+ "osoba, adresa, telefon, povezuje WHERE osoba.id=povezuje.id_osoba AND adresa.id=povezuje.id_adresa AND "
					+ "telefon.id=povezuje.id_telefon AND osoba.prezime LIKE ?";
				
				execQuery(txtData, querySurname);
			}
			else if(cmbData.equals("CITY")) {
			
				String queryCity = "SELECT osoba.id, adresa.id, telefon.id, povezuje.id, osoba.ime, osoba.prezime, "
					+ "adresa.mesto, telefon.telefon, povezuje.id_osoba, povezuje.id_adresa, povezuje.id_telefon FROM "
					+ "osoba, adresa, telefon, povezuje WHERE osoba.id=povezuje.id_osoba AND adresa.id=povezuje.id_adresa AND "
					+ "telefon.id=povezuje.id_telefon AND adresa.mesto LIKE ?";
				
				execQuery(txtData, queryCity);
			}
			else if(cmbData.equals("PHONE")) {
			
				String queryPhone = "SELECT osoba.id, adresa.id, telefon.id, povezuje.id, osoba.ime, osoba.prezime, "
					+ "adresa.mesto, telefon.telefon, povezuje.id_osoba, povezuje.id_adresa, povezuje.id_telefon FROM "
					+ "osoba, adresa, telefon, povezuje WHERE osoba.id=povezuje.id_osoba AND adresa.id=povezuje.id_adresa AND "
					+ "telefon.id=povezuje.id_telefon AND telefon.telefon LIKE ?";
				
				execQuery(txtData, queryPhone);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	// POPULATING COMBOBOX USER
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		cmbUser.getItems().clear();
		cmbUser.setItems(cmbUserData);
		cmbUser.getSelectionModel().select(0);
	}

}
