package com.example.rapidoRider.DTO;

public class VehicleDto {
	private String type;
	private String name;
	private String vehicleno;
	private String model;
	public VehicleDto(String type, String name, String vehicleno, String model) {
		super();
		this.type = type;
		this.name = name;
		this.vehicleno = vehicleno;
		this.model = model;
	}
	public VehicleDto() {
		super();
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
