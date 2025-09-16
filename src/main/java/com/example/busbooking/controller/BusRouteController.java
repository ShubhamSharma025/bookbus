package com.example.busbooking.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.HttpExchange;

import com.example.busbooking.dto.BusRoute.BusRouteResponse;
import com.example.busbooking.service.BusRouteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/routes")
// @CrossOrigin(origins = "https://localhost:8080")
public class BusRouteController {
        private final BusRouteService busRouteService;
        
        @GetMapping
        public ResponseEntity<List<BusRouteResponse>> getAllRoutes(){
            return ResponseEntity.ok(busRouteService.getAllRoutes()
                                .stream()
                                .map(BusRouteResponse::fromEntity)
                                .collect(Collectors.toList()));
        }

        @GetMapping("/search")
        public ResponseEntity<List<BusRouteResponse>>searchRoutes(
                        @RequestParam String from,
                        @RequestParam String to,
                        @RequestParam String date
        ){

            return ResponseEntity.ok(busRouteService.searchRoutes(from, to, date)
            .stream()
            .map(BusRouteResponse::fromEntity)
            .collect(Collectors.toList()));
        }


        @GetMapping("/cities")
        public ResponseEntity<List<String>> getAllCities(){
            return ResponseEntity.ok(busRouteService.getAllCities());
        }

        @GetMapping("/operators")
        public ResponseEntity<List<String>> getAllOperators(){
            return ResponseEntity.ok(busRouteService.getAllOperators());
        }
        @GetMapping("/popular")
        public ResponseEntity<List<BusRouteResponse>> getPopularRoutes(){
            return ResponseEntity.ok(busRouteService.getPopulaRoutes()
                            .stream()
                            .map(BusRouteResponse::fromEntity)
                            .collect(Collectors.toList()));
        }
        
}
