package com.suntravels.callcenter.repository;

import com.suntravels.callcenter.model.Contract;
import com.suntravels.callcenter.model.RoomDetail;
import com.suntravels.callcenter.repository.ContractRepository;
import com.suntravels.callcenter.repository.RoomDetailRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the ContractRepository class.
 * <p>
 * This test class uses @DataJpaTest to provide an in-memory database for testing repository operations
 * related to contracts and room details.
 */
@DataJpaTest
public class ContractRepositoryTest {

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private RoomDetailRepository roomDetailRepository;

    private Contract contract;

    /**
     * Unit tests for the ContractRepository class.
     * <p>
     * This test class uses @DataJpaTest to provide an in-memory database for testing repository operations
     * related to contracts and room details.
     */
    @BeforeEach
    public void setUp() {
        // Create RoomDetails
        RoomDetail room1 = new RoomDetail(null, "Deluxe", 200.0, 10, 2);
        RoomDetail room2 = new RoomDetail(null, "Suite", 350.0, 5, 3);
        roomDetailRepository.saveAll(List.of(room1, room2));

        // Create a contract
        contract = new Contract();
        contract.setHotelName("Test Hotel");
        contract.setStartDate(LocalDate.of(2024, 1, 1));
        contract.setEndDate(LocalDate.of(2024, 12, 31));
        contract.setMarkUpRate(10.0);
        contract.setRoomDetails(List.of(room1, room2));

        contractRepository.save(contract);
    }

    /**
     * Tests saving a contract.
     * <p>
     * This test verifies that a contract is saved and its ID is automatically generated.
     */
    @Test
    public void testSaveContract() {
        assertNotNull(contract.getContractId());
        assertEquals("Test Hotel", contract.getHotelName());
    }


    /**
     * Tests finding contracts by hotel name.
     * <p>
     * This test ensures that the contract can be retrieved using the hotel name.
     */
    @Test
    public void testFindByHotelName() {
        List<Contract> contracts = contractRepository.findByHotelName("Test Hotel");
        assertFalse(contracts.isEmpty());
        assertEquals("Test Hotel", contracts.get(0).getHotelName());
    }

    /**
     * Tests finding contracts within a specified date range.
     * <p>
     * This test ensures that contracts are returned if their date range overlaps with the specified period.
     */
    @Test
    public void testFindContractsByDateRange() {
        LocalDate checkInDate = LocalDate.of(2024, 6, 1);
        LocalDate checkOutDate = LocalDate.of(2024, 6, 10);

        List<Contract> contracts = contractRepository.findContractsByDateRange(checkInDate, checkOutDate);
        assertFalse(contracts.isEmpty());
        assertTrue(contracts.get(0).getStartDate().isBefore(checkOutDate));
        assertTrue(contracts.get(0).getEndDate().isAfter(checkInDate));
    }

    /**
     * Tests the case when no contracts are available in the specified date range.
     * <p>
     * This test ensures that the repository returns an empty list if no contracts match the given date range.
     */
    @Test
    public void testNoContractsInDateRange() {
        LocalDate checkInDate = LocalDate.of(2025, 1, 1);
        LocalDate checkOutDate = LocalDate.of(2025, 1, 10);

        List<Contract> contracts = contractRepository.findContractsByDateRange(checkInDate, checkOutDate);
        assertTrue(contracts.isEmpty());
    }
}
