package com.campus.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record BookingRequestDTO(
    @NotNull(message = "Resource ID is required") 
    Long resourceId,
    
    @NotNull @Future(message = "Start time must be in the future") 
    LocalDateTime startTime,
    
    @NotNull @Future(message = "End time must be in the future") 
    LocalDateTime endTime
) {}