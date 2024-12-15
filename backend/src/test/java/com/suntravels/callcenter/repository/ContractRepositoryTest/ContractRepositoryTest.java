package com.suntravels.callcenter.repository.ContractRepositoryTest;

import com.suntravels.callcenter.model.Contract;
import com.suntravels.callcenter.repository.ContractRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ContractRepositoryTest {

    @Autowired
    private ContractRepository contractRepository;

    @Test
    void testFindByHotelName() {
        // Arrange
        Contract contract1 = new Contract();
        contract1.setHotelName("Hotel1");
        contractRepository.save(contract1);

        Contract contract2 = new Contract();
        contract2.setHotelName("Hotel2");
        contractRepository.save(contract2);

        // Act
        List<Contract> contracts = contractRepository.findByHotelName("Hotel1");

        // Assert
        assertEquals(1, contracts.size());
        assertEquals("Hotel1", contracts.get(0).getHotelName());
    }

    @Test
    void testFindContractsByDateRange() {
        // Arrange
        Contract contract = new Contract();
        contract.setStartDate(LocalDate.of(2024, 1, 1));
        contract.setEndDate(LocalDate.of(2024, 12, 31));
        contractRepository.save(contract);

        // Act
        List<Contract> contracts = contractRepository.findContractsByDateRange(
                LocalDate.of(2024, 1, 1), LocalDate.of(2024, 6, 30));

        // Assert
        assertFalse(contracts.isEmpty());
    }
}
