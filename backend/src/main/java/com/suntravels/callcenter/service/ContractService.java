package com.suntravels.callcenter.service;

import com.suntravels.callcenter.dto.ContractDTO;
import com.suntravels.callcenter.model.Contract;
import com.suntravels.callcenter.model.RoomDetail;
import com.suntravels.callcenter.repository.ContractRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

        // Validate the end date of the contract
        if (contractDTO.getEndDate().isBefore(LocalDate.now())) {
            throw new IllegalStateException("This contract has already ended");
        }

        //Create a contract Object and save it
        Contract contract = Contract.builder()
                .hotelName(contractDTO.getHotelName())
                .endDate(contractDTO.getStartDate())
                .startDate(contractDTO.getEndDate())
                .roomDetails(contractDTO.getRoomDetailDTOS().stream()      // Map room details from DTO to RoomDetail objects
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

    public Contract searchContract(){
        return new Contract();

    }


}
