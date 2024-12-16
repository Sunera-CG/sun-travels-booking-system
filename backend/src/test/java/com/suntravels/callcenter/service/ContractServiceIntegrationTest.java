package com.suntravels.callcenter.service;

import com.suntravels.callcenter.dto.*;
import com.suntravels.callcenter.exception.NoContractsFoundException;
import com.suntravels.callcenter.model.Contract;
import com.suntravels.callcenter.model.RoomDetail;
import com.suntravels.callcenter.repository.ContractRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the ContractService class.
 * These tests interact with the database to verify the service logic.
 */
@SpringBootTest
@Transactional
class ContractServiceIntegrationTest {

    @Autowired
    private ContractService contractService;

    @Autowired
    private ContractRepository contractRepository;

    @BeforeEach
    public void setUp() {
        // Clear the database before each test
        contractRepository.deleteAll();
    }

    /**
     * Tests the retrieval of all contracts from the database.
     * Verifies that the service correctly retrieves and returns all contracts.
     */
    @Test
    public void testGetAllContracts() {
        // Arrange
        Contract contract = Contract.builder()
                .hotelName("Hotel A")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(10))
                .markUpRate(15.0)
                .roomDetails(Collections.singletonList(RoomDetail.builder()
                        .roomType("Deluxe")
                        .pricePerPerson(100.0)
                        .numberOfRooms(10)
                        .maxAdults(2)
                        .build()))
                .build();
        contractRepository.save(contract);

        // Act
        List<Contract> contracts = contractService.getAllContracts();

        // Assert
        assertEquals(1, contracts.size());
        assertEquals("Hotel A", contracts.get(0).getHotelName());
    }

    /**
     * Tests adding a new contract through the service layer.
     * Verifies that the contract is correctly saved in the database.
     */
    @Test
    public void testAddContract() {
        // Arrange
        ContractDTO contractDTO = ContractDTO.builder()
                .hotelName("Hotel B")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(30))
                .markUpRate(20.0)
                .roomDetails(Collections.singletonList(RoomDetailDTO.builder()
                        .roomType("Double")
                        .pricePerPerson(120.0)
                        .numberOfRooms(5)
                        .maxAdults(3)
                        .build()))
                .build();

        // Act
        Contract savedContract = contractService.addContract(contractDTO);

        // Assert
        assertNotNull(savedContract.getContractId());
        assertEquals("Hotel B", savedContract.getHotelName());
        assertEquals(1, savedContract.getRoomDetails().size());
    }

    /**
     * Tests searching for contracts by hotel name.
     * Verifies that the service returns the correct results.
     */
    @Test
    public void testSearchByName() {
        // Arrange
        Contract contract = Contract.builder()
                .hotelName("Hotel C")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(15))
                .markUpRate(10.0)
                .roomDetails(Collections.singletonList(RoomDetail.builder()
                        .roomType("Suite")
                        .pricePerPerson(150.0)
                        .numberOfRooms(3)
                        .maxAdults(4)
                        .build()))
                .build();
        contractRepository.save(contract);

        // Act
        List<Contract> contracts = contractService.searchByName("Hotel C");

        // Assert
        assertEquals(1, contracts.size());
        assertEquals("Hotel C", contracts.get(0).getHotelName());
    }

    /**
     * Tests searching for a hotel name that does not exist.
     * Verifies that the service throws a NoContractsFoundException.
     */
    @Test
    public void testSearchByNameNotFound() {
        // Act & Assert
        assertThrows(NoContractsFoundException.class, () -> contractService.searchByName("Nonexistent Hotel"));
    }

    /**
     * Tests deleting a contract by ID.
     * Verifies that the contract is removed from the database.
     */
    @Test
    public void testDeleteContract() {
        // Arrange
        Contract contract = Contract.builder()
                .hotelName("Hotel D")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(20))
                .markUpRate(25.0)
                .roomDetails(Collections.singletonList(RoomDetail.builder()
                        .roomType("Economy")
                        .pricePerPerson(80.0)
                        .numberOfRooms(15)
                        .maxAdults(2)
                        .build()))
                .build();
        contractRepository.save(contract);

        Integer contractId = contract.getContractId();

        // Act
        contractService.deleteContract(contractId);

        // Assert
        assertFalse(contractRepository.existsById(contractId));
    }

    /**
     * Tests searching for available contracts based on specific criteria.
     * Verifies that the service returns the correct available contracts.
     */
    @Test
    public void testSearchAvailability() {
        // Arrange
        Contract contract = Contract.builder()
                .hotelName("Hotel E")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(30))
                .markUpRate(10.0)
                .roomDetails(Collections.singletonList(RoomDetail.builder()
                        .roomType("Luxury")
                        .pricePerPerson(200.0)
                        .numberOfRooms(10)
                        .maxAdults(2)
                        .build()))
                .build();
        contractRepository.save(contract);

        SearchDTO searchDTO = SearchDTO.builder()
                .checkInDate(LocalDate.now().plusDays(5))
                .noOfNights(3)
                .roomRequirements(Collections.singletonList(RoomRequirementDTO.builder()
                        .maxAdults(2)
                        .numberOfRooms(1)
                        .build()))
                .build();

        // Act
        List<AvailableContractDTO> availableContracts = contractService.searchAvailability(searchDTO);

        // Assert
        assertEquals(1, availableContracts.size());
        assertEquals("Hotel E", availableContracts.get(0).getHotelName());
        assertEquals(1, availableContracts.get(0).getAvailableRooms().size());
    }

    /**
     * Tests searching for availability that does not match any contract.
     * Verifies that the service throws a NoContractsFoundException.
     */
    @Test
    public void testSearchAvailabilityNotFound() {
        // Arrange
        SearchDTO searchDTO = SearchDTO.builder()
                .checkInDate(LocalDate.now())
                .noOfNights(3)
                .roomRequirements(Collections.singletonList(RoomRequirementDTO.builder()
                        .maxAdults(2)
                        .numberOfRooms(5)
                        .build()))
                .build();

        // Act & Assert
        assertThrows(NoContractsFoundException.class, () -> contractService.searchAvailability(searchDTO));
    }
}
