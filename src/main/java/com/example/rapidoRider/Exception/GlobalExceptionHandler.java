package com.example.rapidoRider.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.rapidoRider.DTO.ResponseStructure;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(RiderNotFoundException.class)
	public ResponseStructure<String> riderHandle() {
		ResponseStructure<String> rs = new ResponseStructure<String>();
		rs.setStatuscode(HttpStatus.NOT_FOUND.value());
		rs.setMessage("Rider Not Found");
		rs.setData(null);
		return rs;
	}
	
	@ExceptionHandler(VehicleNotFoundException.class)
	public ResponseStructure<String> vehicleHandle() {
		ResponseStructure<String> rs = new ResponseStructure<String>();
		rs.setStatuscode(HttpStatus.NOT_FOUND.value());
		rs.setMessage("VehicleNotFound");
		rs.setData(null);
		return rs;
	}
}
