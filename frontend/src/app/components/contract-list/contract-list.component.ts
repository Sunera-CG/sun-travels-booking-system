import { Component, OnInit } from '@angular/core';
import { Contract, ContractService } from '../../services/contract.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-contract-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './contract-list.component.html',
  styleUrls: ['./contract-list.component.scss'],
})
export class ContractListComponent implements OnInit {
  contracts: Contract[] = [];
  filteredContracts: Contract[] = [];
  isViewPopUpOpen = false;
  isAddContractOpen = false;
  selectedContract: Contract | null = null;

  // Search term for filtering contracts
  searchTerm: string = '';

  // New contract object to bind to the form
  newContract: Contract = {
    contractId: null,
    hotelName: '',
    startDate: new Date(),
    endDate: new Date(),
    markUp: 0,
    roomDetails: [
      { roomType: '', pricePerPerson: 0, numberOfRooms: 0, maxAdults: 0 },
    ],
  };

  // Function to add a new room detail
  addRoom() {
    this.newContract.roomDetails.push({
      roomType: '',
      pricePerPerson: 0,
      numberOfRooms: 0,
      maxAdults: 0,
    });
  }

  constructor(private contractService: ContractService) {}

  ngOnInit(): void {
    this.contractService
      .getAllContracts()
      .subscribe((contracts: Contract[]) => {
        this.contracts = contracts;
        this.filteredContracts = contracts;
      });
  }

  searchContract(): void {
    if (this.searchTerm.trim() === '') {
      // If search term is empty, show all contracts
      this.filteredContracts = this.contracts;
    } else {
      this.filteredContracts = this.contracts.filter((contract) =>
        contract.hotelName.toLowerCase().includes(this.searchTerm.toLowerCase())
      );
    }
  }

  openViewPopUp(contract: Contract): void {
    this.selectedContract = contract;
    this.isViewPopUpOpen = true;
  }

  closeViewPopUp(): void {
    this.isViewPopUpOpen = false;
    this.selectedContract = null;
  }

  openAddContract(): void {
    this.isAddContractOpen = true;
  }

  // Add the new contract
  closeAddContract(): void {
    this.isAddContractOpen = false;
    this.newContract = {
      contractId: null,
      hotelName: '',
      startDate: new Date(),
      endDate: new Date(),
      markUp: 0,
      roomDetails: [
        { roomType: '', pricePerPerson: 0, numberOfRooms: 0, maxAdults: 0 },
      ],
    };
  }

  submitAddContract(): void {
    this.contractService.addContract(this.newContract).subscribe(() => {
      this.closeAddContract();
      this.contractService.getAllContracts().subscribe((contracts) => {
        this.contracts = contracts;
      });
    });
  }
}
