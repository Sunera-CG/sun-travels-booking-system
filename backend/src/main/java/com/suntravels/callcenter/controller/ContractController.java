package com.suntravels.callcenter.controller;

import com.suntravels.callcenter.dto.AvailableContractDTO;
import com.suntravels.callcenter.dto.ContractDTO;
import com.suntravels.callcenter.dto.SearchDTO;
import com.suntravels.callcenter.model.Contract;
import com.suntravels.callcenter.service.ContractService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling contract-related requests.
 * Provides endpoints for creating and retrieving contracts.
 */
@RestController
@RequestMapping("/contracts")
@CrossOrigin(origins = "http://localhost:4200")
public class ContractController {

    private final ContractService contractService;

    @Autowired
    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    /**
     * Adds a new contract.
     *
     * @param contractDTO The contract data transfer object containing the contract details.
     * @return A ResponseEntity containing the created Contract object with HTTP status CREATED (201).
     */
    @PostMapping
    public ResponseEntity<Contract> addContract(@RequestBody @Valid ContractDTO contractDTO){
        Contract contract = contractService.addContract(contractDTO);
        return new ResponseEntity<>(contract, HttpStatus.CREATED);
    }

    /**
     * Retrieves all contracts.
     *
     * @return A ResponseEntity containing a list of all Contract objects and an HTTP status of OK (200).
     */
    @GetMapping
    public ResponseEntity<List<Contract>> getAllContracts(){
        List<Contract> contracts = contractService.getAllContracts();
        return new ResponseEntity<>(contracts, HttpStatus.OK);
    }


    /**
     * Searches for contracts based on the provided hotel name.
     *
     * @param hotelName The name of the hotel to search for in the contracts.
     * @return A ResponseEntity containing a list of contracts matching the hotel name, with HTTP status FOUND (302).
     */
    @GetMapping("/{hotelName}")
    public ResponseEntity<List<Contract>> searchByName(@PathVariable String hotelName){
        List<Contract> filteredContracts = contractService.searchByName(hotelName);
        return new ResponseEntity<>(filteredContracts, HttpStatus.OK);
    }


    /**
     * Deletes a contract by its ID.
     *
     * @param contractId the ID of the contract to be deleted.
     * @return a ResponseEntity indicating the result of the operation.
     */
    @DeleteMapping("/{contractId}")
    public ResponseEntity<Void> deleteContract (@PathVariable Integer contractId){
        contractService.deleteContract(contractId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Searches for contracts based on the search criteria provided in SearchDTO.
     *
     * @param searchDTO The search data transfer object containing search parameters.
     * @return A ResponseEntity containing the list of available contracts and HTTP status FOUND (302).
     */
    @PostMapping("/available")
    public ResponseEntity<List<AvailableContractDTO>> searchContract(@RequestBody @Valid SearchDTO searchDTO){
        List<AvailableContractDTO> availableContracts = contractService.searchAvailability(searchDTO);
        return new ResponseEntity<>(availableContracts, HttpStatus.OK);

    }

}
