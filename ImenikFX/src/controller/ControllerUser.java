package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
	
	
	@FXML
	private void userSearch() {
		try {
			function.connection();
			conn = function.getConn();
			
			userList.removeAll(userList);
			
			String txtData = txtUser.getText().trim();
			String cmbData = cmbUser.getValue();
			
			if (txtData.isEmpty()) {
				function.message("The field cannot be empty!");
			}
			else if(cmbData.equals("NAME")) {
				
				String queryName = "SELECT osoba.id, adresa.id, telefon.id, povezuje.id, osoba.ime, osoba.prezime, "
						+ "adresa.mesto, telefon.telefon, povezuje.id_osoba, povezuje.id_adresa, povezuje.id_telefon FROM "
						+ "osoba, adresa, telefon, povezuje WHERE osoba.id=povezuje.id_osoba AND adresa.id=povezuje.id_adresa AND "
						+ "telefon.id=povezuje.id_telefon AND osoba.ime LIKE '%" + txtData + "%'";
				
				PreparedStatement stmt = conn.prepareStatement(queryName);
				ResultSet rs = stmt.executeQuery(queryName);
				
				while(rs.next()) {
					userList.add(new UserModelTable(rs.getString("ime"), rs.getString("prezime"), 
							rs.getString("mesto"), rs.getString("telefon")));
				}
				colUserName.setCellValueFactory(new PropertyValueFactory<>("name"));
				colUserSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
				colUserCity.setCellValueFactory(new PropertyValueFactory<>("city"));
				colUserPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
				
				tableUser.setItems(userList);
				stmt.close();
				
				if(userList.isEmpty()) {
					function.message("Search data does not exist!");
				}
			}
			
			else if(cmbData.equals("SURNAME")) {
			
				String querySurname = "SELECT osoba.id, adresa.id, telefon.id, povezuje.id, osoba.ime, osoba.prezime, "
					+ "adresa.mesto, telefon.telefon, povezuje.id_osoba, povezuje.id_adresa, povezuje.id_telefon FROM "
					+ "osoba, adresa, telefon, povezuje WHERE osoba.id=povezuje.id_osoba AND adresa.id=povezuje.id_adresa AND "
					+ "telefon.id=povezuje.id_telefon AND osoba.prezime LIKE '%" + txtData + "%'";
				
				PreparedStatement stmt = conn.prepareStatement(querySurname);
				ResultSet rs = stmt.executeQuery(querySurname);
				
				while(rs.next()) {
					userList.add(new UserModelTable(rs.getString("ime"), rs.getString("prezime"), 
							rs.getString("mesto"), rs.getString("telefon")));
				}
				colUserName.setCellValueFactory(new PropertyValueFactory<>("name"));
				colUserSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
				colUserCity.setCellValueFactory(new PropertyValueFactory<>("city"));
				colUserPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
				
				tableUser.setItems(userList);
				stmt.close();
				
				if(userList.isEmpty()) {
					function.message("Search data does not exist!");
				}
			}
			else if(cmbData.equals("CITY")) {
			
				String queryCity = "SELECT osoba.id, adresa.id, telefon.id, povezuje.id, osoba.ime, osoba.prezime, "
					+ "adresa.mesto, telefon.telefon, povezuje.id_osoba, povezuje.id_adresa, povezuje.id_telefon FROM "
					+ "osoba, adresa, telefon, povezuje WHERE osoba.id=povezuje.id_osoba AND adresa.id=povezuje.id_adresa AND "
					+ "telefon.id=povezuje.id_telefon AND adresa.mesto LIKE '%" + txtData + "%'";
				
				PreparedStatement stmt = conn.prepareStatement(queryCity);
				ResultSet rs = stmt.executeQuery(queryCity);
				
				while(rs.next()) {
					userList.add(new UserModelTable(rs.getString("ime"), rs.getString("prezime"), 
							rs.getString("mesto"), rs.getString("telefon")));
				}
				colUserName.setCellValueFactory(new PropertyValueFactory<>("name"));
				colUserSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
				colUserCity.setCellValueFactory(new PropertyValueFactory<>("city"));
				colUserPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
				
				tableUser.setItems(userList);
				stmt.close();
				
				if(userList.isEmpty()) {
					function.message("Search data does not exist!");
				}
			}
			else if(cmbData.equals("PHONE")) {
			
				String queryPhone = "SELECT osoba.id, adresa.id, telefon.id, povezuje.id, osoba.ime, osoba.prezime, "
					+ "adresa.mesto, telefon.telefon, povezuje.id_osoba, povezuje.id_adresa, povezuje.id_telefon FROM "
					+ "osoba, adresa, telefon, povezuje WHERE osoba.id=povezuje.id_osoba AND adresa.id=povezuje.id_adresa AND "
					+ "telefon.id=povezuje.id_telefon AND telefon.telefon LIKE '%" + txtData + "%'";
				
				PreparedStatement stmt = conn.prepareStatement(queryPhone);
				ResultSet rs = stmt.executeQuery(queryPhone);
				
				while(rs.next()) {
					userList.add(new UserModelTable(rs.getString("ime"), rs.getString("prezime"), 
							rs.getString("mesto"), rs.getString("telefon")));
				}
				colUserName.setCellValueFactory(new PropertyValueFactory<>("name"));
				colUserSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
				colUserCity.setCellValueFactory(new PropertyValueFactory<>("city"));
				colUserPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
				
				tableUser.setItems(userList);
				stmt.close();
				
				if(userList.isEmpty()) {
					function.message("Search data does not exist!");
				}
			}
			conn.close();

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
