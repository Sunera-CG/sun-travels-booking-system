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

  // Search term for filtering contracts
  searchTerm: string = '';

  constructor(private contractService: ContractService) {}

  ngOnInit(): void {
    this.contractService
      .getAllContracts()
      .subscribe((contracts: Contract[]) => {
        this.contracts = contracts;
      });
  }

  searchContract(searchTerm: string): void {
    this.contractService
      .searchContracts(searchTerm)
      .subscribe((filteredContracts: Contract[]) => {
        this.contracts = filteredContracts;
      });
  }

  openForm(): void {
    this.isAddContractOpen = true;
  }

  closeForm(): void {
    this.isAddContractOpen = false;
  }

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
}
