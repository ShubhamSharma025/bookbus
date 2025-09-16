package com.example.busbooking.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.busbooking.entity.BusRoute;
import com.example.busbooking.repository.BusRouteRepository;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataSeeder {

    private final BusRouteRepository busRouteRepository;

    @Bean
    public CommandLineRunner seedData() {
        return args -> {
            if (busRouteRepository.count() == 0) {
                BusRoute route1 = createRoute(
                        "Delhi", "Mumbai", "08:00", "20:00",
                        "Delhi Express", "AC Sleeper", "40", "40", "1500",
                        "WiFi, Water, Snacks, Blanket"
                );

                BusRoute route2 = createRoute(
                        "Mumbai", "Bangalore", "09:00", "22:00",
                        "Mumbai Travels", "AC Seater", "35", "35", "1200",
                        "WiFi, Water, Blanket"
                );

                BusRoute route3 = createRoute(
                        "Chennai", "Bangalore", "06:00", "14:00",
                        "South Express", "AC Seater", "30", "30", "800",
                        "WiFi, Water, Snacks"
                );

                // Save all routes at once
                busRouteRepository.saveAll(List.of(route1, route2, route3));

                System.out.println("✅ Sample bus routes seeded successfully!");
            }
        };
    }

    private BusRoute createRoute(String origin, String destination, String departureTime, String arrivalTime,
                                 String busOperator, String busType, String totalSeats, String seatsAvailable,
                                 String pricePerSeat, String amenities) {
        BusRoute route = new BusRoute();
        route.setOrigin(origin);
        route.setDestination(destination);
        route.setDepartureTime(departureTime);
        route.setArrivalTime(arrivalTime);
        route.setBusOperator(busOperator);
        route.setBusType(busType);
        route.setTotalSeats(totalSeats);
        route.setSeatsAvailable(seatsAvailable);
        route.setPricePerSeat(pricePerSeat);
        route.setAmenities(amenities);
        return route;
    }
}
