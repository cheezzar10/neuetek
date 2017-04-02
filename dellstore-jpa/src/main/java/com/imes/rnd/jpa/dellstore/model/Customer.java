package com.imes.rnd.jpa.dellstore.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "customers")
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "customerid")
	private Integer id;
	
	@Column(name = "firstname", length = 50, nullable = false)
	private String firstName;
	
	@Column(name = "lastname", length = 50, nullable = false)
	private String lastName;
	
	@Column(length = 50, nullable = false)
	private String address1;
	
	@Column(length = 50)
	private String address2;
	
	@Column(length = 50, nullable = false)
	private String city;
	
	@Column(length = 50)
	private String state;
	
	private Integer zip;
	
	@Column(length = 50, nullable = false)
	private String country;
	
	@Column(nullable = false)
	private int region;
	
	@Column(length = 50)
	private String email;
	
	@Column(length = 50)
	private String phone;
	
	@Column(name = "creditcardtype", nullable = false)
	private int creditCardType;
	
	@Column(name = "creditcard", length = 50, nullable = false)
	private String creditCard;
	
	@Column(name = "creditcardexpiration", length = 50, nullable = false)
	private String creditCardExpiration;
	
	@Column(length = 50, nullable = false)
	private String username;
	
	@Column(length = 50, nullable = false)
	private String password;
	
	private Integer age;
	
	private Integer income;
	
	private Character gender;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Integer getZip() {
		return zip;
	}

	public void setZip(Integer zip) {
		this.zip = zip;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getRegion() {
		return region;
	}

	public void setRegion(int region) {
		this.region = region;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getCreditCardType() {
		return creditCardType;
	}

	public void setCreditCardType(int creditCardType) {
		this.creditCardType = creditCardType;
	}

	public String getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(String creditCard) {
		this.creditCard = creditCard;
	}

	public String getCreditCardExpiration() {
		return creditCardExpiration;
	}

	public void setCreditCardExpiration(String creditCardExpiration) {
		this.creditCardExpiration = creditCardExpiration;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Integer getIncome() {
		return income;
	}

	public void setIncome(Integer income) {
		this.income = income;
	}

	public Character getGender() {
		return gender;
	}

	public void setGender(Character gender) {
		this.gender = gender;
	}
}
