package com.campus.booking.service;

import com.campus.booking.dto.BookingRequestDTO;
import com.campus.booking.exception.BookingConflictException;
import com.campus.booking.exception.QuotaExceededException;
import com.campus.booking.model.Booking;
import com.campus.booking.model.Resource;
import com.campus.booking.model.User;
import com.campus.booking.repository.BookingRepository;
import com.campus.booking.repository.ResourceRepository;
import com.campus.booking.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ResourceRepository resourceRepository;

    public BookingService(BookingRepository bookingRepository, UserRepository userRepository, ResourceRepository resourceRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.resourceRepository = resourceRepository;
    }

    @Transactional
    public Booking createBooking(Long userId, BookingRequestDTO dto) {
        // 1. Quota Validation (Max 4 hours)
        long hours = Duration.between(dto.startTime(), dto.endTime()).toHours();
        if (hours > 4 || hours <= 0) {
            throw new QuotaExceededException("Booking duration must be between 1 and 4 hours.");
        }

        // 2. Fetch User and Resource
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Resource resource = resourceRepository.findById(dto.resourceId())
                .orElseThrow(() -> new IllegalArgumentException("Resource not found"));

        // 3. Pessimistic Lock Check for Overlapping Bookings
        List<Booking> overlaps = bookingRepository.findOverlappingBookings(
                dto.resourceId(), dto.startTime(), dto.endTime());

        if (!overlaps.isEmpty()) {
            throw new BookingConflictException("The requested slot is already booked.");
        }

        // 4. Save Booking
        Booking booking = new Booking(user, resource, dto.startTime(), dto.endTime());
        return bookingRepository.save(booking);
    }
}