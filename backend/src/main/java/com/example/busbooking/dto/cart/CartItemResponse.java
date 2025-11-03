package com.example.busbooking.dto.cart;

import com.example.busbooking.dto.BusRoute.BusRouteResponse;
import com.example.busbooking.entity.CartItem;
import lombok.Data;

import java.time.format.DateTimeFormatter;

@Data
public class CartItemResponse {

    private Long id;
    private Long userId;
    private Long routeId;

    private String seats;           // int → String
    private String createdAt;       // LocalDateTime → ISO String
    private BusRouteResponse route;

    // ISO format: "2025-04-05T10:30:00"
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static CartItemResponse fromEntity(CartItem cartItem) {  // ← Fixed typo: formEntity → fromEntity
        if (cartItem == null) return null;

        CartItemResponse response = new CartItemResponse();
        response.setId(cartItem.getId());
        response.setUserId(cartItem.getUserId());
        response.setRouteId(cartItem.getRouteId());

        // FIX 1: int → String
        response.setSeats(String.valueOf(cartItem.getSeats()));

        // FIX 2: LocalDateTime → String (ISO format)
        response.setCreatedAt(
            cartItem.getCreatedAt() != null
                ? cartItem.getCreatedAt().format(ISO_FORMATTER)
                : null
        );

        return response;
    }
}