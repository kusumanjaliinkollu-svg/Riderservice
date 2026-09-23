package com.example.rapidoRider.Reposetory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.rapidoRider.Entity.Rider;

@Repository
public interface RiderReposetory extends JpaRepository<Rider, Integer>{

}
