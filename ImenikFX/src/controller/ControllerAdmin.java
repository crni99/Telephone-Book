package controller;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.pdfjet.A4;
import com.pdfjet.Cell;
import com.pdfjet.CoreFont;
import com.pdfjet.Font;
import com.pdfjet.PDF;
import com.pdfjet.Page;
import com.pdfjet.Table;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.Callback;
import model.AdminModelTable;

public class ControllerAdmin implements Initializable {

	private Functions function = new Functions();	
	private Connection conn;
	
	@FXML
	private TextField txtAdminID, txtAdminName, txtAdminSurname, txtAdminCity, txtAdminPhone;
	@FXML
	private RadioButton newCityRB, selectCityRB;
	@FXML
	private ToggleGroup groupAdminRB;
	@FXML
	private ComboBox<String> cmbAdminCity;
	@FXML
	private Button btnAdminLogOut, btnAdminAddNew, btnAdminAddExisting, btnAdminDeleteCity, btnClearFields, btnAdminSearch;
	@FXML
	private Button btnPdfExport, btnExcelExport;
	private FileChooser fileChooser = new FileChooser();
	
	@FXML
	private AnchorPane rootPane;
	@FXML
	private Button btnAdminFillTable;
	@FXML
	private TableView<AdminModelTable> tableAdmin;
	@FXML
	private TableColumn<AdminModelTable, Integer> colAdminID;
	@FXML
	private TableColumn<AdminModelTable, String> colAdminName;
	@FXML
	private TableColumn<AdminModelTable, String> colAdminSurname;
	@FXML
	private TableColumn<AdminModelTable, String> colAdminCity;
	@FXML
	private TableColumn<AdminModelTable, String> colAdminPhone;
	@FXML
	private TableColumn<AdminModelTable, Void> colAdminEdit;
	@FXML
	private TableColumn<AdminModelTable, Void> colAdminDelete;
	private ObservableList<AdminModelTable> adminList = FXCollections.observableArrayList();
	
	
	// LOG OUT FROM ADMIN PANE AND PUT IN FRONT HOME PANE
	@FXML
	private void logOut() {
		try {
			AnchorPane paneLogin = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
			rootPane.getChildren().setAll(paneLogin);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	// FILL COMBO BOX WITH CITIES FROM DATABASE
	@FXML
	private void fillAdminCMB() {
		try {
			
			cmbAdminCity.getItems().clear();
			
			function.connection();
			conn = function.getConn();
			
			String query = "SELECT * FROM adresa ORDER BY mesto";
			java.sql.Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				cmbAdminCity.getItems().addAll(rs.getString("mesto")); 
			}
			cmbAdminCity.getSelectionModel().select(0);
			stmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	// ADD NEW PERSON WITH NEW ID
	@FXML
	private void addNewContact() {
		try {
			String name = txtAdminName.getText().trim();
			String surname = txtAdminSurname.getText().trim();
			String phone = txtAdminPhone.getText().trim();
			
			if(name.isEmpty()) {
				function.message("Enter name!");
			}
			else if(surname.isEmpty()) {
				function.message("Enter surname!");
			}
			else if(phone.isEmpty()) {
				function.message("Enter phone!");
			}
			else {
				function.connection();
				conn = function.getConn();
				
				if (newCityRB.isSelected()) {
					String city = txtAdminCity.getText().trim();
					if (city.isEmpty()) {
						function.message("Enter city!");
					}
					else {
						addNewContactnewCity(name, surname, city, phone);
						fillAdminCMB();
					}
				}
				else if (selectCityRB.isSelected()) {
					addNewContactExistingCity(name, surname, phone);
				}
				else {
					function.message("Select new or existing city!");
				}
				function.message("Data seccessfully added to the database!");
				clearFields();
				adminList.removeAll(adminList);
				fillTable();
				conn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	// ADD NEW PERSON WITH NEW ID AND NEW CITY
	private void addNewContactnewCity(String name, String surname, String city, String phone) throws SQLException, NumberFormatException, Exception {
		try {	
				int id_person = 0;
				int id_address = 0;
				int id_phone = 0;
				
				String queryPerson = "INSERT INTO osoba VALUES (NULL, '"+name+"', '"+surname+"');";
				PreparedStatement stmt1 = conn.prepareStatement(queryPerson);
				stmt1.executeUpdate(queryPerson, java.sql.Statement.RETURN_GENERATED_KEYS);
				ResultSet rs1 = stmt1.getGeneratedKeys();
				if(rs1.next()){
					id_person=rs1.getInt(1);
				}
				stmt1.close();
				
				if (id_person > 0) {
					String queryAddress = "INSERT INTO adresa VALUES (NULL, '"+city+"');";
					PreparedStatement stmt2 = conn.prepareStatement(queryAddress);
					stmt2.executeUpdate(queryAddress, java.sql.Statement.RETURN_GENERATED_KEYS);
					ResultSet rs2 = stmt2.getGeneratedKeys();
					if(rs2.next()){
						id_address=rs2.getInt(1);
					}
					stmt2.close();
				}
				if (id_address > 0) {
					String queryPhone = "INSERT INTO telefon VALUES (NULL, '"+phone+"');";
					PreparedStatement stmt3 = conn.prepareStatement(queryPhone);
					stmt3.executeUpdate(queryPhone, java.sql.Statement.RETURN_GENERATED_KEYS);
					ResultSet rs3 = stmt3.getGeneratedKeys();
					if(rs3.next()){
						id_phone=rs3.getInt(1);
					}
					stmt3.close();
				}
				if (id_phone > 0) {
					String queryConnects = "INSERT INTO povezuje VALUES (NULL, '"+id_person+"', '"+id_address+"', '"+id_phone+"');";
					PreparedStatement stmt4 = conn.prepareStatement(queryConnects);
					stmt4.executeUpdate(queryConnects);
					stmt4.close();
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	// ADD NEW PERSON WITH NEW ID AND CHOOSED CITY FROM COMBO BOX
	private void addNewContactExistingCity(String name, String surname, String phone) throws SQLException, NumberFormatException, Exception {
		try {
			
			String existingCity = cmbAdminCity.getValue();
			
			int id_person = 0;
			int id_address = 0;
			int id_phone = 0;
			
			String queryAddress = "SELECT adresa.id FROM adresa WHERE mesto ='"+ existingCity +"'";
			PreparedStatement stmt1 = conn.prepareStatement(queryAddress);
			stmt1.execute(queryAddress);
			ResultSet rs1 = stmt1.getResultSet();
			if(rs1.next()) {
				id_address=rs1.getInt(1);
			}
			stmt1.close();
			
			if (id_address > 0) {
				String queryPerson = "INSERT INTO osoba VALUES (NULL, '"+name+"', '"+surname+"');";
				PreparedStatement stmt2 = conn.prepareStatement(queryPerson);
				stmt2.executeUpdate(queryPerson, java.sql.Statement.RETURN_GENERATED_KEYS);
				ResultSet rs2 = stmt2.getGeneratedKeys();
				if(rs2.next()){
					id_person=rs2.getInt(1);
				}
				stmt2.close();
			}
			
			if (id_person > 0) {
				String queryPhone = "INSERT INTO telefon VALUES (NULL, '"+phone+"');";
				PreparedStatement stmt3 = conn.prepareStatement(queryPhone);
				stmt3.executeUpdate(queryPhone, java.sql.Statement.RETURN_GENERATED_KEYS);
				ResultSet rs3 = stmt3.getGeneratedKeys();
				if(rs3.next()){
					id_phone=rs3.getInt(1);
				}
				stmt3.close();
			}
			
			if (id_phone > 0) {
				String queryConnects = "INSERT INTO povezuje VALUES (NULL, '"+id_person+"', '"+id_address+"', '"+id_phone+"');";
				PreparedStatement stmt4 = conn.prepareStatement(queryConnects);
				stmt4.executeUpdate(queryConnects);
				stmt4.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	// ADD PERSON WITH SAME ID FROM DATABASE
	@FXML
	private void addExistingContact() {
		try {
			String idPerson = txtAdminID.getText().trim();
			String phone = txtAdminPhone.getText().trim();

			if(idPerson.isEmpty()) {
				function.message("Enter id!");
			}
			else if(phone.isEmpty()) {
				function.message("Enter phone!"); 
			}
			else {
				function.connection();
				conn = function.getConn();
				
				int id = Integer.parseInt(idPerson);
				if (newCityRB.isSelected()) {
					String city = txtAdminCity.getText().trim();
					if (city.isEmpty()) {
						function.message("Enter phone!");
					}
					else {
						addExistingContactnewCity(id, city, phone);
						fillAdminCMB();
					}
				}
				else if (selectCityRB.isSelected()) {
					addExistingContactExistingCity(id, phone);
				}
				else {
					function.message("Select new or existing city!");
				}
				function.message("Data seccessfully added to the database!");
				clearFields();
				adminList.removeAll(adminList);
				fillTable();
				conn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	// ADD EXISTING WITH NEW CITY
	private void addExistingContactnewCity(int id, String city, String phone) throws SQLException, NumberFormatException, Exception {
		try {
			int id_person = 0;
			int id_address = 0;
			int id_phone = 0;
			
			String queryPerson = "SELECT osoba.id FROM osoba WHERE id ='"+ id +"'";
			PreparedStatement stmt1 = conn.prepareStatement(queryPerson);
			stmt1.execute(queryPerson);
			ResultSet rs1 = stmt1.getResultSet();
			if(rs1.next()) {
				id_person=rs1.getInt(1);
			}
			stmt1.close();
			
			if (id_person > 0) {
				String queryAddress = "INSERT INTO adresa VALUES (NULL, '"+city+"');";
				PreparedStatement stmt2 = conn.prepareStatement(queryAddress);
				stmt2.executeUpdate(queryAddress, java.sql.Statement.RETURN_GENERATED_KEYS);
				ResultSet rs2 = stmt2.getGeneratedKeys();
				if(rs2.next()){
					id_address=rs2.getInt(1);
				}
				stmt2.close();
			}
			
			if (id_address > 0) {
				String queryPhone = "INSERT INTO telefon VALUES (NULL, '"+phone+"');";
				PreparedStatement stmt3 = conn.prepareStatement(queryPhone);
				stmt3.executeUpdate(queryPhone, java.sql.Statement.RETURN_GENERATED_KEYS);
				ResultSet rs3 = stmt3.getGeneratedKeys();
				if(rs3.next()){
					id_phone=rs3.getInt(1);
				}
				stmt3.close();
			}
			
			if (id_phone > 0) {
				String queryConnects = "INSERT INTO povezuje VALUES (NULL, '"+id_person+"', '"+id_address+"', '"+id_phone+"');";
				PreparedStatement stmt4 = conn.prepareStatement(queryConnects);
				stmt4.executeUpdate(queryConnects);
				stmt4.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	// ADD EXISTING AND CHOOSED CITY FROM COMBO BOX
	private void addExistingContactExistingCity(int id, String phone) throws SQLException, NumberFormatException, Exception {
		try {
			String existingCity = cmbAdminCity.getValue();
			
			int id_person = 0;
			int id_address = 0;
			int id_phone = 0;
			
			String queryPerson = "SELECT osoba.id FROM osoba WHERE id ='"+ id +"'";
			PreparedStatement stmt1 = conn.prepareStatement(queryPerson);
			stmt1.execute(queryPerson);
			ResultSet rs1 = stmt1.getResultSet();
			if(rs1.next()) {
				id_person=rs1.getInt(1);
			}
			stmt1.close();
			
			if (id_person > 0) {
				String queryAddress = "SELECT adresa.id FROM adresa WHERE mesto ='"+ existingCity +"'";
				PreparedStatement stmt2 = conn.prepareStatement(queryAddress);
				stmt2.execute(queryAddress);
				ResultSet rs2 = stmt2.getResultSet();
				if(rs2.next()) {
					id_address=rs2.getInt(1);
				}
				stmt2.close();
			}
			
			if (id_address > 0) {
				String queryPhone = "INSERT INTO telefon VALUES (NULL, '"+phone+"');"; 
				PreparedStatement stmt3 = conn.prepareStatement(queryPhone);
				stmt3.executeUpdate(queryPhone, java.sql.Statement.RETURN_GENERATED_KEYS);
				ResultSet rs3 = stmt3.getGeneratedKeys();
				if(rs3.next()){
					id_phone=rs3.getInt(1);
				}
				stmt3.close();
			}
			
			if (id_phone > 0) {
				String queryConnects = "INSERT INTO povezuje VALUES (NULL, '"+id_person+"', '"+id_address+"', '"+id_phone+"');";
				PreparedStatement stmt4 = conn.prepareStatement(queryConnects);
				stmt4.executeUpdate(queryConnects);
				stmt4.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	// DELETES CITY IF THERE ARE NO PEOPLE IN CITY
	@FXML
	private void deleteCity() {
		try {
			
			int count = cmbAdminCity.getItems().size();
			
			if(count > 0) {
				String city = cmbAdminCity.getValue();
				
				if(city.isEmpty()) {
					function.message("Fill select city and select city to delete!");
				}
				else {
					function.connection();
					conn = function.getConn();
					
					int id_city = 0;
					int countCity = 0;
					
					String queryTakeIdCity = "SELECT id FROM adresa WHERE mesto ='"+ city +"'";
					PreparedStatement stmt1 = conn.prepareStatement(queryTakeIdCity);
					stmt1.execute(queryTakeIdCity);
					ResultSet rs1 = stmt1.getResultSet();
					if(rs1.next()) {
						id_city=rs1.getInt(1);
					}
					
					String queryCountCity = "SELECT COUNT(*) FROM povezuje WHERE id_adresa ='"+ id_city +"'";
					PreparedStatement stmt2 = conn.prepareStatement(queryCountCity);
					stmt2.execute(queryCountCity);
					ResultSet rs2 = stmt2.getResultSet();
					if(rs2.next()) {
						countCity=rs2.getInt(1);
					}
					stmt1.close();
					stmt2.close();
							
					if (countCity > 0) {
						function.message("THE PLACE IS STILL IN USE AND CANNOT BE DELETED!");
					}
					else {
						String queryDeleteCity = "DELETE FROM adresa WHERE id ='"+ id_city +"'";
						PreparedStatement stmt3 = conn.prepareStatement(queryDeleteCity);
						stmt3.execute();
						stmt3.close();
						function.message("City successfully deleted!");
						fillAdminCMB();
						fillTable();
					}
					conn.close();
				}
			}
			else {
				function.message("Fill select city and select city to delete!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	// FILLS COLUMNS IN ADMIN TABLE
	private void adminTableAppearance() {
		colAdminID.setCellValueFactory(new PropertyValueFactory<>("id_osoba"));
		colAdminName.setCellValueFactory(new PropertyValueFactory<>("name"));
		colAdminSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
		colAdminCity.setCellValueFactory(new PropertyValueFactory<>("city"));
		colAdminPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
		
		addEditBtn();
		addDeleteBtn();
		editableCols();
	}
	
	
	// CAN BE SEARCHED ONLY BY ONE TYPE AT SAME TIME WITH FIELDS ON TOP OF APP
	@FXML
	private void adminSearch() {
		try {
			adminList.removeAll(adminList);
			
			function.connection();
			conn = function.getConn();
			
			String name = txtAdminName.getText().trim();
			String surname = txtAdminSurname.getText().trim();
			String city = txtAdminCity.getText().trim();
			String phone = txtAdminPhone.getText().trim();
			
			if(name.isEmpty() && surname.isEmpty() && city.isEmpty() && phone.isEmpty()) {
				function.message("All fields are empty, fill one and search again!");
			}
			else if(!name.isEmpty() && surname.isEmpty() && city.isEmpty() && phone.isEmpty()) {
				
				String queryName = "SELECT osoba.id, adresa.id, telefon.id, povezuje.id, osoba.ime, osoba.prezime, "
						+ "adresa.mesto, telefon.telefon, povezuje.id_osoba, povezuje.id_adresa, povezuje.id_telefon FROM "
						+ "osoba, adresa, telefon, povezuje WHERE osoba.id=povezuje.id_osoba AND adresa.id=povezuje.id_adresa AND "
						+ "telefon.id=povezuje.id_telefon AND osoba.ime LIKE '%" + name + "%'";
				
				PreparedStatement stmt = conn.prepareStatement(queryName);
				ResultSet rs = stmt.executeQuery(queryName);
				
				while(rs.next()) {
					adminList.add(new AdminModelTable(rs.getInt("osoba.id"), rs.getInt("povezuje.id"), rs.getInt("telefon.id"),
							rs.getString("ime"), rs.getString("prezime"), rs.getString("mesto"), rs.getString("telefon")));
				}
				tableAdmin.setItems(adminList);
				stmt.close();
			}
			else if (name.isEmpty() && !surname.isEmpty() && city.isEmpty() && phone.isEmpty()) {
				
				String querySurname = "SELECT osoba.id, adresa.id, telefon.id, povezuje.id, osoba.ime, osoba.prezime, "
						+ "adresa.mesto, telefon.telefon, povezuje.id_osoba, povezuje.id_adresa, povezuje.id_telefon FROM "
						+ "osoba, adresa, telefon, povezuje WHERE osoba.id=povezuje.id_osoba AND adresa.id=povezuje.id_adresa AND "
						+ "telefon.id=povezuje.id_telefon AND osoba.prezime LIKE '%" + surname + "%'";
				
				PreparedStatement stmt = conn.prepareStatement(querySurname);
				ResultSet rs = stmt.executeQuery(querySurname);
				
				while(rs.next()) {
					adminList.add(new AdminModelTable(rs.getInt("osoba.id"), rs.getInt("povezuje.id"), rs.getInt("telefon.id"),
							rs.getString("ime"), rs.getString("prezime"), rs.getString("mesto"), rs.getString("telefon")));
				}
				tableAdmin.setItems(adminList);
				stmt.close();
			}
			else if (name.isEmpty() && surname.isEmpty() && !city.isEmpty() && phone.isEmpty()) {
				
				String queryAddress = "SELECT osoba.id, adresa.id, telefon.id, povezuje.id, osoba.ime, osoba.prezime, "
						+ "adresa.mesto, telefon.telefon, povezuje.id_osoba, povezuje.id_adresa, povezuje.id_telefon FROM "
						+ "osoba, adresa, telefon, povezuje WHERE osoba.id=povezuje.id_osoba AND adresa.id=povezuje.id_adresa AND "
						+ "telefon.id=povezuje.id_telefon AND adresa.mesto LIKE '%" + city + "%'";
				
				PreparedStatement stmt = conn.prepareStatement(queryAddress);
				ResultSet rs = stmt.executeQuery(queryAddress);
				
				while(rs.next()) {
					adminList.add(new AdminModelTable(rs.getInt("osoba.id"), rs.getInt("povezuje.id"), rs.getInt("telefon.id"),
							rs.getString("ime"), rs.getString("prezime"), rs.getString("mesto"), rs.getString("telefon")));
				}
				tableAdmin.setItems(adminList);
				stmt.close();
			}
			else if (name.isEmpty() && surname.isEmpty() && city.isEmpty() && !phone.isEmpty()) {
				
				String queryPhone = "SELECT osoba.id, adresa.id, telefon.id, povezuje.id, osoba.ime, osoba.prezime, "
						+ "adresa.mesto, telefon.telefon, povezuje.id_osoba, povezuje.id_adresa, povezuje.id_telefon FROM "
						+ "osoba, adresa, telefon, povezuje WHERE osoba.id=povezuje.id_osoba AND adresa.id=povezuje.id_adresa AND "
						+ "telefon.id=povezuje.id_telefon AND telefon.telefon LIKE '%" + phone + "%'";
				
				PreparedStatement stmt = conn.prepareStatement(queryPhone);
				ResultSet rs = stmt.executeQuery(queryPhone);
				
				while(rs.next()) {
					adminList.add(new AdminModelTable(rs.getInt("osoba.id"), rs.getInt("povezuje.id"), rs.getInt("telefon.id"),
							rs.getString("ime"), rs.getString("prezime"), rs.getString("mesto"), rs.getString("telefon")));
				}
				tableAdmin.setItems(adminList);
				stmt.close();
			}
			adminTableAppearance();
			conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	// FILL TABLE WITH DATA FROM DATABASE
	@FXML
	private void fillTable() {
		try {
			adminList.removeAll(adminList);
			
			function.connection();
			conn = function.getConn();
			
			String queryFillTableAdmin = "SELECT osoba.id, adresa.id, telefon.id, povezuje.id, osoba.ime, osoba.prezime, "
					+ "adresa.mesto, telefon.telefon, povezuje.id_osoba, povezuje.id_adresa, povezuje.id_telefon FROM "
					+ "osoba, adresa, telefon, povezuje WHERE osoba.id=povezuje.id_osoba AND adresa.id=povezuje.id_adresa AND "
					+ "telefon.id=povezuje.id_telefon ORDER BY osoba.id";
			
			PreparedStatement stmt = conn.prepareStatement(queryFillTableAdmin);
			ResultSet rs = stmt.executeQuery(queryFillTableAdmin);
			
			while(rs.next()) {
				adminList.add(new AdminModelTable(rs.getInt("osoba.id"), rs.getInt("povezuje.id"), rs.getInt("telefon.id"),
						rs.getString("ime"), rs.getString("prezime"), rs.getString("mesto"), rs.getString("telefon")));
			}
			adminTableAppearance();
			tableAdmin.setItems(adminList);
			stmt.close();
			conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	// ADD EDIT BUTTON, TO EVERY ROW THAT HAVE DATA
	private void addEditBtn() {

        Callback<TableColumn<AdminModelTable, Void>, TableCell<AdminModelTable, Void>> cellFactory = 
        									new Callback<TableColumn<AdminModelTable, Void>, TableCell<AdminModelTable, Void>>() {
            @Override
            public TableCell<AdminModelTable, Void> call(final TableColumn<AdminModelTable, Void> param) {
                final TableCell<AdminModelTable, Void> cell = new TableCell<AdminModelTable, Void>() {
                		private final Button tableAdminEdit = new Button("EDIT");
                    {
                    	tableAdminEdit.getStyleClass().add("tableControlBtns");
                    	tableAdminEdit.setOnAction((ActionEvent event) -> {
                        AdminModelTable a = getTableView().getItems().get(getIndex());
                            
                            if (a.getId_osoba() == 0 || a.getId_telefon() == 0 || a.getId_povezuje() == 0
                            		|| a.getName().isEmpty() || a.getSurname().isEmpty() || a.getCity().isEmpty() || a.getPhone().isEmpty()) {
                            	function.message("COLUMNS MUST NOT BE EMPTY!");
							}
                            else {
                            	try {
                            		function.connection();
                        			conn = function.getConn();
                            		
                            		int tableIdPerson = a.getId_osoba();
                            		int tableIdPhone = a.getId_telefon();
                            		String tableName = a.getName();
                            		String tableSurname = a.getSurname();
                            		String tablePhone = a.getPhone();
                      
                            		String dataName = null;
                            		String dataSurname = null;
                            		String dataPhone = null;
                            		
                            		String queryFindNameSurname = "SELECT * FROM osoba WHERE id ='"+ tableIdPerson +"'";
                            		PreparedStatement stmtID = conn.prepareStatement(queryFindNameSurname);
                            		stmtID.execute(queryFindNameSurname);
                            		ResultSet rsID = stmtID.getResultSet();
                					if(rsID.next()) {
                						dataName=rsID.getString(2);
                						dataSurname=rsID.getString(3);
                					}
                					
                					String queryFindPhone = "SELECT * FROM telefon WHERE id ='"+ tableIdPhone +"'";
                					PreparedStatement stmtFindPhone = conn.prepareStatement(queryFindPhone);
                					stmtFindPhone.execute(queryFindPhone);
                            		ResultSet rsPhone = stmtFindPhone.getResultSet();
                					if(rsPhone.next()) {
                						dataPhone=rsPhone.getString(2);
                					}
                            		
                					String querySurname = "UPDATE osoba SET prezime = ? WHERE id ='"+ tableIdPerson +"'";
                					PreparedStatement stmtSurname = conn.prepareStatement(querySurname);
                					stmtSurname.setString(1, tableSurname);	
                					
                					String queryName = "UPDATE osoba SET ime = ? WHERE id ='"+ tableIdPerson +"'";
                					PreparedStatement stmtName = conn.prepareStatement(queryName);
                					stmtName.setString(1, tableName);
               					
                					String queryNameSurname = "UPDATE osoba SET ime = ? ,prezime = ? WHERE id ='"+ tableIdPerson +"'";
                					PreparedStatement stmtNameSurname = conn.prepareStatement(queryNameSurname);
                					stmtNameSurname.setString(1, tableName);
                					stmtNameSurname.setString(2, tableSurname);
                					
                					String queryPhone = "UPDATE telefon SET telefon = ? WHERE id ='"+ tableIdPhone +"'";
                					PreparedStatement stmtPhone = conn.prepareStatement(queryPhone);
                					stmtPhone.setString(1, tablePhone);
                					
                					if (tableSurname.equals(dataSurname) && tableName.equals(dataName) && tablePhone.equals(dataPhone)) {
                						function.message("CHANGE AT LEAST ONE INFORAMTION!");
									}
                					else if (!tableSurname.equals(dataSurname) && !tableName.equals(dataName) && !tablePhone.equals(dataPhone)) {
                						stmtNameSurname.execute();
                						stmtPhone.execute();
                						function.message("DATA SUCCESSFULLY CHANGED!");
									}
                					else if (!tableSurname.equals(dataSurname) && tableName.equals(dataName) && !tablePhone.equals(dataPhone)) {
										stmtSurname.execute();
										stmtPhone.execute();
										function.message("SURNAME AND PHONE SUCCESSFULLY CHANGED!");
									}
                					else if (tableSurname.equals(dataSurname) && !tableName.equals(dataName) && !tablePhone.equals(dataPhone)) {
										stmtName.execute();
										stmtPhone.execute();
										function.message("NAME AND PHONE SUCCESSFULLY CHANGED!");
									}
                					else if (!tableSurname.equals(dataSurname) && !tableName.equals(dataName) && tablePhone.equals(dataPhone)) {
										stmtNameSurname.execute();
										function.message("NAME AND SURANME SUCCESSFULLY CHANGED!");
									}
                					else if (tableSurname.equals(dataSurname) && !tableName.equals(dataName) && tablePhone.equals(dataPhone)) {
										stmtName.execute();
										function.message("NAME SUCCESSFULLY CHANGED!");
									}
                					else if (!tableSurname.equals(dataSurname) && tableName.equals(dataName) && tablePhone.equals(dataPhone)) {
										stmtSurname.execute();
										function.message("SURNAME SUCCESSFULLY CHANGED!");
									}
                					else if (tableSurname.equals(dataSurname) && tableName.equals(dataName) && !tablePhone.equals(dataPhone)) {
										stmtPhone.execute();
										function.message("PHONE SUCCESSFULLY CHANGED!");
									}
                					
                					stmtSurname.close();
                					stmtName.close();
                					stmtNameSurname.close();
                					stmtPhone.close();
                					fillTable();
                					conn.close();
                					
								} catch (Exception e) {
									e.printStackTrace();
								}
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(tableAdminEdit);
                        }
                    }
                };
                return cell;
            }
        };
        colAdminEdit.setCellFactory(cellFactory);
	}
	
	
	// ADD DELETE BUTTON, TO EVERY ROW THAT HAVE DATA
	private void addDeleteBtn() {
		Callback<TableColumn<AdminModelTable, Void>, TableCell<AdminModelTable, Void>> cellFactory = 
				new Callback<TableColumn<AdminModelTable, Void>, TableCell<AdminModelTable, Void>>() {
		@Override
		public TableCell<AdminModelTable, Void> call(final TableColumn<AdminModelTable, Void> param) {
		final TableCell<AdminModelTable, Void> cell = new TableCell<AdminModelTable, Void>() {
			private final Button tableAdminDelete = new Button("DELETE");
            {
            	tableAdminDelete.getStyleClass().add("tableControlBtns");
            	tableAdminDelete.setOnAction((ActionEvent event) -> {
                AdminModelTable a = getTableView().getItems().get(getIndex());
                    
                	try {
						int id_3 = a.getId_povezuje();
						String city = a.getCity();
						
						if(id_3 == 0) {
							function.message("ID 3 MISSING!");
						}
						else {
							
							function.connection();
							conn = function.getConn();
							
							int id_person = 0;
							int count = 0;
							int id_phone = 0;
							
							String queryPersons = "SELECT id_osoba FROM povezuje WHERE id ='"+ id_3 +"'";
							PreparedStatement stmt4 = conn.prepareStatement(queryPersons);
							stmt4.execute(queryPersons);
							ResultSet rs4 = stmt4.getResultSet();
							if(rs4.next()) {
								id_person=rs4.getInt(1);
							}
							
							String queryCountPersons = "SELECT COUNT(*) FROM povezuje WHERE id_osoba ='"+ id_person +"'";
							PreparedStatement stmt5 = conn.prepareStatement(queryCountPersons);
							stmt5.execute(queryCountPersons);
							ResultSet rs5 = stmt5.getResultSet();
							if(rs5.next()) {
								count=rs5.getInt(1);
							}
							
							String queryFindPhone = "SELECT id_telefon FROM povezuje WHERE id ='"+ id_3 +"'";
							PreparedStatement stmt6 = conn.prepareStatement(queryFindPhone);
							stmt6.execute(queryFindPhone);
							ResultSet rs6 = stmt6.getResultSet();
							if(rs6.next()) {
								id_phone=rs6.getInt(1);
							}
							
							String queryConnects = "DELETE FROM povezuje WHERE id ='"+ id_3 +"'";
							PreparedStatement stmt1 = conn.prepareStatement(queryConnects);
							
							String queryPhone = "DELETE FROM telefon WHERE id ='"+ id_phone +"'";
							PreparedStatement stmt2 = conn.prepareStatement(queryPhone);
							
							String queryPerson = "DELETE FROM osoba WHERE id ='"+ id_person +"'";
							PreparedStatement stmt3 = conn.prepareStatement(queryPerson);
							
							if (count == 1) {
								stmt2.execute();
								stmt3.execute();
								stmt1.execute();
							}
							else {
								stmt2.execute();
								stmt1.execute();
							}
							stmt1.close();
							stmt2.close();
							stmt3.close();
							stmt4.close();
							stmt5.close();
							
							function.message("PERSON SUCCESSFULLY DELETED FROM CITY: " + city);
							clearFields();
							fillTable();
							conn.close();
						}
				} catch (Exception e) {
					e.printStackTrace();
                    }
                });
            }

            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(tableAdminDelete);
                }
            }
		};
		return cell;
		}
		};
		colAdminDelete.setCellFactory(cellFactory);
	}
	
	
	// NAME, SURNAME AND PHONE CAN BE EDITED DIRECTLY IN TABLE AND WHEN PRESS EDIT BUTTON, EDIT DATA IN DATABASE
	private void editableCols() {
		colAdminName.setCellFactory(TextFieldTableCell.forTableColumn());
		colAdminName.setOnEditCommit(e -> {
			e.getTableView().getItems().get(e.getTablePosition().getRow()).setName(e.getNewValue());
		});
		
		colAdminSurname.setCellFactory(TextFieldTableCell.forTableColumn());
		colAdminSurname.setOnEditCommit(e -> {
			e.getTableView().getItems().get(e.getTablePosition().getRow()).setSurname(e.getNewValue());
		});
		
		colAdminPhone.setCellFactory(TextFieldTableCell.forTableColumn());
		colAdminPhone.setOnEditCommit(e -> {
			e.getTableView().getItems().get(e.getTablePosition().getRow()).setPhone(e.getNewValue());
		});
		
		tableAdmin.setEditable(true);
	}
	

	// EXPORT TABLE VIEW TO PDF
	@FXML
	private void exportPDF() {
		try {
			File file = new File("");
			Window stage = btnPdfExport.getScene().getWindow();
			fileChooser.setTitle("Save PDF File");
			file = fileChooser.showSaveDialog(stage);
			
			if (file != null) {	
				fileChooser.setInitialDirectory(file.getParentFile());
				FileOutputStream fos = new FileOutputStream(file);
				PDF pdf = new PDF(fos);
				Page page = new Page(pdf, A4.PORTRAIT);
				
				Font f1 = new Font(pdf, CoreFont.HELVETICA_BOLD);
				Font f2 = new Font(pdf, CoreFont.HELVETICA);
				
				Table tablePDF = new Table();
				List<List<Cell>> tableData = new ArrayList<List<Cell>>();
				List<Cell> tableRow = new ArrayList<Cell>();
			
				Cell cell = new Cell(f1, colAdminID.getText());
				tableRow.add(cell);
				
				cell = new Cell(f1, colAdminName.getText());
				tableRow.add(cell);
				
				cell = new Cell(f1, colAdminSurname.getText());
				tableRow.add(cell);
				
				cell = new Cell(f1, colAdminCity.getText());
				tableRow.add(cell);
				
				cell = new Cell(f1, colAdminPhone.getText());
				tableRow.add(cell);
				
				tableData.add(tableRow);
				
				List<AdminModelTable> items = tableAdmin.getItems();
				
				for(AdminModelTable a : items) {
					Cell id = new Cell(f2, String.valueOf(a.getId_osoba()));
					Cell name = new Cell(f2, a.getName());
					Cell surname = new Cell(f2, a.getSurname());
					Cell city = new Cell(f2, a.getCity());
					Cell phone = new Cell(f2, a.getPhone());
					
					tableRow = new ArrayList<Cell>();
					tableRow.add(id);
					tableRow.add(name);
					tableRow.add(surname);
					tableRow.add(city);
					tableRow.add(phone);
					tableData.add(tableRow);
				}
				
				if (tableData.size() <= 1) {
					function.message("Table is empty!");
				}
				else {
					tablePDF.setData(tableData);
					tablePDF.setPosition(20f, 60f);
					tablePDF.setColumnWidth(0, 60f);	// ID
					tablePDF.setColumnWidth(1, 120f);	// NAME
					tablePDF.setColumnWidth(2, 140f);	// SURNAME
					tablePDF.setColumnWidth(3, 120f);	// CITY
					tablePDF.setColumnWidth(4, 120f);	// PHONE
					
					while (true) {
						tablePDF.drawOn(page);
						if (!tablePDF.hasMoreData()) {
							tablePDF.resetRenderedPagesCount();
							break;
						}
						page = new Page(pdf, A4.PORTRAIT);
					}
					function.message("Saved to : " + file.getAbsolutePath());
				}
				
				pdf.close();
				fos.close();	
			}
			else {
				function.message("File is not saved!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	// EXPORT TABLE VIEW TO EXCEL
	@FXML
	private void exportExcel() {
		try {
			 
			XSSFWorkbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet("ImenikFxTable");
			
			String[] columnHeadings = {"ID", "NAME", "SURNAME", "CITY", "PHONE"};
			org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setFontHeightInPoints((short) 12);
			headerFont.setColor(IndexedColors.BLACK.index);
			
			CellStyle headerStyle = workbook.createCellStyle();
			headerStyle.setFont(headerFont);
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
			
			Row headerRow = sheet.createRow(0);
			
			for (int i = 0; i < columnHeadings.length; i++) {
				org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
				cell.setCellValue(columnHeadings[i]);
				cell.setCellStyle(headerStyle);
			}
			
			int rownum = 1;
			List<AdminModelTable> items = tableAdmin.getItems();
			for(AdminModelTable a : items) {
				Row row = sheet.createRow(rownum++);
				row.setHeight((short) 350);
				
				row.createCell(0).setCellValue(String.valueOf(a.getId_osoba()));
				row.createCell(1).setCellValue(a.getName());
				row.createCell(2).setCellValue(a.getSurname());
				row.createCell(3).setCellValue(a.getCity());
				row.createCell(4).setCellValue(a.getPhone());
			}
			
			//AUTOSIZE COLUMNS
			for(int i = 0; i < columnHeadings.length; i++) {
				sheet.autoSizeColumn(i+1);
			}
			
			if (rownum <= 1) {
				function.message("Table is empty!");
			}
			else {
				File file = new File("");
				Window stage = btnPdfExport.getScene().getWindow();
				fileChooser.setTitle("Save EXCEL File");
				file = fileChooser.showSaveDialog(stage);
				
				if (file != null) {
					fileChooser.setInitialDirectory(file.getParentFile());
					FileOutputStream fos = new FileOutputStream(file);
					workbook.write(fos);
					fos.close();
					function.message("Saved to : " + file.getAbsolutePath());
	              }
				else {
					function.message("File is not saved!");
				}
			}
			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	// CLEAR FIELDS AND TABLE
	@FXML
	private void clearFields() {
		txtAdminID.setText("");
		txtAdminName.setText("");
		txtAdminSurname.setText("");
		txtAdminCity.setText("");
		txtAdminPhone.setText("");
	}


	@Override
	public void initialize(URL url, ResourceBundle rb) {
		fillAdminCMB();
		fileChooser.setInitialFileName("PhoneBookFX");
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel Workbook", "*.xlsx"), new FileChooser.ExtensionFilter("PDF", "*.pdf"));
	}

	
}
