package com.example.busbooking.service;

import com.example.busbooking.dto.BusRoute.BusRouteResponse;  // ← ONLY ONCE
import com.example.busbooking.dto.cart.CartItemResponse;
import com.example.busbooking.entity.CartItem;
import com.example.busbooking.entity.User;
import com.example.busbooking.repository.BusRouteRepository;
import com.example.busbooking.repository.CartItemRepository;
import com.example.busbooking.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final BusRouteRepository busRouteRepository;

    private CartItemResponse toResponse(CartItem item) {
      CartItemResponse dto = CartItemResponse.fromEntity(item);
 // typo?
        // Should be: CartItemResponse.fromEntity(item);

        busRouteRepository.findById(item.getRouteId())
                .ifPresent(route -> dto.setRoute(BusRouteResponse.fromEntity(route)));
        return dto;
    }

    public List<CartItemResponse> getCartItems(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return cartItemRepository.findByUserId(user.getId()) // Efficient!
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public CartItemResponse addToCart(String email, Long routeId, String seats) {
        User user = getUser(email);
        int seatCount = parseSeats(seats);

        CartItem item = new CartItem();
        item.setUserId(user.getId());
        item.setRouteId(routeId);
        item.setSeats(seatCount);
        item.setCreatedAt(LocalDateTime.now()); // NOT .toString()

        CartItem saved = cartItemRepository.save(item);
        return toResponse(saved);
    }

    public void removeFromCart(String email, Long id) {
        User user = getUser(email);
        CartItem item = cartItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));
        if (!item.getUserId().equals(user.getId())) throw new RuntimeException("Unauthorized");
        cartItemRepository.delete(item);
    }

    public void clearCart(String email) {
        User user = getUser(email);
        cartItemRepository.deleteByUserId(user.getId());
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private int parseSeats(String s) {
        try {
            int n = Integer.parseInt(s);
            if (n <= 0) throw new IllegalArgumentException();
            return n;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid seats");
        }
    }
}