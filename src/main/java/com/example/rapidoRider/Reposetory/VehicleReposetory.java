package com.example.rapidoRider.Reposetory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.rapidoRider.Entity.Vehicle;

@Repository
public interface VehicleReposetory extends JpaRepository<Vehicle, Integer>{

}
