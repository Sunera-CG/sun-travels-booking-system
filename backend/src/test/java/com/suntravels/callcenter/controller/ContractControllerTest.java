package com.suntravels.callcenter.controller;


import com.suntravels.callcenter.dto.ContractDTO;
import com.suntravels.callcenter.dto.RoomDetailDTO;
import com.suntravels.callcenter.model.Contract;
import com.suntravels.callcenter.service.ContractService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for ContractController. Contains unit tests for all endpoints in the ContractController.
 */
class ContractControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ContractService contractService;

    @InjectMocks
    private ContractController contractController;

    private ObjectMapper objectMapper;

    /**
     * Setup method to initialize the test environment.
     * This runs before each test case.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(contractController).build();
        objectMapper = new ObjectMapper();
        // support Java 8 date time apis
        objectMapper.registerModule(new JavaTimeModule());
    }


    /**
     * Test for the GET /contracts endpoint.
     * Verifies that all contracts can be retrieved successfully.
     *
     * @throws Exception if there is an issue with the request execution.
     */
    @Test
    void testGetAllContracts() throws Exception {
        Contract contract1 = new Contract();
        contract1.setContractId(1);
        contract1.setHotelName("Hotel A");

        Contract contract2 = new Contract();
        contract2.setContractId(2);
        contract2.setHotelName("Hotel B");

        // Mocking the service method
        List<Contract> contracts = Arrays.asList(contract1, contract2);
        when(contractService.getAllContracts()).thenReturn(contracts);

        // Performing the request and verifying the response
        mockMvc.perform(get("/contracts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].hotelName").value("Hotel A"))
                .andExpect(jsonPath("$[1].hotelName").value("Hotel B"));

        // Verifying the service method was called once
        verify(contractService, times(1)).getAllContracts();
    }

    /**
     * Test for the POST /contracts endpoint.
     * Verifies that a new contract can be added successfully.
     *
     * @throws Exception if there is an issue with the request execution.
     */
    @Test
    void testAddContract() throws Exception {
        // Create a mock ContractDTO
        ContractDTO contractDTO = ContractDTO.builder()
                .hotelName("Hotel A")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(30))
                .markUpRate(15.0)
                .roomDetails(Collections.singletonList(RoomDetailDTO.builder()
                        .roomType("Deluxe")
                        .pricePerPerson(100.0)
                        .numberOfRooms(10)
                        .maxAdults(2)
                        .build()))
                .build();

        // Create the expected Contract response
        Contract contract = new Contract();
        contract.setContractId(1);
        contract.setHotelName(contractDTO.getHotelName());

        // Mock the service method
        when(contractService.addContract(any(ContractDTO.class))).thenReturn(contract);

        // Perform the POST request and verify the response
        mockMvc.perform(post("/contracts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contractDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.contractId").value(1))
                .andExpect(jsonPath("$.hotelName").value("Hotel A"));

        // Verify the service method was called once
        verify(contractService, times(1)).addContract(any(ContractDTO.class));
    }


    /**
     * Test for the GET /contracts/{hotelName} endpoint.
     * Verifies that contracts can be retrieved based on the hotel name.
     *
     * @throws Exception if there is an issue with the request execution.
     */
    @Test
    void testSearchByName() throws Exception {
        String hotelName = "Hotel A";

        // Creating a mock contract
        Contract contract = new Contract();
        contract.setContractId(1);
        contract.setHotelName(hotelName);

        // Mocking the service method
        List<Contract> contracts = Arrays.asList(contract);
        when(contractService.searchByName(hotelName)).thenReturn(contracts);

        // Performing the request and verifying the response
        mockMvc.perform(get("/contracts/{hotelName}", hotelName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].hotelName").value(hotelName));

        // Verifying the service method was called once
        verify(contractService, times(1)).searchByName(hotelName);
    }


    /**
     * Test for the DELETE /contracts/{contractId} endpoint.
     * Verifies that a contract can be deleted successfully.
     *
     * @throws Exception if there is an issue with the request execution.
     */
    @Test
    void testDeleteContract() throws Exception {
        int contractId = 1;

        // Mocking the service method to perform no action (void)
        doNothing().when(contractService).deleteContract(contractId);

        // Performing the delete request and verifying the response
        mockMvc.perform(delete("/contracts/{contractId}", contractId))
                .andExpect(status().isOk());

        // Verifying the service method was called once
        verify(contractService, times(1)).deleteContract(contractId);
    }


    /**
     * Test for the GET /contracts/{hotelName} endpoint when no contracts are found.
     * Verifies that an empty list is returned.
     *
     * @throws Exception if there is an issue with the request execution.
     */
    @Test
    void testSearchByNameNotFound() throws Exception {
        String hotelName = "Nonexistent Hotel";

        // Mock the service method to return an empty list
        when(contractService.searchByName(hotelName)).thenReturn(Collections.emptyList());

        // Perform the GET request and verify the response
        mockMvc.perform(get("/contracts/{hotelName}", hotelName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        // Verify the service method was called once
        verify(contractService, times(1)).searchByName(hotelName);
    }


}
