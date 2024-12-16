package com.suntravels.callcenter.controller;

import com.suntravels.callcenter.dto.ContractDTO;
import com.suntravels.callcenter.dto.RoomDetailDTO;
import com.suntravels.callcenter.dto.RoomRequirementDTO;
import com.suntravels.callcenter.dto.SearchDTO;
import com.suntravels.callcenter.model.Contract;
import com.suntravels.callcenter.model.RoomDetail;
import com.suntravels.callcenter.repository.ContractRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Integration tests for the ContractController.
 * <p>
 * This test class uses @SpringBootTest to load the full application context and MockMvc to test the HTTP endpoints.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class ContractControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ContractRepository contractRepository;

    private ObjectMapper objectMapper;

    /**
     * Sets up the test environment by initializing the ObjectMapper and adding necessary configurations.
     * This method is executed before each test case.
     */
    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        contractRepository.deleteAll();
    }

    /**
     * Tests the GET /contracts endpoint to verify all contracts can be retrieved successfully.
     * <p>
     * This test checks if the GET request for contracts returns a list of contracts.
     *
     * @throws Exception if there is an issue with the request execution.
     */
    @Test
    public void testGetAllContracts() throws Exception {
        Contract contract1 = Contract.builder()
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
        contractRepository.save(contract1);

        Contract contract2 = Contract.builder()
                .hotelName("Hotel B")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(10))
                .markUpRate(25.0)
                .roomDetails(Collections.singletonList(RoomDetail.builder()
                        .roomType("Double")
                        .pricePerPerson(100.0)
                        .numberOfRooms(12)
                        .maxAdults(5)
                        .build()))
                .build();
        contractRepository.save(contract2);

        // Perform GET request to retrieve all contracts
        mockMvc.perform(get("/contracts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].hotelName").value("Hotel A"))
                .andExpect(jsonPath("$[1].hotelName").value("Hotel B"));
    }

    /**
     * Tests the POST /contracts endpoint to verify a new contract can be added successfully.
     * <p>
     * This test sends a POST request to create a new contract and checks if it was created successfully.
     *
     * @throws Exception if there is an issue with the request execution.
     */
    @Test
    public void testAddContract() throws Exception {
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

        // Perform POST request to add a new contract
        mockMvc.perform(post("/contracts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contractDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.hotelName").value("Hotel A"))
                .andExpect(jsonPath("$.contractId").exists());
    }

    /**
     * Tests the GET /contracts/{hotelName} endpoint to search for contracts by hotel name.
     * <p>
     * This test simulates a GET request with a hotel name parameter and verifies that the correct contract is returned.
     *
     * @throws Exception if there is an issue with the request execution.
     */
    @Test
    public void testSearchByName() throws Exception {
        String hotelName = "Hotel A";

        // Create a contract with all required fields set
        Contract contract = new Contract();
        contract.setHotelName(hotelName);
        contract.setStartDate(LocalDate.now()); // Ensure startDate is set
        contract.setEndDate(LocalDate.now().plusDays(30)); // Ensure endDate is set
        contract.setMarkUpRate(10.0); // Ensure markUpRate is set
        contractRepository.save(contract); // Save to the database

        // Perform GET request to search contracts by hotel name
        mockMvc.perform(get("/contracts/{hotelName}", hotelName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1)) // Expect 1 contract
                .andExpect(jsonPath("$[0].hotelName").value(hotelName)); // Expect hotelName to match
    }

    /**
     * Tests the DELETE /contracts/{contractId} endpoint to delete a contract.
     * <p>
     * This test simulates a DELETE request and verifies that a contract is deleted successfully.
     *
     * @throws Exception if there is an issue with the request execution.
     */
    @Test
    public void testDeleteContract() throws Exception {
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

        // Perform DELETE request to remove the contract
        mockMvc.perform(delete("/contracts/{contractId}", contract.getContractId()))
                .andExpect(status().isOk());

        // Verify the contract is deleted by checking if it no longer exists in the database
        mockMvc.perform(get("/contracts/{contractId}", contract.getContractId()))
                .andExpect(status().isNotFound())  // Expect 404 NotFound Status
                .andExpect(jsonPath("$").value("No Contracts Found"));
    }

    /**
     * Tests the GET /contracts/{hotelName} endpoint when no contracts are found.
     * <p>
     * This test simulates a GET request with a hotel name that does not exist in the database.
     * It checks if the response returns an empty list.
     *
     * @throws Exception if there is an issue with the request execution.
     */
    @Test
    public void testSearchByNameNotFound() throws Exception {
        String hotelName = "Nonexistent Hotel";

        // Perform GET request to search for contracts by hotel name
        mockMvc.perform(get("/contracts/{hotelName}", hotelName))
                .andExpect(status().isNotFound())  // Expect 404 NotFound
                .andExpect(jsonPath("$").value("No Contracts Found"));  // Expect error message from the IllegalStateException
    }


    /**
     * Tests the POST /contracts/available endpoint to search for available contracts.
     * <p>
     * This test sends a POST request with the search parameters and verifies the returned list of available contracts.
     *
     * @throws Exception if there is an issue with the request execution.
     */
    @Test
    public void testSearchAvailability() throws Exception {
        // Arrange: Add some contracts with room details
        Contract contract1 = Contract.builder()
                .hotelName("Hotel A")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(10))
                .markUpRate(15.0)
                .roomDetails(Collections.singletonList(RoomDetail.builder()
                        .roomType("Deluxe")
                        .pricePerPerson(100.0)
                        .numberOfRooms(12)
                        .maxAdults(2)
                        .build()))
                .build();
        contractRepository.save(contract1);

        Contract contract2 = Contract.builder()
                .hotelName("Hotel B")
                .startDate(LocalDate.now().plusDays(5))
                .endDate(LocalDate.now().plusDays(15))
                .markUpRate(20.0)
                .roomDetails(Collections.singletonList(RoomDetail.builder()
                        .roomType("Suite")
                        .pricePerPerson(200.0)
                        .numberOfRooms(5)
                        .maxAdults(4)
                        .build()))
                .build();
        contractRepository.save(contract2);

        // Create a SearchDTO to simulate the search query
        RoomRequirementDTO roomRequirementDTO = RoomRequirementDTO.builder()
                .numberOfRooms(1)
                .maxAdults(2)
                .build();

        SearchDTO searchDTO = SearchDTO.builder()
                .checkInDate(LocalDate.now())
                .noOfNights(5)
                .roomRequirements(Collections.singletonList(roomRequirementDTO))
                .build();

        // Act: Perform POST request to search for available contracts based on search criteria
        mockMvc.perform(post("/contracts/available")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchDTO)))
                .andExpect(status().isOk())  // Expect HTTP 200 OK
                .andExpect(jsonPath("$.length()").value(1))  // Expect 1 contract to match the criteria
                .andExpect(jsonPath("$[0].hotelName").value("Hotel A"))  // Expect "Hotel A"
                .andExpect(jsonPath("$[0].availableRooms[0].roomType").value("Deluxe")) // Expect room type "Deluxe"
                .andExpect(jsonPath("$[0].availableRooms[0].totalPrice").value(1150));
    }

    /**
     * Tests the POST /contracts/available endpoint when no contracts match the search criteria.
     * <p>
     * This test sends a POST request with search parameters that do not match any contracts
     * and verifies that the response returns an empty list.
     *
     * @throws Exception if there is an issue with the request execution.
     */
    @Test
    public void testSearchAvailabilityNotFound() throws Exception {
        // Create a RoomRequirementDTO with search criteria that will not match any contracts
        RoomRequirementDTO roomRequirementDTO = RoomRequirementDTO.builder()
                .numberOfRooms(3)  // No contract should have 3 rooms available
                .maxAdults(2)
                .build();

        SearchDTO searchDTO = SearchDTO.builder()
                .checkInDate(LocalDate.now())
                .noOfNights(5)
                .roomRequirements(Collections.singletonList(roomRequirementDTO))
                .build();

        // Perform POST request to search for available contracts that do not exist
        mockMvc.perform(post("/contracts/available")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchDTO)))
                .andExpect(status().isNotFound()) // Expect 404 NotFound even if no contracts are found
                .andExpect(jsonPath("$").value("No Available Contracts Found")); // Expect an empty list
    }

}
