package com.example.rapidoRider.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.rapidoRider.DTO.BookingResponseDto;
import com.example.rapidoRider.DTO.ResponseStructure;
import com.example.rapidoRider.DTO.RiderDto;
import com.example.rapidoRider.DTO.RiderLocationDto;
import com.example.rapidoRider.DTO.VehicleDto;
import com.example.rapidoRider.Entity.Rider;
import com.example.rapidoRider.Services.RiderService;

import jakarta.servlet.http.HttpServletResponse;

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
	
	@DeleteMapping("rapido/rider/delete")
	public ResponseStructure<Rider> deleterider(@RequestParam int id) {
		return rs.delrider(id);
	}
	
	@PatchMapping("rapido/rider/updateveh")
	public ResponseStructure<Rider> updateveh(@RequestParam int id, @RequestBody VehicleDto vdto) {
		return rs.updatevehicle(id,vdto);
	}

	@PostMapping("/rider/sendcurrentlocation")
	public String sendcurrentloc(@RequestParam int rid,@RequestParam String vehc,@RequestBody RiderLocationDto rld){
		return rs.currentloc(rid,vehc,rld);
	}
	
	@PatchMapping("/rider/updatestatus")
	public ResponseStructure<String> updatestatus(@RequestParam int riderId) {
		return rs.updastatus(riderId);
	}
	
	@PostMapping("/rider/acceptride")
	public ResponseStructure<BookingResponseDto> acceptRide(@RequestParam int cid,@RequestParam String vehc,@RequestParam int riderId,@RequestParam int rideId) {
	    return rs.acceptRide(cid,vehc,riderId,rideId);
	}
	@GetMapping("/rider/getassignedrider")
	public ResponseStructure<Integer> getAssignedRider(@RequestParam int rideId) {
	    return rs.getAssignedRider(rideId);
	}
	
	@GetMapping("/rider/movetowardspickup")
    public void movetowardspickup(@RequestParam int bid,@RequestParam double longitude,@RequestParam double latitude,HttpServletResponse resp) {
	rs.movetopickup(bid,longitude,latitude,resp);
	}
	
	@GetMapping("/rider/movetowardsdrop")
    public void movetowardsdestination(@RequestParam int bid,@RequestParam double longitude,@RequestParam double latitude,HttpServletResponse resp) {
	rs.movetodestination(bid,longitude,latitude,resp);
	}
	
	@PatchMapping("/rider/updatearrival")
	public void updatearrival(@RequestParam int bid) {
		rs.updatearrivall(bid);
	}
	
	@PatchMapping("/rider/cancelride")
	public void cancleride(@RequestParam int bid) {
		rs.cancleride(bid);
	}
	
	@GetMapping("/rider/verifyotp")
	public ResponseStructure<String> verifyrideotp(@RequestParam String otp,@RequestParam int bid) {
		return rs.verifyrideotp(otp,bid);
	}
	
	@GetMapping("/rider/ridehistory")
	public List<Map<String, Object>> ridehistory(@RequestParam int riderId,@RequestParam String status) {
		return rs.ridehistoryy(riderId,status);
	}
	public Map<String, Object> summary(@RequestParam int rid,@RequestParam String Fromdate,@RequestParam  String Todate) {
		return rs.earnings(rid,Fromdate,Todate);
	}
}
