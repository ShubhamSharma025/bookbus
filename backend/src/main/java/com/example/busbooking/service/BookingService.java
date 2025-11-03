package com.example.busbooking.service;

import java.time.LocalDateTime;
import java.util.List;
import com.example.busbooking.dto.cart.CartItemResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.busbooking.entity.Booking;
import com.example.busbooking.entity.CartItem;
import com.example.busbooking.entity.User;
import com.example.busbooking.repository.BookingRepository;
import com.example.busbooking.repository.UserRepository;
import com.example.busbooking.entity.BusRoute;
import com.example.busbooking.repository.BusRouteRepository;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
@Transactional
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BusRouteRepository busRouteRepository;
    private final UserRepository userRepository;
    private final CartService cartService;

    // 1. GET BOOKINGS
    public List<Booking> getUserBookings(String email) {
        User user = getUserByEmail(email);
        return bookingRepository.findByUserId(user.getId()); // Efficient
    }
// ADD THIS METHOD
public Booking getBookingById(Long id, String email) {
    User user = getUserByEmail(email);
    Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Booking not found"));

    if (!booking.getUserId().equals(user.getId())) {
        throw new RuntimeException("Access denied");
    }

    return booking;
}
    // 2. CREATE BOOKING – FIXED TYPO & TYPE
    public Booking createBooking(String email, Long routeId, String seatsParam) {
        User user = getUserByEmail(email);
        int seats = parseSeats(seatsParam);

        BusRoute route = busRouteRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("Route not found"));

        int pricePerSeat = parsePrice(route.getPricePerSeat());
        int totalAmount = seats * pricePerSeat;

        Booking booking = new Booking();
        booking.setUserId(user.getId());
        booking.setRouteId(routeId);
        booking.setSeats(seats);
        booking.setTotalAmount(totalAmount);
        booking.setBookingDate(LocalDateTime.now()); // NOT .toString()
        booking.setStatus("CONFIRMED");

        return bookingRepository.save(booking);
    }

    // 3. CANCEL – FIXED: ownership check
    public void cancelBooking(Long bookingId, String email) {
        User user = getUserByEmail(email);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getUserId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }
        if ("CANCELLED".equals(booking.getStatus())) {
            throw new RuntimeException("Already cancelled");
        }

        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);
    }

    // 4. CHECKOUT – FIXED: LocalDateTime
    public List<Booking> checkout(String email) {
        User user = getUserByEmail(email);
        List<CartItemResponse> cartItems = cartService.getCartItems(email);
        if (cartItems.isEmpty()) throw new RuntimeException("Cart is empty");

        List<Booking> bookings = cartItems.stream()
                .map(item -> {
                    BusRoute route = busRouteRepository.findById(item.getRouteId())
                            .orElseThrow(() -> new RuntimeException("Route not found"));

                    int seats = parseSeats(item.getSeats());
                    int pricePerSeat = parsePrice(route.getPricePerSeat());

                    Booking b = new Booking();
                    b.setUserId(user.getId());
                    b.setRouteId(item.getRouteId());
                    b.setSeats(seats);
                    b.setTotalAmount(seats * pricePerSeat);
                    b.setBookingDate(LocalDateTime.now());
                    b.setStatus("CONFIRMED");
                    return bookingRepository.save(b);
                })
                .toList();

        cartService.clearCart(email);
        return bookings;
    }

    // HELPERS
    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private int parseSeats(String s) {
        try {
            int n = Integer.parseInt(s);
            if (n <= 0) throw new IllegalArgumentException();
            return n;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid seats: " + s);
        }
    }

    private int parsePrice(String s) {
        try {
            int p = Integer.parseInt(s);
            if (p < 0) throw new IllegalArgumentException();
            return p;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid price: " + s);
        }
    }
}