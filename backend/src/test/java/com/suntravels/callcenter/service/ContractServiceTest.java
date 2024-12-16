package com.suntravels.callcenter.service;

import com.suntravels.callcenter.dto.*;
import com.suntravels.callcenter.exception.NoContractsFoundException;
import com.suntravels.callcenter.model.Contract;
import com.suntravels.callcenter.repository.ContractRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContractServiceTest {

    @Mock
    private ContractRepository contractRepository;

    @InjectMocks
    private ContractService contractService;

    private ContractDTO contractDTO;

    /**
     * Sets up the test environment before each test case. Initializes the mocks.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Set up a dummy contractDTO for testing
    }

    /**
     * Tests the addContract method for successfully adding a contract.
     * <p>
     * This test ensures that the contract is saved properly and the method behaves as expected.
     */

    @Test
    void testAddContract() {
        // Create a mock ContractDTO with all necessary fields populated
        ContractDTO contractDTO = new ContractDTO();
        contractDTO.setHotelName("Test Hotel");
        contractDTO.setStartDate(LocalDate.now());
        contractDTO.setEndDate(LocalDate.now().plusDays(2));
        contractDTO.setMarkUpRate(10.0);
        contractDTO.setRoomDetails(Collections.emptyList());


        // Manually creating a mock Contract
        Contract mockContract = new Contract();
        mockContract.setHotelName(contractDTO.getHotelName());

        // Manually setting up the mock repository behavior
        ContractRepository contractRepository = mock(ContractRepository.class);
        when(contractRepository.save(any(Contract.class))).thenReturn(mockContract);

        // Instantiate the service with the mocked repository
        ContractService contractService = new ContractService(contractRepository);

        // Call the method to test
        Contract result = contractService.addContract(contractDTO);

        // Verify interactions and assertions
        verify(contractRepository, times(1)).save(any(Contract.class));
        assertNotNull(result);
        assertEquals(contractDTO.getHotelName(), result.getHotelName());
    }


    /**
     * Tests the addContract method for handling an invalid end date.
     * <p>
     * This test ensures that when an invalid contract with a past end date is provided, an exception is thrown.
     */
    @Test
    void testAddContractWithInvalidEndDate() {
        // Set the contractDTO with an invalid end date (past date)
        contractDTO = new ContractDTO();
        contractDTO.setHotelName("Test Hotel");
        contractDTO.setStartDate(LocalDate.now());
        contractDTO.setMarkUpRate(10.0);
        contractDTO.setRoomDetails(Collections.emptyList());
        contractDTO.setEndDate(LocalDate.now().minusDays(2));

        // Call the method to test and assert that it throws an exception
        assertThrows(IllegalStateException.class, () -> contractService.addContract(contractDTO));
    }

    /**
     * Tests the searchByName method for finding a contract by hotel name when the contract exists.
     * <p>
     * This test ensures that the search returns the expected contract when the hotel name matches.
     */
    @Test
    void testSearchByNameFound() {
        Contract contract = new Contract();
        contract.setHotelName("Test Hotel");

        // Mock the contract repository behavior
        when(contractRepository.findByHotelName("Test Hotel")).thenReturn(List.of(contract));

        // Call the method to test
        List<Contract> contracts = contractService.searchByName("Test Hotel");

        // Verify and assert the results
        assertNotNull(contracts);
        assertEquals(1, contracts.size());
        assertEquals("Test Hotel", contracts.get(0).getHotelName());
    }

    /**
     * Tests the searchByName method for handling cases where no contract is found.
     * <p>
     * This test ensures that an exception is thrown when no contract matches the given hotel name.
     */
    @Test
    void testSearchByNameNotFound() {
        // Mock an empty result for search
        when(contractRepository.findByHotelName("Nonexistent Hotel")).thenReturn(Collections.emptyList());

        // Call the method to test and assert that it throws an exception
        assertThrows(NoContractsFoundException.class, () -> contractService.searchByName("Nonexistent Hotel"));
    }


    /**
     * Tests the deleteContract method for successfully deleting a contract.
     * <p>
     * This test verifies that the contract is deleted when it exists in the repository.
     */
    @Test
    void testDeleteContract() {
        Integer contractId = 1;

        // Mock repository to return true when checking existence
        when(contractRepository.existsById(contractId)).thenReturn(true);

        // Call the method to test
        contractService.deleteContract(contractId);

        // Verify the delete method is called
        verify(contractRepository, times(1)).deleteById(contractId);
    }

    /**
     * Tests the deleteContract method when the contract is not found.
     * <p>
     * This test ensures that an exception is thrown when attempting to delete a non-existent contract.
     */
    @Test
    void testDeleteContractNotFound() {
        Integer contractId = 1;

        // Mock repository to return false for existence check
        when(contractRepository.existsById(contractId)).thenReturn(false);

        // Call the method to test and assert that it throws an exception
        assertThrows(NoContractsFoundException.class, () -> contractService.deleteContract(contractId));
    }

    /**
     * Tests the searchAvailability method for finding available contracts within the specified date range.
     * <p>
     * This test verifies that the method returns available contracts when valid search criteria are provided.
     */
    @Test
    void testSearchAvailability() {
        // Prepare SearchDTO
        SearchDTO searchDTO = new SearchDTO(LocalDate.now(), 5, Collections.emptyList());

        // Mock the contractRepository behavior
        Contract contract = new Contract();
        contract.setHotelName("Test Hotel");
        when(contractRepository.findContractsByDateRange(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of(contract));

        // Call the method to test
        List<AvailableContractDTO> availableContracts = contractService.searchAvailability(searchDTO);

        // Verify results
        assertNotNull(availableContracts);
        assertFalse(availableContracts.isEmpty());
    }

    /**
     * Tests the searchAvailability method when no contracts are available.
     * <p>
     * This test ensures that an exception is thrown when no contracts match the given search criteria.
     */
    @Test
    void testSearchAvailabilityNoContracts() {
        // Prepare SearchDTO
        SearchDTO searchDTO = new SearchDTO(LocalDate.now(), 5, Collections.emptyList());

        // Mock repository to return empty list
        when(contractRepository.findContractsByDateRange(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Collections.emptyList());

        // Call the method to test and assert that it throws an exception
        assertThrows(NoContractsFoundException.class, () -> contractService.searchAvailability(searchDTO));
    }

}
