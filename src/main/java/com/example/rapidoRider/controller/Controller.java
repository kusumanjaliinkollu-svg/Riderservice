package com.example.rapidoRider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.rapidoRider.DTO.ResponseStructure;
import com.example.rapidoRider.DTO.RiderDto;
import com.example.rapidoRider.Entity.Rider;
import com.example.rapidoRider.Services.RiderService;

@RestController
public class Controller {
	@Autowired
	RiderService rs;
	
	@PostMapping("rapido/rider/save")
	public ResponseStructure<RiderDto> saRider(@RequestBody RiderDto rdt){
		return rs.saveRider(rdt);
	}
	
	@GetMapping("rapido/rider/find")
	public ResponseStructure<Rider> findrid(@RequestParam int id){
		return rs.findRider(id);
	}
}
