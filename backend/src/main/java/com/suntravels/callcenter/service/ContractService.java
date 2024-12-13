package com.suntravels.callcenter.service;


import com.suntravels.callcenter.dto.*;
import com.suntravels.callcenter.model.Contract;
import com.suntravels.callcenter.model.RoomDetail;
import com.suntravels.callcenter.repository.ContractRepository;
import com.suntravels.callcenter.validator.DateValidator;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ContractService {

    private final ContractRepository contractRepository;

    @Autowired
    public ContractService(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    /**
     * Fetches all contracts from the database.
     *
     * @return A list of all Contract objects.
     */
    public List<Contract> getAllContracts() {
        return contractRepository.findAll();
    }

    /**
     * Adds a new contract to the system.
     * Validates the contract's end date and constructs a Contract object from the provided ContractDTO.
     *
     * @param contractDTO The contract data transfer object containing contract details.
     * @return The created Contract object.
     * @throws IllegalStateException if the contract's end date is in the past.
     */
    @Transactional
    public Contract addContract(@Valid ContractDTO contractDTO) {

        DateValidator.validateDates(contractDTO.getStartDate(), contractDTO.getEndDate());

        //Create a contract Object and save it
        Contract contract = Contract.builder()
                .hotelName(contractDTO.getHotelName())
                .startDate(contractDTO.getStartDate())
                .endDate(contractDTO.getEndDate())
                .markUpRate(contractDTO.getMarkUpRate())
                .roomDetails(contractDTO.getRoomDetails().stream()      // Map room details from DTO to RoomDetail objects
                        .map(roomDetailDTO ->
                                RoomDetail.builder()
                                        .roomType(roomDetailDTO.getRoomType())
                                        .pricePerPerson(roomDetailDTO.getPricePerPerson())
                                        .numberOfRooms(roomDetailDTO.getNumberOfRooms())
                                        .maxAdults(roomDetailDTO.getMaxAdults())
                                        .build()
                        ).toList())
                .build();
        contractRepository.save(contract);
        return contract;
    }

    public List<Contract> searchByName(String hotelName) {
        List<Contract> filteredContracts = contractRepository.findByHotelName(hotelName);
        if (filteredContracts.isEmpty()) {
            throw new IllegalStateException("No Contracts Found");
        }
        return filteredContracts;
    }

    /**
     * Deletes the contract with the specified ID.
     *
     * @param contractId the ID of the contract to be deleted.
     * @throws IllegalStateException if no contract with the given ID exists.
     */
    public void deleteContract(Integer contractId) {
        if (!contractRepository.existsById(contractId)) {
            throw new IllegalStateException("Contract doesn't exit bi this Id");
        }
        contractRepository.deleteById(contractId);
    }


    /**
     * Searches for contracts based on the provided search criteria.
     *
     * @param searchDTO The search data transfer object containing search parameters.
     * @return A list of available contracts that meet the search criteria.
     */
    public List<AvailableContractDTO> searchAvailability(SearchDTO searchDTO) {

        // Calculate the checkout date based on the check-in date and the number of nights.
        LocalDate checkOutDate = searchDTO.getCheckInDate().plusDays(searchDTO.getNoOfNights());

        // Fetch contracts within the date range
        List<Contract> contracts = contractRepository.findContractsByDateRange(searchDTO.getCheckInDate(), checkOutDate);
        List<AvailableContractDTO> availableContracts = new ArrayList<>();

        //For each contract, check if room requirements are met
        for (Contract contract : contracts) {
            List<AvailableRoomDTO> availableRooms = new ArrayList<>();
            boolean isContractValid = true;

            //Check if the contract can fulfill all room requirements
            for (int i = 0; i < searchDTO.getRoomRequirements().size(); i++) {
                RoomRequirementDTO requirement = searchDTO.getRoomRequirements().get(i);

                // Try to find a matching room for the current requirement.
                List<AvailableRoomDTO> matchingRooms = findAvailableRoom(contract, requirement, i + 1, searchDTO.getNoOfNights());
                if (!matchingRooms.isEmpty()) {
                    availableRooms.addAll(matchingRooms);
                } else {
                    isContractValid = false;
                    break;  // If one requirement isn't satisfied, no need to check further
                }
            }

            // If all room requirements are satisfied, add the contract to the result
            if (isContractValid) {
                System.out.println("Adding valid contract: " + contract);
                AvailableContractDTO availableContract = AvailableContractDTO.builder()
                        .hotelName(contract.getHotelName())
                        .availableRooms(availableRooms)
                        .build();
                availableContracts.add(availableContract);
            }
        }

        return availableContracts;
    }

    /**
     * Finds a room in the contract that satisfies the given room requirement.
     *
     * @param contract      The contract to search within.
     * @param requirement   The room requirement to satisfy.
     * @param requirementId The ID of the requirement (for tracking purposes).
     * @param noOfNights    The number of nights for which the room is required.
     * @return An AvailableRoomDTO if a matching room is found, or null if no match is found.
     */
    private List<AvailableRoomDTO> findAvailableRoom(Contract contract, RoomRequirementDTO requirement, int requirementId, int noOfNights) {

        List<AvailableRoomDTO> availableRooms = new ArrayList<>();

        List<RoomDetail> validRooms = contract.getRoomDetails().stream()
                .filter(roomDetail -> roomDetail.getMaxAdults() == requirement.getMaxAdults())
                .toList();

        for (RoomDetail roomDetail : validRooms) {
            if (roomDetail.getNumberOfRooms() >= requirement.getNumberOfRooms()) {
                // Calculate the price based on room details and markup.
                double markUpPrice = roomDetail.getPricePerPerson() * noOfNights * roomDetail.getMaxAdults() * roomDetail.getNumberOfRooms() * contract.getMarkUpRate() / 100;

                // Return the matching room as an AvailableRoomDTO.
                availableRooms.add(AvailableRoomDTO.builder()
                        .requirementId(requirementId)
                        .roomType(roomDetail.getRoomType())
                        .totalPrice(markUpPrice)
                        .build());
            }
        }
        return availableRooms;
    }
}


