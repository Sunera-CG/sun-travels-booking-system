package com.suntravels.callcenter.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing room details for a contract.
 * This class maps to a database table and is used to persist room-specific data for contracts.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roomDetailId;

    @Column(nullable = false)
    private String roomType;

    @Column(nullable = false)
    private double pricePerPerson;

    @Column(nullable = false)
    private int numberOfRooms;

    @Column(nullable = false)
    private int maxAdults;

}
