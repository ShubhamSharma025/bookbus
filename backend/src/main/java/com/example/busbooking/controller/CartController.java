package com.example.busbooking.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.example.busbooking.dto.cart.CartItemResponse;
import com.example.busbooking.service.CartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // GET CART ITEMS – return enriched list directly
    @GetMapping
    public ResponseEntity<List<CartItemResponse>> getCartItems(
            @AuthenticationPrincipal UserDetails userDetails) {

        List<CartItemResponse> items = cartService.getCartItems(userDetails.getUsername());
        return ResponseEntity.ok(items);  // No .map() needed!
    }

    // ADD TO CART – return enriched DTO
    @PostMapping("/{routeId}")
    public ResponseEntity<CartItemResponse> addToCart(
            @PathVariable Long routeId,
            @RequestParam String seats,
            @AuthenticationPrincipal UserDetails userDetails) {

        CartItemResponse added = cartService.addToCart(
                userDetails.getUsername(), routeId, seats);
        return ResponseEntity.ok(added);  // No formEntity()!
    }

    // DELETE & CLEAR – unchanged
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeFromCart(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        cartService.removeFromCart(userDetails.getUsername(), id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal UserDetails userDetails) {
        cartService.clearCart(userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
}