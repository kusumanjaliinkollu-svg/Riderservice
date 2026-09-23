package com.example.rapidoRider.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.rapidoRider.DTO.ResponseStructure;
import com.example.rapidoRider.DTO.RiderDto;
import com.example.rapidoRider.Entity.Rider;
import com.example.rapidoRider.Entity.Vehicle;
import com.example.rapidoRider.Exception.RiderNotFoundException;
import com.example.rapidoRider.Reposetory.RiderReposetory;
import com.example.rapidoRider.Reposetory.VehicleReposetory;

@Service
public class RiderService {
	@Autowired
	RiderReposetory rp;
	
	@Autowired
	VehicleReposetory vr;

	public ResponseStructure<RiderDto> saveRider(RiderDto rdt) {
		Vehicle v = new Vehicle();
		v.setName(rdt.getVehicle().getName());
		v.setModel(rdt.getVehicle().getModel());
		v.setType(rdt.getVehicle().getType());
		v.setVehicleno(rdt.getVehicle().getVehicleno());
		vr.save(v);
		Rider r = new Rider();
		r.setGender(rdt.getGender());
		r.setLicence(rdt.getLicence());
		r.setMail(rdt.getMail());
		r.setMobno(rdt.getMobno());
		r.setName(rdt.getName());
		r.setVehicle(v);
		ResponseStructure<RiderDto> rs = new ResponseStructure<RiderDto>();
		rs.setStatuscode(HttpStatus.CREATED.value());
		rs.setMessage("Rider Created Sucessfully");
		rs.setData(rdt);
		return rs;
	}

	public ResponseStructure<Rider> findRider(int id) {
		Rider r = rp.findById(id).orElseThrow(() -> new RiderNotFoundException());
		ResponseStructure<Rider> rs = new ResponseStructure<Rider>();
		rs.setStatuscode(HttpStatus.FOUND.value());
		rs.setMessage("Rider Found");
		rs.setData(r);
		return rs;
	}
	
	
}
