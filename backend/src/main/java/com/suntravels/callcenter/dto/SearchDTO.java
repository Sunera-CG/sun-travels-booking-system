package com.suntravels.callcenter.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Data Transfer Object (DTO) used for searching accommodation details.
 * This DTO contains the required information for searching based on check-in date,
 * number of nights, and room requirements. It includes validation constraints to ensure required fields are provided.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class SearchDTO {

    /**
     * The check-in date for the accommodation search.
     * Cannot be null, validated by @NotNull annotation.
     */
    @NotNull(message = "Check in date is required")
    private LocalDate checkInDate;

    /**
     * The number of nights for the accommodation search.
     * Cannot be null, validated by @NotNull annotation.
     */
    @NotNull(message = "No of nights required")
    private Integer noOfNights;

    /**
     * A list of room requirements for the accommodation search.
     * Cannot be empty, validated by @NotEmpty annotation.
     * Each room requirement is validated individually by @Valid.
     */
    @NotEmpty(message = "Room requirements are required")
    @Valid
    private List<RoomRequirementDTO> roomRequirements;

}
