package com.example.busbooking.dto.booking;

import com.example.busbooking.entity.Booking;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {

    private Long id;
    private Long userId;
    private Long routeId;
    private String seats;        // int → String
    private String totalAmount;  // int → String
    private String bookingDate;  // LocalDateTime → ISO String
    private String status;

    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static BookingResponse fromEntity(Booking booking) {
        if (booking == null) return null;

        BookingResponse response = new BookingResponse();
        response.setId(booking.getId());
        response.setUserId(booking.getUserId());
        response.setRouteId(booking.getRouteId());
        response.setSeats(String.valueOf(booking.getSeats()));
        response.setTotalAmount(String.valueOf(booking.getTotalAmount()));
        response.setBookingDate(
            booking.getBookingDate() != null
                ? booking.getBookingDate().format(ISO_FORMATTER)
                : null
        );
        response.setStatus(booking.getStatus());

        return response;
    }
}