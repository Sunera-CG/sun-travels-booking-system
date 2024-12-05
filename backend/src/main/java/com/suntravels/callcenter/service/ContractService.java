package com.suntravels.callcenter.service;

import com.suntravels.callcenter.dto.AvailableContract;
import com.suntravels.callcenter.dto.ContractDTO;
import com.suntravels.callcenter.dto.SearchDTO;
import com.suntravels.callcenter.model.Contract;
import com.suntravels.callcenter.model.RoomDetail;
import com.suntravels.callcenter.repository.ContractRepository;
import com.suntravels.callcenter.validator.DateValidator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;


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
    public List<Contract> getAllContracts(){
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
    public Contract addContract(ContractDTO contractDTO){

        DateValidator.validateDates(contractDTO.getStartDate(), contractDTO.getEndDate());

        //Create a contract Object and save it
        Contract contract = Contract.builder()
                .hotelName(contractDTO.getHotelName())
                .startDate(contractDTO.getStartDate())
                .endDate(contractDTO.getEndDate())
                .roomDetails(contractDTO.getRoomDetailDTOS().stream()      // Map room details from DTO to RoomDetail objects
                        .map( roomDetailDTO ->
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

    /**
     * Searches for contracts based on the provided search criteria.
     *
     * @param searchDTO The search data transfer object containing search parameters.
     * @return A list of available contracts.
     */
//    public List<AvailableContract> searchContract(SearchDTO searchDTO) {
//        // Calculate checkout date
//        LocalDate checkoutDate = searchDTO.getCheckInDate().plusDays(searchDTO.getNoOfNights());
//        DateValidator.validateDates(searchDTO.getCheckInDate(), checkoutDate);
//
//        // Fetch available contracts based on basic criteria
//        List<Contract> tempAvailableContracts = contractRepository.findAvailablContract(
//                searchDTO.getCheckInDate(),
//                checkoutDate,
//                searchDTO.getRoomRequirements().getFirst().getNumberOfRooms(),
//                searchDTO.getRoomRequirements().getFirst().getMaxAdults()
//        );
//
//        // If no contracts are found, return a list with one "Unavailable" contract
//        if (tempAvailableContracts.isEmpty()) {
//            return List.of(AvailableContract.builder().status("Unavailable").build());
//        }
//
//        // Filter and map contracts to AvailableContract DTOs
//        List<AvailableContract> availableContracts = tempAvailableContracts.stream()
//                .flatMap(contract -> searchDTO.getRoomRequirements().stream().map(roomRequirementDTO -> {
//                    boolean matches = contract.getRoomDetails().stream().anyMatch(roomDetail ->
//                            roomDetail.getNumberOfRooms() >= roomRequirementDTO.getNumberOfRooms() &&
//                                    roomDetail.getMaxAdults() >= roomRequirementDTO.getMaxAdults()
//                    );
//                    if (matches) {
//                        return AvailableContract.builder()
//                                .hotelName(contract.getHotelName())
//                                .status("Available")
//                                .build();
//                    } else {
//                        return null;
//                    }
//                }))
//                .filter(Objects::nonNull)
//                .toList();
//
//        // If no contracts match, return "Unavailable"
//        if (availableContracts.isEmpty()) {
//            return List.of(AvailableContract.builder().status("Unavailable").build());
//        }
//
//        return availableContracts;
//    }


}
