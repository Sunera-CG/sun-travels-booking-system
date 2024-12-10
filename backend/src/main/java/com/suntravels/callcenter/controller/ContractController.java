package com.suntravels.callcenter.controller;

import com.suntravels.callcenter.dto.ContractDTO;
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
     * Searches for contracts based on the search criteria provided in SearchDTO.
     *
     * @param searchDTO The search data transfer object containing search parameters.
     * @return A ResponseEntity containing the list of available contracts and HTTP status FOUND (302).
     */
//    @PostMapping("/available")
//    public ResponseEntity<List<AvailableContract>> searchContract(@RequestBody @Valid SearchDTO searchDTO){
//        List<AvailableContract> availableContracts = contractService.searchContract(searchDTO);
//        return new ResponseEntity<>(availableContracts, HttpStatus.FOUND);
//
//    }



}
