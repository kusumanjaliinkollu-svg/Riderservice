package com.example.rapidoRider.Entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class Rider {
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private int id;
	private String name;
	private long mobno;
	private String mail;
	private String gender;
	private double wallet;
	private int noofrides;
	private String licence;
	private String status="online";
	@OneToOne(cascade = CascadeType.ALL)
	private Vehicle vehicle;
	public Rider(String name, long mobno, String mail, String gender, double wallet, int noofrides, String licence,
			Vehicle vehicle,String status) {
		super();
		this.name = name;
		this.mobno = mobno;
		this.mail = mail;
		this.gender = gender;
		this.wallet = wallet;
		this.noofrides = noofrides;
		this.licence = licence;
		this.vehicle = vehicle;
		this.status=status;
	}
	public Rider() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getMobno() {
		return mobno;
	}
	public void setMobno(long mobno) {
		this.mobno = mobno;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public double getWallet() {
		return wallet;
	}
	public void setWallet(double wallet) {
		this.wallet = wallet;
	}
	public int getNoofrides() {
		return noofrides;
	}
	public void setNoofrides(int noofrides) {
		this.noofrides = noofrides;
	}
	public String getLicence() {
		return licence;
	}
	public void setLicence(String licence) {
		this.licence = licence;
	}
	public Vehicle getVehicle() {
		return vehicle;
	}
	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
