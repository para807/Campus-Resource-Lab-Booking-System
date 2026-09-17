package com.campus.booking.controller;

import com.campus.booking.dto.BookingRequestDTO;
import com.campus.booking.model.Booking;
import com.campus.booking.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(
            @AuthenticationPrincipal String userId,
            @Valid @RequestBody BookingRequestDTO dto) {
        Booking booking = bookingService.createBooking(Long.parseLong(userId), dto);
        return ResponseEntity.ok(booking);
    }
}
