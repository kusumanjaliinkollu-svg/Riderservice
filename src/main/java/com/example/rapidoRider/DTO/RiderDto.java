package com.example.rapidoRider.DTO;

import com.example.rapidoRider.Entity.Vehicle;

public class RiderDto {
	private String name;
	private long mobno;
	private String mail;
	private String gender;
	private String licence;
	private VehicleDto vehicle;
	public RiderDto(String name, long mobno, String mail, String gender, String licence, VehicleDto vehicle) {
		super();
		this.name = name;
		this.mobno = mobno;
		this.mail = mail;
		this.gender = gender;
		this.licence = licence;
		this.vehicle = vehicle;
	}
	public RiderDto() {
		super();
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
	public String getLicence() {
		return licence;
	}
	public void setLicence(String licence) {
		this.licence = licence;
	}
	public VehicleDto getVehicle() {
		return vehicle;
	}
	public void setVehicle(VehicleDto vehicle) {
		this.vehicle = vehicle;
	}
	
}
