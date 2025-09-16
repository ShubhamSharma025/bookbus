package com.example.busbooking.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.busbooking.dto.cart.CartItemResponse;
import com.example.busbooking.service.CartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
  private final CartService cartService;

  @GetMapping
  public ResponseEntity<List<CartItemResponse>> getCartItems(@AuthenticationPrincipal UserDetails userDetails){
    return ResponseEntity.ok(cartService.getCartItems(userDetails.getUsername())
                    .stream()
                    .map(CartItemResponse::formEntity)
                    .collect(Collectors.toList()));
  }

  @PostMapping({"/routeId"})
  public ResponseEntity<CartItemResponse> addToCart(
                    @PathVariable Long routeId,
                    @RequestParam String seats,
                    @AuthenticationPrincipal UserDetails userDetails
  ){
    return ResponseEntity.ok(CartItemResponse.formEntity(
        cartService.addTocart(userDetails.getUsername(), routeId, seats)
    ));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> removeFromCart(
                    @PathVariable Long id,
                    @AuthenticationPrincipal UserDetails userDetails
  )
  {
    cartService.removeFromCart(userDetails.getUsername(), id);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping
  public ResponseEntity<Void> clearCart(@AuthenticationPrincipal UserDetails userDetails){
    cartService.clearCart(userDetails.getUsername());
    return ResponseEntity.ok().build();
  }
}
