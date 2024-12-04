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
    public ResponseEntity<Contract> addContract(@Valid @RequestBody ContractDTO contractDTO){
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

}
