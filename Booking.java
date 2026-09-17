package com.campus.booking.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "resource_id")
    private Resource resource;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private Status status = Status.CONFIRMED;

    public enum Status { CONFIRMED, CANCELLED }

    public Booking() {}
    public Booking(User user, Resource resource, LocalDateTime startTime, LocalDateTime endTime) {
        this.user = user;
        this.resource = resource;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getId() { return id; }
    public User getUser() { return user; }
    public Resource getResource() { return resource; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public Status getStatus() { return status; }
}