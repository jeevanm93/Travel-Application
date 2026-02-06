package com.travelplanner.auth.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userEmail;

    private String hotelId;
    private String hotelName;

    private String roomType;

    private String checkIn;
    private String checkOut;

    private String currency;
    private Double price;

    private String status; // CONFIRMED
}
