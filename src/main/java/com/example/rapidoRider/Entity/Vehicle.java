package com.example.rapidoRider.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Vehicle {
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private int id;
	private String type;
	private String name;
	private String vehicleno;
	private String model;
	public Vehicle(String type, String name, String vehicleno, String model) {
		super();
		this.type = type;
		this.name = name;
		this.vehicleno = vehicleno;
		this.model = model;
	}
	public Vehicle() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVehicleno() {
		return vehicleno;
	}
	public void setVehicleno(String vehicleno) {
		this.vehicleno = vehicleno;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	
}
