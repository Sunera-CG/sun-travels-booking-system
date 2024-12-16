import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { ContractService } from '../../services/contract.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ContractSearchComponent } from '../../contracts/contract-search/contract-search.component';
import { ContractViewComponent } from '../../contracts/contract-view/contract-view.component';
import { ContractAddComponent } from '../../contracts/contract-add/contract-add.component';
import { Contract } from '../../models/contract.model';

@Component({
  selector: 'app-contract-list',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ContractSearchComponent,
    ContractViewComponent,
    ContractAddComponent,
  ],
  templateUrl: './contract-list.component.html',
  styleUrls: ['./contract-list.component.scss'],
})
export class ContractListComponent implements OnInit {
  contracts: Contract[] = [];
  isAddContractOpen = false;
  isContractsNotFound = false;

  constructor(private contractService: ContractService) {}

  ngOnInit(): void {
    this.contractService
      .getAllContracts()
      .subscribe((contracts: Contract[]) => {
        this.contracts = contracts;
      });
  }

  /**
   * Searches contracts based on the provided search term.
   * @param searchTerm The term to search for in the contracts.
   */
  searchContract(searchTerm: string): void {
    this.isContractsNotFound = false;
    if (searchTerm === '') {
      this.ngOnInit();
    }
    this.contractService.searchContracts(searchTerm).subscribe(
      (filteredContracts: Contract[]) => {
        this.contracts = filteredContracts;
      },
      (error) => {
        if (error.status === 404) {
          this.isContractsNotFound = true;
        }
      }
    );
  }

  /**
   * Opens the form to add a new contract.
   */
  openForm(): void {
    this.isAddContractOpen = true;
  }

  /**
   * Closes the form for adding a new contract.
   */
  closeForm(): void {
    this.isAddContractOpen = false;
  }

  /**
   * Submits the newly added contract to the backend.
   * @param newContract The contract to be added.
   */
  submitAddContract(newContract: Contract): void {
    console.log('Submitting contract:', newContract);
    this.contractService.addContract(newContract).subscribe({
      next: (response) => {
        console.log('Contract added successfully:', response);
        this.contracts.push(response);
      },
      error: (error) => {
        console.error('Error adding contract:', error);
      },
    });
  }

  /**
   * Removes a contract by its ID.
   * @param contractId The ID of the contract to be removed.
   */
  removeContract(contractId: number): void {
    this.contractService.removeContract(contractId).subscribe({
      next: () => {
        console.log('Contract removed successfully:', contractId);
        this.contracts = this.contracts.filter(
          (c) => c.contractId !== contractId
        );
      },
      error: (error) => {
        console.error('Error removing contract:', error);
      },
    });
  }
}
