package com.example.busbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.busbooking.entity.BusRoute;

@Repository
public interface BusRouteRepository extends JpaRepository <BusRoute,Long>{

}
