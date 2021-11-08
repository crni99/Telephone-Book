package model;

public class AdminModelTable {

	private int id_osoba, id_povezuje, id_telefon;
	private String name, surname, city, phone;
	
	public AdminModelTable(int id_osoba, int id_povezuje, int id_telefon, String name, String surname, String city, String phone) {
		super();
		this.id_osoba = id_osoba;
		this.id_povezuje = id_povezuje;
		this.id_telefon = id_telefon;
		this.name = name;
		this.surname = surname;
		this.city = city;
		this.phone = phone;
	}

	public AdminModelTable() {
		super();
	}

	public int getId_osoba() {
		return id_osoba;
	}

	public void setId_osoba(int id_osoba) {
		this.id_osoba = id_osoba;
	}

	public int getId_povezuje() {
		return id_povezuje;
	}

	public void setId_povezuje(int id_povezuje) {
		this.id_povezuje = id_povezuje;
	}

	public int getId_telefon() {
		return id_telefon;
	}

	public void setId_telefon(int id_telefon) {
		this.id_telefon = id_telefon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
}
