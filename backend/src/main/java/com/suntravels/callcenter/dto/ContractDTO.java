package com.suntravels.callcenter.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;


/**
 * Data Transfer Object (DTO) for contract information.
 * This DTO is used to transfer contract data from the client to the backend.
 * It includes validation constraints to ensure required fields are provided.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Valid
public class ContractDTO {

    private Integer contractId;

    /**
     * The name of the hotel associated with the contract.
     * Cannot be blank, validated by @NotBlank annotation.
     */
    @NotBlank(message = "Hotel name is required")
    private String hotelName;

    /**
     * The start date of the contract.
     * Cannot be null, validated by @NotNull annotation.
     */
    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    /**
     * The end date of the contract.
     * Cannot be null, validated by @NotNull annotation.
     */
    @NotNull(message = "End date is required")
    private LocalDate endDate;

    /**
     * List of room details associated with the contract.
     * Cannot be empty, validated by @NotEmpty annotation.
     * Each room detail is validated individually .
     */
    @NotEmpty(message = "Room details are required")
    @Valid
    private List<RoomDetailDTO> roomDetailDTOS;
}