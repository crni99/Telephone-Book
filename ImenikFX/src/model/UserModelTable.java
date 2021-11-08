package model;

public class UserModelTable {

	private String name, surname, city, phone;

	public UserModelTable(String name, String surname, String city, String phone) {
		super();
		this.name = name;
		this.surname = surname;
		this.city = city;
		this.phone = phone;
	}

	public UserModelTable() {
		super();
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
