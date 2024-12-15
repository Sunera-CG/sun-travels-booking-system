import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Contract } from '../models/contract.model';
import {
  AvailableContract,
  SearchContract,
} from '../models/available-contract.model';

/**
 * Service for managing contracts, including adding, retrieving, searching, and removing contracts,
 * as well as searching for available contracts based on specific criteria.
 */
@Injectable({
  providedIn: 'root',
})
export class ContractService {
  private apiUrl = 'http://localhost:8080/contracts'; // API base URL for contract management

  constructor(private http: HttpClient) {}

  /**
   * Adds a new contract to the system.
   * @param contract The contract to add.
   * @returns An observable containing the added contract.
   */
  addContract(contract: Contract): Observable<Contract> {
    return this.http.post<Contract>(this.apiUrl, contract);
  }

  /**
   * Retrieves all contracts from the system.
   * @returns An observable containing a list of all contracts.
   */
  getAllContracts(): Observable<Contract[]> {
    return this.http.get<Contract[]>(this.apiUrl);
  }

  /**
   * Searches for contracts based on a search term.
   * @param searchTerm The term to search for in the contracts.
   * @returns An observable containing the list of contracts that match the search term.
   */
  searchContracts(searchTerm: string): Observable<Contract[]> {
    const searchUrl = `${this.apiUrl}/${searchTerm}`;
    return this.http.get<Contract[]>(searchUrl);
  }

  /**
   * Removes a contract by its ID.
   * @param contractId The ID of the contract to remove.
   * @returns An observable that completes when the contract is successfully removed.
   */
  removeContract(contractId: number): Observable<void> {
    const removeUrl = `${this.apiUrl}/${contractId}`;
    return this.http.delete<void>(removeUrl);
  }

  /**
   * Searches for available contracts based on specified criteria.
   * @param searchContract The criteria for searching available contracts.
   * @returns An observable containing a list of available contracts matching the search criteria.
   */
  searchAvailableContracts(
    searchContract: SearchContract
  ): Observable<AvailableContract[]> {
    const searchUrl = `${this.apiUrl}/available`;
    return this.http.post<AvailableContract[]>(searchUrl, searchContract);
  }
}
