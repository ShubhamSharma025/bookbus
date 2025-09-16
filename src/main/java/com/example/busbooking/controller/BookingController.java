package com.example.busbooking.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.busbooking.dto.booking.BookingResponse;
import com.example.busbooking.service.BookingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    public ResponseEntity<List<BookingResponse>> getUserBooking(@AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(bookingService.getUserBookings(userDetails.getUsername())
                                .stream()
                                .map(BookingResponse::fromEntity)
                                .collect(Collectors.toList()));
    }
    @PostMapping("/{routeId}")
    public ResponseEntity<BookingResponse> createBooking(
                    @PathVariable Long routeId,
                    @RequestParam String seats,
                    @AuthenticationPrincipal UserDetails userDetails
    ){
        return ResponseEntity.ok(BookingResponse.fromEntity(
                bookingService.creatBooking( userDetails.getUsername(),routeId, seats)));
    }

    @PostMapping("/{bookingId}/cancel")
    public ResponseEntity<Void> cancelBooking(
                        @PathVariable Long bookingId,
                        @AuthenticationPrincipal UserDetails userDetails
    ){
        bookingService.cancelBooking(bookingId);
        return ResponseEntity.ok().build();

    }
}
