package com.example.busbooking.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.busbooking.entity.BusRoute;
import com.example.busbooking.repository.BusRouteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BusRouteService {

    private final BusRouteRepository busRouteRepository;

    public List<BusRoute> getAllRoutes(){
        return busRouteRepository.findAll();
    }

    public List<BusRoute> searchRoutes(String from,String to,String date){
        return busRouteRepository.findAll().stream()
        .filter(route->route.getOrigin().equalsIgnoreCase(from)&&
                            route.getDestination().equalsIgnoreCase(to))
                            .toList();
    }

    public List<String> getAllCities(){
        return busRouteRepository.findAll().stream()
              .flatMap(route->List.of(route.getOrigin(),route.getDestination()).stream())
              .distinct()
              .toList();
    }

    public List<String> getAllOperators(){
        return busRouteRepository.findAll().stream()
        .map(BusRoute::getBusOperator)
        .distinct()
        .toList();
    }
        
        public BusRoute getRouteById(Long id) {
    return busRouteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Route not found with id: " + id));
}
    public List<BusRoute> getPopulaRoutes(){
        return busRouteRepository.findAll().stream()
        .limit(5)
        .toList();
    }
}
