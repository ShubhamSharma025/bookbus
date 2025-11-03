package com.example.busbooking.controller;

import com.example.busbooking.dto.booking.BookingResponse;
import com.example.busbooking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/checkout")
@RequiredArgsConstructor
public class CheckOutController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<List<BookingResponse>> checkout(
            @AuthenticationPrincipal UserDetails userDetails) {

        List<BookingResponse> bookings = bookingService.checkout(userDetails.getUsername())
                .stream()
                .map(BookingResponse::fromEntity)
                .toList();

        return ResponseEntity.ok(bookings);
    }
}