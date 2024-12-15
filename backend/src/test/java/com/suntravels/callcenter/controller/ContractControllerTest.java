package com.suntravels.callcenter.controller;

import com.suntravels.callcenter.dto.AvailableContractDTO;
import com.suntravels.callcenter.dto.ContractDTO;
import com.suntravels.callcenter.dto.RoomDetailDTO;
import com.suntravels.callcenter.dto.SearchDTO;
import com.suntravels.callcenter.model.Contract;
import com.suntravels.callcenter.model.RoomDetail;
import com.suntravels.callcenter.service.ContractService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ContractControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ContractService contractService;

    @InjectMocks
    private ContractController contractController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(contractController).build();
        objectMapper = new ObjectMapper();
    }


    @Test
    void testGetAllContracts() throws Exception {
        Contract contract1 = new Contract();
        contract1.setContractId(1);
        contract1.setHotelName("Hotel A");

        Contract contract2 = new Contract();
        contract2.setContractId(2);
        contract2.setHotelName("Hotel B");

        List<Contract> contracts = Arrays.asList(contract1, contract2);

        when(contractService.getAllContracts()).thenReturn(contracts);

        mockMvc.perform(get("/contracts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].hotelName").value("Hotel A"))
                .andExpect(jsonPath("$[1].hotelName").value("Hotel B"));

        verify(contractService, times(1)).getAllContracts();
    }

    @Test
    void testSearchByName() throws Exception {
        String hotelName = "Hotel A";

        Contract contract = new Contract();
        contract.setContractId(1);
        contract.setHotelName(hotelName);

        List<Contract> contracts = Arrays.asList(contract);

        when(contractService.searchByName(hotelName)).thenReturn(contracts);

        mockMvc.perform(get("/contracts/{hotelName}", hotelName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].hotelName").value(hotelName));

        verify(contractService, times(1)).searchByName(hotelName);
    }

    @Test
    void testDeleteContract() throws Exception {
        int contractId = 1;

        doNothing().when(contractService).deleteContract(contractId);

        mockMvc.perform(delete("/contracts/{contractId}", contractId))
                .andExpect(status().isOk());

        verify(contractService, times(1)).deleteContract(contractId);
    }

    @Test
    void testAddContract() throws Exception {
        // Create a valid RoomDetailDTO
        RoomDetailDTO roomDetailDTO = RoomDetailDTO.builder()
                .roomType("Deluxe")
                .pricePerPerson(200.0)
                .numberOfRooms(5)
                .maxAdults(2)
                .build();

        // Create a valid ContractDTO
        ContractDTO contractDTO = ContractDTO.builder()
                .hotelName("Hotel A")
                .startDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 12, 31))
                .markUpRate(10.0)
                .roomDetails(Arrays.asList(roomDetailDTO))
                .build();

        // Create a Contract entity to simulate the returned object from the service
        Contract contract = new Contract();
        contract.setContractId(1);
        contract.setHotelName("Hotel A");
        contract.setStartDate(LocalDate.of(2024, 1, 1));
        contract.setEndDate(LocalDate.of(2024, 12, 31));
        contract.setMarkUpRate(10.0);

        // Use the RoomDetail builder to create RoomDetail instances
        RoomDetail roomDetail = RoomDetail.builder()
                .roomType("Deluxe")
                .pricePerPerson(200.0)
                .numberOfRooms(5)
                .maxAdults(2)
                .build();

        contract.setRoomDetails(Arrays.asList(roomDetail));

        // Mock the contractService to return the created contract
        when(contractService.addContract(contractDTO)).thenReturn(contract);

        // Perform the POST request with the valid contractDTO
        mockMvc.perform(post("/contracts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contractDTO)))
                .andExpect(status().isCreated())  // Expecting HTTP 201 status
                .andExpect(jsonPath("$.contractId").value(contract.getContractId()))
                .andExpect(jsonPath("$.hotelName").value(contract.getHotelName()))
                .andExpect(jsonPath("$.startDate").value(contract.getStartDate().toString()))
                .andExpect(jsonPath("$.endDate").value(contract.getEndDate().toString()))
                .andExpect(jsonPath("$.markUpRate").value(contract.getMarkUpRate()))
                .andExpect(jsonPath("$.roomDetails[0].roomType").value("Deluxe"))
                .andExpect(jsonPath("$.roomDetails[0].pricePerPerson").value(200.0))
                .andExpect(jsonPath("$.roomDetails[0].numberOfRooms").value(5))
                .andExpect(jsonPath("$.roomDetails[0].maxAdults").value(2));

        // Verify the service method was called once
        verify(contractService, times(1)).addContract(contractDTO);
    }

}
