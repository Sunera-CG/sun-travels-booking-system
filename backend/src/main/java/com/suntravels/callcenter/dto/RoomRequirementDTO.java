package com.suntravels.callcenter.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing room requirements for a contract.
 * This DTO is used to transfer the required room details, including the number of rooms and adults,
 * from the client to the backend. It includes validation constraints to ensure required fields are provided.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Valid
@Builder
public class RoomRequirementDTO {

    /**
     * The number of rooms requested.
     * Cannot be null, validated by @NotNull annotation.
     */
    @NotNull(message = "No. of rooms are required")
    private int numberOfRooms;

    /**
     * The number of adults for the room booking.
     * Cannot be null, validated by @NotNull annotation.
     */
    @NotNull(message = "No. of adults are required")
    private int maxAdults;
}
