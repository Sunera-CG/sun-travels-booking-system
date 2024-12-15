package com.suntravels.callcenter.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Data Transfer Object (DTO) for room details associated with a contract.
 * This DTO is used to transfer room-specific information from the client to the backend.
 * It includes validation constraints to ensure required fields are provided.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Valid
public class RoomDetailDTO {

    /**
     * The type of the room (e.g., single, double, suite).
     * Cannot be blank, validated by @NotBlank annotation.
     */
    @NotBlank(message = "Room type is required")
    private String roomType;

    /**
     * The price per person for the room.
     * Cannot be null and must be positive, validated by @NotNull and @Min annotations.
     */
    @NotNull(message = "Room price is required")
    @Min(value = 0, message = "Price per person must be positive")
    private Double pricePerPerson;

    /**
     * The number of rooms available.
     * Cannot be null and must be at least 1, validated by @NotNull and @Min annotations.
     */
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Number of rooms must be at least 1")
    private Integer numberOfRooms;

    /**
     * The maximum number of adults the room can accommodate.
     * Cannot be null and must be at least 1, validated by @NotNull and @Min annotations.
     */
    @NotNull(message = "Room  capacity is required")
    @Min(value = 1, message = "Maximum adults must be at least 1")
    private Integer maxAdults;

}
