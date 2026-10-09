package com.example.rapidoRider.Services;

import java.io.IOException;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.data.geo.Point;

import com.example.rapidoRider.DTO.BookingResponseDto;
import com.example.rapidoRider.DTO.ResponseStructure;
import com.example.rapidoRider.DTO.RiderDto;
import com.example.rapidoRider.DTO.RiderLocationDto;
import com.example.rapidoRider.DTO.VehicleDto;
import com.example.rapidoRider.Entity.Rider;
import com.example.rapidoRider.Entity.Vehicle;
import com.example.rapidoRider.Exception.RiderNotFoundException;
import com.example.rapidoRider.Reposetory.RiderReposetory;
import com.example.rapidoRider.Reposetory.VehicleReposetory;


import jakarta.servlet.http.HttpServletResponse;

import org.springframework.data.redis.core.RedisTemplate;
@Service
public class RiderService {
	@Autowired
	RiderReposetory rp;

	@Autowired
	VehicleReposetory vr;

	@Autowired
	RestTemplate rtemplate;

	@Autowired
	@Qualifier("getRedisTemplate")
	private RedisTemplate<String, String> redistemplate;
	
	
	@Autowired
	@Qualifier("rideRedisTemplate")
	private RedisTemplate<String, Map<String,Object>> rideRedisTemplate;
	
	@Autowired
	@Qualifier("lockRedisTemplate")
	private RedisTemplate<String, String> lockRedisTemplate;
	
	
	@Autowired
	@Qualifier("geoRedisTemplate")
	private RedisTemplate<String, String> geoRedisTemplate;
	
	
	@Autowired
	@Qualifier("bookingRedisTemplate")
	private RedisTemplate<String, String> bookingRedisTemplate;

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
		rp.save(r);
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

	public ResponseStructure<Rider> delrider(int id) {
		Rider r = rp.findById(id).orElseThrow(() -> new RiderNotFoundException());
		rp.delete(r);
		ResponseStructure<Rider> rs = new ResponseStructure<Rider>();
		rs.setStatuscode(HttpStatus.FOUND.value());
		rs.setMessage("Rider Found");
		rs.setData(r);
		return rs;

	}

	public ResponseStructure<Rider> updatevehicle(int id, VehicleDto vdto) {
		Rider r = rp.findById(id).orElseThrow(() -> new RiderNotFoundException());
		r.getVehicle().setName(vdto.getName());
		r.getVehicle().setModel(vdto.getModel());
		r.getVehicle().setType(vdto.getType());
		r.getVehicle().setVehicleno(vdto.getVehicleno());
		rp.save(r);
		ResponseStructure<Rider> rs = new ResponseStructure<Rider>();
		rs.setStatuscode(HttpStatus.CREATED.value());
		rs.setMessage("Rider Created Sucessfully");
		rs.setData(r);
		return rs;
	}

	public String currentloc(int rid, String vehc, RiderLocationDto rld) {
		String riderid = vehc + ":" + rid;
		double lati = rld.getLatitude();
		double longi = rld.getLongitude();
		Point p = new Point(longi, lati);
		redistemplate.opsForGeo().add(vehc, p, riderid);
		return "Done";
	}

	public ResponseStructure<BookingResponseDto> acceptRide(int cid, String vehc,  int riderId, int rideId) {

	    // Create Redis lock key
	    String lockKey = "ride:" + rideId + ":lock";

	    // Try to lock the ride
	    Boolean locked = lockRedisTemplate.opsForValue().setIfAbsent(lockKey,String.valueOf(riderId));

	    // If another rider already locked it
	    if (Boolean.FALSE.equals(locked)) {
	    	ResponseStructure<BookingResponseDto> rs = new ResponseStructure<>();
            rs.setStatuscode(HttpStatus.CONFLICT.value());
            rs.setMessage(  "Ride Already Accepted By Another Rider");
            rs.setData(null);
	        return rs;
	    }


	    // Call Customer Service
	    String url = "http://localhost:8081/customer/confirmride?cid="+ cid+ "&vehc=" + vehc+ "&riderId=" + riderId + "&rideId=" + rideId;
	    ResponseEntity<ResponseStructure> response = rtemplate.postForEntity(url,null,ResponseStructure.class);


	    // Get ride data from Redis
	    Map<String, Object> rideData =rideRedisTemplate.opsForValue().get("ride:" + rideId);
	    String distance = String.valueOf(rideData.get("distance"));
	           
	    // Get Customer Service response
	    ResponseStructure responseBody =response.getBody();
	    Object data = responseBody.getData();
	    // Convert data into Map
	    Map<String, Object> bookingData = (Map<String, Object>) data;
	    // Get booking details
	    int bookingId = ((Number) bookingData .get("bookingId")).intValue();
	    String fare =String.valueOf(bookingData.get("fare"));
        String customerName = String.valueOf( bookingData.get("customerName"));
        String customerMobileNumber = String.valueOf(bookingData.get("customerMobileNumber"));
	    // Change rider status
	    Rider rider =rp.findById(riderId) .orElseThrow(   () -> new RuntimeException(  "Rider Not Found"));
	    rider.setStatus("Bussy");
         rp.save(rider);
         
	    // Remove rider from Redis GEO
	    geoRedisTemplate.opsForGeo() .remove(vehc, vehc + ":" + riderId);
	    // Create response DTO
	    BookingResponseDto dto =  new BookingResponseDto();
	    dto.setBookingId(bookingId);
	    dto.setFare(fare);
	    dto.setDistance(distance);
	    dto.setCustomerName(customerName);
	    dto.setCustomerMobileNumber(customerMobileNumber);
	   // Final response
	    ResponseStructure<BookingResponseDto> rs =  new ResponseStructure<>();
        rs.setStatuscode( HttpStatus.ACCEPTED.value());
        rs.setMessage("Ride Accepted Successfully");
	    rs.setData(dto);
	    return rs;
	}
	
	
	
	
	public ResponseStructure<Integer> getAssignedRider(int rideId) {

	    String rideKey = "ride:" + rideId;

	    Map<String,Object> rideData =rideRedisTemplate.opsForValue().get(rideKey);

	    ResponseStructure<Integer> rs = new ResponseStructure<Integer>();

	    if(rideData == null) {
	        rs.setStatuscode(HttpStatus.NOT_FOUND.value());
	        rs.setMessage("Ride Not Found");
	        rs.setData(null);
	        return rs;
	    }

	    String status = String.valueOf(rideData.get("status"));

	    if(!status.equals("ACCEPTED")) {
	        rs.setStatuscode(HttpStatus.NOT_FOUND.value());
	        rs.setMessage("Rider Not Assigned");
	        rs.setData(null);
	        return rs;
	    }

	    Integer riderId =Integer.parseInt( String.valueOf(rideData.get("riderId")));

	    rs.setStatuscode(HttpStatus.OK.value());
	    rs.setMessage("Rider Assigned");
	    rs.setData(riderId);

	    return rs;
	}

	public void movetopickup(int bid,double longi,double lati,HttpServletResponse resp) {
	String url ="http://localhost:8081/customer/booking/location?bookingId="+ bid;
	ResponseEntity<ResponseStructure> response = rtemplate.getForEntity(url, ResponseStructure.class);
    Map<String, Object> data = (Map<String, Object>) response.getBody().getData();
    double pickupLatitude = ((Number) data.get("pickupLatitude")).doubleValue();
    double pickupLongitude = ((Number) data.get("pickupLongitude")).doubleValue();
	String gurl="https://www.google.com/maps/dir/?api=1&destination=" +  pickupLatitude + "," + pickupLongitude+ "&travelmode=driving";
		try {
			resp.sendRedirect(gurl);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void movetodestination( int bid,double longitude,double latitude,HttpServletResponse resp) {
		String url ="http://localhost:8081/customer/booking/location?bookingId="+ bid;
		 ResponseEntity<ResponseStructure> response =rtemplate.getForEntity(url, ResponseStructure.class);     
		 Map<String, Object> data =   (Map<String, Object>) response.getBody().getData();
		 double dropLatitude =((Number) data.get("dropLatitude")).doubleValue();
		 double dropLongitude = ((Number) data.get("dropLongitude")).doubleValue();
		 System.out.println("Booking ID: " + bid);
		 System.out.println("Drop Latitude: " + dropLatitude);
		 System.out.println("Drop Longitude: " + dropLongitude);
		 String gurl = "https://www.google.com/maps/dir/?api=1" + "&origin=" + latitude + "," + longitude  + "&destination=" + dropLatitude + "," + dropLongitude + "&travelmode=driving";
		 System.out.println("Google URL: " + gurl);   
		 try {
		        resp.sendRedirect(gurl);
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}

	public void updatearrivall(int bid) {
		  String rideId =bookingRedisTemplate.opsForValue() .get("booking:" + bid);
		  if (rideId == null) {
		        System.out.println("Booking not found");
		        return;
		    }

		    String rideKey = "ride:" + rideId;

		    Map<String, Object> rideData = rideRedisTemplate.opsForValue().get(rideKey);
		    if (rideData == null) {
		        System.out.println("Ride not found");
		        return;
		    }
		    rideData.put("status", "ARRIVED");

		    rideRedisTemplate.opsForValue().set(rideKey, rideData);
		}

		
		public ResponseStructure<String> verifyrideotp(String otp, int bid) {
				String url ="http://localhost:8081/customer/booking/verifyotp" + "?bookingId=" + bid + "&otp=" + otp;
			    System.out.println("OTP received by Rider = [" + otp + "]");
			    System.out.println("URL = " + url);
			    ResponseEntity<ResponseStructure> response =rtemplate.getForEntity(url, ResponseStructure.class);
			    System.out.println("Customer response = " + response.getBody());       
			    ResponseStructure<String> rs =   new ResponseStructure<>();     
			    rs.setStatuscode(response.getBody().getStatuscode());
			    rs.setMessage(response.getBody().getMessage());
			    rs.setData(String.valueOf(response.getBody().getData()));
			    return rs;
		}

		public ResponseStructure<String> cancleride(int bid) {
				String rideId = bookingRedisTemplate.opsForValue().get("booking:" + bid);
			    ResponseStructure<String> rs = new ResponseStructure<>();
			    if (rideId == null) {
			        rs.setStatuscode(HttpStatus.NOT_FOUND.value());
			        rs.setMessage("Booking Not Found");
			        rs.setData(null);
			        return rs;
			    }
			    String rideKey = "ride:" + rideId;               
			    Map<String, Object> rideData = rideRedisTemplate.opsForValue().get(rideKey);
			    if (rideData == null) {
			        rs.setStatuscode(HttpStatus.NOT_FOUND.value());
			        rs.setMessage("Ride Not Found");
			        rs.setData(null);
			        return rs;
			    }
			    rideData.put("status", "CANCELLED");
			    rideRedisTemplate.opsForValue().set(rideKey, rideData);
			    rs.setStatuscode(HttpStatus.OK.value());
			    rs.setMessage("Ride Cancelled");
			    rs.setData("CANCELLED");
			    return rs;
	}
			public ResponseStructure<String> updastatus(int riderId) {
			Rider r = rp.findById(riderId).orElseThrow(() -> new RuntimeException("Rider Not Found"));
			if (r.getStatus().equalsIgnoreCase("ONLINE")) {
			r.setStatus("OFFLINE");          
			} else { 
			    r.setStatus("ONLINE");  
			 }
			 rp.save(r);    
			 ResponseStructure<String> response = new ResponseStructure<>();
			 response.setStatuscode(200);
			 response.setMessage("Rider Status Updated");
			 response.setData("Status Updated");
			 return response;
		}

			public List<Map<String, Object>> ridehistoryy(int riderId, String status) {
				Set<String> keys = rideRedisTemplate.keys("ride:*");
					List<Map<String, Object>> history = new ArrayList<>();
					if (keys == null) {
			        return history;
			    }

			    for (String key : keys) {
			    	System.out.println("Checking Redis key = " + key);
			    	Object value = rideRedisTemplate.opsForValue().get(key);

			        System.out.println("Redis value = " + value);
			        System.out.println("Redis value class = " +
			                (value == null ? "null" : value.getClass().getName()));
			        if (!(value instanceof Map)) {
			            System.out.println("Skipping non-map key = " + key);
			            continue;
			        }

			        Map<String, Object> rideData =(Map<String, Object>) value;
			        Object riderObject = rideData.get("riderId");
			        if (riderObject == null) {
			            continue;
			        }

			        int rid = ((Number) riderObject).intValue();
			        String rideStatus =String.valueOf(rideData.get("status"));
                    if (rid == riderId && (status.equalsIgnoreCase("ALL")  || rideStatus.equalsIgnoreCase(status))) {
                    	history.add(rideData);
			        }
			    }
			    return history;
}

			public Map<String, Object> earnings(int rid, String fromdate, String todate) {
				Set<String> keys = rideRedisTemplate.keys("ride:*");
				double totalEarnings = 0;
			    int totalRides = 0;

			    if (keys != null) {

			        for (String key : keys) {

			            Map<String, Object> rideData =rideRedisTemplate.opsForValue().get(key);
			       if (rideData == null) {
			                continue;
			            }

			            Object riderObject = rideData.get("riderId");

			            if (riderObject == null) {
			                continue;
			            }

			            int riderId = ((Number) riderObject).intValue();
			                   

			            String status = String.valueOf(rideData.get("status"));
			                   

			            if (riderId == rid &&status.equalsIgnoreCase("COMPLETED")) {
			           Object fareObject = rideData.get("fare");

			                if (fareObject != null) {
			                    totalEarnings +=
			                            Double.parseDouble(String.valueOf(fareObject));
			                }

			                totalRides++;
			            }
			        }
			    }

			    Map<String, Object> summary = new HashMap<>();

			    summary.put("riderId", rid);
			    summary.put("fromDate", fromdate);
			    summary.put("toDate", todate);
			    summary.put("totalRides", totalRides);
			    summary.put("totalEarnings", totalEarnings);

			    return summary;
			}
}