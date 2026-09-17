package com.campus.booking.repository;

import com.campus.booking.model.Booking;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Overlapping check with Pessimistic Write Lock to prevent double-booking
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT b FROM Booking b 
        WHERE b.resource.id = :resourceId 
        AND b.status = 'CONFIRMED' 
        AND (:startTime < b.endTime AND :endTime > b.startTime)
    """)
    List<Booking> findOverlappingBookings(
        @Param("resourceId") Long resourceId,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime
    );
}