package com.example.busbooking.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Service;

import com.example.busbooking.entity.CartItem;
import com.example.busbooking.entity.User;
import com.example.busbooking.repository.CartItemRepository;
import com.example.busbooking.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    public List<CartItem> getCartItems(String email){
        User user =userRepository.findByEmail(email)
        .orElseThrow(()-> new RuntimeException("User not Found"));
        return cartItemRepository.findAll().stream()
            .filter(item->item.getUserId().equals(user.getId()))
            .toList();
    }

    public CartItem addTocart(String email,Long routeId,String seats){
        User user=userRepository.findByEmail(email)
        .orElseThrow(()-> new RuntimeException("user not found"));

        CartItem cartItem=new CartItem();
        cartItem.setUserId(user.getId());
        cartItem.setRouteId(routeId);
        cartItem.setSeats(seats);
        cartItem.setCreatedAt(LocalDateTime.now().toString());

        return cartItemRepository.save(cartItem);

    }

    public void removeFromCart(String email,Long cartItemId){
        User user=userRepository.findByEmail(email)
        .orElseThrow(()->new RuntimeException("user not found"));

        CartItem cartItem=cartItemRepository.findById(cartItemId)
             .orElseThrow(()-> new RuntimeException("Cart item not found"));

        if(!cartItem.getUserId().equals(user.getId())){
            throw new RuntimeException("Unauthorized");
        }
        cartItemRepository.delete(cartItem);
    }

    public void clearCart(String email){
        User user=userRepository.findByEmail(email)
        .orElseThrow(()-> new RuntimeException("user not found"));

        List<CartItem> userCartItems=cartItemRepository.findAll().stream()
                  .filter(item->item.getUserId().equals(user.getId()))
                  .toList();

                cartItemRepository.deleteAll(userCartItems);
    }
}
