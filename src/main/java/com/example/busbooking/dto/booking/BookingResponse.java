package com.example.busbooking.dto.booking;

import com.example.busbooking.entity.Booking;

import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.NoArgsConstructor;


@Data

@AllArgsConstructor
@NoArgsConstructor

public class BookingResponse {

    private Long id;
    private Long userId;
    private Long routeId;
    private String seats;
    private String totalAmount;
    private String bookingDate;
    private String status;

    public static BookingResponse fromEntity(Booking booking){
        BookingResponse response=new BookingResponse();
        response.setId(booking.getId());
        response.setUserId(booking.getUserId());
        response.setRouteId(booking.getRouteId());
        response.setSeats(booking.getSeats());
        response.setTotalAmount(booking.getTotalAmount());
        response.setBookingDate(booking.getBookingDate());
        response.setStatus(booking.getSeats());
        return response;

    }

}
