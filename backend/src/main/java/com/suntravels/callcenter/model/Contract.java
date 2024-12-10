package com.suntravels.callcenter.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Entity representing a contract.
 * This class maps to a database table and is used to persist contract data.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer contractId;

    @Column(nullable = false)
    private String hotelName;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private Double markUpRate;

    /**
     * A list of room details associated with the contract.
     * Each room detail is mapped to the contract and is loaded eagerly.
     * CascadeType.ALL ensures that operations on the contract (like saving or deleting)
     * are propagated to the associated room details.
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name= "contract_id" , referencedColumnName = "contractId")
    private List<RoomDetail> roomDetails;

}
