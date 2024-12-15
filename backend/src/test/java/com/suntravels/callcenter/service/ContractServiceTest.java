package com.suntravels.callcenter.service;

import com.suntravels.callcenter.dto.ContractDTO;
import com.suntravels.callcenter.model.Contract;
import com.suntravels.callcenter.repository.ContractRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ContractServiceTest {

    @Mock
    private ContractRepository contractRepository;

    @InjectMocks
    private ContractService contractService;

    @Test
    void testAddContract() {
        // Arrange
        ContractDTO contractDTO = new ContractDTO(); // Set up the contractDTO with necessary data
        Contract contract = new Contract(); // Setup contract entity
        when(contractRepository.save(any(Contract.class))).thenReturn(contract);

        // Act
        Contract result = contractService.addContract(contractDTO);

        // Assert
        assertEquals(contract, result);
        verify(contractRepository, times(1)).save(any(Contract.class));
    }

    @Test
    void testSearchByName() {
        // Arrange
        List<Contract> contracts = List.of(new Contract(), new Contract());
        when(contractRepository.findByHotelName("Hotel")).thenReturn(contracts);

        // Act
        List<Contract> result = contractService.searchByName("Hotel");

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void testDeleteContract() {
        // Arrange
        when(contractRepository.existsById(1)).thenReturn(true);
        doNothing().when(contractRepository).deleteById(1);

        // Act
        contractService.deleteContract(1);

        // Assert
        verify(contractRepository, times(1)).deleteById(1);
    }
}
