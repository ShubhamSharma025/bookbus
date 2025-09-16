package com.example.busbooking.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.busbooking.entity.Booking;
import com.example.busbooking.entity.CartItem;
import com.example.busbooking.entity.User;
import com.example.busbooking.repository.BookingRepository;
import com.example.busbooking.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final CartService cartService;

    public List<Booking> getUserBookings(String email){
        User user=userRepository.findByEmail(email)
                    .orElseThrow(()-> new RuntimeException("User not found"));
        return bookingRepository.findAll().stream()
                            .filter(booking->booking.getUserId().equals(user.getId()))
                            .toList();
        
    }

    public Booking creatBooking(String email,Long routeId,String seats){
        User user=userRepository.findByEmail(email)
                  .orElseThrow(()->new RuntimeException("user not found"));

                Booking booking=new Booking();
                booking.setUserId(user.getId());
                booking.setRouteId(routeId);
                booking.setSeats(seats);
                booking.setTotalAmount("500");
                booking.setBookingDate(LocalDateTime.now().toString());
                booking.setStatus("CONFIRMED");

                return bookingRepository.save(booking);
    }

    public void cancelBooking(Long bookingid){

        Booking booking=bookingRepository.findById(bookingid)
                    .orElseThrow(()-> new RuntimeException("Booking not found"));
                booking.setStatus("CANCELLED");
                bookingRepository.save(booking);
                }
    
    public List<Booking> checkout(String email){
        User user=userRepository.findByEmail(email)
        .orElseThrow(()->new RuntimeException("User Not found"));

        List<CartItem> cartItems=cartService.getCartItems(email);

        if(cartItems.isEmpty()){
            throw new RuntimeException("Cart is Empty");
        }

        List<Booking> bookings =cartItems.stream()
                    .map(cartItem->{
                        Booking booking=new Booking();
                        booking.setUserId(user.getId());
                        booking.setRouteId(cartItem.getRouteId());
                        booking.setSeats(cartItem.getSeats());
                        booking.setTotalAmount("500");
                        booking.setBookingDate(LocalDateTime.now().toString());
                        booking.setStatus("CONFIRMED");
                        return bookingRepository.save(booking);

                    })
                    .toList();
            cartService.clearCart(email);
            return bookings;
    }
}
